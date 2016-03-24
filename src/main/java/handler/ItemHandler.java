package handler;

/**
 * Created by paranoidq on 16/3/22.
 */

import beans.trans.Item;
import org.apache.commons.lang3.StringUtils;
import util.Constants;
import util.FileUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Set;

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
     * 将item写入指定的文件路径
     * (无序)
     * @param items
     * @param path
     */
    public static void writeItems(Set<Integer> items, String path) {
        try {
            BufferedWriter bw = FileUtil.writeFile(path);
            for (Integer itemId : items) {
                bw.write(Item.getItem(itemId).toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}