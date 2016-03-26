package main;

import beans.Edge;
import beans.trans.TransSet;
import handler.EdgeHandler;
import handler.FeatsHandler;
import handler.ItemHandler;
import handler.TransHandler;
import util.PathRules;

import java.util.List;
import java.util.Map;
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

            Map<Integer, Set<Integer>> feats = FeatsHandler.loadFeats(PathRules.getFeatsPath());
            Map<Integer, List<Integer>> filteredFeats = FeatsHandler.filterByTFIDF(feats);

            // 根据过滤过的feats构建trans
            List<Edge> edges = EdgeHandler.loadEdges(PathRules.getEdgesPath());
            TransSet posTrans = TransHandler.genPosTransRandomly(filteredFeats, edges);
            TransSet negTrans = TransHandler.genNegTransRandomly(filteredFeats, edges, posTrans.size());


            // 写入filtered_items文件
           ItemHandler.writeFilteredItems(posTrans, negTrans);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
