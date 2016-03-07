package evaluator;

import beans.trans.WrappedInstances;
import evaluator.result.EvalResult;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

/**
 * Created by paranoidq on 16/3/7.
 */
public class Evaluator {

    private EvalParams params;
    private EvalResult result;

    private Evaluator(EvalParams params) {
        this.params = params;
    }

    public static Evaluator newEvaluator(EvalParams params) {
        return new Evaluator(params);
    }


    public void evaluate() throws Exception {
        WrappedInstances instances = WrappedInstances.newInstances(params.getTrans());
        Classifier classifier = params.getClassifier();
        int numFolds = params.getNumFolds();
        for (int fold = 0; fold < numFolds; fold++) {
            Instances train = instances.getTrain(fold);
            Instances test = instances.getTest(fold);

            Evaluation evaluation = new Evaluation(train);
            evaluation.setPriors(train); // ???????????????
            Classifier cls = AbstractClassifier.makeCopy(classifier);
            cls.buildClassifier(train);
            evaluation.evaluateModel(cls, test);

            // You can add other records to result !
            result.addRecord(evaluation.weightedPrecision(), evaluation.weightedRecall());
        }
    }

    public EvalResult getEvalResult() {
        return this.result;
    }


}
