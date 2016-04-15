package predictor.evaluator.Topology;

import beans.Edge;
import org.apache.commons.collections.CollectionUtils;
import predictor.evaluator.AbstractEvaluator;

/**
 * Created by paranoidq on 2016/4/13.
 */
public class Jaccard extends AbstractEvaluator {

    @Override
    public double metrics(Edge e) {
        if (!groundNetwork.containsKey(e.getId1()) || !groundNetwork.containsKey(e.getId2())) {
            return 0;
        }
        return (double) CollectionUtils.intersection(groundNetwork.get(e.getId1()), groundNetwork.get(e.getId2())).size()
                / CollectionUtils.union(groundNetwork.get(e.getId1()), groundNetwork.get(e.getId2())).size();
    }

    @Override
    public String name() {
        return "JA";
    }
}
