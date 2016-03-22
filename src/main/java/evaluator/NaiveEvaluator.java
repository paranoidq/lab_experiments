package evaluator;

import beans.pattern.Pattern;
import beans.trans.WrappedInstances;
import weka.core.Instances;

import java.util.List;

/**
 * Created by paranoidq on 16/3/7.
 */
public class NaiveEvaluator extends Evaluator {

    public NaiveEvaluator(EvalParams params) {
        super(params);
    }

    @Override
    protected List<? extends Pattern> filter(WrappedInstances instances, Instances train, int fold) {
        return null;
    }

    @Override
    protected void printRst() {
        // TODO
    }
}
