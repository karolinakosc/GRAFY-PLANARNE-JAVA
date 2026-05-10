package pl.edu.pw.graph.algorithms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class ProcessExecutor {

    public static void runCEngine(String executablePath, String inputFilePath, String algorithm) {
        try {
            // Komenda uruchomienia: ./graf plik.txt -a algorithm -v
            ProcessBuilder pb = new ProcessBuilder(executablePath, inputFilePath, "-a", algorithm, "-v");

            pb.redirectErrorStream(true);

            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[Silnik C]: " + line);
            }

            int exitCode = process.waitFor();
            System.out.println("Proces zakończył działanie z kodem błędu: " + exitCode);

        } catch (IOException | InterruptedException e) {
            System.err.println("Błąd podczas komunikacji z procesem w C: " + e.getMessage());
        }
    }
}
