package predictor.evaluator.main;

import beans.Edge;
import com.sun.org.apache.xpath.internal.operations.Neg;
import handler.EdgeHandler;
import predictor.evaluator.PredictorParamConstants;
import util.FileUtil;
import util.PathRules;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by paranoidq on 2016/4/13.
 */
public class NegEdgePreprocess {
    protected static final int MAX_ID_BOUND = PredictorParamConstants.MAX_ID + 1;

    /**
     * 产生所有的未链接点对
     * newScoreEdge constructor保证id1 < id2，否则会进行交换
     * 根据Edge的hash()保证不重复
     */
    private static void generateTestNegEdges() {
        List<Edge> allEdges = EdgeHandler.loadEdges(PathRules.getAllPosEdgesPath());
        Set<Edge> allEdgesSet = new HashSet<>(allEdges);

        List<Edge> allNegs = new ArrayList<>();
        for (Integer id1 = 0; id1 < MAX_ID_BOUND; id1++) {
            for (Integer id2 = id1+1; id2 < MAX_ID_BOUND; id2++) {
                if(id2 == id1) {
                    continue;
                } else {
                    Edge candidate = Edge.newEdge(id1, id2);
                    if (allEdgesSet.contains(candidate)) {
                        continue;
                    }
                    allNegs.add(candidate);
                }
            }
        }

        try (BufferedWriter bw = FileUtil.writeFile(PathRules.getAllNegEdgesPath())) {
            for (Edge e : allNegs) {
                bw.write(e.toString());
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NegEdgePreprocess.generateTestNegEdges();
    }
}
