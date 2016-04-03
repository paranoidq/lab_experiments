package handler;

import beans.Edge;
import org.apache.commons.lang3.StringUtils;
import util.Constants;
import util.FileUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by paranoidq on 16/3/25.
 */
public class EdgeHandler {

    public static List<Edge> loadEdges(String path) {
        List<Edge> list = new LinkedList<>();
        try (BufferedReader br = FileUtil.readFile(path)) {
            String line;
            while ( (line = br.readLine()) != null) {
                String[] sp = StringUtils.strip(line, Constants.NEWLINE).split(Constants.COMMA);
                if (sp.length != 2) {
                    throw new RuntimeException("Invalid edge");
                }
                list.add(Edge.newEdge(Integer.parseInt(sp[0]), Integer.parseInt(sp[1])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void writeEdges(List<Edge> edges, String path) {
        try (BufferedWriter bw = FileUtil.writeFile(path)) {
            for (Edge edge : edges) {
                bw.write("" + edge.getId1() + "," + edge.getId2());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, Set<Integer>> loadEdgesMap(String path) throws IOException {
        Map<Integer, Set<Integer>> edges = new HashMap<>();
        try (BufferedReader br = FileUtil.readFile(path)) {
            String line;
            while ( (line = br.readLine()) != null) {
                String[] sp = StringUtils.strip(line, Constants.NEWLINE).split(Constants.COMMA);
                if (sp.length != 2) {
                    throw new RuntimeException("Invalid edge");
                }
                Edge edge = Edge.newEdge(Integer.parseInt(sp[0]), Integer.parseInt(sp[1]));
                if (!edges.containsKey(edge.getId1())) {
                    edges.put(edge.getId1(), new HashSet<>());
                }
                edges.get(edge.getId1()).add(edge.getId2());
                if (!edges.containsKey(edge.getId2())) {
                    edges.put(edge.getId2(), new HashSet<>());
                }
                edges.get(edge.getId2()).add(edge.getId1());
            }
        }
        return edges;
    }
}
