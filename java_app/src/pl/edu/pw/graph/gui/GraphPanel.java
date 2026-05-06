package pl.edu.pw.graph.gui;

import javax.swing.*;
import java.awt.*;

public class GraphPanel extends JPanel {

    public GraphPanel() {
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2)); // Grubość linii
        g2d.drawLine(400, 100, 200, 400); // Lewa linia
        g2d.drawLine(400, 100, 600, 400); // Prawa linia
        g2d.drawLine(200, 400, 600, 400); // Dolna linia

        
        g2d.setColor(Color.RED);
        int radius = 15; // Promień wierzchołka
        g2d.fillOval(400 - radius, 100 - radius, radius * 2, radius * 2); // Górny
        g2d.fillOval(200 - radius, 400 - radius, radius * 2, radius * 2); // Lewy dolny
        g2d.fillOval(600 - radius, 400 - radius, radius * 2, radius * 2); // Prawy dolny
    }
}