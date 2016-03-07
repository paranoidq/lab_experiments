package beans.trans;

import org.apache.commons.lang3.StringUtils;
import util.Constants;

/**
 * Created by paranoidq on 16/3/7.
 */
public class Item {

    private static int INCR_ID = 0;

    private int id;
    private String key;  // can be null for hashTag
    private String val;
    private ItemType itemType;


    public Item(String key, String val) {
        this.id = ++INCR_ID;
        this.key = key;
        this.val = val;
        this.itemType = ItemType.k_v;
    }

    public Item(String val) {
        this(StringUtils.EMPTY, val);
        this.itemType = ItemType.tag;
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getVal() {
        return val;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setVal(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        if (itemType == ItemType.k_v) {
            return key + Constants.EQUAL + this.val;
        } else {
            return Constants.TAG + this.val;
        }
    }
}
