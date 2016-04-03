package util;

import java.io.File;

/**
 * Created by paranoidq on 16/3/22.
 */
public class PatternCmdRules {

    private static final String _PREFIX = ClassifierParamConstants._PREFIX;

    public static String getFpPatternCmd(String trans4Fold_Path, String pats4Fold4Class_Path) {
        StringBuilder sb = new StringBuilder();
        sb.append(_PREFIX + File.separator);
        sb.append("fpgrowth.exe -tc -q1 -m")
                .append(ClassifierParamConstants.PATTERN_MIN_LEN + " -s" + ClassifierParamConstants.MIN_SUPPORT_FP)
                .append(" -v\"|%a\" " + trans4Fold_Path + " " + pats4Fold4Class_Path);
        System.out.println(sb.toString());
        return sb.toString();
    }

    public static String getCosinePatternCmd(String trans4Fold_Path, String pats4Fold4Class_Path) {
        StringBuilder sb = new StringBuilder();
        sb.append(_PREFIX + File.separator);
        sb.append("fpgrowth_cosine.exe -x -q1 -c")
                .append(ClassifierParamConstants.COSINE + " -s" + ClassifierParamConstants.MIN_SUPPORT_COSINE)
                .append(" -v\"|%a\" " + trans4Fold_Path + " " + pats4Fold4Class_Path);
        System.out.println(sb.toString());
        return sb.toString();
    }
}
