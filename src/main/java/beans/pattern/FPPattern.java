package beans.pattern;

import beans.trans.Item;

/**
 * Created by paranoidq on 16/3/7.
 */
@Deprecated
public class FPPattern extends Pattern {

    private double revelance;
    private double gain;

    private int support;

    public int getSupport(){
        return this.support;
    }
    public void setSupport(int support){
        this.support = support;
    }



    public double getGain() {
        return this.gain;
    }

    public double getRevelance() {
        return this.revelance;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }

    public void setRevelance(double revelance) {
        this.revelance = revelance;
    }





    public void addEntry(Item entry) {
        super.addEntry(entry);
    }
}
