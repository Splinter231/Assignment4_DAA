package graph;

import model.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    @Test
    public void testGraphCreation() throws Exception {
        Graph g = Graph.fromJson("data/small_1.json");
        System.out.println(g);
        assertTrue(g.getVertexCount() > 0);
        assertNotNull(g.getEdges());
    }
}
