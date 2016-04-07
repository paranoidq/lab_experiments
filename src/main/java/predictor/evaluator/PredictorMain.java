package predictor.evaluator;

/**
 * Created by paranoidq on 2016/4/4.
 */
public class PredictorMain {

    public static void main(String[] args) {

        AUCEvaluator evaluator = new AUCEvaluator();
        evaluator.evaluate();

        System.out.println("---------------------");

        PrecisionEvaluator precisionEvaluator = new PrecisionEvaluator();
        precisionEvaluator.evaluate();
    }
}
