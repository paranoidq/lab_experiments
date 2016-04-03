package main;

import beans.pattern.ClassType;
import beans.trans.TransSet;
import evaluator.EvalParams;
import evaluator.LinkEvaluator;
import handler.ItemHandler;
import handler.TransHandler;
import util.ClassifierParamConstants;
import util.PathRules;


/**
 * Created by paranoidq on 16/3/7.
 */
public class Main {

    public static void main(String[] args) {

        try {
            ItemHandler.loadItems(PathRules.getItemPathAfterTFIDF());
            TransSet posTransSet = TransHandler.loadTransSetAfterTFIDF(PathRules.getPosTransPathAfterTFIDF(), ClassType.POSITIVE);
            TransSet negTransSet = TransHandler.loadTransSetAfterTFIDF(PathRules.getNegTransPathAfterTFIDF(), ClassType.NEGATIVE);

            EvalParams params = new EvalParams(ClassifierParamConstants.classifier, ClassifierParamConstants.NUM_FOLDS);

            LinkEvaluator evaluator = new LinkEvaluator(params, posTransSet, negTransSet);
            evaluator.evaluate();

            /*
            print result
             */
            ClassifierParamConstants.printParams();
            evaluator.printResult();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
