package pl.edu.pw.graph.gui;

import pl.edu.pw.graph.model.Graph;
import pl.edu.pw.graph.model.Vertex;
import pl.edu.pw.graph.model.Edge; // import krawędzi

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphPanel extends JPanel {

    private Graph graph;
    private double scale = 50.0;

    private double panX = 0.0;
    private double panY = 0.0;
    private Point dragStartPoint = null;

    // zmienna do wag (ustawiona na true, żebyś od razu widziała efekt)
    private boolean showWeights = true;

    private Vertex selectedVertex = null;

    private JLabel infoArea = null;

    public void setInfoArea(JLabel infoArea) {
        this.infoArea = infoArea;
    }
    public GraphPanel() {
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStartPoint = e.getPoint(); //do przesuwania kamery

                // szukamy klikniętego wierzchołka
                if (graph == null || graph.getVertices() == null) return;

                int offsetX = getWidth() / 2 + (int) panX;
                int offsetY = getHeight() / 2 + (int) panY;
                boolean found = false;

                for (Vertex v : graph.getVertices()) {
                    if (isInvalid(v)) continue;
                    int drawX = (int) (v.getX() * scale) + offsetX;
                    int drawY = (int) (v.getY() * scale) + offsetY;

                    // sprawdzamy czy kliknięto w promień kółka
                    if (Math.pow(e.getX() - drawX, 2) + Math.pow(e.getY() - drawY, 2) <= Math.pow(12, 2)) {
                        selectedVertex = v;
                        found = true;

                        // wpisuje dane do ramki w prawym górnym rogu
                        if (infoArea != null) {
                            String html = String.format(
                                    "<html><div style='width: 140px; padding: 2px;'>" +
                                            "<b>ID:</b> <font color='#d35400'>%d</font> &nbsp;|&nbsp; <b>Krawędzie:</b> <font color='#d35400'>%d</font><br>" +
                                            "<hr color='#bdc3c7'>" +
                                            "<b>X:</b> <i>%.3f</i><br>" +
                                            "<b>Y:</b> <i>%.3f</i>" +
                                            "</div></html>",
                                    v.getId(), getVertexDegree(v.getId()), v.getX(), v.getY()
                            );
                            infoArea.setText(html);
                        }

                        repaint();
                        break;
                    }
                }

                // jeśli kliknięto w puste tło(odznaczamy wierzchołek)
                if (!found) {
                    selectedVertex = null;
                    if (infoArea != null) {
                        infoArea.setText("<html><div style='text-align: center; width: 140px;'><br>Kliknij wierzchołek,<br>aby zobaczyć dane.<br><br></div></html>");
                    }
                    repaint();
                }
            }
        });

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    scale *= 1.1; // powiększ
                } else {
                    scale /= 1.1; //pomniejsz
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

                    double distance = Math.sqrt(dx * dx + dy * dy);

                    if (distance < 3.0) {
                        return;
                    }

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

    public void setShowWeights(boolean showWeights) {
        this.showWeights = showWeights;
        this.repaint();
    }

    //liczy ile połączeń ma wybrany wierzchołek
    private int getVertexDegree(int vertexId) {
        if (graph == null || graph.getEdges() == null) return 0;
        return (int) graph.getEdges().stream()
                .filter(e -> e.getSourceId() == vertexId || e.getTargetId() == vertexId)
                .count();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (graph == null || graph.getVertices() == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int offsetX = getWidth() / 2 + (int) panX;
        int offsetY = getHeight() / 2 + (int) panY;

        // rysowanie krawędzi z uwzględnieniem wagi
        for (Edge edge : graph.getEdges()) {
            Vertex v1 = findVertexById(edge.getSourceId());
            Vertex v2 = findVertexById(edge.getTargetId());

            if (v1 != null && v2 != null && !isInvalid(v1) && !isInvalid(v2)) {
                int x1 = (int) (v1.getX() * scale) + offsetX;
                int y1 = (int) (v1.getY() * scale) + offsetY;
                int x2 = (int) (v2.getX() * scale) + offsetX;
                int y2 = (int) (v2.getY() * scale) + offsetY;

                // im większa waga tym grubsza krawędź
                float thickness = (float) Math.max(1.0, edge.getWeight() / 2.0);
                g2d.setStroke(new BasicStroke(thickness));
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawLine(x1, y1, x2, y2);

                // wyświetlanie wartości wagi(środek linii)
                if (showWeights) {
                    g2d.setColor(Color.BLUE);
                    g2d.setFont(new Font("Arial", Font.BOLD, 12));
                    int midX = (x1 + x2) / 2;
                    int midY = (y1 + y2) / 2;
                    //waga z jednym miejscem po przecinku
                    g2d.drawString(String.format("%.1f", edge.getWeight()), midX, midY);
                }
            }
        }

        //kolory wierzchołków
        int radius = 10;
        g2d.setStroke(new BasicStroke(1.0f));

        for (Vertex v : graph.getVertices()) {
            if (isInvalid(v)) continue;

            int drawX = (int) (v.getX() * scale) + offsetX;
            int drawY = (int) (v.getY() * scale) + offsetY;

            // (stopień wierzchołka)
            int degree = getVertexDegree(v.getId());

            // kolor na podstawie ilości połączeń
            if (degree <= 1) {
                g2d.setColor(new Color(100, 149, 237));      // niebieski (pojedyńcze)
            } else if (degree <= 3) {
                g2d.setColor(new Color(60, 179, 113));       // zielony (średnie)
            } else if (degree <= 5) {
                g2d.setColor(new Color(255, 165, 0));        // pomarańczowy (gęste)
            } else {
                g2d.setColor(new Color(220, 20, 60));        // czerwony (bardzo gęste)
            }

           if (v == selectedVertex) {
                //zaznaczony wierzchołek rysujemy go o 5 pikseli większego (promień + 5)
                g2d.fillOval(drawX - radius - 5, drawY - radius - 5, (radius + 5) * 2, (radius + 5) * 2);

                // ciemna obwódka
                g2d.setColor(Color.DARK_GRAY);
                g2d.setStroke(new BasicStroke(2.0f));
                g2d.drawOval(drawX - radius - 5, drawY - radius - 5, (radius + 5) * 2, (radius + 5) * 2);
            } else {
                g2d.fillOval(drawX - radius, drawY - radius, radius * 2, radius * 2);
            }

            // numerek wierzchołka (dla zaznaczonego robimy pogrubiony)
            g2d.setColor(Color.BLACK);
            if (v == selectedVertex) {
                g2d.setFont(new Font("Arial", Font.BOLD, 13));
            } else {
                g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            }
            g2d.drawString(String.valueOf(v.getId()), drawX + 12, drawY + 5);
        }

    }

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