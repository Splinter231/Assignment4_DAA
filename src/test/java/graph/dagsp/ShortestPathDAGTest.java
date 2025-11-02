package graph.dagsp;

import model.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ShortestPathDAGTest {

    @Test
    public void testShortestPaths() throws Exception {
        Graph g = Graph.fromJson("data_final/medium_2.json");
        int source = g.getOutgoingEdges(0).isEmpty() ? 1 : 0;

        ShortestPathDAG sp = new ShortestPathDAG(g, source);
        sp.run();
        sp.printSummary();

        for (int i = 0; i < g.getVertexCount(); i++) {
            assertTrue(sp.getDistance(i) >= 0 || Double.isInfinite(sp.getDistance(i)));
        }

        System.out.println("Relaxations: " + sp.getRelaxCount());
    }
}
