package predictor.evaluator;

import beans.Edge;
import handler.EdgeHandler;
import org.apache.commons.collections.CollectionUtils;
import util.PathRules;
import util.PredictorParamConstants;

import java.io.IOException;
import java.util.*;

/**
 * Created by paranoidq on 2016/4/2.
 */
public class AUCEvaluator {

    private final static int MAX_ID_BOUND = 361 + 1;

    private static Random trueEdgeRandom = new Random(System.nanoTime());
    private static Random fakeEdgeRandom = new Random(System.nanoTime());

    private Set<Edge> allEdges;
    private List<Edge> network;
    private Map<Integer, Set<Integer>> networkMap;
    private List<Edge> testEdges;

    private ScoreMetrics metrics;


    public AUCEvaluator() {
        init();
    }

    private void init() {
        List<Edge> edges = EdgeHandler.loadEdges(PathRules.getEdgesPath());
        allEdges = new HashSet<>(edges);
        Collections.shuffle(edges, new Random(System.nanoTime()));

        int partition = (int)(0.8 * edges.size());
        network = edges.subList(0, partition);
        networkMap = mapEdges(network);

        testEdges = edges.subList(partition+1, edges.size());
        metrics = new ScoreMetrics(networkMap);

    }

    public void evaluate() throws IOException {
        evaluateCN();
        evaluateJaccard();
        evaluateAA();
        evaluateRA();
        evaluatePA();
    }

    private void evaluateCN() {
        double totalScore = 0;
        for (int i=0; i<PredictorParamConstants.N_AUC; i++) {
            Edge trueEdge = testEdges.get(trueEdgeRandom.nextInt(testEdges.size()));
            int trueScore = metrics.cn(trueEdge.getId1(), trueEdge.getId2());

            // fake edge
            Edge fakeEdge = genFakeEdge();
            int fakeScore = metrics.cn(fakeEdge.getId1(), fakeEdge.getId2());

            if (trueScore > fakeScore) {
                totalScore += 1;
            } else if (trueScore == fakeScore) {
                totalScore += 0.5;
            }
        }
        System.out.println("CN AUC: " + totalScore/PredictorParamConstants.N_AUC);
    }


    private void evaluateJaccard() {
        double totalScore = 0;
        for (int i=0; i<PredictorParamConstants.N_AUC; i++) {
            Edge trueEdge = testEdges.get(trueEdgeRandom.nextInt(testEdges.size()));
            double trueScore = metrics.jaccard(trueEdge.getId1(), trueEdge.getId2());

            // fake edge
            Edge fakeEdge = genFakeEdge();
            double fakeScore = metrics.jaccard(fakeEdge.getId1(), fakeEdge.getId2());

            if (trueScore > fakeScore) {
                totalScore += 1;
            } else if (trueScore == fakeScore) {
                totalScore += 0.5;
            }
        }
        System.out.println("JA AUC: " + totalScore/PredictorParamConstants.N_AUC);
    }



    private void evaluateAA() {
        double totalScore = 0;
        for (int i=0; i<PredictorParamConstants.N_AUC; i++) {
            Edge trueEdge = testEdges.get(trueEdgeRandom.nextInt(testEdges.size()));
            double trueScore = metrics.aa(trueEdge.getId1(), trueEdge.getId2());

            // fake edge
            Edge fakeEdge = genFakeEdge();
            double fakeScore = metrics.aa(fakeEdge.getId1(), fakeEdge.getId2());

            if (trueScore > fakeScore) {
                totalScore += 1;
            } else if (trueScore == fakeScore) {
                totalScore += 0.5;
            }
        }
        System.out.println("AA AUC: " + totalScore/PredictorParamConstants.N_AUC);
    }


    private void evaluateRA() {
        double totalScore = 0;
        for (int i=0; i<PredictorParamConstants.N_AUC; i++) {
            Edge trueEdge = testEdges.get(trueEdgeRandom.nextInt(testEdges.size()));
            double trueScore = metrics.ra(trueEdge.getId1(), trueEdge.getId2());

            // fake edge
            Edge fakeEdge = genFakeEdge();
            double fakeScore = metrics.ra(fakeEdge.getId1(), fakeEdge.getId2());

            if (trueScore > fakeScore) {
                totalScore += 1;
            } else if (trueScore == fakeScore) {
                totalScore += 0.5;
            }
        }
        System.out.println("RA AUC: " + totalScore/PredictorParamConstants.N_AUC);
    }


    private void evaluatePA() {
        double totalScore = 0;
        for (int i=0; i<PredictorParamConstants.N_AUC; i++) {
            Edge trueEdge = testEdges.get(trueEdgeRandom.nextInt(testEdges.size()));
            double trueScore = metrics.pa(trueEdge.getId1(), trueEdge.getId2());

            // fake edge
            Edge fakeEdge = genFakeEdge();
            double fakeScore = metrics.pa(fakeEdge.getId1(), fakeEdge.getId2());

            if (trueScore > fakeScore) {
                totalScore += 1;
            } else if (trueScore == fakeScore) {
                totalScore += 0.5;
            }
        }
        System.out.println("PA AUC: " + totalScore/PredictorParamConstants.N_AUC);
    }



    private Edge genFakeEdge() {
        int id1 = fakeEdgeRandom.nextInt(MAX_ID_BOUND);
        int id2 = fakeEdgeRandom.nextInt(MAX_ID_BOUND);
        Edge edge = Edge.newEdge(id1,id2);
        while (allEdges.contains(edge) || id1 == id2) {
            id1 = fakeEdgeRandom.nextInt(MAX_ID_BOUND);
            id2 = fakeEdgeRandom.nextInt(MAX_ID_BOUND);
            edge = Edge.newEdge(id1,id2);
        }
        return edge;
    }


    private Map<Integer, Set<Integer>> mapEdges(List<Edge> edges) {
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


    public static void main(String[] args) throws IOException {
        AUCEvaluator evaluator = new AUCEvaluator();
        evaluator.evaluate();
    }

}
