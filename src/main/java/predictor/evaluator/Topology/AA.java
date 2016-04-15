package predictor.evaluator.Topology;

import beans.Edge;
import org.apache.commons.collections.CollectionUtils;
import predictor.evaluator.AbstractEvaluator;

import java.util.Collection;

/**
 * Created by paranoidq on 2016/4/13.
 */
public class AA extends AbstractEvaluator {

    @Override
    public double metrics(Edge e) {
        if (!groundNetwork.containsKey(e.getId1()) || !groundNetwork.containsKey(e.getId2())) {
            return 0;
        }
        double score = 0;
        Collection<Integer> CNs = CollectionUtils.intersection(groundNetwork.get(e.getId1()), groundNetwork.get(e.getId2()));
        for (Integer id : CNs) {
            int degree = groundNetwork.get(id).size();
            score += (double)1 / Math.log(degree) * Math.log(2);
        }
        return score;
    }

    @Override
    public String name() {
        return "AA";
    }
}
