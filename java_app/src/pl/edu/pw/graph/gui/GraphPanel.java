package pl.edu.pw.graph.gui;

import pl.edu.pw.graph.model.Graph;
import pl.edu.pw.graph.model.Vertex;


import javax.swing.*;
import java.awt.*;

public class GraphPanel extends JPanel {

    private Graph graph;

    public GraphPanel() {
        setBackground(Color.WHITE);
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (graph == null || graph.getVertices() == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int scale = 150;
        int offsetX = getWidth() / 2;
        int offsetY = getHeight() / 2;

        g2d.setColor(Color.LIGHT_GRAY);
        for (pl.edu.pw.graph.model.Edge edge : graph.getEdges()) {
            Vertex v1 = findVertexById(edge.getSourceId());
            Vertex v2 = findVertexById(edge.getTargetId());

            if (v1 != null && v2 != null) {
                int x1 = (int) (v1.getX() * scale) + offsetX;
                int y1 = (int) (v1.getY() * scale) + offsetY;
                int x2 = (int) (v2.getX() * scale) + offsetX;
                int y2 = (int) (v2.getY() * scale) + offsetY;
                g2d.drawLine(x1, y1, x2, y2);
            }
        }

        int radius = 10;
        for (Vertex v : graph.getVertices()) {

            int drawX = (int) (v.getX() * scale) + offsetX;
            int drawY = (int) (v.getY() * scale) + offsetY;

            g2d.setColor(Color.RED);
            g2d.fillOval(drawX - radius, drawY - radius, radius * 2, radius * 2);

            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(v.getId()), drawX + 12, drawY + 5);
        }
    }

    private Vertex findVertexById(int id) {
        return graph.getVertices().stream()
                .filter(v -> v.getId() == id)
                .findFirst()
                .orElse(null);
    }
}