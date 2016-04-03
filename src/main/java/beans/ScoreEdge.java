package beans;

/**
 * Created by paranoidq on 2016/4/3.
 */
public class ScoreEdge extends Edge implements Comparable<ScoreEdge> {

    private double score;


    protected ScoreEdge(int id1, int id2) {
        super(id1, id2);
    }

    public static ScoreEdge newScoreEdge(int id1, int id2) {
        return new ScoreEdge(id1, id2);
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore() {
        return this.score;
    }

    @Override
    public int compareTo(ScoreEdge o) {
        return Double.compare(o.getScore(), this.score);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
