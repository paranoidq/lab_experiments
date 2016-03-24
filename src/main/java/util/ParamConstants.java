package util;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;

/**
 * Created by paranoidq on 16/3/7.
 */
public interface ParamConstants {

    // 分类器
    Classifier classifier = new NaiveBayesMultinomial();




    int FP_IG_DELTA = 1; // 每个instance至少被pattern覆盖1次
    double COVERAGE_DELTA = 0.8;

    // evaluator
    int NUM_FOLDS = 2;
    double TRAIN_RATIO = 0.5;

    // fp_growth
    int PATTERN_MIN_LEN = 2;
    double MIN_SUPPORT = 0.8;
    String FP_EXE_PATH = "fp_growth.exe";

}
