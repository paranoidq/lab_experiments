package handler;

import org.apache.commons.lang3.StringUtils;
import util.Constants;
import util.FileUtil;
import util.ClassifierParamConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by paranoidq on 16/3/25.
 */
public class FeatsHandler {

    /**
     * 加载features文件
     *      uid:itemid,itemid,...
     * @param path
     * @return
     * @throws IOException
     */
    public static Map<Integer, Set<Integer>> loadFeats(String path) {
        Map<Integer, Set<Integer>> uid2Feats = new HashMap<>();

        try (BufferedReader br = FileUtil.readFile(path)) {
            String line;
            while ( (line = br.readLine()) != null) {
                Set<Integer> feats = new HashSet<>();
                String[] sp = StringUtils.strip(line, "\n").split(Constants.COLON);
                int uid = Integer.parseInt(sp[0]);
                for (String featIdStr : sp[1].split(Constants.COMMA)) {
                    feats.add(Integer.parseInt(featIdStr));
                }
                uid2Feats.put(uid, feats);
            }
        } catch(IOException e) {
            e.printStackTrace();
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
            int size = (int)(ClassifierParamConstants.TFIDF_DELTA * tfIdfList.size());
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
