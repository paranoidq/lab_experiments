package handler;

import org.apache.commons.lang3.StringUtils;
import util.FileUtil;
import util.ParamConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by paranoidq on 16/3/25.
 */
public class FeatsHandler {

    /**
     * 加载原始的features文件
     *      line no = uid
     *      id      = item id
     * @param path
     * @return
     * @throws IOException
     */
    public static Map<Integer, Set<Integer>> loadFeats(String path) throws IOException {
        Map<Integer, Set<Integer>> uid2Feats = new HashMap<>();

        try (BufferedReader br = FileUtil.readFile(path)) {
            String line;
            int uid = -1;
            while ( (line = br.readLine()) != null) {
                ++uid;
                Set<Integer> feats = new HashSet<>();
                String[] sp = StringUtils.strip(line, "\n").split(",");
                for (String featIdStr : sp) {
                    feats.add(Integer.parseInt(featIdStr));
                }
                uid2Feats.put(uid, feats);
            }
        }
        return uid2Feats;
    }

    public static Map<Integer, List<Integer>> filterByTFIDF(Map<Integer, Set<Integer>> uid2feats) {
        Map<Integer, List<Integer>> filteredUid2Feats = new HashMap<>();

        int totalUid = uid2feats.size();
        Map<Integer, Integer> featOccurCount = new HashMap<>();
        for (Integer uid : uid2feats.keySet()) {
            Map<Integer, Double> tfIdfMap = new HashMap<>();
            int totalFeats4uid = uid2feats.get(uid).size();
            for (Integer feat : uid2feats.get(uid)) {
                int occurCount4Feat;
                if (featOccurCount.containsKey(feat)) {
                    occurCount4Feat = featOccurCount.get(feat);
                } else {
                    occurCount4Feat = calFeatOccurCount(uid2feats, feat);
                    featOccurCount.put(feat, occurCount4Feat);
                }
                double tfIdf = (double)1 / totalFeats4uid * Math.log(totalUid / (occurCount4Feat+1)) / Math.log(2);
                tfIdfMap.put(feat, tfIdf);
            }

            // 过滤
            List<Map.Entry<Integer, Double>> tfIdfList = new ArrayList<>(tfIdfMap.entrySet());
            Collections.sort(tfIdfList, (o1, o2) -> Double.compare(o2.getValue(), o1.getValue()));
            int size = (int)(ParamConstants.TFIDF_DELTA * tfIdfList.size());
            List<Map.Entry<Integer, Double>> filteredEntry = tfIdfList.subList(0, size);

            List<Integer> filterFeats = filteredEntry.stream().map(Map.Entry::getKey).collect(Collectors.toList());
            Collections.sort(filterFeats);
            filteredUid2Feats.put(uid, filterFeats);
        }
        return filteredUid2Feats;
    }

    private static int calFeatOccurCount(Map<Integer, Set<Integer>> uid2feats, int feat) {
        int featOccurCount = 0;
        for (Set<Integer> feats : uid2feats.values()) {
            if (feats.contains(feat)) {
                ++featOccurCount;
            }
        }
        return featOccurCount;
    }

}
