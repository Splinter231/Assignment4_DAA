package graph.scc;

import model.Graph;
import model.Edge;

import java.util.*;

public class CondensationGraphBuilder {

    private final Graph originalGraph;
    private final TarjanSCC tarjan;
    private final List<List<Integer>> components;

    private final Map<Integer, Integer> vertexToComponent = new HashMap<>();
    private final Map<Integer, Set<Integer>> dagAdj = new HashMap<>();

    public CondensationGraphBuilder(Graph graph, TarjanSCC tarjan) {
        this.originalGraph = graph;
        this.tarjan = tarjan;
        this.components = tarjan.getComponents();

        for (int compId = 0; compId < components.size(); compId++) {
            for (int v : components.get(compId)) {
                vertexToComponent.put(v, compId);
            }
        }

        buildCondensationGraph();
    }

    private void buildCondensationGraph() {
        for (int i = 0; i < components.size(); i++) {
            dagAdj.put(i, new HashSet<>());
        }

        for (Edge e : originalGraph.getEdges()) {
            int fromComp = vertexToComponent.get(e.getFrom());
            int toComp = vertexToComponent.get(e.getTo());
            if (fromComp != toComp) {
                dagAdj.get(fromComp).add(toComp);
            }
        }
    }

    public Map<Integer, Set<Integer>> getDagAdjacency() {
        return dagAdj;
    }

    public Map<Integer, Integer> getVertexToComponentMap() {
        return vertexToComponent;
    }

    public int getCondensedVertexCount() {
        return components.size();
    }

    public void printSummary() {
        System.out.println(" Condensed Graph Summary ");
        System.out.printf("Original vertices: %d | SCCs: %d%n",
                originalGraph.getVertexCount(), components.size());

        System.out.println("SCC sizes:");
        for (int i = 0; i < components.size(); i++) {
            System.out.printf("  SCC %d: size=%d, vertices=%s%n",
                    i, components.get(i).size(), components.get(i));
        }

        System.out.println("DAG edges between components:");
        for (int from : dagAdj.keySet()) {
            for (int to : dagAdj.get(from)) {
                System.out.printf("  SCC %d → SCC %d%n", from, to);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Graph g = Graph.fromJson("data_final/medium_1.json");
        TarjanSCC tarjan = new TarjanSCC(g);
        tarjan.run();

        CondensationGraphBuilder cond = new CondensationGraphBuilder(g, tarjan);
        cond.printSummary();
    }
}
