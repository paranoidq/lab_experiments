package predictor.evaluator.PBLRW.utils;

import beans.pattern.Pattern;
import beans.trans.Trans;
import beans.trans.TransSet;
import util.FileUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by paranoidq on 2016/4/14.
 */
public class TransHelper {

    /**
     * 将trans写入指定的文件路径
     *
     * @param transSet
     * @param path
     */
    public static void writeTrans(TransSet transSet, String path) {
        try (BufferedWriter bw = FileUtil.writeFile(path)) {
            for (Trans trans : transSet.getTransSet()) {
                bw.write(trans.toString());
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void calPosCover(List<Pattern> posPatterns, TransSet posTransSet) {
        for (Trans trans : posTransSet.getTransSet()) {
            posPatterns.stream().filter(p -> p.cover(trans)).forEach(p -> {
                p.addCoverEdge(trans.getEdge());
                trans.getEdge().addPosPattern(p);
            });
        }
    }

    public static void calNegCover(List<Pattern> negPatterns, TransSet negTransSet) {
        for (Trans trans : negTransSet.getTransSet()) {
            negPatterns.stream().filter(p -> p.cover(trans)).forEach(p -> {
                p.addCoverEdge(trans.getEdge());
                trans.getEdge().addNegPattern(p);
            });
        }
    }
}
