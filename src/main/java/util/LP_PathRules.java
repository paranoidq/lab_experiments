package util;

import java.io.File;

/**
 * Created by paranoidq on 2016/4/12.
 */
public class LP_PathRules {

    private static final String _PREFIX = ClassifierParamConstants._PREFIX;

    public static String getOriginTrainPath() {
        return _PREFIX + File.separator + "FEAT" + File.separator + "origin_train";
    }
    public static String getOriginTestPath() {
        return _PREFIX + File.separator + "FEAT" + File.separator + "origin_test_pos";
    }
    public static String getOriginTestPath2() {
        return _PREFIX + File.separator + "FEAT" + File.separator + "origin_test_neg";
    }
}
