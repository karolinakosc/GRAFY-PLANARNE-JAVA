package pl.edu.pw.graph.model;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Vertex> vertices;
    private List<Edge> edges;

    public Graph() {
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public void addVertex(Vertex v) {
        if (vertices.stream().noneMatch(vertex -> vertex.getId() == v.getId())) {
            vertices.add(v);
        }
    }

    public void addEdge(Edge e) {
        edges.add(e);
        addVertex(new Vertex(e.getSourceId()));
        addVertex(new Vertex(e.getTargetId()));
    }

    public List<Vertex> getVertices() { return vertices; }
    public List<Edge> getEdges() { return edges; }
}
