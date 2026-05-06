package pl.edu.pw.graph.model;

public class Edge {
    private String name;
    private int sourceId;
    private int targetId;
    private double weight;

    public Edge(String name, int sourceId, int targetId, double weight) {
        this.name = name;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.weight = weight;
    }

    public String getName() { return name; }
    public int getSourceId() { return sourceId; }
    public int getTargetId() { return targetId; }
    public double getWeight() { return weight; }

    @Override
    public String toString() {
        return "Edge{" + "name='" + name + '\'' + ", source=" + sourceId + ", target=" + targetId + ", weight=" + weight + '}';
    }
}

