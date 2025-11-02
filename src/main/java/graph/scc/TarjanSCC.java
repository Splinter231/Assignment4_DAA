package graph.scc;

import model.Graph;
import model.Edge;

import java.util.*;

public class TarjanSCC {

    private final Graph graph;
    private final int n;

    private int time;
    private final int[] disc;
    private final int[] low;
    private final boolean[] onStack;
    private final Deque<Integer> stack;
    private final List<List<Integer>> components;

    private int dfsVisits = 0;
    private int edgeTraversals = 0;

    public TarjanSCC(Graph graph) {
        this.graph = graph;
        this.n = graph.getVertexCount();
        this.disc = new int[n];
        this.low = new int[n];
        this.onStack = new boolean[n];
        this.stack = new ArrayDeque<>();
        this.components = new ArrayList<>();

        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);
    }

    public void run() {
        for (int v = 0; v < n; v++) {
            if (disc[v] == -1) dfs(v);
        }
    }

    private void dfs(int u) {
        dfsVisits++;
        disc[u] = low[u] = ++time;
        stack.push(u);
        onStack[u] = true;

        for (Edge e : graph.getOutgoingEdges(u)) {
            edgeTraversals++;
            int v = e.getTo();
            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        if (low[u] == disc[u]) {
            List<Integer> scc = new ArrayList<>();
            int v;
            do {
                v = stack.pop();
                onStack[v] = false;
                scc.add(v);
            } while (v != u);
            components.add(scc);
        }
    }

    public List<List<Integer>> getComponents() {
        return components;
    }

    public int getSccCount() {
        return components.size();
    }

    public int getDfsVisits() {
        return dfsVisits;
    }

    public int getEdgeTraversals() {
        return edgeTraversals;
    }

    public void printSummary() {
        System.out.printf("Found %d SCCs\n", getSccCount());
        for (int i = 0; i < components.size(); i++) {
            System.out.println("SCC " + (i + 1) + ": " + components.get(i));
        }
        System.out.printf("DFS Visits: %d | Edge Traversals: %d\n",
                dfsVisits, edgeTraversals);
    }

    public static void main(String[] args) throws Exception {
        Graph g = Graph.fromJson("data_final/small_1.json");
        TarjanSCC tarjan = new TarjanSCC(g);
        tarjan.run();
        tarjan.printSummary();
    }
}
