package predictor.evaluator.PBLRW.utils;

import org.apache.commons.lang3.StringUtils;
import util.Constants;
import util.FileUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by paranoidq on 2016/4/14.
 */
public class FeatHelper {

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
}
