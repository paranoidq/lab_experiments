package predictor.evaluator;

import beans.Edge;
import beans.ScoreEdge;
import handler.EdgeHandler;
import org.apache.commons.collections.CollectionUtils;
import util.PathRules;
import java.util.*;

/**
 * Created by paranoidq on 2016/4/3.
 */
public class PrecisionEvaluator {

    private final static int MAX_ID_BOUND = 361 + 1;

    private Set<Edge> allEdges;
    private Set<Edge> network;
    private Map<Integer, Set<Integer>> networkMap;
    private List<Edge> testEdges;

    private ScoreMetrics metrics;

    private Set<ScoreEdge> predictedEdges;


    private final static int L = 100;

    public PrecisionEvaluator() {
        init();
    }

    private void init() {
        List<Edge> edges = EdgeHandler.loadEdges(PathRules.getEdgesPath());
        allEdges = new HashSet<>(edges);
        Collections.shuffle(edges, new Random(System.nanoTime()));

        int partition = (int)(0.8 * edges.size());
        network = new HashSet<>(edges.subList(0, partition));
        networkMap = mapEdges(network);

        testEdges = edges.subList(partition+1, edges.size());
        metrics = new ScoreMetrics(networkMap);
        predictedEdges = new HashSet<>();

        //
        genAllPredictedEdges();
    }

    public void evaluate() {
        evaluateCN();
        evaluateJaccard();
        evaluateAA();
        evaluateRA();
        evaluatePA();

        evaluateLP();
        //evaluateKazz();
    }

    private void genAllPredictedEdges() {
        for (Integer id1 : networkMap.keySet()) {
            for (Integer id2 : networkMap.keySet()) {
                if(id2 == id1) {
                    continue;
                } else {
                    ScoreEdge possibleEdge = ScoreEdge.newScoreEdge(id1, id2);
                    if (network.contains(possibleEdge)) {
                        continue;
                    }
                    predictedEdges.add(possibleEdge);
                }
            }
        }
    }

    private void evaluateCN() {
        double count = 0;
        List<ScoreEdge> sortedEdges = new ArrayList<>();
        for (ScoreEdge edge : predictedEdges) {
            edge.setScore(metrics.cn(edge.getId1(), edge.getId2()));
            sortedEdges.add(edge);
        }
        Collections.sort(sortedEdges);
        Collection<ScoreEdge> intersection = CollectionUtils.intersection(sortedEdges.subList(0, L), testEdges);
        if (!CollectionUtils.isEmpty(intersection)) {
            count += intersection.size();
        }

        double precisionL = count / L;
        System.out.println("CN Prec@" + L + ": " + precisionL);
    }



    private void evaluateJaccard() {
        double count = 0;
        List<ScoreEdge> sortedEdges = new ArrayList<>();
        for (ScoreEdge edge : predictedEdges) {
            edge.setScore(metrics.jaccard(edge.getId1(), edge.getId2()));
            sortedEdges.add(edge);
        }
        Collections.sort(sortedEdges);
        Collection<ScoreEdge> intersection = CollectionUtils.intersection(sortedEdges.subList(0, L), testEdges);
        if (!CollectionUtils.isEmpty(intersection)) {
            count += intersection.size();
        }

        double precisionL = count / L;
        System.out.println("JA Prec@" + L + ": " + precisionL);
    }

    private void evaluateAA() {
        double count = 0;
        List<ScoreEdge> sortedEdges = new ArrayList<>();
        for (ScoreEdge edge : predictedEdges) {
            edge.setScore(metrics.aa(edge.getId1(), edge.getId2()));
            sortedEdges.add(edge);
        }
        Collections.sort(sortedEdges);
        Collection<ScoreEdge> intersection = CollectionUtils.intersection(sortedEdges.subList(0, L), testEdges);
        if (!CollectionUtils.isEmpty(intersection)) {
            count += intersection.size();
        }

        double precisionL = count / L;
        System.out.println("AA Prec@" + L + ": " + precisionL);
    }

    private void evaluatePA() {
        double count = 0;
        List<ScoreEdge> sortedEdges = new ArrayList<>();
        for (ScoreEdge edge : predictedEdges) {
            edge.setScore(metrics.pa(edge.getId1(), edge.getId2()));
            sortedEdges.add(edge);
        }
        Collections.sort(sortedEdges);
        Collection<ScoreEdge> intersection = CollectionUtils.intersection(sortedEdges.subList(0, L), testEdges);
        if (!CollectionUtils.isEmpty(intersection)) {
            count += intersection.size();
        }

        double precisionL = count / L;
        System.out.println("PA Prec@" + L + ": " + precisionL);
    }

    private void evaluateRA() {
        double count = 0;
        List<ScoreEdge> sortedEdges = new ArrayList<>();
        for (ScoreEdge edge : predictedEdges) {
            edge.setScore(metrics.ra(edge.getId1(), edge.getId2()));
            sortedEdges.add(edge);
        }
        Collections.sort(sortedEdges);
        Collection<ScoreEdge> intersection = CollectionUtils.intersection(sortedEdges.subList(0, L), testEdges);
        if (!CollectionUtils.isEmpty(intersection)) {
            count += intersection.size();
        }

        double precisionL = count / L;
        System.out.println("RA Prec@" + L + ": " + precisionL);
    }

    private void evaluateLP() {
        double count = 0;
        List<ScoreEdge> sortedEdges = new ArrayList<>();
        for (ScoreEdge edge : predictedEdges) {
            edge.setScore(metrics.lp(edge.getId1(), edge.getId2()));
            sortedEdges.add(edge);
        }
        Collections.sort(sortedEdges);
        Collection<ScoreEdge> intersection = CollectionUtils.intersection(sortedEdges.subList(0, L), testEdges);
        if (!CollectionUtils.isEmpty(intersection)) {
            count += intersection.size();
        }

        double precisionL = count / L;
        System.out.println("LP Prec@" + L + ": " + precisionL);
    }

    private void evaluateKazz() {
        double count = 0;
        List<ScoreEdge> sortedEdges = new ArrayList<>();
        for (ScoreEdge edge : predictedEdges) {
            edge.setScore(metrics.kazz(edge.getId1(), edge.getId2()));
            sortedEdges.add(edge);
        }
        Collections.sort(sortedEdges);
        Collection<ScoreEdge> intersection = CollectionUtils.intersection(sortedEdges.subList(0, L), testEdges);
        if (!CollectionUtils.isEmpty(intersection)) {
            count += intersection.size();
        }

        double precisionL = count / L;
        System.out.println("KZ Prec@" + L + ": " + precisionL);
    }


    private Map<Integer, Set<Integer>> mapEdges(Set<Edge> edges) {
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

    public static void main(String[] args) {

    }
}
