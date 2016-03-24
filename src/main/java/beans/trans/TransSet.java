package beans.trans;

import beans.pattern.ClassType;
import beans.pattern.Pattern;

import java.util.*;

/**
 * Created by paranoidq on 16/3/23.
 */
public class TransSet {

    private List<Trans> transSet;


    public TransSet() {
        this.transSet = new ArrayList<>();
    }

    public void addTrans(Trans trans) {
        this.transSet.add(trans);
    }

    /**
     * TODO: size的复杂度依赖于transSet的具体实现
     * @return
     */
    public int size() {
        return this.transSet.size();
    }

    public List<Trans> getTransSet() {
        return this.transSet;
    }

    /**
     * 生成train
     * 不修改原有的transSet
     * @param numFolds
     * @param fold
     * @return
     */
    public TransSet trainCV(int numFolds, int fold) {
        int size = this.size();
        int part = (int)((double)size/numFolds);
        if (fold == 0) {
            TransSet trainCV = new TransSet();
            List<Trans> transSubset = transSet.subList(part, size);
            for (Trans trans : transSubset) {
                trainCV.addTrans(trans.copy());
            }
            return trainCV;
        } else {
            TransSet trainCV = new TransSet();
            List<Trans> transSubset = transSet.subList(0, fold*part);
            for (Trans trans : transSubset) {
                trainCV.addTrans(trans.copy());
            }
            transSubset = transSet.subList((fold+1)*part, (numFolds-1-fold)*part);
            for (Trans trans : transSubset) {
                trainCV.addTrans(trans.copy());
            }
            return trainCV;
        }
    }

    /**
     * 生成test
     * 不修改原有的transSet
     * @param numFolds
     * @param fold
     * @return
     */
    public TransSet testCV(int numFolds, int fold) {
        int size = this.size();
        int part = (int)((double)size/numFolds);
        int start;
        int end;
        if (fold == numFolds-1) {
            start = fold * part;
            end = size;
        } else {
            start = fold * part;
            end = (fold+1) * part;
        }
        TransSet testCV = new TransSet();
        List<Trans> transSubset = transSet.subList(start, end);
        for (Trans trans : transSubset) {
            testCV.addTrans(trans.copy());
        }
        return testCV;
    }


    /**
     * 浅拷贝!!!
     * @param posTransSet
     * @param negTransSet
     * @return
     */
    public static TransSet union(TransSet posTransSet, TransSet negTransSet) {
        TransSet union = new TransSet();
        posTransSet.getTransSet().forEach(union::addTrans);
        negTransSet.getTransSet().forEach(union::addTrans);

        // shuffle
        Collections.shuffle(union.getTransSet(), new Random(System.nanoTime()));
        return union;
    }


    public Map<ClassType, List<Trans>> map2Class() {
        Map<ClassType, List<Trans>> map = new HashMap<>();
        map.put(ClassType.POSITIVE, new LinkedList<>());
        map.put(ClassType.NEGATIVE, new LinkedList<>());

        for (Trans trans : this.transSet) {
            map.get(trans.getCt()).add(trans);
        }
        return map;
    }


    public void calSuppD(List<Pattern> patterns) {
        for (Trans trans : this.transSet) {
            patterns.stream().filter(pattern -> pattern.cover(trans)).forEach(Pattern::incrementSuppD);
        }
    }


    public int termDocCount(Integer itemId) {
        int termCount = 0;
        for (Trans trans : transSet) {
            if (trans.contains(itemId)) {
                ++termCount;
            }
        }
        return termCount;
    }

}
