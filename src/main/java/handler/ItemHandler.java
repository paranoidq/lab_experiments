package handler;

/**
 * Created by paranoidq on 16/3/22.
 */

import beans.trans.Item;
import beans.trans.Trans;
import beans.trans.TransSet;
import org.apache.commons.lang3.StringUtils;
import util.Constants;
import util.FileUtil;
import util.PathRules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Load Items
 */
public class ItemHandler {

    public static void loadItems(String path) throws IOException {
        BufferedReader br = FileUtil.readFile(path);
        String line;
        while ( (line = br.readLine()) != null) {
            line = StringUtils.strip(line);
            String[] sp = line.split(Constants.COMMA);
            int itemId = Integer.parseInt(sp[0]);
            Item.addItem(itemId, sp[1]);
        }
    }


    /**
     * 将过滤后的item写入指定路径
     * @param posTransSet
     * @param negTransSet
     */
    public static void writeFilteredItems(TransSet posTransSet, TransSet negTransSet) {
        Set<Integer> itemIdsSet = new HashSet<>();
        for (Trans trans : posTransSet.getTransSet()) {
            itemIdsSet.addAll(trans.getItems());
        }
        for (Trans trans : negTransSet.getTransSet()) {
            itemIdsSet.addAll(trans.getItems());
        }
        List<Integer> list = new ArrayList<>(itemIdsSet);
        Collections.sort(list);
        try (BufferedWriter bw = FileUtil.writeFile(PathRules.getItemPathAfterTFIDF())) {
            for (Integer itemId : list) {
                bw.write(Item.getItem(itemId).toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}