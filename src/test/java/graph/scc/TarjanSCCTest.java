package graph.scc;

import model.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TarjanSCCTest {

    @Test
    public void testTarjanSccDetection() throws Exception {
        Graph g = Graph.fromJson("data_final/medium_1.json");
        TarjanSCC tarjan = new TarjanSCC(g);
        tarjan.run();

        assertTrue(tarjan.getSccCount() > 0);
        System.out.println("SCC count: " + tarjan.getSccCount());
        System.out.println("DFS visits: " + tarjan.getDfsVisits());
        System.out.println("Edges traversed: " + tarjan.getEdgeTraversals());
    }
}
