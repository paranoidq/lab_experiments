package handler;

import beans.pattern.ClassType;
import beans.pattern.FPPattern;
import beans.pattern.PUPattern;
import beans.trans.Item;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Constants;
import util.FileUtil;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by paranoidq on 16/3/12.
 */
public class PatternHandler {
    static Logger logger = LoggerFactory.getLogger(PatternHandler.class);

    public static class Loader {
        public static List<FPPattern> loadFPPatterns(int fold) throws IOException {
            List<FPPattern> patternList = new LinkedList<>();

            String patternPath = Constants._fp_pattern4fold + fold + Constants._pat_postfix;
            BufferedReader br = FileUtil.readFile(patternPath);
            String line;
            while ( (line = br.readLine()) != null) {
                FPPattern pattern = new FPPattern();
                String[] sp = line.split(Constants.PATTERN_FEQ_SPLIT);

                int support = Integer.parseInt(sp[1]);
                String[] entrys = sp[0].split(Constants.PATTERN_ENTRY_SPLIT);

                for (String idStr : entrys) {
                    if(idStr.equals(StringUtils.EMPTY)) {
                        logger.error("loadSrc FpPattern bug: [" + entrys.toString() + "]");
                    }
                    pattern.addEntry(Item.getItem(Integer.parseInt(idStr)));
                }
                pattern.setSupport(support); // 设置support值
                patternList.add(pattern);
            }
            return patternList;
        }


        public static List<PUPattern> loadPUPatterns(int fold, ClassType ct) throws IOException {
            List<PUPattern> patternList = new LinkedList<>();

            String patternPath = Constants._pu_pattern4fold_4c + fold + ct.toString() + Constants._pat_postfix;
            BufferedReader br = FileUtil.readFile(patternPath);
            String line;
            while ( (line = br.readLine()) != null) {
                PUPattern pattern = new PUPattern();
                String[] sp = line.split(Constants.PATTERN_FEQ_SPLIT);

                int support = Integer.parseInt(sp[1]);
                String[] entrys = sp[0].split(Constants.PATTERN_ENTRY_SPLIT);

                for (String idStr : entrys) {
                    if(idStr.equals(StringUtils.EMPTY)) {
                        logger.error("loadSrc puPattern bug: [" + entrys.toString() + "]");
                    }
                    pattern.addEntry(Item.getItem(Integer.parseInt(idStr)));
                }

                pattern.setClass4Pattern(ct);  // 设置pattern是从那个类别中挖出来的
                pattern.setSuppL(support);      // 设置supportL值
                patternList.add(pattern);
            }
            return patternList;
        }
    }

    public static class Generator {

        public static void genFPPattern(Instances train, int fold) throws Exception {
            TransHandler.Generator.generateFPTrans(train, fold);
            String transPath = Constants._fp_trans4fold + fold + Constants._trans_postfix;
            String patPath = Constants._fp_pattern4fold + fold + Constants._pat_postfix;

            String cmd = "";  // TODO
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
        }

        public static void genPUPattern(Instances train, int fold, ClassType ct) throws Exception {
            TransHandler.Generator.generatePUTrans(train, fold, ct);
            String transPath = Constants._pu_trans4fold_4c + fold + Constants._trans_postfix;
            String patPath = Constants._pu_pattern4fold_4c + fold + ct.toString() + Constants._pat_postfix;

            String cmd = "";  // TODO
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
        }
    }



}
