package graph.dagsp;

import graph.scc.CondensationGraphBuilder;
import graph.scc.TarjanSCC;
import model.Graph;
import java.util.*;

public class CondensedShortestPathDAG {

    private final Graph originalGraph;
    private final CondensationGraphBuilder cond;
    private final int sourceComp;
    private final Map<Integer, Double> dist = new HashMap<>();
    private final Map<Integer, Integer> parent = new HashMap<>();
    private int relaxCount = 0;

    public CondensedShortestPathDAG(Graph graph, CondensationGraphBuilder cond, int sourceVertex) {
        this.originalGraph = graph;
        this.cond = cond;
        this.sourceComp = cond.getVertexToComponentMap().get(sourceVertex);
    }

    public void run() {
        Map<Integer, Set<Integer>> dagAdj = cond.getDagAdjacency();
        List<Integer> order = topoOrder(dagAdj);

        for (int v : dagAdj.keySet()) dist.put(v, Double.POSITIVE_INFINITY);
        dist.put(sourceComp, 0.0);

        for (int u : order) {
            if (dist.get(u) != Double.POSITIVE_INFINITY) {
                for (int v : dagAdj.get(u)) {
                    double w = 1.0;
                    if (dist.get(v) > dist.get(u) + w) {
                        dist.put(v, dist.get(u) + w);
                        parent.put(v, u);
                        relaxCount++;
                    }
                }
            }
        }
    }

    private List<Integer> topoOrder(Map<Integer, Set<Integer>> dag) {
        Map<Integer, Integer> indeg = new HashMap<>();
        for (int v : dag.keySet()) indeg.put(v, 0);
        for (int u : dag.keySet()) for (int v : dag.get(u)) indeg.put(v, indeg.get(v) + 1);
        Queue<Integer> q = new ArrayDeque<>();
        for (int v : indeg.keySet()) if (indeg.get(v) == 0) q.add(v);

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (int v : dag.get(u)) {
                indeg.put(v, indeg.get(v) - 1);
                if (indeg.get(v) == 0) q.add(v);
            }
        }
        return order;
    }

    public void printSummary() {
        System.out.println("=== Shortest Paths on Condensed DAG ===");
        System.out.printf("Source component: %d%n", sourceComp);
        for (int v : dist.keySet()) {
            double d = dist.get(v);
            System.out.printf("  SCC %d → dist=%.2f %s%n", v, d,
                    (d == Double.POSITIVE_INFINITY ? "(unreachable)" : ""));
        }
        System.out.printf("Total relaxations: %d%n", relaxCount);
    }

    public static void main(String[] args) throws Exception {
        Graph g = Graph.fromJson("data_final/small_1.json");
        TarjanSCC tarjan = new TarjanSCC(g);
        tarjan.run();
        CondensationGraphBuilder cond = new CondensationGraphBuilder(g, tarjan);

        CondensedShortestPathDAG sp = new CondensedShortestPathDAG(g, cond, 0);
        sp.run();
        cond.printSummary();
        sp.printSummary();
    }
}
