package evaluator;

import filter.PatternFilter;
import beans.pattern.ClassType;
import beans.pattern.Pattern;
import beans.trans.Trans;
import beans.trans.TransSet;
import handler.InstancesHandler;
import util.PathRules;
import handler.PatternHandler;
import handler.TransHandler;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

import java.util.*;

/**
 * Created by paranoidq on 16/3/22.
 */
public class LinkEvaluator {

    private EvalResult evalResult;
    private EvalParams params;
    private TransSet posTransSet;
    private TransSet negTransSet;


    public LinkEvaluator(EvalParams params, TransSet posTransSet, TransSet negTransSet) {
        this.params = params;
        this.posTransSet = posTransSet;
        this.negTransSet = negTransSet;
        this.evalResult = new EvalResult();
    }


    public void evaluate() throws Exception {
        TransSet allTransSet = TransSet.union(posTransSet, negTransSet);
        int numFolds = params.getNumFolds();
        for (int fold = 0; fold < numFolds; fold++) {
            TransSet train = allTransSet.trainCV(numFolds, fold);
            TransSet test = allTransSet.testCV(numFolds, fold);

            // @updated 改用TreeSet
            SortedSet<Pattern> union = new TreeSet<>((p1, p2) -> {
                return Double.compare(p2.getDxValue(), p1.getDxValue());
            });

            // 划分类别
            Map<ClassType, List<Trans>> map2Class = train.map2Class();

            // 分别在每个类别上分别挖pattern
            for (ClassType ct : ClassType.values()) {
                List<Trans> transList = map2Class.get(ct);
                // 1. 挖pattern
                PatternHandler.genPatterns(transList, fold, ct);
                List<Pattern> patterns = PatternHandler.loadFpPatterns(fold, ct);

                // 2. 计算D(X)的值
                /*
                 * 需要全局的trans,包括test,计算的是全局上的pattern支持度
                 */
                calAndSetDx(patterns, allTransSet);

                // 3. 合并到union
                union.addAll(patterns); // TODO 如果一个pattern既从c1上挖出,也从c2上挖出,怎么办?
            }

            // 过滤
            List<Pattern> filteredPatterns = PatternFilter.filter(train, union.iterator());

            // PU_FS(过滤后)
            TransHandler.genAugTrans(filteredPatterns, train, test, fold);
            Instances evalTrain = InstancesHandler.loadInstances(PathRules.getAugTrainPath(fold));
            Instances evalTest = InstancesHandler.loadInstances(PathRules.getAugTestPath(fold));

            eval(evalTrain, evalTest);

            union.clear();
        }
    }


    // TODO
    public void printResult() {
        System.out.println("-------------\n");
        System.out.println("Precision \t Recall \t F1 \n");
        evalResult.printMax();
        System.out.println("-------------\n");
    }

    private void eval(Instances train, Instances test)
            throws Exception {
        Evaluation evaluation = new Evaluation(train);
        evaluation.setPriors(train); // TODO ???
        Classifier cls = AbstractClassifier.makeCopy(params.getClassifier());
        cls.buildClassifier(train);
        evaluation.evaluateModel(cls, test);
        evalResult.addRecord(evaluation.weightedPrecision(), evaluation.weightedRecall());
    }

    private void calAndSetDx(List<Pattern> patterns,
                             TransSet allTransSet) {
        // suppD
        allTransSet.calSuppD(patterns);
        // suppL
        // 在loadPattern时一起给定

        // Dx
        int D = allTransSet.size();
        for (Pattern pattern : patterns) {
            double dx = pattern.getSuppL() * Math.log(D/pattern.getSuppD()) / Math.log(2);
            pattern.setDxValue(dx);
        }
    }

}
