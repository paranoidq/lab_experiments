package predictor.evaluator.Topology;

import beans.Edge;
import org.apache.commons.collections.CollectionUtils;
import predictor.evaluator.AbstractEvaluator;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by paranoidq on 2016/4/13.
 */
public class LP extends AbstractEvaluator {

    @Override
    public double metrics(Edge e) {
        double alpha = 0.01;
        if (!groundNetwork.containsKey(e.getId1()) || !groundNetwork.containsKey(e.getId2())) {
            return 0;
        }
        double hop2s = CollectionUtils.intersection(groundNetwork.get(e.getId1()), groundNetwork.get(e.getId2())).size();
        Set<Integer> cn1 = groundNetwork.get(e.getId1());
        Set<Integer> cncn1 = new HashSet<>();
        for (Integer id : cn1) {
            for (Integer hop2cn : groundNetwork.get(id)) {
                if (!cn1.contains(hop2cn)) { // 不能是cn内部的环
                    cncn1.add(hop2cn);
                }
            }
        }
        double hop3s = CollectionUtils.intersection(cncn1, groundNetwork.get(e.getId2())).size();
        return hop2s + alpha*hop3s;
    }

    @Override
    public String name() {
        return "LP";
    }
}
