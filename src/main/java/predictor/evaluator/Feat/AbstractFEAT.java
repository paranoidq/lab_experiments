package predictor.evaluator.Feat;

import beans.Edge;
import beans.trans.Item;
import handler.FeatsHandler;
import handler.ItemHandler;
import org.apache.commons.collections.CollectionUtils;
import predictor.evaluator.AbstractEvaluator;
import predictor.evaluator.PredictorParamConstants;
import util.PathRules;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import weka.core.Instance;

import java.util.*;

/**
 * 由于FEAT basd方法需要构建INtances，因此在评价指标时随机取边的方法不同于Topology中的方法，（无法做到Edge与Instance对应）
 *
 * 想不到好的办法统一接口，因此FEAT based方法重写evaluateAUC和evaluatePrecision
 *
 *
 * Created by paranoidq on 2016/4/13.
 */
public abstract class AbstractFEAT extends AbstractEvaluator {

    protected static Map<Integer, Set<Integer>> feats;
    protected static Map<Integer, Integer> newID2id;
    protected static int itemCount;

    static {
        feats = FeatsHandler.loadFeats(PathRules.getFilteredFeatsPath());
        ItemHandler.loadItems(PathRules.getItemPath());
        newID2id = Item.map2NewId();
        itemCount = newID2id.size();
    }

    protected Instances train;
    protected Instances posTestInstances;
    protected Instances negTestInstances;

    private Evaluation evaluation;
    private Classifier classifier;

    @Override
    protected void evaluateAUC() {
        System.out.println("Evaluating AUC...");
        double totalScore = 0;
        for (int i = 0; i < PredictorParamConstants.N_AUC; i++) {
            Instance trueEdge = posTestInstances.get(posEdgeRandom.nextInt(testPosEdgesCount));
            double trueScore = feat_metrics(trueEdge);

            Instance fakeEdge = negTestInstances.get(negEdgeRandom.nextInt(testNegEdgesCount));
            double fakeScore = feat_metrics(fakeEdge);

            if (trueScore > fakeScore) {
                totalScore += 1;
            } else if (trueScore == fakeScore) {
                totalScore += 0.5;
            }
        }
        System.out.println(name() + " AUC: " + totalScore/PredictorParamConstants.N_AUC);
    }
    @Override
    protected void evaluatePrecision() {
        System.out.println("Evaluating Precision...");
        double count = 0;
        Map<Instance, Double> sortedEdges = new HashMap<>();
        for (Instance edge : negTestInstances) {
            sortedEdges.put(edge, feat_metrics(edge));
        }
        for (Instance edge : posTestInstances) {
            sortedEdges.put(edge, feat_metrics(edge));
        }
        List<Map.Entry<Instance, Double>> entrys = new ArrayList<>(sortedEdges.entrySet());
        entrys.sort((o1, o2) -> Double.compare(o2.getValue(), o1.getValue()));
        List<Instance> topL = new ArrayList<>();
        List<Map.Entry<Instance, Double>> sub = entrys.subList(0, L);
        for (Map.Entry<Instance, Double> entry : sub) {
            topL.add(entry.getKey());
        }
        Collection<Edge> intersection = CollectionUtils.intersection(topL, posTestInstances);
        if (!CollectionUtils.isEmpty(intersection)) {
            count += intersection.size();
        }

        double precisionL = count / L;
        System.out.println(name() + " Prec@" + L + ": " + precisionL);
    }



    /**
     * DO not call this in FEAT based methods
     * Instead call @feat_metrics
     * Return the probability of linkage of an edge as its score
     * @param edge
     * @return
     */
    @Override
    protected double metrics(Edge edge) {
//        try {
//            return classifier.distributionForInstance(getInstance(edge))[0];
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
        return 0;
    }

    private double feat_metrics(Instance instance) {
        try {
            return classifier.distributionForInstance(instance)[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }



    public void buildModel() {
        try {
//            buildTrain();
//            buildTest();
            loadTrain();
            loadTest();

            evaluation = new Evaluation(train);
            evaluation.setPriors(train);
            classifier = new Logistic();
            classifier.buildClassifier(train);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    protected abstract void buildTrain();
    protected abstract void buildTest();
    protected abstract void loadTrain();
    protected abstract void loadTest();


}
