package pl.edu.pw.graph;

import pl.edu.pw.graph.gui.GraphPanel;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import pl.edu.pw.graph.model.Graph;
import pl.edu.pw.graph.io.FileParser;
import pl.edu.pw.graph.algorithms.ProcessExecutor;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Wizualizacja Grafów Planarnych");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 600);
        frame.setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel(new BorderLayout(20, 0));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        controlPanel.setPreferredSize(new Dimension(0, 95));

        JPanel buttonsPanel = new JPanel();
        JButton btnLoad = new JButton("Wczytaj Graf");
        JComboBox<String> comboAlgorithm = new JComboBox<>(new String[]{"Algorytm Spektralny", "Triangulacja"});
        JButton btnGenerate = new JButton("Generuj");
        JButton btnSave = new JButton("Zapisz Wyniki");

        buttonsPanel.add(btnLoad);
        buttonsPanel.add(comboAlgorithm);
        buttonsPanel.add(btnGenerate);
        buttonsPanel.add(btnSave);

        //legenda wierzchołków
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        legendPanel.setBorder(BorderFactory.createTitledBorder("Legenda stopni wierzchołków"));

        JLabel lblCorner1 = new JLabel("<html><span style='color: rgb(100,149,237); font-size: 14px;'>■</span> Stopień &le; 1</html>");
        JLabel lblCorner2 = new JLabel("<html><span style='color: rgb(60,179,113); font-size: 14px;'>■</span> Stopień 2 - 3</html>");
        JLabel lblCorner3 = new JLabel("<html><span style='color: rgb(255,165,0); font-size: 14px;'>■</span> Stopień 4 - 5</html>");
        JLabel lblCorner4 = new JLabel("<html><span style='color: rgb(220,20,60); font-size: 14px;'>■</span> Stopień &ge; 6</html>");

        Font legendFont = new Font("Arial", Font.BOLD, 12);
        lblCorner1.setFont(legendFont);
        lblCorner2.setFont(legendFont);
        lblCorner3.setFont(legendFont);
        lblCorner4.setFont(legendFont);

        legendPanel.add(lblCorner1);
        legendPanel.add(lblCorner2);
        legendPanel.add(lblCorner3);
        legendPanel.add(lblCorner4);

        JLabel vertexInfoArea = new JLabel();
        vertexInfoArea.setFont(new Font("Arial", Font.PLAIN, 12));
        vertexInfoArea.setText("<html><div style='text-align: center; width: 140px;'><br>Kliknij wierzchołek,<br>aby zobaczyć dane.<br><br></div></html>");
        vertexInfoArea.setBorder(BorderFactory.createTitledBorder("Dane wierzchołka"));
        vertexInfoArea.setPreferredSize(new Dimension(200, 65));

        //przyciski po lewej, legenda w centrum, dane po prawej
        controlPanel.add(buttonsPanel, BorderLayout.WEST);
        controlPanel.add(legendPanel, BorderLayout.CENTER);
        controlPanel.add(vertexInfoArea, BorderLayout.EAST);

        JTextArea logArea = new JTextArea(5, 40);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        logArea.append("System gotowy...\n");

        GraphPanel canvasPanel = new GraphPanel();
        canvasPanel.setInfoArea(vertexInfoArea);

        final File[] currentFile = {null};
        final Graph[] currentGraph = {null};

        btnLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);

            if (result == JFileChooser.APPROVE_OPTION) {
                currentFile[0] = fileChooser.getSelectedFile();
                try {
                    currentGraph[0] = FileParser.parseInputGraph(currentFile[0].getAbsolutePath());
                    canvasPanel.setGraph(currentGraph[0]); // dajemy graf do panelu
                    canvasPanel.repaint();

                    logArea.append("[INFO] Wczytano strukturę: " + currentFile[0].getName() + "\n");
                } catch (Exception ex) {
                    logArea.append("[ERROR] Błąd parsowania: " + ex.getMessage() + "\n");
                }
            }
        });


        btnGenerate.addActionListener(e -> {
            if (currentFile[0] == null) {
                JOptionPane.showMessageDialog(frame, "Najpierw wczytaj plik!");
                return;
            }

            String selectedAlgo = (String) comboAlgorithm.getSelectedItem();
            String algoParam = selectedAlgo.contains("Spektralny") ? "spectral" : "triangulation";

            logArea.append("[WORK] Uruchamiam silnik C...\n");

            try {
                File oldResult = new File("./c_app/wynik.txt");
                if (oldResult.exists()) oldResult.delete();
                ProcessExecutor.runCEngine("./c_app/program", "./c_app/" + currentFile[0].getName(), algoParam);

                FileParser.updateGraphWithCoordinates(currentGraph[0], "./c_app/wynik.txt");

                canvasPanel.repaint();
                JOptionPane.showMessageDialog(frame, "Graf wygenerowany pomyślnie!");
                logArea.append("[INFO] Obliczenia zakończone.\n");

            } catch (Exception ex) {
                logArea.append("[ERROR] Coś poszło nie tak: " + ex.getMessage() + "\n");
                JOptionPane.showMessageDialog(frame, "Błąd: " + ex.getMessage(), "Błąd silnika", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSave.addActionListener(e -> {
            logArea.append("[INFO] Otwieram okno zapisu wyników...\n");
        });

        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(canvasPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.setLocationRelativeTo(null); // środkuje okno na ekranie
        frame.setVisible(true);
    }
}
