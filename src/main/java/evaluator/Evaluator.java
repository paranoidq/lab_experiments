package evaluator;

import beans.pattern.Pattern;
import beans.trans.WrappedInstances;
import evaluator.result.EvalResult;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

import java.util.List;

/**
 * Created by paranoidq on 16/3/7.
 */
public abstract class Evaluator {

    protected EvalParams params;
    protected EvalResult result;



    public Evaluator(EvalParams params) {
        this.params = params;
    }


    public void evaluate() throws Exception {
        WrappedInstances instances = WrappedInstances.newInstances(params.getTrans());
        Classifier classifier = params.getClassifier();
        int numFolds = params.getNumFolds();
        for (int fold = 0; fold < numFolds; fold++) {
            Instances train = instances.getTrain(fold);
            Instances test = instances.getTest(fold);

            // Augment transaction with patterns
            List<? extends Pattern> patterns = filter(instances, train, fold);
            if (patterns != null) {
                instances = augment(instances, patterns);
                train = instances.getTrain(fold);
                test = instances.getTest(fold);
            }


            Evaluation evaluation = new Evaluation(train);
            evaluation.setPriors(train); // ???????????????
            Classifier cls = AbstractClassifier.makeCopy(classifier);
            cls.buildClassifier(train);
            evaluation.evaluateModel(cls, test);

            // You can add other records to result !
            result.addRecord(evaluation.precision(0));
        }
    }

    public EvalResult getEvalResult() {
        return this.result;
    }

    protected abstract List<? extends Pattern> filter(WrappedInstances instances, Instances train, int fold);


    protected abstract void printRst();

    /**
     * 注意: 不适用于single feature, 如果需要使用,需要修改pattern为单feature!
     *
     * TODO: 是否还需要augment了?或者直接用组合特征是否更好?
     *
     * @param instances
     * @param patterns
     * @return
     */
    private WrappedInstances augment(WrappedInstances instances, List<? extends Pattern> patterns) {
        return instances;

    }
}
