package predictor.evaluator.main;

import predictor.evaluator.AbstractEvaluator;
import predictor.evaluator.Feat.FEAT_ORIGIN;
import predictor.evaluator.Topology.*;

/**
 * Created by paranoidq on 2016/4/13.
 */
public class MainEval {

    public static void main(String[] args) {
//        AbstractEvaluator[] baselines1 = {
//                new CN(),
//                new Jaccard(),
//                new AA(),
//                new RA(),
//                new PA(),
//                new LP(),
//                new LRW()
//        };
//
//        for (AbstractEvaluator evaluator : baselines1) {
//            evaluator.evaluate();
//        }

        AbstractEvaluator[] baselines2 = {
                new FEAT_ORIGIN()

        };

        for (AbstractEvaluator evaluator : baselines2) {
            evaluator.evaluate();
        }
    }
}
