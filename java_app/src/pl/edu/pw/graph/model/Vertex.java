package pl.edu.pw.graph.model;

public class Vertex {
    private int id;
    private double x;
    private double y;

    public Vertex(int id) {
        this.id = id;
        this.x = 0.0;
        this.y = 0.0;
    }

    public int getId() { return id; }
    public double getX() { return x; }
    public double getY() { return y; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    @Override
    public String toString() {
        return "Vertex{" + "id=" + id + ", x=" + x + ", y=" + y + '}';
    }
}