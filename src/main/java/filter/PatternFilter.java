package filter;

import beans.pattern.Pattern;
import beans.trans.Trans;
import beans.trans.TransSet;
import util.FileUtil;
import util.ParamConstants;
import util.PathRules;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by paranoidq on 16/3/22.
 */
public class PatternFilter {

    /**
     * 过滤规则:
     *      每次选取较大的Dx值得pattern,直到数据集中的instance被cover到一定的比例
     *      (不按照每个instance都被cover1次来算,因为有些instance始终无法被cover到 !!)
     * @param transSet
     * @param patterns
     * @return
     */
    public static List<Pattern> filter(TransSet transSet, List<Pattern> patterns, int fold) {
        return filterByCoverage(transSet, patterns, fold, ParamConstants.COVERAGE_DELTA);
    }


    /**
     * 几种方案:
     *      (1) train中被cover一定的比例
     *      (2) test中被cover一定的比例
     *      (3) train+test中被cover一定的比例
     * @param transSet
     * @param patterns
     * @param coverage
     * @return
     */
    private static List<Pattern> filterByCoverage(TransSet transSet, List<Pattern> patterns, int fold,
                                                  double coverage) {
        List<Pattern> filtered = new LinkedList<>();

        double threshold = transSet.size() * coverage;
        Set<Integer> coveredInstances = new HashSet<>();
        int total = patterns.size();
        Iterator<Pattern> iterator = patterns.iterator();
        while (iterator.hasNext()) {
            Pattern pattern = iterator.next();
            if ((double)coveredInstances.size() >= threshold) {
                break;
            }
//            coveredInstances.addAll(
//                    transSet.getTransSet().stream().filter(trans -> pattern.cover(trans))
//                            .collect(Collectors.toList()));
            for (int i=0; i<transSet.getTransSet().size(); i++) {
                if (pattern.cover(transSet.getTransSet().get(i))) {
                    coveredInstances.add(i);
                }
            }
            filtered.add(pattern);
        }
        writeFilteredPatterns(filtered, total, fold);

        return filtered;
    }

    private static void writeFilteredPatterns(List<Pattern> filtered, int total, int fold) {
        String path = PathRules.getFilteredPatternPath(fold);
        String reprPath = PathRules.getFilteredPatternReprPath(fold);
        try (BufferedWriter bw = FileUtil.writeFile(path);
             BufferedWriter bw2 = FileUtil.writeFile(reprPath)) {
            bw.write("filtered patterns: " + filtered.size() + "/" + total);
            bw2.write("filtered patterns: " + filtered.size() + "/" + total);
            bw.newLine();
            bw2.newLine();

            for (Pattern pattern : filtered) {
                bw.write(pattern.toString());
                bw.newLine();

                bw2.write(pattern.repr());
                bw2.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
