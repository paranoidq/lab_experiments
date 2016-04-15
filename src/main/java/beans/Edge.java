package beans;

import beans.pattern.Pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paranoidq on 16/3/25.
 */
public class Edge implements Comparable<Edge>{


    private int id;

    private int id1;
    private int id2;

    // predict score
    private double score;


    /* cover patterns */
    private List<Pattern> posPatterns = new ArrayList<>();
    private List<Pattern> negPatterns = new ArrayList<>();



    protected Edge(int id1, int id2) {
        if (id1 < id2) {
            this.id1 = id1;
            this.id2 = id2;
        } else if (id1 > id2){
            this.id1 = id2;
            this.id2 = id1;
        }
    }

    public static Edge newEdge(int id1, int id2) {
        return new Edge(id1, id2);
    }


//    public void setEdgeId(int id) {
//        this.id = id;
//    }
//    public int getEdgeId() {
//        return this.id;
//    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Edge)) {
            return false;
        }
        Edge e = (Edge) o;
        return this.id1 == e.id1 && this.id2 == e.id2;
    }

    @Override
    public int hashCode() {
        return 19*(id1 + 37*id2);
    }

    public int getId1() {
        return this.id1;
    }

    public int getId2() {
        return this.id2;
    }


    @Override
    public String toString() {
        return "" + id1 + "," + id2;
    }

    @Override
    public int compareTo(Edge o) {
        return Double.compare(o.getScore(), this.score);
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore() {
        return this.score;
    }



    public void addPosPattern(Pattern p) {
        this.posPatterns.add(p);
    }

    public void addNegPattern(Pattern p) {
        this.negPatterns.add(p);
    }

    public List<Pattern> getPosPatterns() {
        return this.posPatterns;
    }

    public List<Pattern> getNegPatterns() {
        return this.negPatterns;
    }
}
