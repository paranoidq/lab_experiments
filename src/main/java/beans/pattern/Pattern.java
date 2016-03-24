package beans.pattern;

import beans.trans.Trans;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by paranoidq on 16/3/7.
 */
public class Pattern {

    // pattern items
    private List<Integer> items;
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
    }

    /**
     * 判断pattern是否cover一个trans
     * @param trans
     * @return
     */
    public boolean cover(Trans trans) {
        return trans.getItems().contains(items);
    }

    /**
     * 自增全局支持度
     */
    public void incrementSuppD() {
        this.suppD++;
    }


    public void addItem(Integer itemId) {
        this.items.add(itemId);
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
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
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
}
