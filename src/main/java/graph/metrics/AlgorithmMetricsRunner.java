package graph.metrics;

import graph.scc.TarjanSCC;
import graph.topo.TopologicalSort;
import graph.dagsp.ShortestPathDAG;
import graph.dagsp.LongestPathDAG;
import model.Graph;

import java.io.File;
import java.util.*;

public class AlgorithmMetricsRunner {

    public static void main(String[] args) throws Exception {
        File dataDir = new File("data_final");
        File metricsDir = new File("metrics");
        if (!metricsDir.exists()) metricsDir.mkdirs();

        File[] jsonFiles = dataDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (jsonFiles == null) {
            System.out.println("No datasets found.");
            return;
        }

        for (File file : jsonFiles) {
            System.out.println("\n=== Running metrics for " + file.getName() + " ===");
            Graph g = Graph.fromJson(file.getPath());
            Metrics m = new Metrics();

            TarjanSCC scc = new TarjanSCC(g);
            m.startTimer();
            scc.run();
            m.stopTimer();
            m.put("dataset", file.getName());
            m.put("vertices", g.getVertexCount());
            m.put("edges", g.getEdges().size());
            m.put("scc_count", scc.getSccCount());
            m.put("scc_time_ms", m.getElapsedMs());

            Map<Integer, Set<Integer>> dag = new HashMap<>();
            for (int i = 0; i < g.getVertexCount(); i++) {
                dag.put(i, new HashSet<>());
                for (var e : g.getOutgoingEdges(i)) dag.get(i).add(e.getTo());
            }

            TopologicalSort topo = new TopologicalSort(dag);
            topo.runKahn();
            m.put("topo_push", topo.getPushCount());
            m.put("topo_pop", topo.getPopCount());

            ShortestPathDAG sp = new ShortestPathDAG(g, 0);
            m.startTimer();
            sp.run();
            m.stopTimer();
            m.put("shortest_time_ms", m.getElapsedMs());

            LongestPathDAG lp = new LongestPathDAG(g, 0);
            m.startTimer();
            lp.run();
            m.stopTimer();
            m.put("longest_time_ms", m.getElapsedMs());

            String outPath = "metrics/" + file.getName().replace(".json", "_metrics.json");
            m.saveToFile(outPath);
            System.out.println("Saved metrics → " + outPath);
        }
    }
}
