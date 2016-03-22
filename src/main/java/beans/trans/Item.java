package beans.trans;


import org.apache.commons.lang3.StringUtils;
import util.Constants;
import util.FileUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by paranoidq on 16/3/7.
 */

public class Item {

    private static Map<Integer, Item> items = new HashMap<>();
    static {
        ItemHandler.loadItems();
    }

    public static Map<Integer, Item> getItems() {
        return items;
    }

    public static Item getItem(int id) {
        return items.get(id);
    }


    private int id;
    private String item;

    public Item(int id, String item) {
        this.id = id;
        this.item = item;
    }

    public int getId() {
        return this.id;
    }

    /**
     * Load Items
     */
    public static class ItemHandler {

        public static void loadItems() {
            if (items.isEmpty()) {
                try {
                    loadItems0();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //TODO
        private static void loadItems0() throws IOException {
            BufferedReader br = FileUtil.readFile(Constants._item_path);
            String line;
            while ( (line = br.readLine()) != null) {
                line = StringUtils.strip(line);
                String[] sp = line.split(Constants.COMMA);
                items.put(Integer.parseInt(sp[0]), Integer.parseInt(sp[1]));
            }
        }
    }

}
