package beans.pattern;

import beans.trans.Trans;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;

/**
 * Created by paranoidq on 16/3/7.
 */
public class Pattern {

    // pattern id
    private int id;
    // pattern items
    private List<Integer> items;
    // pattern items (set)
    private Set<Integer> itemsSet;

    // pattern所属的类别
    private ClassType class4Pattern;

    // class支持度
    private int suppL;
    // 全局支持度
    private int suppD;
    // Dx值
    private double DxValue;


    public Pattern() {
        this.items = new LinkedList<>();
        this.itemsSet = new HashSet<>();
    }

    /**
     * 判断pattern是否cover一个trans
     * @param trans
     * @return
     */
    public boolean cover(Trans trans) {
        // return CollectionUtils.isSubCollection(items, trans.getItemsAsList());
        return trans.getItemsAsSet().containsAll(itemsSet);
    }

    /**
     * 自增全局支持度
     */
    public void incrementSuppD() {
        this.suppD++;
    }


    public void addItem(Integer itemId) {
        this.items.add(itemId);
        this.itemsSet.add(itemId);
    }

    public void setClass4Pattern(ClassType class4Pattern) {
        this.class4Pattern = class4Pattern;
    }

    public double getDxValue() {
        return DxValue;
    }

    public void setDxValue(double dxValue) {
        DxValue = dxValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Pattern)) {
            return false;
        }
        Pattern p = (Pattern)o;
        return p.id == this.id;
    }

    @Override
    public int hashCode() {
        return 19*id + 37;
    }

    public int getSuppL() {
        return this.suppL;
    }

    public void setSuppL(int supportL) {
        this.suppL = supportL;
    }

    public int getSuppD() {
        return this.suppD;
    }

    public void setId(int id) {
        this.id = id;
    }
}
