package util;

import util.ParamConstants;

/**
 * Created by paranoidq on 16/3/22.
 */
public class PatternCmdRules {

    private static final String _PREFIX = "/Users/paranoidq/316-data/polblogs2/";

    public static String getFpPatternCmd(String trans4Fold_Path, String pats4Fold4Class_Path) {
        StringBuilder sb = new StringBuilder();
        sb.append(_PREFIX);
        sb.append("fpgrowth -tm -q1 -m")
                .append(ParamConstants.PATTERN_MIN_LEN + " -s" + ParamConstants.MIN_SUPPORT)
                .append("-v\"|%a\" " + trans4Fold_Path + " " + pats4Fold4Class_Path);
        return sb.toString();
    }
}
