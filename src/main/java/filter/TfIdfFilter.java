package filter;

import beans.trans.Item;
import beans.trans.Trans;
import beans.trans.TransSet;

import java.util.*;

/**
 *
 * TODO: 使用tf-CRF指标
 *
 * 过滤掉的词汇是最后的约10%,但是因为选取的边是随机的50%,所以还有部分词汇本身就没有出现在trans里面!!!
 * 因此(过滤词 + remained) != items, 而是小于items
 *
 * 最终remained保持在2w4左右的水平
 *
 * Created by paranoidq on 16/3/23.
 */
@Deprecated
public class TfIdfFilter {

    public static final double delta = 0.2;

    private Set<Integer> remainedItems;

    public TfIdfFilter() {
        this.remainedItems = new HashSet<>();
    }


    public Set<Integer> getRemainedItems() {
        return this.remainedItems;
    }

    public void filter(TransSet posTransSet, TransSet negTransSet) {
        Map<Integer, Double> posTfIdfMap = new HashMap<>();
        Map<Integer, Double> negTfIdfMap = new HashMap<>();

        Map<Integer, Integer> posTermsMap = calTermCounts(posTransSet);
        Map<Integer, Integer> negTermsMap = calTermCounts(negTransSet);

        // 统计所有词汇的总出现次数
        int posM = calM(posTermsMap);
        int negM = calM(negTermsMap);

        // 文档总数
        int N = posTransSet.size() + negTransSet.size();

        for (Integer itemId : posTermsMap.keySet()) {
            if (posTfIdfMap.containsKey(itemId)) {
                continue;
            }
            // 该词出现的次数(类别)
            int pos_Term = posTermsMap.get(itemId);
            int neg_Term = negTermsMap.containsKey(itemId) ? negTermsMap.get(itemId) : 0;

            double tf = (double)pos_Term / posM;
            double idf = Math.log((double)N * posM / (pos_Term + neg_Term)) / Math.log(2);
            posTfIdfMap.put(itemId, tf*idf);
        }
        for (Integer itemId : negTermsMap.keySet()) {
            if (negTfIdfMap.containsKey(itemId)) {
                continue;
            }
            // 该词出现的次数(类别)
            int pos_Term = posTermsMap.containsKey(itemId) ? posTermsMap.get(itemId) : 0;
            int neg_Term = negTermsMap.get(itemId);
            double tf = (double)neg_Term / negM;
            double idf = Math.log((double)N * negM / (pos_Term + neg_Term)) / Math.log(2);
            negTfIdfMap.put(itemId, tf*idf);
        }

        List<Map.Entry<Integer, Double>> pos = new ArrayList<>(posTfIdfMap.entrySet());
        Collections.sort(pos,
                (o1, o2) -> Double.compare(o2.getValue(), o1.getValue()));

        List<Map.Entry<Integer, Double>> neg = new ArrayList<>(negTfIdfMap.entrySet());
        Collections.sort(neg,
                (o1, o2) -> Double.compare(o2.getValue(), o1.getValue()));

        for (Map.Entry<Integer, Double> entry : pos.subList(0, (int)(delta*pos.size() + 1))) {
            remainedItems.add(entry.getKey());
            System.out.println(Item.getItem(entry.getKey())); // 调试
        }
        for (Map.Entry<Integer, Double> entry : neg.subList(0, (int)(delta*neg.size() + 1))) {
            remainedItems.add(entry.getKey());
            System.out.println(Item.getItem(entry.getKey())); // 调试
        }

    }

    private Map<Integer, Integer> calTermCounts(TransSet transSet) {
        Map<Integer, Integer> termsCount = new HashMap<>();
        for (Trans trans : transSet.getTransSet()) {
            for (Integer itemId : trans.getItems()) {
                if (termsCount.containsKey(itemId)) {
                    termsCount.put(itemId, termsCount.get(itemId) + 1);
                } else {
                    termsCount.put(itemId, 1);
                }
            }
        }
        return termsCount;
    }

    private int calM(Map<Integer, Integer> termsCountMap) {
        int M = 0;
        for (Integer termCount : termsCountMap.values()) {
            M += termCount;
        }
        return M;
    }

}
