package predictor.evaluator.Feat;

import beans.Edge;
import beans.pattern.ClassType;
import beans.trans.Trans;
import beans.trans.TransSet;
import handler.InstancesHandler;
import org.apache.commons.collections.CollectionUtils;
import util.FileUtil;
import util.PathRules;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by paranoidq on 2016/4/13.
 */
public class FEAT_ORIGIN extends AbstractFEAT {



    public FEAT_ORIGIN() {
        super.buildModel();
    }


    @Override
    protected String name() {
        return "FEAT_ORIGIN";
    }


    @Override
    protected void buildTrain() {
        System.out.println("Building train...");
        TransSet trainSet = new TransSet();
        for (Edge e : trainPosEdges) {
            List<Integer> edgeFeats = (List<Integer>)CollectionUtils.intersection(feats.get(e.getId1()), feats.get(e.getId2()));
            Trans trans = new Trans(ClassType.POSITIVE);
            trans.setFeats(edgeFeats);
            trainSet.addTrans(trans);
        }
        for (Edge e : trainNegEdges) {
            List<Integer> edgeFeats = (List<Integer>)CollectionUtils.intersection(feats.get(e.getId1()), feats.get(e.getId2()));
            Trans trans = new Trans(ClassType.NEGATIVE);
            trans.setFeats(edgeFeats);
            trainSet.addTrans(trans);
        }
        Collections.shuffle(trainSet.getTransSet());
        String trainPath = PathRules.getLinkTrainPath_FEAT_ORIGIN();
        try (BufferedWriter bw = FileUtil.writeFile(trainPath)) {
            InstancesHandler.writeOriginInstances(trainSet, itemCount, newID2id, bw);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Build train end.");
    }

    @Override
    protected void buildTest() {
        System.out.println("Building test...");
        TransSet posTestSet = new TransSet();
        for (Edge e : testPosEdges) {
            List<Integer> edgeFeats = (List<Integer>)CollectionUtils.intersection(feats.get(e.getId1()), feats.get(e.getId2()));
            Trans trans = new Trans(ClassType.POSITIVE);
            trans.setFeats(edgeFeats);
            posTestSet.addTrans(trans);
        }
        try (BufferedWriter bw = FileUtil.writeFile(PathRules.getLinkPosTestPath_FEAT_ORIGIN())) {
            InstancesHandler.writeOriginInstances(posTestSet, itemCount, newID2id, bw);
        } catch (IOException e) {
            e.printStackTrace();
        }

        TransSet negTestSet = new TransSet();
        for (Edge e : testNegEdges) {
            List<Integer> edgeFeats = (List<Integer>)CollectionUtils.intersection(feats.get(e.getId1()), feats.get(e.getId2()));
            Trans trans = new Trans(ClassType.NEGATIVE);
            trans.setFeats(edgeFeats);
            negTestSet.addTrans(trans);
        }
        try (BufferedWriter bw = FileUtil.writeFile(PathRules.getLinkNegTestPath_FEAT_ORIGIN())) {
            InstancesHandler.writeOriginInstances(negTestSet, itemCount, newID2id, bw);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Build test end.");
    }

    @Override
    protected void loadTrain() {
        train = InstancesHandler.loadInstances(PathRules.getLinkTrainPath_FEAT_ORIGIN());
    }
    @Override
    protected void loadTest() {
        posTestInstances = InstancesHandler.loadInstances(PathRules.getLinkPosTestPath_FEAT_ORIGIN());
        negTestInstances = InstancesHandler.loadInstances(PathRules.getLinkNegTestPath_FEAT_ORIGIN());
    }
}
