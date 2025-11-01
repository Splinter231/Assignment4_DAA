package util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class GraphLoader {

    public static class Edge {
        public int u;
        public int v;
        public int w;

        public Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        @Override
        public String toString() {
            return String.format("(%d -> %d, w=%d)", u, v, w);
        }
    }

    public static class Graph {
        public boolean directed;
        public int n;
        public List<Edge> edges;
        public int source;
        public String weight_model;

        public Map<Integer, List<Edge>> adj = new HashMap<>();

        public void buildAdjacency() {
            for (int i = 0; i < n; i++) adj.put(i, new ArrayList<>());
            for (Edge e : edges) {
                adj.get(e.u).add(e);
            }
        }

        @Override
        public String toString() {
            return String.format("Graph(n=%d, edges=%d, directed=%s, weight_model=%s)",
                    n, edges.size(), directed, weight_model);
        }
    }

    public static Graph loadGraph(String filename) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Graph>(){}.getType();
        try (FileReader reader = new FileReader(filename)) {
            Graph g = gson.fromJson(reader, type);
            g.buildAdjacency();
            return g;
        }
    }

    public static void main(String[] args) throws IOException {
        Graph g = loadGraph("data/small_1.json");
        System.out.println("Loaded: " + g);
        System.out.println("Source vertex: " + g.source);
        System.out.println("Adjacency list:");
        for (int v : g.adj.keySet()) {
            System.out.print(v + " -> ");
            for (Edge e : g.adj.get(v)) {
                System.out.print(e.v + "(" + e.w + ") ");
            }
            System.out.println();
        }
    }
}
