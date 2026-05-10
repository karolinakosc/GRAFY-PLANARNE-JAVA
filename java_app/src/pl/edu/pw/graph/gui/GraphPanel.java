package pl.edu.pw.graph.gui;

import pl.edu.pw.graph.model.Graph;
import pl.edu.pw.graph.model.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphPanel extends JPanel {

    private Graph graph;
    private double scale = 50.0;

    private double panX = 0.0;
    private double panY = 0.0;
    private Point dragStartPoint = null;

    public GraphPanel() {
        setBackground(Color.WHITE);

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    scale *= 1.1; // Scroll w górę - powiększ
                } else {
                    scale /= 1.1; // Scroll w dół - pomniejsz
                }
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStartPoint = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStartPoint != null) {
                    double dx = e.getPoint().getX() - dragStartPoint.getX();
                    double dy = e.getPoint().getY() - dragStartPoint.getY();
                    panX += dx;
                    panY += dy;
                    dragStartPoint = e.getPoint();
                    repaint();
                }
            }
        });
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
        this.scale = 50.0;
        this.panX = 0.0;
        this.panY = 0.0;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (graph == null || graph.getVertices() == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int offsetX = getWidth() / 2 + (int) panX;
        int offsetY = getHeight() / 2 + (int) panY;

        g2d.setColor(Color.LIGHT_GRAY);
        for (pl.edu.pw.graph.model.Edge edge : graph.getEdges()) {
            Vertex v1 = findVertexById(edge.getSourceId());
            Vertex v2 = findVertexById(edge.getTargetId());

            if (v1 != null && v2 != null) {
                // ZABEZPIECZENIE przed błędami matematycznymi (NaN z silnika C)
                if (isInvalid(v1) || isInvalid(v2)) continue;

                int x1 = (int) (v1.getX() * scale) + offsetX;
                int y1 = (int) (v1.getY() * scale) + offsetY;
                int x2 = (int) (v2.getX() * scale) + offsetX;
                int y2 = (int) (v2.getY() * scale) + offsetY;
                g2d.drawLine(x1, y1, x2, y2);
            }
        }

        int radius = 10;
        for (Vertex v : graph.getVertices()) {
            // ZABEZPIECZENIE przed błędami matematycznymi (NaN z silnika C)
            if (isInvalid(v)) continue;

            System.out.println("Rysuję wierzchołek " + v.getId() + " na: " + v.getX() + ", " + v.getY());

            int drawX = (int) (v.getX() * scale) + offsetX;
            int drawY = (int) (v.getY() * scale) + offsetY;

            g2d.setColor(Color.RED);
            g2d.fillOval(drawX - radius, drawY - radius, radius * 2, radius * 2);

            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(v.getId()), drawX + 12, drawY + 5);
        }
    }

    // Funkcja filtrująca zepsute wierzchołki (ochrona programu przed crashem)
    private boolean isInvalid(Vertex v) {
        return Double.isNaN(v.getX()) || Double.isInfinite(v.getX()) ||
                Double.isNaN(v.getY()) || Double.isInfinite(v.getY());
    }

    private Vertex findVertexById(int id) {
        return graph.getVertices().stream()
                .filter(v -> v.getId() == id)
                .findFirst()
                .orElse(null);
    }
}