package util;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;

/**
 * Created by paranoidq on 16/3/7.
 */
public interface ClassifierParamConstants {

    String _PREFIX = "E:\\ExperimentCode\\polblogs2";

    long seed = System.nanoTime();

    // 分类器
    Classifier classifier = new LibSVM();
    // SMO // A fast algorithm for training support vector machines


    int PER_INS_COVER_TIMES = 10; // 每个instance至少被pattern覆盖1次
    double COVERAGE_DELTA = 0.9;

    // evaluator
    int NUM_FOLDS = 4;
    double TRAIN_RATIO = 0.5;

    // fp_growth
    int PATTERN_MIN_LEN = 2;
    int MIN_SUPPORT_FP = 20;

    // cosine
    double MIN_SUPPORT_COSINE = 1;
    double COSINE = 0.6;


    // TFIDF过滤参数, 即每篇文章的feats留下80%的词汇
    double TFIDF_DELTA = 0.8;


    static void printParams() {
        System.out.println("Classifier: " + classifier.toString());
        System.out.println("Coverage_Delta: " + COVERAGE_DELTA);
        System.out.println("Folds: " + NUM_FOLDS);
        System.out.println("FP_MIN_SUPP: " + MIN_SUPPORT_FP);
        System.out.println("COSINE_MIN_SUPP: " + MIN_SUPPORT_COSINE);
        System.out.println("COSINE: " + COSINE);
        System.out.println("RandomSeed: " + seed);
    }


}
