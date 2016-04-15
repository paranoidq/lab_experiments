package predictor.evaluator;

import beans.Edge;
import handler.EdgeHandler;
import org.apache.commons.collections.CollectionUtils;
import util.PathRules;

import java.util.*;

/**
 * Created by paranoidq on 2016/4/13.
 */
public abstract class AbstractEvaluator {
    protected static final int L = PredictorParamConstants.L;

    private static final double PARTITION = PredictorParamConstants.TRAIN_PARTITION;
    private static final double TEST_RATIO_VS_TRAIN = PredictorParamConstants.TEST_RATIO_VS_TRAIN;
    protected static final int MAX_ID_BOUND = PredictorParamConstants.MAX_ID + 1;

    protected static List<Edge> allPosEdges;
    protected static List<Edge> allNegEdges;
    protected static Set<Edge> allPosEdgesSet;

    protected static List<Edge> trainPosEdges;
    protected static List<Edge> trainNegEdges;
    protected static List<Edge> testPosEdges;
    protected static List<Edge> testNegEdges;



    protected static Map<Integer, Set<Integer>> groundNetwork;


    static {
        allPosEdges = EdgeHandler.loadEdges(PathRules.getAllPosEdgesPath());
        allNegEdges = EdgeHandler.loadEdges(PathRules.getAllNegEdgesPath());

        allPosEdgesSet = new HashSet<>(allPosEdges);
        Collections.shuffle(allPosEdges, new Random(System.nanoTime()));

        int partition = (int)(PARTITION* allPosEdges.size());
        trainPosEdges = allPosEdges.subList(0, partition);
        testPosEdges = allPosEdges.subList(partition+1, allPosEdges.size());


        int testPartition = (int)TEST_RATIO_VS_TRAIN * partition;
        Collections.shuffle(allNegEdges, new Random(System.nanoTime()));
        trainNegEdges = allNegEdges.subList(0, testPartition);
        testNegEdges = allNegEdges.subList(testPartition+1, allNegEdges.size());

        groundNetwork = map(trainPosEdges);
    }


    protected abstract double metrics(Edge e);
    protected abstract String name();

    protected static Random posEdgeRandom = new Random(System.nanoTime());
    protected static Random negEdgeRandom = new Random(System.nanoTime());
    protected static int testPosEdgesCount = testPosEdges.size();
    protected static int testNegEdgesCount = testNegEdges.size();
    public void evaluate() {
        evaluateAUC();
        evaluatePrecision();
    }
    protected void evaluateAUC() {
        double totalScore = 0;
        for (int i = 0; i < PredictorParamConstants.N_AUC; i++) {
            Edge trueEdge = testPosEdges.get(posEdgeRandom.nextInt(testPosEdgesCount));
            double trueScore = metrics(trueEdge);

            Edge fakeEdge = testNegEdges.get(negEdgeRandom.nextInt(testNegEdgesCount));
            double fakeScore = metrics(fakeEdge);

            if (trueScore > fakeScore) {
                totalScore += 1;
            } else if (trueScore == fakeScore) {
                totalScore += 0.5;
            }
        }
        System.out.println(name() + " AUC: " + totalScore/PredictorParamConstants.N_AUC);
    }

    protected void evaluatePrecision() {
        double count = 0;
        List<Edge> sortedEdges = new ArrayList<>();
        for (Edge edge : testNegEdges) {
            edge.setScore(metrics(edge));
            sortedEdges.add(edge);
        }
        for (Edge edge : testPosEdges) {
            edge.setScore(metrics(edge));
            sortedEdges.add(edge);
        }
        Collections.sort(sortedEdges);
        Collection<Edge> intersection = CollectionUtils.intersection(sortedEdges.subList(0, L), testPosEdges);
        if (!CollectionUtils.isEmpty(intersection)) {
            count += intersection.size();
        }

        double precisionL = count / L;
        System.out.println(name() + " Prec@" + L + ": " + precisionL);
    }




    private static Map<Integer, Set<Integer>> map(List<Edge> edges) {
        Map<Integer, Set<Integer>> map = new HashMap<>();
        for (Edge edge : edges) {
            if (!map.containsKey(edge.getId1())) {
                map.put(edge.getId1(), new HashSet<>());
            }
            map.get(edge.getId1()).add(edge.getId2());
            if (!map.containsKey(edge.getId2())) {
                map.put(edge.getId2(), new HashSet<>());
            }
            map.get(edge.getId2()).add(edge.getId1());
        }
        return map;
    }
}
