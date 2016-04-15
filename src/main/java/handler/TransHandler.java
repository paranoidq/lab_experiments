package handler;

import beans.Edge;
import beans.pattern.ClassType;
import beans.pattern.Pattern;
import beans.pattern.PatternType;
import beans.trans.Item;
import beans.trans.Trans;
import beans.trans.TransSet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import util.Constants;
import util.FileUtil;
import util.PathRules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TransHandler
 * <p>
 * 非对齐的数据,用itemId表示,不能用于最终的evaluator分类,
 * 需要经过InstancesHandler加载到Instances类中才能用于分类
 * <p>
 * Created by paranoidq on 16/3/12.
 */
public class TransHandler {
    private static Map<Integer, Integer> newID2id;

    static {
        newID2id = Item.map2NewId();
    }


    /**
     * 生成挖掘pattern所需的trans
     * <p>
     * 直接将每个trans的itemId写入文件即可
     *
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
        bw.flush();
        bw.close();
    }

    /**
     * 根据pattern扩充原有的instances,并写入文件
     * <p>
     * 1. 为filtered_items和pattern统一指定ID
     * 2. 将trans映射为等长的instances
     * 3. 写入id和instances
     *
     * @param patterns
     * @param train
     * @param test
     * @param fold
     */
    public static void genAugTrans(List<Pattern> patterns, TransSet train, TransSet test, int fold, PatternType pt) throws IOException {
        String augTrainPath = PathRules.getAugTrainPath(fold, pt);
        String augTestPath = PathRules.getAugTestPath(fold, pt);

        int itemCount = newID2id.size();

        BufferedWriter bw = FileUtil.writeFile(augTrainPath);
        writeHeader(bw, itemCount, patterns.size());
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
        bw.flush();

        bw = FileUtil.writeFile(augTestPath);
        writeHeader(bw, itemCount, patterns.size());
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
        bw.flush(); // 这里必须flush,否则trans生成不完全....
        bw.close();
    }


    public static void genOriginTrans(TransSet train, TransSet test, int fold) throws IOException {
        String originTrainPath = PathRules.getOriginTrainPath(fold);
        String originTestPath = PathRules.getOriginTestPath(fold);

        int itemCount = newID2id.size();

        BufferedWriter bw = FileUtil.writeFile(originTrainPath);
        writeOriginHeader(bw, itemCount);
        for (Trans trans : train.getTransSet()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < itemCount; i++) {
                if (trans.contains(newID2id.get(i))) {
                    sb.append("1,");
                } else {
                    sb.append("0,");
                }
            }
            sb.append(trans.getCt().toString());
            bw.write(sb.toString());
            bw.newLine();
        }
        bw.flush();

