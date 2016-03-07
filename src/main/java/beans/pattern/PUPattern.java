package beans.pattern;

/**
 * Created by paranoidq on 16/3/7.
 */
public class PUPattern {

    private Pattern pattern;

    private int suppLink;
    private int suppNoLink;
    private int supp;

    private double dValue;

    private double nLink;
    private double idf;


    // pattern所属的类别
    private PatternType patternType;

    public void incrSuppLink() {
        this.suppLink++;
    }

    public void incrSuppNoLink() {
        this.suppNoLink++;
    }

    public void incrSupp() {
        this.supp++;
    }

    public void reset() {
        this.suppLink = 0;
        this.suppNoLink = 0;
        this.supp = 0;
    }



}
