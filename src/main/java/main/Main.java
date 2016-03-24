package main;

import beans.pattern.ClassType;
import beans.trans.TransSet;
import evaluator.EvalParams;
import evaluator.LinkEvaluator;
import handler.ItemHandler;
import handler.TransHandler;
import util.ParamConstants;
import util.PathRules;


/**
 * Created by paranoidq on 16/3/7.
 */
public class Main {

    public static void main(String[] args) {

        try {
//            ItemHandler.loadItems();
//            //WrappedInstances instances = TransHandler.Loader.loadTrans(Constants._TRANS_PATH);
//
//            TransSet posTransSet = TransHandler.loadTransSet(PathRules.getPosTransPath(), ClassType.POSITIVE);
//            TransSet negTransSet = TransHandler.loadTransSet(PathRules.getNegTransPath(), ClassType.NEGATIVE);
//
//            TfIdfFilter filter = new TfIdfFilter();
//            filter.filter(posTransSet, negTransSet);
//            Set<Integer> remainedItems = filter.getRemainedItems();
//
//            // 重新构建Trans
//            TransHandler.filterTransSet(posTransSet, remainedItems);
//            TransHandler.filterTransSet(negTransSet, remainedItems);

            ItemHandler.loadItems(PathRules.getItemPathAfterTFIDF());
            TransSet posTransSet = TransHandler.loadTransSet(PathRules.getPosTransPathAfterTFIDF(), ClassType.POSITIVE);
            TransSet negTransSet = TransHandler.loadTransSet(PathRules.getNegTransPathAfterTFIDF(), ClassType.NEGATIVE);

            EvalParams params = new EvalParams(ParamConstants.classifier, ParamConstants.NUM_FOLDS);

            LinkEvaluator evaluator = new LinkEvaluator(params, posTransSet, negTransSet);
            evaluator.evaluate();

            /*
            print result
             */
            evaluator.printResult();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
