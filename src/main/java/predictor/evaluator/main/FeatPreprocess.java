package predictor.evaluator.main;

import handler.FeatsHandler;
import handler.ItemHandler;
import util.Constants;
import util.FileUtil;
import util.PathRules;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by paranoidq on 2016/4/13.
 */
public class FeatPreprocess {

    public static void main(String[] args) {
        try {
            ItemHandler.loadItems(PathRules.getItemPath());
            Map<Integer, Set<Integer>> feats = FeatsHandler.loadFeats(PathRules.getoriginFeatsPath());
            Map<Integer, List<Integer>> filteredFeats = FeatsHandler.filterByTFIDF(feats);
            writeFeats(filteredFeats);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void writeFeats(Map<Integer, List<Integer>> filteredFeats) {
        try (BufferedWriter bw = FileUtil.writeFile(PathRules.getFilteredFeatsPath())) {
            for (Integer id : filteredFeats.keySet()) {
                List<Integer> feats = filteredFeats.get(id);
                StringBuilder sb = new StringBuilder();
                sb.append(id).append(Constants.COLON);
                for (Integer feat : feats) {
                    sb.append(feat).append(Constants.COMMA);
                }
                sb.replace(sb.length()-1, sb.length(), Constants.NEWLINE);
                bw.write(sb.toString());
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
