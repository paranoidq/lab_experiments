package evaluator;

import beans.trans.WrappedInstances;
import handler.TransHandler;
import weka.classifiers.Classifier;

import java.util.Random;

/**
 * Created by paranoidq on 16/3/7.
 */
public class EvalParams {

    private Classifier classifier;
    private WrappedInstances data;
    private int numFolds;


    public Classifier getClassifier() {
        return classifier;
    }

    public WrappedInstances getTrans() {
        return data;
    }

    public int getNumFolds() {
        return numFolds;
    }

    public EvalParams(Classifier classifier, String transPath, int numFolds, Random rand) {
        this.classifier = classifier;
        this.numFolds = numFolds;

        init(transPath, rand);
    }

    private void init(String transPath, Random random) {
        try {
            data = TransHandler.Loader.loadSrc(transPath);
            data.getInstances().randomize(random);

            if (data.getInstances().classAttribute().isNominal()) {
                data.getInstances().stratify(numFolds);
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
