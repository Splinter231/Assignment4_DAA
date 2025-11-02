package graph.dagsp;

import model.Graph;
import org.junit.jupiter.api.Test;

public class LongestPathDAGTest {

    @Test
    public void testCriticalPath() throws Exception {
        Graph g = Graph.fromJson("data_final/small_2.json");
        LongestPathDAG lp = new LongestPathDAG(g, 0);
        lp.run();
        lp.printSummary();
    }
}
