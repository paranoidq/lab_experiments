package predictor.evaluator.PBLRW;

import beans.Edge;
import beans.pattern.ClassType;
import beans.pattern.Pattern;
import beans.trans.Item;
import beans.trans.Trans;
import beans.trans.TransSet;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import org.apache.commons.collections.CollectionUtils;
import predictor.evaluator.AbstractEvaluator;
import predictor.evaluator.PBLRW.utils.FeatHelper;
import predictor.evaluator.PBLRW.utils.ItemHelper;
import predictor.evaluator.PBLRW.utils.PatternHelper;
import predictor.evaluator.PBLRW.utils.TransHelper;
import predictor.evaluator.PredictorParamConstants;
import util.PathRules;

import java.util.*;

/**
 * Created by paranoidq on 2016/4/13.
 */
public class PBLRW extends AbstractEvaluator {

    private static final int MAX_HOPS = 3;
    private static final int MATRIX_LEN = MAX_ID_BOUND;
    private static final Map<Integer, DoubleMatrix1D> lrwCaches = new HashMap<>();

    @Override
    protected double metrics(Edge e) {
        if (!groundNetwork.containsKey(e.getId1()) || !groundNetwork.containsKey(e.getId2())) {
            return 0;
        }

        double score1 = lrw4Id(e.getId1(), e.getId2());
        double score2 = lrw4Id(e.getId2(), e.getId1());
        return score1 + score2;
    }

    @Override
    protected String name() {
        return "PB-LRW";
    }

    private static DoubleMatrix2D transMatrixT; // 转移矩阵的转置
    private static int edgesCount = trainPosEdges.size();
    static {
        buildTransMatrix();
    }

    private double lrw4Id(int id, int dest) {
        DoubleMatrix1D probVector;
        if (lrwCaches.containsKey(id)) {
            probVector = lrwCaches.get(id);
        } else {
            probVector = new SparseDoubleMatrix1D(MATRIX_LEN);
            probVector.set(id, 1);
            int i = 0;
            while (i < MAX_HOPS) {
                probVector = Algebra.DEFAULT.mult(transMatrixT, probVector);
                i++;
            }
            lrwCaches.put(id, probVector); // cache
        }
        return (double)1000*groundNetwork.get(id).size()/2/edgesCount * probVector.get(dest);
    }


    protected static Map<Integer, Set<Integer>> feats;
    protected static Map<Integer, Integer> newID2id;
    protected static int itemCount;

    static {
        feats = FeatHelper.loadFeats(PathRules.getFilteredFeatsPath());
        ItemHelper.loadItems(PathRules.getItemPath());
        newID2id = Item.map2NewId();
        itemCount = newID2id.size();
    }
    private static void buildTransMatrix() {

        int count = 0;
        DoubleMatrix2D p = new SparseDoubleMatrix2D(MATRIX_LEN, MATRIX_LEN);
        for (int key = 0; key < MATRIX_LEN; key++) {
            if (groundNetwork.containsKey(key)) {
                Set<Integer> edges = groundNetwork.get(key);
                int degree = edges.size();
                for (Integer id : edges) {
                    p.set(key, id, (double)1/degree);
                    ++count;
                }
            } else {
                for (int id = 0; id < MATRIX_LEN; id++) {
                    p.set(key, id, (double)1/(MATRIX_LEN-1)); // 度为0的点特殊处理
                }
            }
        }
        transMatrixT = Algebra.DEFAULT.transpose(p);
        assert edgesCount == count/2;
    }


    private void buildTrans() {
        TransSet posTransSet = new TransSet();
        for (Edge e : trainPosEdges) {
            List<Integer> edgeFeats = (List<Integer>) CollectionUtils.intersection(feats.get(e.getId1()), feats.get(e.getId2()));
            Trans trans = new Trans(ClassType.POSITIVE);
            trans.setFeats(edgeFeats);
            trans.setEdge(e);
            posTransSet.addTrans(trans);
        }
        TransHelper.writeTrans(posTransSet, PathRules.getPosTransPath_PBLRW());

        TransSet negTransSet = new TransSet();
        for (Edge e : trainNegEdges) {
            List<Integer> edgeFeats = (List<Integer>) CollectionUtils.intersection(feats.get(e.getId1()), feats.get(e.getId2()));
            Trans trans = new Trans(ClassType.NEGATIVE);
            trans.setFeats(edgeFeats);
            trans.setEdge(e);
            negTransSet.addTrans(trans);
        }
        TransHelper.writeTrans(negTransSet, PathRules.getNegTransPath_PBLRW());


        // gen patterns
        List<Pattern> posPatterns = PatternHelper.buildFpPatterns(PathRules.getPosTransPath_PBLRW(), PathRules.getPosPatternPath_PBLRW());
        List<Pattern> negPatterns = PatternHelper.buildFpPatterns(PathRules.getNegTransPath_PBLRW(), PathRules.getNegPatternPath_PBLRW());

        // filter patterns
        TransSet trainTransSet = TransSet.union(posTransSet, negTransSet);
        calCoverAndSetDx(posPatterns, trainTransSet, posTransSet, ClassType.POSITIVE);
        calCoverAndSetDx(negPatterns, trainTransSet, negTransSet, ClassType.NEGATIVE);

        Collections.sort(posPatterns, (o1, o2) -> Double.compare(o2.getDxValue(), o1.getDxValue()));
        Collections.sort(negPatterns, (o1, o2) -> Double.compare(o2.getDxValue(), o1.getDxValue()));

        posPatterns = PatternHelper.filter(trainTransSet, posPatterns, PredictorParamConstants.COVERAGE_DELTA, PredictorParamConstants.PER_INS_COVER_TIMES);
        negPatterns = PatternHelper.filter(trainTransSet, negPatterns, PredictorParamConstants.COVERAGE_DELTA, PredictorParamConstants.PER_INS_COVER_TIMES);

        // calPattern Strength
        PatternHelper.calPatternStrength(posPatterns);
        PatternHelper.calPatternStrength(negPatterns);

        // build transmission matrix

    }

    /**
     * 统计：
     * 1. 每个pattern cover多少edge
     * 2. 每个edge cover多少pattern
     * 3. Dx值计算并设置
     * @param patterns
     * @param trainSet
     */
    private void calCoverAndSetDx(List<Pattern> patterns,
                                  TransSet trainSet, TransSet posORnegTransSet, ClassType ct) {
        // suppD
        trainSet.calSuppD(patterns);
        // suppL
        // 在loadPattern时一起给定
        if (ct == ClassType.POSITIVE) {
            TransHelper.calPosCover(patterns, posORnegTransSet);
        } else {
            TransHelper.calNegCover(patterns, posORnegTransSet);
        }

        // Dx
        int D = trainSet.size();
        for (Pattern pattern : patterns) {
            double dx = pattern.getSuppL() * Math.log(D/pattern.getSuppD()) / Math.log(2);
            pattern.setDxValue(dx);
        }
    }
}
