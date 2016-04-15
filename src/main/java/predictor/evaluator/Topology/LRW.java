package predictor.evaluator.Topology;

import beans.Edge;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import predictor.evaluator.AbstractEvaluator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by paranoidq on 2016/4/13.
 */
public class LRW extends AbstractEvaluator {

    private static final int MAX_HOPS = 3;
    private static final int MATRIX_LEN = MAX_ID_BOUND;
    private static final Map<Integer, DoubleMatrix1D> lrwCaches = new HashMap<>();


    @Override
    public double metrics(Edge e) {
        if (!groundNetwork.containsKey(e.getId1()) || !groundNetwork.containsKey(e.getId2())) {
            return 0;
        }

        double score1 = lrw4Id(e.getId1(), e.getId2());
        double score2 = lrw4Id(e.getId2(), e.getId1());
        return score1 + score2;
    }

    @Override
    public String name() {
        return "LRW";
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


}
