package pl.edu.pw.graph;

import pl.edu.pw.graph.gui.GraphPanel;
import javax.swing.*;
import java.awt.*;
import java.io.File;

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


        btnLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                logArea.append("[INFO] Wczytano plik: " + selectedFile.getAbsolutePath() + "\n");

            } else {
                logArea.append("[INFO] Anulowano wybór pliku.\n");
            }
        });

        btnGenerate.addActionListener(e -> {
            String selectedAlgo = (String) comboAlgorithm.getSelectedItem();
            logArea.append("[WORK] Uruchamiam " + selectedAlgo + " w silniku C...\n");
        });

        btnSave.addActionListener(e -> {
            logArea.append("[INFO] Otwieram okno zapisu wyników...\n");

        });

        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(canvasPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null); // Środkuje okno na ekranie
        frame.setVisible(true);
    }
}
