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

        if (graph == null || graph.getVertices() == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // miejsce na rysowanie krawędzi (w przyszłości)

        g2d.setColor(Color.RED);
        int radius = 15;

        for (Vertex v : graph.getVertices()) {
            double x = v.getX();
            double y = v.getY();

            // zabezpieczenie przed błędami z c
            if (Double.isNaN(x) || Double.isNaN(y) || Double.isInfinite(x) || Double.isInfinite(y)) {
                System.err.println("Pominięto uszkodzony wierzchołek z C!");
                continue;
            }

            int drawX = (int) x - radius;
            int drawY = (int) y - radius;

            g2d.fillOval(drawX, drawY, radius * 2, radius * 2);

            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(v.getId()), drawX + 35, drawY + 15);
            g2d.setColor(Color.RED);
        }
    }
}