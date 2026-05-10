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

            int resultCode = 0;

            //okienka JOPTIONPANE
            if (resultCode == 0) {
                canvasPanel.repaint();
                JOptionPane.showMessageDialog(frame,
                        "Graf został wygenerowany pomyślnie!",
                        "Sukces",
                        JOptionPane.INFORMATION_MESSAGE);
                logArea.append("[INFO] Obliczenia zakończone sukcesem.\n");
            }
            else if (resultCode == 1 || resultCode == 2) {
                JOptionPane.showMessageDialog(frame,
                        "Błąd dostępu do danych! Upewnij się, że plik wejściowy jest poprawny.",
                        "Błąd Pliku",
                        JOptionPane.ERROR_MESSAGE);
                logArea.append("[ERROR] Zły plik wejściowy lub argumenty.\n");
            }
            else if (resultCode == 3) {
                JOptionPane.showMessageDialog(frame,
                        "Niepoprawna struktura grafu! Silnik nie mógł przetworzyć danych.",
                        "Błąd Grafu",
                        JOptionPane.WARNING_MESSAGE);
                logArea.append("[ERROR] Struktura grafu została odrzucona przez silnik C.\n");
            }
            else if (resultCode == -1) {
                JOptionPane.showMessageDialog(frame,
                        "Krytyczny błąd silnika obliczeniowego! Operacja została przerwana, aby zapobiec zawieszeniu programu.",
                        "Awaria Silnika",
                        JOptionPane.ERROR_MESSAGE);
                logArea.append("[FATAL] Proces C został wymuszony do zamknięcia (Timeout).\n");
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
