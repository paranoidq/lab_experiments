package evaluator;

import weka.classifiers.Classifier;

/**
 * Created by paranoidq on 16/3/7.
 */
public class EvalParams {

    private Classifier classifier;
    private int numFolds;


    public Classifier getClassifier() {
        return classifier;
    }

    public int getNumFolds() {
        return numFolds;
    }

    public EvalParams(Classifier classifier, int numFolds) {
        this.classifier = classifier;
        this.numFolds = numFolds;
    }

}
