package predictor.evaluator.PBLRW.utils;

import beans.trans.Item;
import org.apache.commons.lang3.StringUtils;
import util.Constants;
import util.FileUtil;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by paranoidq on 2016/4/14.
 */
public class ItemHelper {

    public static void loadItems(String path) {
        try (BufferedReader br = FileUtil.readFile(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = StringUtils.strip(line);
                String[] sp = line.split(Constants.COMMA);
                int itemId = Integer.parseInt(sp[0]);
                Item.addItem(itemId, sp[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