        bw = FileUtil.writeFile(originTestPath);
        writeOriginHeader(bw, itemCount);
        for (Trans trans : test.getTransSet()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < itemCount; i++) {
                if (trans.contains(newID2id.get(i))) {
                    sb.append("1,");
                } else {
                    sb.append("0,");
                }
            }
            sb.append(trans.getCt().toString());
            bw.write(sb.toString());
            bw.newLine();
        }
        bw.flush(); // 这里必须flush,否则trans生成不完全....
        bw.close();
    }


    public static void genPatternAugTrans(List<Pattern> patterns, TransSet train, TransSet test, int fold, PatternType pt) throws IOException {
        String augTrainPath = PathRules.getPatternAugTrainPath(fold, pt);
        String augTestPath = PathRules.getPatternAugTestPath(fold, pt);

        BufferedWriter bw = FileUtil.writeFile(augTrainPath);
        writePatternHeader(bw, patterns.size());
        for (Trans trans : train.getTransSet()) {
            StringBuilder sb = new StringBuilder();
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
        bw.flush();

        bw = FileUtil.writeFile(augTestPath);
        writePatternHeader(bw, patterns.size());
        for (Trans trans : test.getTransSet()) {
            StringBuilder sb = new StringBuilder();
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
        bw.flush(); // 这里必须flush,否则trans生成不完全....
        bw.close();
    }


    /**
     * 加载过滤之后生成的trans
     *
     * @param path
     * @param ct
     * @return
     * @throws IOException
     */
    public static TransSet loadTransSetAfterTFIDF(String path, ClassType ct) {
        TransSet transSet = new TransSet();

        try (BufferedReader br = FileUtil.readFile(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] sp = StringUtils.strip(line).split(Constants.COMMA);
                Trans trans = new Trans(ct);
                for (String str : sp) {
                    trans.addItemId(Integer.parseInt(str));
                }
                transSet.addTrans(trans);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return transSet;
    }


    /**
     * 随机产生50%的posTrans
     * 会shuffle输入的edges list
     *
     * @param filteredUidFeats
     * @param edges
     * @return
     */
    public static TransSet genPosTransRandomly(Map<Integer, List<Integer>> filteredUidFeats,
                                               List<Edge> edges) {
        TransSet transSet = new TransSet();
        Collections.shuffle(edges, new Random(System.nanoTime()));
        List<Edge> sub = edges.subList(0, edges.size() / 2);
        List<Edge> pickedEdges = new LinkedList<>();
        for (Edge edge : sub) {
            Trans trans = new Trans(ClassType.POSITIVE);

            List<Integer> feats1 = filteredUidFeats.get(edge.getId1());
            List<Integer> feats2 = filteredUidFeats.get(edge.getId2());
            List<Integer> feats = (List<Integer>) CollectionUtils.intersection(feats1, feats2);
            if (!feats.isEmpty()) {
                trans.setFeats(feats);
                pickedEdges.add(edge);
            } else {
                System.out.println("Empty trans: " + edge);
            }
            transSet.addTrans(trans);
        }

        writeTrans(transSet, PathRules.getPosTransPathAfterTFIDF());
        EdgeHandler.writeEdges(pickedEdges, PathRules.getPosEdgesPathAfterTFIDF());
        return transSet;
    }

    public static TransSet genNegTransRandomly(Map<Integer, List<Integer>> filteredUidFeats,
                                               List<Edge> edges, int count) {
        Set<String> linked = edges.stream().map(edge -> "" + edge.getId1() + "," + edge.getId2())
                .collect(Collectors.toSet());
        List<Edge> pickedEdges = new LinkedList<>();

        TransSet transSet = new TransSet();
        int maxUid = filteredUidFeats.size() - 1;
        Random random = new Random(System.nanoTime());
        int curCount = 0;
        while (curCount < count) {
            int id1 = random.nextInt(maxUid + 1);
            int id2 = random.nextInt(maxUid + 1);
            if (id1 != id2 && !linked.contains(candidateEdge(id1, id2))) {
                Trans trans = new Trans(ClassType.NEGATIVE);
                List<Integer> feats1 = filteredUidFeats.get(id1);
                List<Integer> feats2 = filteredUidFeats.get(id2);
                List<Integer> candidateFeats = (List<Integer>) CollectionUtils.intersection(feats1, feats2);
                if (!candidateFeats.isEmpty()) {
                    trans.setFeats(candidateFeats);
                    ++curCount;
                    pickedEdges.add(Edge.newEdge(id1, id2));
                    transSet.addTrans(trans);
                }

            }
        }
        writeTrans(transSet, PathRules.getNegTransPathAfterTFIDF());
        EdgeHandler.writeEdges(pickedEdges, PathRules.getNegEdgesPathAfterTFIDF());
        return transSet;
    }

    private static String candidateEdge(int id1, int id2) {
        return id1 < id2 ? "" + id1 + "," + id2 : "" + id2 + "," + id1;
    }


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

    public static void writeHeader(BufferedWriter bw, int itemCount, int patternCount) throws IOException {
        bw.write("@relation polblogs");
        bw.newLine();
        for (int i = 0; i < itemCount; i++) {
            bw.write("@attribute item" + i + " {0, 1}");
            bw.newLine();
        }
        for (int i = 0; i < patternCount; i++) {
            bw.write("@attribute pattern" + i + " {0, 1}");
            bw.newLine();
        }
        bw.write("@attribute class {POSITIVE, NEGATIVE}");
        bw.newLine();
        bw.write("@data");
        bw.newLine();
    }

    public static void writeOriginHeader(BufferedWriter bw, int itemCount) throws IOException {
        bw.write("@relation polblogs");
        bw.newLine();
        for (int i = 0; i < itemCount; i++) {
            bw.write("@attribute item" + i + " {0, 1}");
            bw.newLine();
        }
        bw.write("@attribute class {POSITIVE, NEGATIVE}");
        bw.newLine();
        bw.write("@data");
        bw.newLine();
    }

    public static void writePatternHeader(BufferedWriter bw, int patternCount) throws IOException {
        bw.write("@relation polblogs");
        bw.newLine();
        for (int i = 0; i < patternCount; i++) {
            bw.write("@attribute pattern" + i + " {0, 1}");
            bw.newLine();
        }
        bw.write("@attribute class {POSITIVE, NEGATIVE}");
        bw.newLine();
        bw.write("@data");
        bw.newLine();
    }
}
