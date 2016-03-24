package main;

import filter.TfIdfFilter;
import beans.pattern.ClassType;
import beans.trans.TransSet;
import handler.ItemHandler;
import handler.TransHandler;
import util.PathRules;

import java.util.Set;

/**
 * 预处理:
 *      利用TF_IDF过滤item
 *
 * Created by paranoidq on 16/3/23.
 */
public class PreMain {

    public static void main(String[] args) {
        try {
            ItemHandler.loadItems(PathRules.getItemPath());

            TransSet posTransSet = TransHandler.loadTransSet(PathRules.getPosTransPath(), ClassType.POSITIVE);
            TransSet negTransSet = TransHandler.loadTransSet(PathRules.getNegTransPath(), ClassType.NEGATIVE);

            TfIdfFilter filter = new TfIdfFilter();
            filter.filter(posTransSet, negTransSet);
            Set<Integer> remainedItems = filter.getRemainedItems();

            // 重新构建Trans
            TransHandler.filterTransSet(posTransSet, negTransSet, remainedItems);
            // 写入filtered_trans文件
            TransHandler.writeTrans(posTransSet, PathRules.getPosTransPathAfterTFIDF(),
                    negTransSet, PathRules.getNegTransPathAfterTFIDF());
            // 写入filtered_items文件
            ItemHandler.writeItems(remainedItems, PathRules.getItemPathAfterTFIDF());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
