package evaluator;

import beans.pattern.ClassType;
import beans.pattern.FPPattern;
import beans.pattern.Pattern;
import beans.trans.WrappedInstances;
import handler.PatternHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ParamConstants;
import weka.core.Instance;
import weka.core.Instances;

import java.io.IOException;
import java.util.*;

/**
 * Created by paranoidq on 16/3/7.
 */
public class FPEvaluator extends Evaluator {
    static Logger logger = LoggerFactory.getLogger(FPEvaluator.class);

    public FPEvaluator(EvalParams params) {
        super(params);
    }


    @Override
    public List<? extends Pattern> filter(WrappedInstances instances, Instances train, int fold) {
        try {
            PatternHandler.Generator.genFPPattern(train, fold);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<FPPattern> patterns = null;
        try {
            patterns = PatternHandler.Loader.loadFPPatterns(fold);
        } catch (IOException e) {
            logger.error("Load FPPattern error:[" + e.getMessage() + "]", e.getCause());
        }

        patterns = IgFilter.filter(train, patterns, ParamConstants.FP_IG_DELTA);
        return patterns;
    }

    @Override
    protected void printRst() {
        //TODO
    }


    private static class IgFilter {
        //TODO: 需要优化!!!
        public static List<FPPattern> filter(Instances train, List<FPPattern> patterns, int coverageDelta) {
            calAndSetRelevance(train, patterns);
            sortByGain(patterns);

            List<FPPattern> rst = new ArrayList<>();
            Set<Instance> coveredSet = new HashSet<>();

            int cover = 0;
            int numSelected = 0;

            int numInstances = train.numInstances();

            int numPatterns = patterns.size();

            while (true) {
                if (numSelected >= numPatterns) {
                    logger.debug("FP_IG selected all patterns.");
                    break;
                }
                if (cover >= coverageDelta) {
                    break;
                }
                FPPattern selected = patterns.remove(patterns.size()-1);
                rst.add(selected);
                for (FPPattern pattern : patterns) {
                    updateGain(pattern, selected, train);
                }
                sortByGain(patterns);

                numSelected++;
                for (Instance instance : selected.getCoverSet()) {
                    if (coveredSet.size() == numInstances) {
                        coveredSet.clear();
                        cover++;
                    }
                    coveredSet.add(instance);
                }
            }
            return rst;
        }

        private static void calAndSetRelevance(Instances train, List<FPPattern> patterns) {
            Map<ClassType, List<Instance>> map = mapInstances(train.iterator());

            double info = infoOfTrain(train, map);
            for (FPPattern pattern : patterns) {
                double iGain = info - infoOfPattern(train, pattern, map);
                pattern.setRevelance(iGain); //TODO ???
                pattern.setGain(iGain); // This is the initial value of gain of every pattern, when no selected.
            }
        }

        private static void sortByGain(List<FPPattern> patterns) {
            Collections.sort(patterns, (arg1, arg2) -> Double.compare(arg1.getGain(), arg2.getGain()));
        }

        private static boolean updateGain(FPPattern pattern, FPPattern selected, Instances train) {
            Set<Instance> covered1 = new HashSet<>(pattern.getCoverSet());
            Set<Instance> covered2 = new HashSet<>(selected.getCoverSet());

            int p1 = covered1.size();
            int p2 = covered2.size();
            covered1.retainAll(covered2);
            int p12 = covered1.size();

            double r = (double)p12 / (p1 + p2 - p12)
                    * Math.min(selected.getRevelance(), pattern.getRevelance());
            double gain = pattern.getRevelance() - r;
            if (gain < pattern.getGain()) {
                pattern.setGain(gain);
                logger.debug("updated: [pattern id: " + pattern.getPatternId() + "]");
                return true;
            }
            return false;
        }

        private static double classEntropy(double total, double classCount) {
            return -(classCount/total*Math.log(classCount/total)/Math.log(2));
        }
        private static double infoOfTrain(Instances train, Map<ClassType, List<Instance>> map){
            double total = train.numInstances();
            double info = 0;
            for (List<Instance> list : map.values()) {
                int classCount = list.size();
                if (classCount > 0) {
                    info += classEntropy(total, classCount);
                }
            }
            return info;
        }
        private static double infoOfPattern(Instances train, FPPattern pattern,
                                            Map<ClassType, List<Instance>> map) {
            Set<Instance> fitSet = new HashSet<>();
            Set<Instance> noFitSet = new HashSet<>();

            Iterator<Instance> iterator = train.iterator();
            while (iterator.hasNext()) {
                Instance instance = iterator.next();
                if (pattern.fit(instance)) {
                    fitSet.add(instance);
                } else {
                    noFitSet.add(instance);
                }
            }
            pattern.setCoverSet(fitSet); // new HashSet(fitSet) 是否需要new
            double total = train.numInstances();
            double c1Count = fitSet.size();
            double c2Count = noFitSet.size();

            return c1Count/total*infoOfClass(fitSet) + c2Count/total*infoOfClass(noFitSet);

        }
        private static double infoOfClass(Set<Instance> instances) {
            double info = 0;

            int count = instances.size();
            int linkCount = 0;
            int noLinkCount = 0;
            for (Instance instance : instances) {
                if (instance.classValue() == 1.0) { // 为了节省时间和空间,所以直接算,不再mapInstances
                    linkCount++;
                } else {
                    noLinkCount++;
                }
            }
            // 排除=0的情况,否则会导致log求值为NaN
            if (linkCount != 0) {
                info += classEntropy(count, linkCount);
            }
            if (noLinkCount != 0) {
                info += classEntropy(count, noLinkCount);
            }
            return info;
        }



        private static Map<ClassType, List<Instance>> mapInstances(Iterator<Instance> iterator) {
            Map<ClassType, List<Instance>> map = new HashMap<>();
            while (iterator.hasNext()) {
                Instance instance = iterator.next();
                ClassType ct = ClassType.get(instance.classValue());
                if (map.containsKey(ct)) {
                    map.get(ct).add(instance);
                } else {
                    List<Instance> list = new LinkedList<>();
                    list.add(instance);
                    map.put(ct, list);
                }
            }
            return map;
        }
    }
}
