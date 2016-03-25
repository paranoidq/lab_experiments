package handler;

import beans.pattern.ClassType;
import beans.pattern.Pattern;
import beans.trans.Trans;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by paranoidq on 16/3/12.
 */
public class PatternHandler {
    private static int INCREMENT_PAT_ID = -1;

    static Logger logger = LoggerFactory.getLogger(PatternHandler.class);

    public static List<Pattern> loadFpPatterns(int fold, ClassType ct) throws IOException {
        List<Pattern> patternList = new LinkedList<>();

        String patternPath = PathRules.getPatternsPATH(fold, ct);
        BufferedReader br = FileUtil.readFile(patternPath);
        String line;
        while ( (line = br.readLine()) != null) {
            Pattern pattern = new Pattern();
            String[] sp = line.split(Constants.PATTERN_FEQ_SPLIT);

            int support = Integer.parseInt(sp[1]);
            String[] items = sp[0].split(Constants.PATTERN_ENTRY_SPLIT);

            for (String idStr : items) {
                if(idStr.equals(StringUtils.EMPTY)) {
                    logger.error("loadInstances puPattern bug: [" + items.toString() + "]");
                }
                pattern.addItem(Integer.parseInt(idStr));
            }

            pattern.setClass4Pattern(ct);   // 设置pattern是从那个类别中挖出来的
            pattern.setSuppL(support);      // 设置supportL值
            pattern.setId(++INCREMENT_PAT_ID);
            patternList.add(pattern);
        }
        return patternList;
    }


    public static void genPatterns(List<Trans> trans, int fold, ClassType classType) throws Exception {
        TransHandler.genTrans4Mine(trans, fold, classType);

        String transPath = PathRules.getTrans4MinePath(fold, classType);
        String patternsPath = PathRules.getPatternsPATH(fold, classType);

        File file = new File(patternsPath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        if (System.getProperty("os.name").equals("Mac OS X")) {
            ProcessBuilder pb = new ProcessBuilder("./run_fp.sh", Integer.toString(ParamConstants.PATTERN_MIN_LEN),
                    Double.toString(ParamConstants.MIN_SUPPORT),
                    transPath,
                    patternsPath);
            pb.directory(new File("/Users/paranoidq/316-data/polblogs2"));
            Process p = pb.start();

            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while( (line = br.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println();
            p.waitFor();
        } else {
            String cmd = PatternCmdRules.getFpPatternCmd(transPath, patternsPath);
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
        }
    }



}
