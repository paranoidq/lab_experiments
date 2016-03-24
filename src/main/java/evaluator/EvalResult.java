package evaluator;

import util.Constants;
import weka.core.Utils;

import java.util.List;

/**
 * Created by paranoidq on 16/3/7.
 */
public class EvalResult {

    private List<Triple> records;
    private Triple max; // f1最优的triple


    public void addRecord(double precison, double recall) {
        Triple t = new Triple(precison, recall);
        this.records.add(t);
        if (max == null || t.f1() > max.f1()) {
            max = t;
        }
    }


    public void printMax() {
        StringBuilder sb = new StringBuilder();
        sb.append(Utils.doubleToString(max.precision(), WIDTH, DECIMAL)).append(Constants.NEWLINE)
                .append(Utils.doubleToString(max.recall(), WIDTH, DECIMAL)).append(Constants.TAB)
                .append(Utils.doubleToString(max.f1(), WIDTH, DECIMAL)).append(Constants.NEWLINE);
        System.out.println(sb.toString());
    }


    public static class Triple {
        private double precision;
        private double recall;
        private double f1;

        public Triple(double precision, double recall) {
            this.precision = precision;
            this.recall = recall;
            this.f1 = 2 * precision * recall / (precision + recall);
        }

        public double precision() {
            return this.precision;
        }
        public double recall() {
            return this.recall;
        }
        public double f1() {
            return this.f1;
        }
    }

    public static final int WIDTH = 7;
    public static final int DECIMAL = 4;
}
