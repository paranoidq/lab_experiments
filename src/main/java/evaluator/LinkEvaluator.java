package evaluator;

import beans.pattern.PatternType;
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

    private EvalResult originEvalResult;
    private EvalResult fpEvalResult;
    private EvalResult fpPatternEvalResult;
    private EvalResult cosineEvalResult;
    private EvalResult cosinePatternEvalResult;



    private EvalParams params;
    private TransSet posTransSet;
    private TransSet negTransSet;


    public LinkEvaluator(EvalParams params, TransSet posTransSet, TransSet negTransSet) {
        this.params = params;
        this.posTransSet = posTransSet;
        this.negTransSet = negTransSet;

        this.originEvalResult = new EvalResult("Origin");
        this.fpEvalResult = new EvalResult("FP");
        this.fpPatternEvalResult = new EvalResult("FP_Pat");
        this.cosineEvalResult = new EvalResult("Cosine");
        this.cosinePatternEvalResult = new EvalResult("Cosine_Pat");
    }


    public void evaluate() throws Exception {
        TransSet allTransSet = TransSet.union(posTransSet, negTransSet);
        int numFolds = params.getNumFolds();
        for (int fold = 0; fold < numFolds; fold++) {
            TransSet train = allTransSet.trainCV(numFolds, fold);
            TransSet test = allTransSet.testCV(numFolds, fold);



            // 划分类别
            Map<ClassType, List<Trans>> map2Class = train.map2Class();
            /**
             * FP pattern引入后预测
             */
            {
                List<Pattern> fpPatterns = doGenAndFilter(map2Class, fold, allTransSet, PatternType.FP);
                TransHandler.genAugTrans(fpPatterns, train, test, fold, PatternType.FP);
                Instances evalTrain = InstancesHandler.loadInstances(PathRules.getAugTrainPath(fold, PatternType.FP));
                Instances evalTest = InstancesHandler.loadInstances(PathRules.getAugTestPath(fold, PatternType.FP));
                eval(evalTrain, evalTest, fpEvalResult);

                // 直接用pattern
                TransHandler.genPatternAugTrans(fpPatterns, train, test, fold, PatternType.FP);
                evalTrain = InstancesHandler.loadInstances(PathRules.getPatternAugTrainPath(fold, PatternType.FP));
                evalTest  = InstancesHandler.loadInstances(PathRules.getPatternAugTestPath(fold, PatternType.FP));
                eval(evalTrain, evalTest, fpPatternEvalResult);
            }

            /**
             * Cosine pattern引入后预测
             */
            {
                List<Pattern> cosinePatterns = doGenAndFilter(map2Class, fold, allTransSet, PatternType.COSINE);
                TransHandler.genAugTrans(cosinePatterns, train, test, fold, PatternType.COSINE);
                Instances evalTrain = InstancesHandler.loadInstances(PathRules.getAugTrainPath(fold, PatternType.COSINE));
                Instances evalTest  = InstancesHandler.loadInstances(PathRules.getAugTestPath(fold, PatternType.COSINE));
                eval(evalTrain, evalTest, cosineEvalResult);

                // 直接用pattern
                TransHandler.genPatternAugTrans(cosinePatterns, train, test, fold, PatternType.COSINE);
                evalTrain = InstancesHandler.loadInstances(PathRules.getPatternAugTrainPath(fold, PatternType.COSINE));
                evalTest  = InstancesHandler.loadInstances(PathRules.getPatternAugTestPath(fold, PatternType.COSINE));
                eval(evalTrain, evalTest, cosinePatternEvalResult);
            }

            /**
             * 原始预测
             */
            {
                TransHandler.genOriginTrans(train, test, fold);
                Instances evalTrain = InstancesHandler.loadInstances(PathRules.getOriginTrainPath(fold));
                Instances evalTest  = InstancesHandler.loadInstances(PathRules.getOriginTestPath(fold));
                eval(evalTrain, evalTest, originEvalResult);
            }
        }
    }


    // TODO
    public void printResult() {
        System.out.println("-------max------");
        System.out.println("Name \t Accuracy \t Precision \t Recall \t F1");
        originEvalResult.printMax();
        fpEvalResult.printMax();
        fpPatternEvalResult.printMax();
        cosineEvalResult.printMax();
        cosinePatternEvalResult.printMax();
        System.out.println("------avg-------");
        System.out.println("Name \t Accuracy \t Precision \t Recall \t F1");
        originEvalResult.printAvg();
        fpEvalResult.printAvg();
        fpPatternEvalResult.printAvg();
        cosineEvalResult.printAvg();
        cosinePatternEvalResult.printAvg();
        System.out.println("-------------\n");
    }

    private void eval(Instances train, Instances test, EvalResult evalResult)
            throws Exception {
        Evaluation evaluation = new Evaluation(train);
        evaluation.setPriors(train); // TODO ???
        Classifier cls = AbstractClassifier.makeCopy(params.getClassifier());
        cls.buildClassifier(train);
        evaluation.evaluateModel(cls, test);
        evalResult.addRecord(evaluation.correct()/test.numInstances(), evaluation.weightedPrecision(), evaluation.weightedRecall());
    }

    private void calAndSetDx(List<? extends Pattern> patterns,
                             TransSet transSet) {
        // suppD
        transSet.calSuppD(patterns);
        // suppL
        // 在loadPattern时一起给定

        // Dx
        int D = transSet.size();
        for (Pattern pattern : patterns) {
            double dx = pattern.getSuppL() * Math.log(D/pattern.getSuppD()) / Math.log(2);
            pattern.setDxValue(dx);
        }
    }

    public List<Pattern> doGenAndFilter(Map<ClassType, List<Trans>> map2Class,
                                        int fold, TransSet transSet, PatternType pt) throws Exception {

        Set<Pattern> union = new HashSet<>();
        // 分别在每个类别上分别挖pattern
        for (ClassType ct : ClassType.values()) {
            List<Trans> transList = map2Class.get(ct);
            List<? extends Pattern> patterns;
            // 1. 挖pattern
            if (pt == PatternType.FP) {
                PatternHandler.genFpPatterns(transList, fold, ct);
                patterns = PatternHandler.loadFpPatterns(fold, ct);
            } else {
                PatternHandler.genCosinePatterns(transList, fold, ct);
                patterns = PatternHandler.loadCosinePatterns(fold, ct);
            }


            // 2. 计算D(X)的值
                /*
                 * 需要全局的trans,包括test,计算的是全局上的pattern支持度
                 */
            calAndSetDx(patterns, transSet);

            // 3. 合并到union
            for (Pattern pattern : patterns) {
                union.add(pattern);
            }
            //union.addAll(patterns); // TODO 如果一个pattern既从c1上挖出,也从c2上挖出,怎么办?
        }

        List<Pattern> allPats = new ArrayList<>(union);
        Collections.sort(allPats, (o1, o2) -> Double.compare(o2.getDxValue(), o1.getDxValue()));
        // 过滤
        List<Pattern> filteredPatterns = PatternFilter.filter(transSet, allPats, fold, pt);
        return filteredPatterns;
    }

}
