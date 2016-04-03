package predictor.evaluator;

import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by paranoidq on 2016/4/3.
 */
public class ScoreMetrics {

    private Map<Integer, Set<Integer>> networkMap;

    public ScoreMetrics(Map<Integer, Set<Integer>> networkMap) {
        this.networkMap = networkMap;

    }

    public int cn(int id1, int id2) {
        if (!networkMap.containsKey(id1) || !networkMap.containsKey(id2)) {
            return 0;
        }
        return CollectionUtils.intersection(networkMap.get(id1), networkMap.get(id2)).size();
    }

    public double jaccard(int id1, int id2) {
        if (!networkMap.containsKey(id1) || !networkMap.containsKey(id2)) {
            return 0;
        }
        return (double)CollectionUtils.intersection(networkMap.get(id1), networkMap.get(id2)).size()
                / CollectionUtils.union(networkMap.get(id1), networkMap.get(id2)).size();
    }

    public double aa(int id1, int id2) {
        if (!networkMap.containsKey(id1) || !networkMap.containsKey(id2)) {
            return 0;
        }
        double score = 0;
        Collection<Integer> CNs = CollectionUtils.intersection(networkMap.get(id1), networkMap.get(id2));
        for (Integer id : CNs) {
            int degree = networkMap.get(id).size();
            score += (double)1 / Math.log(degree) * Math.log(2);
        }
        return score;
    }

    public double ra(int id1, int id2) {
        if (!networkMap.containsKey(id1) || !networkMap.containsKey(id2)) {
            return 0;
        }
        double score = 0;
        Collection<Integer> CNs = CollectionUtils.intersection(networkMap.get(id1), networkMap.get(id2));
        for (Integer id : CNs) {
            int degree = networkMap.get(id).size();
            score += (double)1 / degree;
        }
        return score;
    }

    public double pa(int id1, int id2) {
        if (!networkMap.containsKey(id1) || !networkMap.containsKey(id2)) {
            return 0;
        }
        return networkMap.get(id1).size() * networkMap.get(id2).size();
    }
}
