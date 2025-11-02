package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class RandomGraphGenerator {

    private static final Random rand = new Random();

    public static void main(String[] args) throws IOException {
        new java.io.File("data_final").mkdirs();

        generateCategory("small", 6, 10, 3, 0.15, 0.25, 1);
        generateCategory("medium", 10, 20, 3, 0.25, 0.40, 3);
        generateCategory("large", 20, 50, 3, 0.45, 0.65, 6);

        System.out.println("9 datasets generated under /data_final/");
    }

    private static void generateCategory(String prefix, int minN, int maxN, int count,
                                         double minDensity, double maxDensity, int maxCycles)
            throws IOException {

        for (int i = 1; i <= count; i++) {
            int n = rand.nextInt(maxN - minN + 1) + minN;
            double density = minDensity + rand.nextDouble() * (maxDensity - minDensity);

            List<Map<String, Object>> edges = new ArrayList<>();
            int possible = n * (n - 1);
            int edgeCount = (int) (possible * density);

            for (int k = 0; k < edgeCount; k++) {
                int u = rand.nextInt(n);
                int v = rand.nextInt(n);
                if (u == v) continue;

                boolean allowCycle = switch (prefix) {
                    case "small" -> rand.nextDouble() < 0.15;
                    case "medium" -> rand.nextDouble() < 0.35;
                    case "large" -> rand.nextDouble() < 0.6;
                    default -> false;
                };
                if (!allowCycle && u > v) continue;

                int w = rand.nextInt(1, 10);
                Map<String, Object> e = new HashMap<>();
                e.put("u", u);
                e.put("v", v);
                e.put("w", w);
                edges.add(e);
            }

            Map<String, Object> graph = new LinkedHashMap<>();
            graph.put("directed", true);
            graph.put("n", n);
            graph.put("edges", edges);
            graph.put("source", rand.nextInt(n));
            graph.put("weight_model", "edge");

            String filename = String.format("data_final/%s_%d.json", prefix, i);
            try (FileWriter writer = new FileWriter(filename)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(graph, writer);
            }

            System.out.printf("%s_%d.json → n=%d, edges=%d, density=%.2f%n",
                    prefix, i, n, edges.size(), density);
        }
    }
}
