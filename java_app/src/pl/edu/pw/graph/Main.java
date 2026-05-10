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
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        JButton btnLoad = new JButton("Wczytaj Graf");
        JComboBox<String> comboAlgorithm = new JComboBox<>(new String[]{"Algorytm Spektralny", "Triangulacja"});
        JButton btnGenerate = new JButton("Generuj");
        JButton btnSave = new JButton("Zapisz Wyniki");

        controlPanel.add(btnLoad);
        controlPanel.add(comboAlgorithm);
        controlPanel.add(btnGenerate);
        controlPanel.add(btnSave);

        JTextArea logArea = new JTextArea(5, 40);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        logArea.append("System gotowy...\n");

        GraphPanel canvasPanel = new GraphPanel();

        final File[] currentFile = {null};
        final Graph[] currentGraph = {null};

        btnLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);

            if (result == JFileChooser.APPROVE_OPTION) {
                currentFile[0] = fileChooser.getSelectedFile();
                try {
                    currentGraph[0] = FileParser.parseInputGraph(currentFile[0].getAbsolutePath());
                    canvasPanel.setGraph(currentGraph[0]); // Dajemy graf do panelu
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
                if (oldResult.exists()) oldResult.delete(); // Usuwamy śmieci po poprzednim razie
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

        frame.setLocationRelativeTo(null); // środkuje okno na ekranie
        frame.setVisible(true);
    }
}
