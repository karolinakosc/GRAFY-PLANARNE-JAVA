package pl.edu.pw.graph.io;

import pl.edu.pw.graph.model.Edge;
import pl.edu.pw.graph.model.Graph;
import pl.edu.pw.graph.model.Vertex;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class FileParser {

    public static Graph parseInputGraph(String filePath) throws IOException {
        Graph graph = new Graph();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\s+");
                if (parts.length >= 4) {
                    graph.addEdge(new Edge(parts[0], Integer.parseInt(parts[1]),
                            Integer.parseInt(parts[2]), Double.parseDouble(parts[3])));
                }
            }
        }
        return graph;
    }

    public static void updateGraphWithCoordinates(Graph graph, String outputFilePath) throws IOException {
        // Regex szuka liczb (również ujemnych i z kropką)
        Pattern numPattern = Pattern.compile("[-+]?\\d*\\.?\\d+");

        try (BufferedReader br = new BufferedReader(new FileReader(outputFilePath))) {
            String line;
            int idx = 0;
            List<Vertex> vertices = graph.getVertices();

            while ((line = br.readLine()) != null) {
                Matcher m = numPattern.matcher(line);
                List<Double> nums = new ArrayList<>();
                while (m.find()) nums.add(Double.parseDouble(m.group()));

                // Jeśli 3 liczby (format: ID X Y z algorytmu spektralnego)
                if (nums.size() >= 3) {
                    updateVertex(graph, nums.get(0).intValue(), nums.get(1), nums.get(2));
                }
                // Jeśli 2 liczby (format: "pozycje: X, Y" z triangulacji)
                else if (nums.size() >= 2 && idx < vertices.size()) {
                    vertices.get(idx).setX(nums.get(0));
                    vertices.get(idx).setY(nums.get(1));
                    idx++;
                }
            }
        }
    }

    private static void updateVertex(Graph g, int id, double x, double y) {
        for (Vertex v : g.getVertices()) {
            if (v.getId() == id) {
                v.setX(x); v.setY(y);
                break;
            }
        }
    }
}