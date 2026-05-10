package pl.edu.pw.graph.algorithms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ProcessExecutor {

    public static void runCEngine(String executablePath, String inputFilePath, String algorithm)
            throws IOException, InterruptedException, TimeoutException {

        ProcessBuilder pb = new ProcessBuilder("wsl", executablePath, inputFilePath, "-a", algorithm, "-v", "-w", "./c_app/wynik.txt");
        pb.redirectErrorStream(true);

        Process process = pb.start();

        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[Silnik C]: " + line);
                }
            } catch (IOException ignored) {
            }
        }).start();

        //oczekujemy maksymalnie 10 sekund na wynik
        boolean finished = process.waitFor(10, TimeUnit.SECONDS);

        if (!finished) {
            process.destroy();
            throw new TimeoutException("Przekroczono limit czasu. Silnik obliczeniowy zawiesił się w nieskończonej pętli i został zatrzymany.");
        }

        int exitCode = process.exitValue();
        if (exitCode != 0) {
            throw new RuntimeException("Wystąpił błąd silnika obliczeniowego (kod powrotu: " + exitCode + "). Sprawdź logi lub poprawność danych wejściowych.");
        }
    }
}