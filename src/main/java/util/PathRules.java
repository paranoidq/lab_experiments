package util;

import beans.pattern.ClassType;

import java.io.File;

/**
 * Created by paranoidq on 16/3/22.
 */
public class PathRules {

    private static final String _PREFIX = "/Users/paranoidq/316-data/polblogs2";
    private static final String _POSTFIX = "txt";

    /**
     * 获取原始的pos trans path
     * @return
     */
    public static String getPosTransPath() {
        return _PREFIX + File.separator + "pos_trans";
    }

    /**
     * 获取tf-idf之后的trans
     * @return
     */
    public static String getPosTransPathAfterTFIDF() {
        return _PREFIX + File.separator + "filtered_pos_trans";
    }

    /**
     * 获取原始的neg trans path
     * @return
     */
    public static String getNegTransPath() {return _PREFIX + File.separator + "neg_trans"; }

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

    public static String getItemPathRemoved() {
        return _PREFIX + File.separator + "removed_items";
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

    public static String getPatternsPATH(int fold, ClassType classType) {
        return _PREFIX + File.separator + fold + File.separator + "patterns"
                + File.separator + classType.toString();
    }


}
