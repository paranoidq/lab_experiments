package beans.trans;

import beans.pattern.ClassType;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * Created by paranoidq on 16/3/7.
 */

public class Trans {

    private List<Integer> items;
    private Set<Integer> itemsSet;
    private ClassType ct;


    public Trans(ClassType ct) {
        this.ct = ct;
        this.items = Lists.newLinkedList();
        this.itemsSet = Sets.newHashSet();
    }


    public void addItemId(int itemId){
        this.items.add(itemId);
        this.itemsSet.add(itemId);
    }

    public void setFeats(List<Integer> feats) {
        feats.sort((o1, o2) -> Integer.compare(o1, o2));
        this.items = feats;
    }

    public List<Integer> getItemsAsList(){
        return this.items;
    }

    public Set<Integer> getItemsAsSet() { return this.itemsSet; }

    public ClassType getCt() {
        return this.ct;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer itemId : items) {
            sb.append(itemId).append(",");
        }
//        if(!items.isEmpty()) {
//            sb.replace(sb.length()-1, sb.length(), "");
//        }
        if (sb.length() < 1) {
            System.out.println("");
        }
        sb.replace(sb.length()-1, sb.length(), "");
        return sb.toString();
    }


    /**
     * updated: 使用hash set,提高处理效率
     * @param itemId
     * @return
     */
    public boolean contains(Integer itemId) {
        return itemsSet.contains(itemId);
    }

}
