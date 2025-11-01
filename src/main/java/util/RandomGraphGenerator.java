package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class RandomGraphGenerator {

    private static class Edge {
        int u, v, w;
        Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    private static class GraphData {
        boolean directed = true;
        int n;
        List<Edge> edges;
        int source;
        String weight_model = "edge";
    }

    private static final Random rand = new Random();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) throws IOException {
        generateAllDatasets("data");
    }

    public static void generateAllDatasets(String outputDir) throws IOException {
        Files.createDirectories(Path.of(outputDir));

        StringBuilder summary = new StringBuilder("=== DATASET SUMMARY ===\n");

        generateCategory(outputDir, "small", 6, 10, 3, summary);
        generateCategory(outputDir, "medium", 10, 20, 3, summary);
        generateCategory(outputDir, "large", 20, 50, 3, summary);

        try (FileWriter fw = new FileWriter(outputDir + "/summary.txt")) {
            fw.write(summary.toString());
        }

        System.out.println("All datasets generated successfully in /data");
    }

    private static void generateCategory(String dir, String label, int minNodes, int maxNodes,
                                         int count, StringBuilder summary) throws IOException {

        for (int i = 1; i <= count; i++) {
            int n = rand.nextInt(maxNodes - minNodes + 1) + minNodes;

            double density;
            if (label.equals("small")) density = rand.nextDouble(0.1, 0.25);
            else if (label.equals("medium")) density = rand.nextDouble(0.25, 0.45);
            else density = rand.nextDouble(0.45, 0.75);

            int cycles;
            if (label.equals("small")) cycles = rand.nextInt(3);
            else if (label.equals("medium")) cycles = rand.nextInt(2, 5);
            else cycles = rand.nextInt(4, 8);

            GraphData g = generateGraph(n, density, cycles);

            String filename = String.format("%s/%s_%d.json", dir, label, i);
            try (FileWriter fw = new FileWriter(filename)) {
                gson.toJson(g, fw);
            }

            summary.append(String.format(Locale.US,
                    "%s_%d.json → n=%d, edges=%d, density=%.2f, cycles=%d\n",
                    label, i, g.n, g.edges.size(), density, cycles));

            System.out.printf("Generated %-10s | nodes=%2d | edges=%3d | cycles=%d\n",
                    filename, g.n, g.edges.size(), cycles);
        }
    }

    private static GraphData generateGraph(int n, double density, int cycles) {
        GraphData g = new GraphData();
        g.n = n;
        g.source = rand.nextInt(n);
        g.edges = new ArrayList<>();

        int maxEdges = n * (n - 1);
        int targetEdges = (int) (density * maxEdges);

        Set<String> used = new HashSet<>();

        for (int i = 0; i < targetEdges; i++) {
            int u = rand.nextInt(n);
            int v = rand.nextInt(n);
            if (u == v || used.contains(u + "-" + v)) continue;
            int w = rand.nextInt(9) + 1;
            g.edges.add(new Edge(u, v, w));
            used.add(u + "-" + v);
        }

        for (int i = 0; i < cycles; i++) {
            int a = rand.nextInt(n);
            int b = rand.nextInt(n);
            if (a != b && !used.contains(b + "-" + a)) {
                int w = rand.nextInt(5) + 1;
                g.edges.add(new Edge(b, a, w));
                used.add(b + "-" + a);
            }
        }

        return g;
    }
}
