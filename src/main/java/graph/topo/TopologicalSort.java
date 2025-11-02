package graph.topo;

import java.util.*;

public class TopologicalSort {

    private final Map<Integer, Set<Integer>> dagAdj;
    private final Map<Integer, Integer> indegree;
    private final List<Integer> topoOrder;
    private int pushCount = 0;
    private int popCount = 0;

    public TopologicalSort(Map<Integer, Set<Integer>> dagAdj) {
        this.dagAdj = dagAdj;
        this.indegree = new HashMap<>();
        this.topoOrder = new ArrayList<>();
        buildIndegree();
    }

    private void buildIndegree() {
        for (int v : dagAdj.keySet()) indegree.put(v, 0);
        for (int u : dagAdj.keySet()) {
            for (int v : dagAdj.get(u)) {
                indegree.put(v, indegree.getOrDefault(v, 0) + 1);
            }
        }
    }

    public void runKahn() {
        Queue<Integer> queue = new ArrayDeque<>();

        for (int v : indegree.keySet()) {
            if (indegree.get(v) == 0) {
                queue.add(v);
                pushCount++;
            }
        }

        while (!queue.isEmpty()) {
            int u = queue.poll();
            popCount++;
            topoOrder.add(u);

            for (int v : dagAdj.get(u)) {
                indegree.put(v, indegree.get(v) - 1);
                if (indegree.get(v) == 0) {
                    queue.add(v);
                    pushCount++;
                }
            }
        }
    }

    public List<Integer> getTopologicalOrder() {
        return topoOrder;
    }

    public int getPushCount() {
        return pushCount;
    }

    public int getPopCount() {
        return popCount;
    }

    public boolean isDAG() {
        return topoOrder.size() == dagAdj.size();
    }

    public void printSummary() {
        System.out.println("=== Topological Sort Summary ===");
        System.out.println("Order: " + topoOrder);
        System.out.printf("Push ops: %d | Pop ops: %d%n", pushCount, popCount);
        System.out.println(isDAG() ? "Graph is DAG " : "Graph contains cycles ");
    }
    public static void main(String[] args) throws Exception {
        Map<Integer, Set<Integer>> dag = new HashMap<>();
        dag.put(0, Set.of(1, 2));
        dag.put(1, Set.of(3));
        dag.put(2, Set.of(3));
        dag.put(3, Set.of());

        TopologicalSort topo = new TopologicalSort(dag);
        topo.runKahn();
        topo.printSummary();
    }
}
