package graph.topo;

import graph.scc.CondensationGraphBuilder;
import graph.scc.TarjanSCC;
import model.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TopologicalSortTest {

    @Test
    public void testTopologicalOrderOnCondensedGraph() throws Exception {
        Graph g = Graph.fromJson("data/small_1.json");
        TarjanSCC tarjan = new TarjanSCC(g);
        tarjan.run();

        CondensationGraphBuilder cond = new CondensationGraphBuilder(g, tarjan);

        TopologicalSort topo = new TopologicalSort(cond.getDagAdjacency());
        topo.runKahn();
        topo.printSummary();

        assertTrue(topo.isDAG());
        assertNotNull(topo.getTopologicalOrder());
        assertEquals(topo.getPopCount(), topo.getTopologicalOrder().size());
    }
}
