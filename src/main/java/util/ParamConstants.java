package util;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;

/**
 * Created by paranoidq on 16/3/7.
 */
public interface ParamConstants {

    // 分类器
    Classifier classifier = new NaiveBayes();


    int FP_IG_DELTA = 1; // 每个instance至少被pattern覆盖1次
    double COVERAGE_DELTA = 0.8;

    // evaluator
    int NUM_FOLDS = 4;
    double TRAIN_RATIO = 0.5;

    // fp_growth
    int PATTERN_MIN_LEN = 2;
    double MIN_SUPPORT = 30;
    String FP_EXE_PATH = "fp_growth.exe";


    // TFIDF过滤参数, 即每篇文章的feats留下80%的词汇
    double TFIDF_DELTA = 0.8;

}
