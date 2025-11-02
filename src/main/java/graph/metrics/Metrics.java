package graph.metrics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Metrics {
    private final Map<String, Object> data = new LinkedHashMap<>();
    private long startTime;
    private long endTime;

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        endTime = System.nanoTime();
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public double getElapsedMs() {
        return (endTime - startTime) / 1_000_000.0;
    }

    public void saveToFile(String filePath) {
        data.put("time_ms", getElapsedMs());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter fw = new FileWriter(filePath)) {
            gson.toJson(data, fw);
        } catch (IOException e) {
            throw new RuntimeException("Error writing metrics file", e);
        }
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
