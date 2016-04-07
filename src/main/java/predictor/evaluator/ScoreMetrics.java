package predictor.evaluator;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import org.apache.commons.collections.CollectionUtils;
import util.PredictorParamConstants;

import java.util.*;

/**
 * Created by paranoidq on 2016/4/3.
 */
public class ScoreMetrics {

    private static final int MATRIX_LEN = PredictorParamConstants.MAX_ID + 1;

    private Map<Integer, Set<Integer>> networkMap;
    private DoubleMatrix2D pt;
    private int edgesCount;

    public ScoreMetrics(Map<Integer, Set<Integer>> networkMap) {
        this.networkMap = networkMap;
        this.loadMatrix(networkMap);
    }

    public void loadMatrix(Map<Integer, Set<Integer>> networkMap) {
        int count = 0;
        DoubleMatrix2D p = new SparseDoubleMatrix2D(MATRIX_LEN, MATRIX_LEN);
        for (int key=0; key<MATRIX_LEN; key++) {
            if (networkMap.containsKey(key)) {
                for (Integer id2 : networkMap.get(key)) {
                    p.set(key, id2, (double) 1 / networkMap.get(key).size());
                    ++count;
                }
            } else {
                for (Integer id2=0; id2<MATRIX_LEN; id2++) {
                    p.set(key, id2, (double) 1 / (MATRIX_LEN-1));
                }
            }
        }
        this.pt = Algebra.DEFAULT.transpose(p);
        assert count%2 == 0;
        this.edgesCount = count/2;
    }



    public int cn(int id1, int id2) {
        if (!networkMap.containsKey(id1) || !networkMap.containsKey(id2)) {
            return 0;
        }
        return CollectionUtils.intersection(networkMap.get(id1), networkMap.get(id2)).size();
    }

    public double jaccard(int id1, int id2) {
        if (!networkMap.containsKey(id1) || !networkMap.containsKey(id2)) {
            return 0;
        }
        return (double)CollectionUtils.intersection(networkMap.get(id1), networkMap.get(id2)).size()
                / CollectionUtils.union(networkMap.get(id1), networkMap.get(id2)).size();
    }

    public double aa(int id1, int id2) {
        if (!networkMap.containsKey(id1) || !networkMap.containsKey(id2)) {
            return 0;
        }
        double score = 0;
        Collection<Integer> CNs = CollectionUtils.intersection(networkMap.get(id1), networkMap.get(id2));
        for (Integer id : CNs) {
            int degree = networkMap.get(id).size();
            score += (double)1 / Math.log(degree) * Math.log(2);
        }
        return score;
    }

    public double ra(int id1, int id2) {
        if (!networkMap.containsKey(id1) || !networkMap.containsKey(id2)) {
            return 0;
        }
        double score = 0;
        Collection<Integer> CNs = CollectionUtils.intersection(networkMap.get(id1), networkMap.get(id2));
        for (Integer id : CNs) {
            int degree = networkMap.get(id).size();
            score += (double)1 / degree;
        }
        return score;
    }



    public double pa(int id1, int id2) {
        if (!networkMap.containsKey(id1) || !networkMap.containsKey(id2)) {
            return 0;
        }
        return networkMap.get(id1).size() * networkMap.get(id2).size();
    }



    public double lp(int id1, int id2) {
        double alpha = 0.01;
        if (!networkMap.containsKey(id1) || !networkMap.containsKey(id2)) {
            return 0;
        }
        double hop2s = CollectionUtils.intersection(networkMap.get(id1), networkMap.get(id2)).size();
        Set<Integer> cn1 = networkMap.get(id1);
        Set<Integer> cncn1 = new HashSet<>();
        for (Integer id : cn1) {
            for (Integer hop2cn : networkMap.get(id)) {
                if (!cn1.contains(hop2cn)) {// 不能是cn内部的环
                    cncn1.add(hop2cn);
                }
            }
        }
        double hop3s = CollectionUtils.intersection(cncn1, networkMap.get(id2)).size();
        return hop2s + alpha*hop3s;

    }

    public double kazz(int id1, int id2) {
        return 0;
    }


    private static int t = 3;
    private Map<Integer, DoubleMatrix1D> lrwRecords = new HashMap<>();

    public double lrw(int id1, int id2) {
        if (!networkMap.containsKey(id1) || !networkMap.containsKey(id2)) {
            return 0;
        }

        double score1 = rwr4Id(id1, id2);
        double score2 = rwr4Id(id2, id1);
        return score1 + score2;
    }

    private double rwr4Id(int id, int dest) {
        DoubleMatrix1D tMatrix;
        if (lrwRecords.containsKey(id)) {
            tMatrix = lrwRecords.get(id);
        } else {
            tMatrix = new SparseDoubleMatrix1D(MATRIX_LEN);
            tMatrix.set(id, 1);
            int i = 0;
            while (i < t) {
                tMatrix = Algebra.DEFAULT.mult(pt, tMatrix);
                i++;
            }
        }
        return (double)1000*networkMap.get(id).size()/2/edgesCount * tMatrix.get(dest);
    }


}
