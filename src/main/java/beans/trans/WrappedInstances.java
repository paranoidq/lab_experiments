package beans.trans;

import weka.core.Instances;

/**
 * Created by paranoidq on 16/3/7.
 */
public class WrappedInstances {

    private Instances instances;
    private int numFolds;
    private double trainTestRatio;

    public WrappedInstances(Instances instances) {
        //TODO
        this.instances = instances;
    }

    public WrappedInstances(Instances instances, int numFolds) {
        //TODO
        this.instances = instances;
        this.numFolds = numFolds;
    }

    public WrappedInstances(Instances instances, int numFolds, double trainTestRatio) {
        this.instances = instances;
        this.numFolds = numFolds;
        this.trainTestRatio = trainTestRatio;
    }

    public static WrappedInstances newInstances(WrappedInstances instances) {
        Instances data =  new Instances(instances.getInstances());
        return new WrappedInstances(data, instances.getNumFolds(), instances.getTrainTestRatio());
    }

    public Instances getInstances() {
        return this.instances;
    }

    public int getNumFolds() {
        return this.numFolds;
    }

    public double getTrainTestRatio() {
        return this.trainTestRatio;
    }

    public Instances getTrain(int fold) {
        return null;
    }

    public Instances getTest(int fold) {
        return null;
    }
}
