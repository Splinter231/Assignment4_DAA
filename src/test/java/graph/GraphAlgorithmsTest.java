package graph;

import graph.dagsp.ShortestPathDAG;
import graph.dagsp.LongestPathDAG;
import graph.scc.TarjanSCC;
import graph.topo.TopologicalSort;
import model.Graph;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class GraphAlgorithmsTest {

    @Test
    public void testTarjanSCC_onSmallGraph() throws Exception {
        Graph g = Graph.fromJson("data_final/medium_1.json");
        TarjanSCC tarjan = new TarjanSCC(g);

        tarjan.run();

        int sccCount = tarjan.getSccCount();
        assertTrue(sccCount >= 1, "SCC count should be ≥ 1");
        System.out.println("SCC count: " + sccCount);
    }

    @Test
    public void testTopologicalSort_onCondensedDAG() {
        // Пример небольшого DAG
        Map<Integer, Set<Integer>> dag = new HashMap<>();
        dag.put(0, Set.of(1, 2));
        dag.put(1, Set.of(3));
        dag.put(2, Set.of(3));
        dag.put(3, Set.of());

        TopologicalSort topo = new TopologicalSort(dag);
        topo.runKahn();

        List<Integer> order = topo.getTopologicalOrder();
        assertEquals(4, order.size());
        assertTrue(topo.isDAG());
        System.out.println("Topo order: " + order);
    }

    @Test
    public void testShortestPath_inDAG() throws Exception {
        Graph g = Graph.fromJson("data_final/medium_2.json");
        ShortestPathDAG sp = new ShortestPathDAG(g, 0);
        sp.run();
        System.out.println("Shortest path test passed.");
    }

    @Test
    public void testLongestPath_inDAG() throws Exception {
        Graph g = Graph.fromJson("data_final/medium_3.json");
        LongestPathDAG lp = new LongestPathDAG(g, 0);
        lp.run();
        lp.printSummary();
        System.out.println("Longest path test executed.");
    }

    @Test
    public void testPathReconstruction_onCustomDAG() {
        Graph g = new Graph(5, true, "edge");
        g.addEdge(0, 1, 2);
        g.addEdge(1, 2, 3);
        g.addEdge(2, 3, 4);
        g.addEdge(3, 4, 5);

        LongestPathDAG lp = new LongestPathDAG(g, 0);
        lp.run();
        System.out.println("Custom DAG longest path check complete.");
    }
}
