package model;

import util.GraphLoader;

import java.util.*;

public class Graph {

    private final int n;
    private final boolean directed;
    private final String weightModel;
    private final Map<Integer, List<Edge>> adj;
    private final List<Edge> edges;

    public Graph(int n, boolean directed, String weightModel) {
        this.n = n;
        this.directed = directed;
        this.weightModel = weightModel;
        this.adj = new HashMap<>();
        this.edges = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.put(i, new ArrayList<>());
    }

    public static Graph fromJson(String path) throws Exception {
        GraphLoader.Graph gdata = GraphLoader.loadGraph(path);

        Graph g = new Graph(gdata.n, gdata.directed, gdata.weight_model);
        for (GraphLoader.Edge e : gdata.edges) {
            g.addEdge(e.u, e.v, e.w);
        }
        return g;
    }

    public void addEdge(int u, int v, double w) {
        Edge e = new Edge(u, v, w);
        adj.get(u).add(e);
        edges.add(e);
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public Map<Integer, List<Edge>> getAdjacency() {
        return adj;
    }

    public int getVertexCount() {
        return n;
    }

    public boolean isDirected() {
        return directed;
    }

    public String getWeightModel() {
        return weightModel;
    }

    public List<Edge> getOutgoingEdges(int vertex) {
        return adj.getOrDefault(vertex, Collections.emptyList());
    }

    @Override
    public String toString() {
        return String.format("Graph(n=%d, directed=%s, edges=%d, weightModel=%s)",
                n, directed, edges.size(), weightModel);
    }
}
