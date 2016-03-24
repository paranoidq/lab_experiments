package filter;

import beans.pattern.Pattern;
import beans.trans.Trans;
import beans.trans.TransSet;
import util.ParamConstants;

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
     * @param iterator
     * @return
     */
    public static List<Pattern> filter(TransSet transSet, Iterator<Pattern> iterator) {
        return filterByCoverage(transSet, iterator, ParamConstants.COVERAGE_DELTA);
    }


    /**
     * 几种方案:
     *    * (1) train中被cover一定的比例
     *      (2) test中被cover一定的比例
     *      (3) train+test中被cover一定的比例
     * @param transSet
     * @param iterator
     * @param coverage
     * @return
     */
    private static List<Pattern> filterByCoverage(TransSet transSet, Iterator<Pattern> iterator, double coverage) {
        List<Pattern> filtered = new LinkedList<>();

        double threshold = transSet.size() * coverage;
        Set<Trans> coveredInstances = new HashSet<>();
        while (iterator.hasNext()) {
            Pattern pattern = iterator.next();
            if ((double)coveredInstances.size() >= threshold) {
                break;
            }
            coveredInstances.addAll(
                    transSet.getTransSet().stream().filter(trans -> pattern.cover(trans))
                            .collect(Collectors.toList()));
            filtered.add(pattern);
        }
        return filtered;
    }
}
