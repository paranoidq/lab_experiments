package evaluator;

import beans.pattern.ClassType;
import beans.pattern.PUPattern;
import beans.pattern.Pattern;
import beans.trans.Item;
import beans.trans.WrappedInstances;
import evaluator.result.EvalResult;
import handler.PatternHandler;
import handler.TransHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ParamConstants;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instance;
import weka.core.Instances;

import java.util.*;

/**
 * Created by paranoidq on 16/3/7.
 */
public class PUEvaluator extends Evaluator {
    static Logger logger = LoggerFactory.getLogger(PUEvaluator.class);

    /**
     * Only for PUEvaluator
     * 在调用时需要将父类cast为PUEvaluator才能调用.
     */
    //TODO: 改进!!
    private EvalResult PUAllRst;
    private EvalResult SINRst;
    private EvalResult PUFSRst;


    public PUEvaluator(EvalParams params) {
        super(params);
    }

    /**
     * PUEvaluator覆盖父类的evaluate方法,以为在本类中的evaluate方法中,同时完成三个对比方法的测试,所以不直接调用父类的evaluate方法
     *      (1) PU_ALL
     *      (2) SIN
     *      (3) PU_FS
     *
     * @throws Exception
     */
    @Override
    public void evaluate() throws Exception {
        WrappedInstances instances = WrappedInstances.newInstances(params.getTrans());
        Classifier classifier = params.getClassifier();
        int numFolds = params.getNumFolds();
        for (int fold = 0; fold < numFolds; fold++) {
            Instances train = instances.getTrain(fold);

            // @updated 改用TreeSet
            SortedSet<PUPattern> union = new TreeSet<>((p1, p2) -> {
                return Double.compare(p2.getDxValue(), p1.getDxValue());
            });
            // 分别在每个class上挖pattern
            for (ClassType ct : ClassType.values()) {
                // 1. 挖pattern
                PatternHandler.Generator.genPUPattern(train, fold, ct);
                List<PUPattern> patterns = PatternHandler.Loader.loadPUPatterns(fold, ct);

                // 2. 计算D(X)的值
                calAndSetDx(patterns, instances);

                // 3. 合并到union
                union.addAll(union); // TODO? 如果一个pattern既从c1上挖出,也从c2上挖出,怎么办?
            }


            /** 评估对比方法:
             * ----------------------------------------------------------------
             */
            WrappedInstances evalInstances;


            /**
             * 对比方法1: PU_ALL(不过滤任何pattern)
             * ----------------------------------------------------------------
             */
            evalInstances = TransHandler.Generator.generateAugTrans(union.iterator(), instances);
            eval(evalInstances.getTrain(fold), evalInstances.getTest(fold), classifier, PUAllRst);
            /**
             * ----------------------------------------------------------------
             */


            // 过滤
            List<PUPattern> filteredPatterns = filterPatterns(train, union.iterator());

            /**
             * 对比方法2: SIN(单feature)
             * ----------------------------------------------------------------
             */
            evalInstances = TransHandler.Generator.generateAugTrans4SIN(
                    extractSinFeatures(filteredPatterns), instances);
            eval(evalInstances.getTrain(fold), evalInstances.getTest(fold), classifier, SINRst);
            /**
             * ----------------------------------------------------------------
             */


            /**
             * 对比方法3: PU_FS(过滤后)
             * ----------------------------------------------------------------
             */
            evalInstances = TransHandler.Generator.generateAugTrans(filteredPatterns.iterator(), instances);
            eval(evalInstances.getTrain(fold), evalInstances.getTest(fold), classifier, PUFSRst);
            /**
             * ----------------------------------------------------------------
             */

            union.clear();
        }
    }

    private void eval(Instances train, Instances test, Classifier classifier, EvalResult result)
            throws Exception {
        Evaluation evaluation = new Evaluation(train);
        evaluation.setPriors(train); // ???????????????
        Classifier cls = AbstractClassifier.makeCopy(classifier);
        cls.buildClassifier(train);
        evaluation.evaluateModel(cls, test);

        // You can add other records to result !
        result.addRecord(evaluation.precision(0)); // TODO 到底是用link的precision还是weighted precision????
    }



    private void calAndSetDx(List<PUPattern> patterns,
                             WrappedInstances instances) {
        // suppD
        Iterator<Instance> iterator = instances.getInstances().iterator();
        while (iterator.hasNext()) {
            Instance instance = iterator.next();
            patterns.stream().filter(pattern -> pattern.fit(instance)).forEach(PUPattern::incrSuppD);
        }
        // suppL
        // TODO: 应该在loadPattern的时候就给出了

        // Dx
        int D = instances.getInstances().numInstances();
        for (PUPattern pattern : patterns) {
            double dx = pattern.getSuppL() * Math.log(D/pattern.getSuppD()) / Math.log(2);
            pattern.setDxValue(dx);
            // setIdf
        }

    }


    private Iterator<Item> extractSinFeatures(List<? extends Pattern> patterns) {
        Set<Item> sinFeatures = new HashSet<>();
        for (Pattern pattern : patterns) {
            sinFeatures.addAll(pattern.getEntryList());
        }
        return sinFeatures.iterator();
    }


    /**
     * 无用
     * @param instances
     * @param train
     * @param fold
     * @return
     */
    @Override
    protected List<? extends Pattern> filter(WrappedInstances instances, Instances train, int fold) {
        throw new RuntimeException("在PUEvaluator中不应该调用此方法!");
    }

    /**
     * 过滤规则:
     *      每次选取较大的Dx值得pattern,直到数据集中的instance被cover到一定的比例
     *      (不按照每个instance都被cover1次来算,因为有些instance始终无法被cover到 !!)
     * @param train
     * @return
     */
    private List<PUPattern> filterPatterns(Instances train, Iterator<PUPattern> iterator) {
        return filterByCoverage(train, iterator, ParamConstants.PU_COVERAGE_DELTA);
    }

    /**
     * 几种方案:
     *    * (1) train中被cover一定的比例
     *      (2) test中被cover一定的比例
     *      (3) train+test中被cover一定的比例
     * @param train
     * @param coverage
     * @return
     */
    private List<PUPattern> filterByCoverage(Instances train, Iterator<PUPattern> iterator, double coverage) {
        List<PUPattern> filtered = new LinkedList<>();

        double threshold = train.numInstances();
        Set<Integer> coveredInstances = new HashSet<>();
        while (iterator.hasNext()) {
            PUPattern pattern = iterator.next();

            if (coveredInstances.size() >= threshold) {
                break;
            }
            for (int i = 0; i < train.numInstances(); i++) {
                Instance instance = train.instance(i);
                if (pattern.fit(instance)) {
                    coveredInstances.add(i);
                }
            }
            filtered.add(pattern);
        }
        return filtered;
    }

    @Override
    protected void printRst() {
        //TODO
    }
}
