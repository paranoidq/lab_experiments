package beans.trans;

import util.ParamConstants;
import weka.core.Instances;

/**
 * Created by paranoidq on 16/3/7.
 *
 * DO NOT directly use weka instances in source code !!!
 * Instead, use WrappedInstances for better compatibility
 */
public class WrappedInstances {


    private Instances instances;


    private int numFolds;
    private double trainRatio;

    private TrainSplitType splitType; // 分割方式, by_fold / by_ratio

    public WrappedInstances(Instances instances) {
        this(instances, ParamConstants.NUM_FOLDS);
    }

    public WrappedInstances(Instances instances, int numFolds) {
        this.instances = instances;
        this.numFolds = numFolds;
        this.trainRatio = 0;
        this.splitType = TrainSplitType.by_fold;
    }

    public WrappedInstances(Instances instances, double trainRatio) {
        this.instances = instances;
        this.trainRatio = trainRatio;
        this.numFolds = 0;
        this.splitType = TrainSplitType.by_ratio;
    }

    public static WrappedInstances newInstances(WrappedInstances instances) {
        Instances data =  new Instances(instances.getInstances());
        if (instances.getSplitType() == TrainSplitType.by_fold) {
            return new WrappedInstances(data, instances.getNumFolds());
        } else {
            return new WrappedInstances(data, instances.getTrainRatio());
        }

    }

    public Instances getInstances() {
        return this.instances;
    }

    public int getNumFolds() {
        return this.numFolds;
    }

    public double getTrainRatio() {
        return this.trainRatio;
    }

    public double getTestRatio() {
        return 1 - this.trainRatio;
    }

    public Instances getTrain(int fold) {
        //TODO
        return null;
    }

    public Instances getTest(int fold) {
        //TODO
        return null;
    }

    public TrainSplitType getSplitType() {
        return splitType;
    }
}
