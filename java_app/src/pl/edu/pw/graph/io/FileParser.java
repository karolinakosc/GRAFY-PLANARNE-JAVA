package pl.edu.pw.graph.io;

import pl.edu.pw.graph.model.Edge;
import pl.edu.pw.graph.model.Graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileParser {

    public static Graph parseFromFile(String filePath) {
        Graph graph = new Graph();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+");

                // Format: <nazwa_krawędzi> <wierzchołek_1> <wierzchołek_2> <waga_krawędzi>
                if (parts.length == 4) {
                    String name = parts[0];
                    int v1 = Integer.parseInt(parts[1]);
                    int v2 = Integer.parseInt(parts[2]);
                    double weight = Double.parseDouble(parts[3]);

                    graph.addEdge(new Edge(name, v1, v2, weight));
                }
            }
            System.out.println("Pomyślnie wczytano plik. Znaleziono " + graph.getEdges().size() + " krawędzi.");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Błąd podczas czytania pliku: " + e.getMessage());
        }

        return graph;
    }
}