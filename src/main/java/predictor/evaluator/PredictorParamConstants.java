package predictor.evaluator;

/**
 * Created by paranoidq on 2016/4/2.
 */
public interface PredictorParamConstants {

    int MAX_ID = 361;

    double TRAIN_PARTITION = .8;

    double TEST_RATIO_VS_TRAIN = 1;

    // AUC
    int N_AUC = 20000;


    // Precision
    int L = 100;


    int PATTERN_MIN_LEN = 2;

    int MIN_SUPPORT_FP = 20;

    int PER_INS_COVER_TIMES = 10; // 每个instance至少被pattern覆盖1次
    double COVERAGE_DELTA = 0.9;


    // negative pattern penalty
    int penalty = 1;



}
