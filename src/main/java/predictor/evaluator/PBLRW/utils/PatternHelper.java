package predictor.evaluator.PBLRW.utils;

import beans.pattern.Pattern;
import beans.trans.Trans;
import beans.trans.TransSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import predictor.evaluator.PredictorParamConstants;
import util.ClassifierParamConstants;
import util.Constants;
import util.FileUtil;
import util.PatternCmdRules;

import java.io.*;
import java.util.*;

/**
 * Created by paranoidq on 2016/4/14.
 */
public class PatternHelper {

    private static int INCREMENT_PAT_ID = -1;
    private static Logger logger = Logger.getLogger(PatternHelper.class);

    /* PBLRW 使用
    **********************************************************************************************************
     */
    public static List<Pattern> buildFpPatterns(String transPath, String patternsPath) {
        File file = new File(patternsPath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            Process p;
            if (System.getProperty("os.name").equals("Mac OS X")) {
                ProcessBuilder pb = new ProcessBuilder("./run_fp.sh", Integer.toString(PredictorParamConstants.PATTERN_MIN_LEN),
                        Double.toString(PredictorParamConstants.MIN_SUPPORT_FP),
                        transPath,
                        patternsPath);
                pb.directory(new File("/Users/paranoidq/316-data/polblogs2"));
                p = pb.start();
                p.waitFor();
            } else {
                String cmd = PatternCmdRules.getFpPatternCmd(transPath, patternsPath);
                p = Runtime.getRuntime().exec(cmd);
                p.waitFor();
            }
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while( (line = br.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return loadPatterns(patternsPath);
    }

    private static List<Pattern> loadPatterns(String patternPath) {
        List<Pattern> patternList = new LinkedList<>();

        try (BufferedReader br = FileUtil.readFile(patternPath)) {
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
                pattern.setSuppL(support);      // 设置supportL值
                pattern.setId(++INCREMENT_PAT_ID);
                patternList.add(pattern);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patternList;
    }


    /**
     *
     * @param transSet
     * @param patterns
     * @param coverage 整个transSet被cover的百分比
     * @param perInstanceCoverage 每个instance至少要求被cover几次
     * @return
     */
    public static List<Pattern> filter(TransSet transSet, List<Pattern> patterns, double coverage, double perInstanceCoverage) {
        List<Pattern> filtered = new LinkedList<>();
        double threshold = transSet.size() * coverage;

        Map<Integer, Integer> instanceCoverTimes = new HashMap<>();
        Set<Integer> coveredInstances = new HashSet<>();

        Iterator<Pattern> iterator = patterns.iterator();
        while (iterator.hasNext()) {
            Pattern pattern = iterator.next();
            if ((double) coveredInstances.size() > threshold) {
                break;
            }
            for (int i=0; i<transSet.getTransSet().size(); i++) {
                if (pattern.cover(transSet.getTransSet().get(i))) {
                    if (instanceCoverTimes.containsKey(i)) {
                        instanceCoverTimes.put(i, instanceCoverTimes.get(i) + 1);
                    } else {
                        instanceCoverTimes.put(i, 1);
                    }
                    if (!coveredInstances.contains(i) && instanceCoverTimes.get(i) >= perInstanceCoverage) {
                        coveredInstances.add(i);
                    }
                }
            }
            filtered.add(pattern);
        }
        return filtered;
    }

    public static void writeFilteredPatterns(List<Pattern> patterns, String path) {
        try (BufferedWriter bw = FileUtil.writeFile(path)) {
            for (Pattern pattern : patterns) {
                bw.write(pattern.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void calPatternStrength(List<Pattern> patterns) {
        for (Pattern p : patterns) {
            p.calStrength();
        }
    }
}
