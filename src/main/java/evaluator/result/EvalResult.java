package evaluator.result;

import util.Constants;
import weka.core.Utils;

import java.util.List;

/**
 * Created by paranoidq on 16/3/7.
 */
public class EvalResult {


    private List<Triple> records;
    private Triple max; // 最优的triple

    // 平均的??


    public Triple getMax() {
        return max;
    }

    public Triple getAvg() {
        double p = 0;
//        double r = 0;
//        double f = 0;

        for (Triple t : records) {
            p += t.getP();
//            r += t.getR();
        }
        int num = records.size();
        p = p / num;
//        r = r / num;
        return new Triple(p);
    }

    public void addRecord(double p) {
        Triple t = new Triple(p);
        this.records.add(t);
        if (max == null || t.getP() > max.getP()) {
            max = t;
        }
    }


    public static final int WIDTH = 7;
    public static final int DECIMAL = 4;

    public void printMax() {
        StringBuilder sb = new StringBuilder();
        sb.append(Utils.doubleToString(max.getP(), WIDTH, DECIMAL)).append(Constants.NEWLINE);
//                .append(Utils.doubleToString(max.getR(), WIDTH, DECIMAL)).append(Constants.TAB)
//                .append(Utils.doubleToString(max.getF(), WIDTH, DECIMAL)).append(Constants.NEWLINE);
        System.out.println(sb.toString());
    }



    public static class Triple {
        private double p;
//        private double r;
//        private double f;

        public Triple(double precision) {
            this.p = precision;
//            this.r = recall;
//            this.f = 2 * precision * recall / (precision + recall);
        }

        public double getP() {
            return p;
        }

//        public double getR() {
//            return r;
//        }
//
//        public double getF() {
//            return f;
//        }
    }
}
