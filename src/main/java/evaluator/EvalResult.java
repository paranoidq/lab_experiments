package evaluator;

import util.Constants;
import weka.core.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paranoidq on 16/3/7.
 */
public class EvalResult {
    private String evalName;
    private List<Triple> records;

    private Triple max; // f1最优的triple
    private Triple avg;

    public EvalResult(String evalName) {
        this.evalName = evalName;
        this.records = new ArrayList<>();
        this.max = null;
    }

    public void addRecord(double accuracy, double precision, double recall) {
        Triple t = new Triple(accuracy, precision, recall);
        this.records.add(t);
        if (max == null || t.f1() > max.f1()) {
            max = t;
        }
    }


    public void printMax() {
        StringBuilder sb = new StringBuilder();
        sb.append(evalName).append(Constants.TAB);
        sb.append(Utils.doubleToString(max.accuarcy(), WIDTH, DECIMAL)).append(Constants.TAB)
                .append(Utils.doubleToString(max.precision(), WIDTH, DECIMAL)).append(Constants.TAB)
                .append(Utils.doubleToString(max.recall(), WIDTH, DECIMAL)).append(Constants.TAB)
                .append(Utils.doubleToString(max.f1(), WIDTH, DECIMAL));
        System.out.println(sb.toString());
    }

        public void printAvg() {
        double accuracy = 0;
        double precision = 0;
        double recall = 0;
        double f1 = 0;
        for (Triple t : records) {
            accuracy += t.accuracy;
            precision += t.precision;
            recall += t.recall;
        }
        accuracy /= records.size();
        precision /= records.size();
        recall /= records.size();

        avg = new Triple(accuracy, precision, recall);
        StringBuilder sb = new StringBuilder();
        sb.append(evalName).append(Constants.TAB);
        sb.append(Utils.doubleToString(avg.accuarcy(), WIDTH, DECIMAL)).append(Constants.TAB)
                .append(Utils.doubleToString(avg.precision(), WIDTH, DECIMAL)).append(Constants.TAB)
                .append(Utils.doubleToString(avg.recall(), WIDTH, DECIMAL)).append(Constants.TAB)
                .append(Utils.doubleToString(avg.f1(), WIDTH, DECIMAL));
        System.out.println(sb.toString());
    }


    public static class Triple {
        private double precision;
        private double recall;
        private double f1;
        private double accuracy;

        public Triple(double accuracy, double precision, double recall) {
            this.accuracy = accuracy;
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
        public double accuarcy() {
            return this.accuracy;
        }
    }

    public static final int WIDTH = 7;
    public static final int DECIMAL = 4;
}
