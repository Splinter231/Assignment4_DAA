package graph.dagsp;

import model.Edge;
import model.Graph;
import graph.topo.TopologicalSort;

import java.util.*;

public class ShortestPathDAG {

    private final Graph graph;
    private final int source;
    private final double[] dist;
    private final int[] parent;
    private int relaxCount = 0;

    public ShortestPathDAG(Graph graph, int source) {
        this.graph = graph;
        this.source = source;
        this.dist = new double[graph.getVertexCount()];
        this.parent = new int[graph.getVertexCount()];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);
    }

    public void run() {
        Map<Integer, Set<Integer>> dagAdj = new HashMap<>();
        for (int v = 0; v < graph.getVertexCount(); v++) {
            Set<Integer> out = new HashSet<>();
            for (Edge e : graph.getOutgoingEdges(v)) {
                out.add(e.getTo());
            }
            dagAdj.put(v, out);
        }

        TopologicalSort topo = new TopologicalSort(dagAdj);
        topo.runKahn();

        List<Integer> order = topo.getTopologicalOrder();
        dist[source] = 0.0;

        for (int u : order) {
            if (dist[u] != Double.POSITIVE_INFINITY) {
                for (Edge e : graph.getOutgoingEdges(u)) {
                    int v = e.getTo();
                    double w = e.getWeight();
                    if (dist[v] > dist[u] + w) {
                        dist[v] = dist[u] + w;
                        parent[v] = u;
                        relaxCount++;
                    }
                }
            }
        }
    }

    public double getDistance(int v) {
        return dist[v];
    }

    public List<Integer> reconstructPath(int target) {
        List<Integer> path = new ArrayList<>();
        for (int at = target; at != -1; at = parent[at]) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    public int getRelaxCount() {
        return relaxCount;
    }

    public void printSummary() {
        System.out.println("=== Shortest Paths in DAG ===");
        System.out.printf("Source vertex: %d%n", source);
        System.out.println("Distances:");
        for (int i = 0; i < dist.length; i++) {
            System.out.printf("  %d → dist=%.2f %s%n", i, dist[i],
                    (dist[i] == Double.POSITIVE_INFINITY ? "(unreachable)" : ""));
        }
        System.out.printf("Total relaxations: %d%n", relaxCount);
    }

    public static void main(String[] args) throws Exception {
        Graph g = Graph.fromJson("data_final/small_1.json");
        int source = 0;
        ShortestPathDAG sp = new ShortestPathDAG(g, source);
        sp.run();
        sp.printSummary();

        System.out.println("Path 0 → 5: " + sp.reconstructPath(5));
    }
}
