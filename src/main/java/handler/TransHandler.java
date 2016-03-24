package handler;

import beans.pattern.ClassType;
import beans.pattern.Pattern;
import beans.trans.Item;
import beans.trans.Trans;
import beans.trans.TransSet;
import org.apache.commons.lang3.StringUtils;
import util.FileUtil;
import util.PathRules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TransHandler
 *
 * 非对齐的数据,用itemId表示,不能用于最终的evaluator分类,
 * 需要经过InstancesHandler加载到Instances类中才能用于分类
 *
 * Created by paranoidq on 16/3/12.
 */
public class TransHandler {

    /**
     * 生成挖掘pattern所需的trans
     *
     *      直接将每个trans的itemId写入文件即可
     * @param transList
     * @param fold
     * @param classType
     */
    public static void genTrans4Mine(List<Trans> transList, int fold, ClassType classType) throws IOException {
        BufferedWriter bw = FileUtil.writeFile(PathRules.getTrans4MinePath(fold, classType));
        for (Trans trans : transList) {
            bw.write(trans.toString());
            bw.newLine();
        }
    }

    /**
     * 根据pattern扩充原有的instances,并写入文件
     *
     *      1. 为filtered_items和pattern统一指定ID
     *      2. 将trans映射为等长的instances
     *      3. 写入id和instances
     *
     * @param patterns
     * @param train
     * @param test
     * @param fold
     */
    public static void genAugTrans(List<Pattern> patterns, TransSet train, TransSet test, int fold) throws IOException {
        String augTrainPath = PathRules.getAugTrainPath(fold);
        String augTestPath = PathRules.getAugTestPath(fold);

        Map<Integer, Integer> newID2id = Item.map2NewId();
        int itemCount = newID2id.size();

        BufferedWriter bw = FileUtil.writeFile(PathRules.getAugTrainPath(fold));
        for (Trans trans : train.getTransSet()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < itemCount; i++) {
                if (trans.contains(newID2id.get(i))) {
                    sb.append("1,");
                } else {
                    sb.append("0,");
                }
            }
            for (Pattern pattern : patterns) {
                if (pattern.cover(trans)) {
                    sb.append("1,");
                } else {
                    sb.append("0,");
                }
            }
            sb.append(trans.getCt().toString());
            bw.write(sb.toString());
            bw.newLine();
        }

        bw = FileUtil.writeFile(PathRules.getAugTestPath(fold));
        for (Trans trans : test.getTransSet()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < itemCount; i++) {
                if (trans.contains(newID2id.get(i))) {
                    sb.append("1,");
                } else {
                    sb.append("0,");
                }
            }
            for (Pattern pattern : patterns) {
                if (pattern.cover(trans)) {
                    sb.append("1,");
                } else {
                    sb.append("0,");
                }
            }
            sb.append(trans.getCt().toString());
            bw.write(sb.toString());
            bw.newLine();
        }
        bw.close();
    }


    /**
     * 用于加载原始的trans
     * @param path
     * @param ct
     * @return
     * @throws IOException
     */
    public static TransSet loadTransSet(String path, ClassType ct) throws IOException {
        TransSet transSet = new TransSet();

        BufferedReader br = FileUtil.readFile(path);
        String line;
        while( (line = br.readLine()) != null) {
            String[] sp = StringUtils.strip(line).split(",");
            Trans trans = new Trans(ct);
            for (String str : sp) {
                trans.addItemId(Integer.parseInt(str));
            }
            transSet.addTrans(trans);
        }
        return transSet;
    }

    /**
     * 根据过滤的词汇重新构建trans
     * @param posTransSet
     * @param negTransSet
     * @param remainedItems
     */
    public static void filterTransSet(TransSet posTransSet, TransSet negTransSet,
                                      Set<Integer> remainedItems) {
        for (Trans trans : posTransSet.getTransSet()) {
            trans.getItems().retainAll(remainedItems);
        }
        for (Trans trans : negTransSet.getTransSet()) {
            trans.getItems().retainAll(remainedItems);
        }
    }


    /**
     * 将trans写入指定的文件路径
     * @param posTransSet
     * @param posPath
     * @param negTransSet
     * @param negPath
     */
    public static void writeTrans(TransSet posTransSet, String posPath,
                                  TransSet negTransSet, String negPath) {
        try {
            BufferedWriter bw = FileUtil.writeFile(posPath);
            for (Trans trans : posTransSet.getTransSet()) {
                bw.write(trans.toString());
                bw.newLine();
            }
            bw.close();
            bw = FileUtil.writeFile(negPath);
            for (Trans trans : negTransSet.getTransSet()) {
                bw.write(trans.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
