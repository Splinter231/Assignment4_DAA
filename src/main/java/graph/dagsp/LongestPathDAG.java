package graph.dagsp;

import model.Graph;
import model.Edge;
import graph.topo.TopologicalSort;

import java.util.*;

public class LongestPathDAG {

    private final Graph graph;
    private final int source;
    private final double[] dist;
    private final int[] parent;
    private int relaxations = 0;

    public LongestPathDAG(Graph g, int source) {
        this.graph = g;
        this.source = source;
        this.dist = new double[g.getVertexCount()];
        this.parent = new int[g.getVertexCount()];
        Arrays.fill(dist, Double.NEGATIVE_INFINITY);
        Arrays.fill(parent, -1);
    }

    public void run() {
        dist[source] = 0;

        Map<Integer, Set<Integer>> dagAdj = new HashMap<>();
        for (int i = 0; i < graph.getVertexCount(); i++) {
            dagAdj.put(i, new HashSet<>());
        }
        for (Edge e : graph.getEdges()) {
            dagAdj.get(e.getFrom()).add(e.getTo());
        }

        TopologicalSort topo = new TopologicalSort(dagAdj);
        topo.runKahn();
        List<Integer> order = topo.getTopologicalOrder();

        for (int u : order) {
            if (dist[u] != Double.NEGATIVE_INFINITY) {
                for (Edge e : graph.getOutgoingEdges(u)) {
                    int v = e.getTo();
                    double w = e.getWeight();
                    if (dist[u] + w > dist[v]) {
                        dist[v] = dist[u] + w;
                        parent[v] = u;
                        relaxations++;
                    }
                }
            }
        }
    }

    public void printSummary() {
        System.out.println("=== Longest (Critical) Path in DAG ===");
        System.out.println("Source vertex: " + source);

        int end = source;
        double maxDist = dist[source];
        for (int i = 0; i < dist.length; i++) {
            if (dist[i] > maxDist) {
                maxDist = dist[i];
                end = i;
            }
        }

        System.out.printf("Critical path length: %.2f%n", maxDist);
        System.out.print("Critical path: ");
        printPath(end);
        System.out.println();
        System.out.println("Total relaxations: " + relaxations);
    }

    private void printPath(int v) {
        List<Integer> path = new ArrayList<>();
        for (int cur = v; cur != -1; cur = parent[cur]) {
            path.add(cur);
        }
        Collections.reverse(path);
        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i));
            if (i < path.size() - 1) System.out.print(" → ");
        }
    }
}
