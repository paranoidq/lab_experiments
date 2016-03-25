package beans;

/**
 * Created by paranoidq on 16/3/25.
 */
public class Edge {

    private int id1;
    private int id2;


    private Edge(int id1, int id2) {
        if (id1 < id2) {
            this.id1 = id1;
            this.id2 = id2;
        } else if (id1 > id2){
            this.id1 = id2;
            this.id2 = id1;
        } else {
            throw new RuntimeException("Cannot be self-circle");
        }
    }

    public static Edge newEdge(int id1, int id2) {
        return new Edge(id1, id2);
    }


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
}
