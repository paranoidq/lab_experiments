package util;

import beans.pattern.ClassType;

import java.io.File;

/**
 * Created by paranoidq on 16/3/22.
 */
public class PathRules {

    private static final String _PREFIX = "/Users/paranoidq/316-data/polblogs2";


    /**
     * 获取tf-idf之后的trans
     * @return
     */
    public static String getPosTransPathAfterTFIDF() {
        return _PREFIX + File.separator + "filtered_pos_trans";
    }


    /**
     * 获取tf-idf之后的trans
     * @return
     */
    public static String getNegTransPathAfterTFIDF() {
        return _PREFIX + File.separator + "filtered_neg_trans";
    }

    /**
     * 获取原始的item path
     * @return
     */
    public static String getItemPath() {
        return _PREFIX + File.separator + "items";
    }

    public static String getItemPathAfterTFIDF() {
        return _PREFIX + File.separator + "filtered_items";
    }

    /**
     * 获取TFIDF之前的uid-feats路径
     * @return
     */
    public static String getFeatsPath() {
        return _PREFIX + File.separator + "features";
    }

    /**
     * 获取原始的edges路径
     * @return
     */
    public static String getEdgesPath() {
        return _PREFIX + File.separator + "edges";
    }

    /**
     * 获取随机生成的pos edge路径, edges与pos_trans对应
     * @return
     */
    public static String getPosEdgesPathAfterTFIDF() {
        return _PREFIX + File.separator + "filtered_pos_edges";
    }
    /**
     * 获取随机生成的neg edge路径, edges与neg_trans对应
     * @return
     */
    public static String getNegEdgesPathAfterTFIDF() {
        return _PREFIX + File.separator + "filtered_neg_edges";
    }

    /**
     *  用于在每个类别上挖pattern时产生的临时trans
     * @param fold
     * @param classType
     * @return
     */
    public static String getTrans4MinePath(int fold, ClassType classType) {
        return _PREFIX + File.separator + fold + File.separator + "mine_trans"
                + File.separator + classType.toString();

    }

    public static String getAugTrainPath(int fold) {
        return _PREFIX + File.separator + fold + File.separator + "aug_train";
    }
    public static String getAugTestPath(int fold) {
        return _PREFIX + File.separator + fold + File.separator + "aug_test";
    }

    public static String getOriginTrainPath(int fold) {
        return _PREFIX + File.separator + fold + File.separator + "origin_train";
    }
    public static String getOriginTestPath(int fold) {
        return _PREFIX + File.separator + fold + File.separator + "origin_test";
    }

    public static String getPatternsPath(int fold, ClassType classType) {
        return _PREFIX + File.separator + fold + File.separator + "patterns"
                + File.separator + classType.toString();
    }

    public static String getRandomSeedPath() {
        return _PREFIX + File.separator + "random_seeds";
    }

}
