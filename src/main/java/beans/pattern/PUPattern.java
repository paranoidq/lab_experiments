package beans.pattern;

import beans.trans.Item;

/**
 * Created by paranoidq on 16/3/7.
 */
public class PUPattern extends Pattern {


    private int suppL;
    private int suppD;

    private double DxValue;


    public PUPattern() {
    }

    // pattern所属的类别
    private ClassType class4Pattern;

    public void incrSuppD() {
        this.suppD++;
    }

    public void reset() {
        this.suppL = 0;
        this.suppD = 0;
    }

    public void addEntry(Item entry) {
        super.addEntry(entry);
    }

    public void setClass4Pattern(ClassType class4Pattern) {
        this.class4Pattern = class4Pattern;
    }

    public ClassType getClass4Pattern() {
        return class4Pattern;
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
