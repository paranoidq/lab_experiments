package predictor.evaluator.Topology;

import beans.Edge;
import predictor.evaluator.AbstractEvaluator;

/**
 * Created by paranoidq on 2016/4/13.
 */
public class PA extends AbstractEvaluator{

    @Override
    public double metrics(Edge e) {
        if (!groundNetwork.containsKey(e.getId1()) || !groundNetwork.containsKey(e.getId2())) {
            return 0;
        }
        return groundNetwork.get(e.getId1()).size() * groundNetwork.get(e.getId2()).size();
    }

    @Override
    public String name() {
        return "PA";
    }
}
