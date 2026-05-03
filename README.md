# Wizualizacja Grafów Planarnych

Celem projektu jest stworzenie dwóch współpracujących ze sobą aplikacji – pierwszej w języku C (silnik obliczeniowy), a drugiej w języku Java (nakładka graficzna / GUI) – służących do wyznaczania współrzędnych węzłów w celu estetycznej wizualizacji grafu planarnego. 

System działa w architekturze wielowarstwowej, gdzie Java odpowiada za interfejs użytkownika i prezentację danych, a zoptymalizowany silnik w C jest wywoływany jako proces w tle i wykonuje operacje matematyczne.

---

## 1. Silnik Obliczeniowy (Backend - C)
Program konsolowy odpowiedzialny za przetwarzanie struktury grafu i wyznaczanie współrzędnych kartezjańskich wierzchołków.

### Zaimplementowane Algorytmy
W ramach modułu C zaimplementowano dwa algorytmy rozmieszczania:
1. *Algorytm triangulacji* (Domyślny) – wyznacza pozycje kolejnych wierzchołków na podstawie znanych odległości (wag krawędzi) i punktów przecięcia okręgów.
2. *Algorytm spektralnego rozmieszczenia* – wykorzystuje wartości i wektory własne macierzy Laplace'a grafu do minimalizacji sumy odległości między połączonymi węzłami.

### Dokumentacja Funkcjonalna (C)
#### Instrukcja obsługi (CLI)
Program uruchamiany jest z poziomu terminala według schematu:
./graf <plik_wejściowy> [flagi]

*Dostępne flagi:*
* -h : Wyświetla menu pomocy z opisem flag.
* -v : Uruchamia tryb verbose (szczegółowe logi operacji).
* -a <nazwa> : Wybór algorytmu: triangulacja (domyślny) lub spectral.
* -w <plik> : Zapisuje wynik do wskazanego pliku (zamiast na standardowe wyjście).

### Dokumentacja Implementacyjna (C)
#### Format wejściowy
Program przyjmuje pliki tekstowe z listą krawędzi. Każda linia musi zawierać:
<nazwa_krawędzi> <wierzchołek_1> <wierzchołek_2> <waga_krawędzi>

#### Format wyjściowy
Współrzędne wierzchołków wypisywane są w formacie:
<nr_wierzchołka> <współrzędna_x> <współrzędna_y>

#### Architektura rozwiązania
Kod został podzielony na moduły:
* main.c : Punkt wejścia i sterowanie programem.
* graf.c/h : Struktury danych reprezentujące graf.
* pob_dane.c/h : Obsługa wejścia i parametrów CLI.
* triangulation.c/h / spectral.c/h : Implementacje algorytmów.
* matrix_operations.c/h : Operacje na macierzach i wektorach.
* planar.c/h : Weryfikacja planarności grafu.
* log.c/h : System logowania i obsługa trybu verbose.
* utils.c/h : Funkcje pomocnicze.

#### Budowanie projektu
* `make` - Kompilacja projektu (narzędzie GCC)
* `make clean` - Usunięcie plików binarnych

## 2. Interfejs Graficzny (Frontend - Java)

Aplikacja okienkowa zbudowana w bibliotece *Java Swing*, służąca jako interaktywna nakładka (wrapper) na silnik obliczeniowy w C. Ukrywa ona skomplikowane operacje terminalowe przed końcowym użytkownikiem.

### Dokumentacja Funkcjonalna (Java)

#### Instrukcja obsługi (GUI)
Aplikacja oferuje intuicyjny graficzny interfejs użytkownika (GUI):
* *Wczytywanie i Zapis:* Przycisk wyboru pliku otwiera okno systemowe do wczytania grafu wejściowego oraz eksportu wyników (JFileChooser).
* *Konfiguracja:* Lista rozwijana (ComboBox) pozwala na wybór algorytmu wyliczającego (Triangulacja / Spektralny).
* *Generowanie:* Przycisk uruchamiający silnik C w tle i inicjujący parsowanie wyników.
* *Wizualizacja (Canvas):* Graf wyświetlany jest na niestandardowym płótnie. Aplikacja automatycznie skaluje rysunek, obsługując ujemne wartości współrzędnych zwracane przez algorytmy.
* *Logi:* Dolny panel tekstowy wyświetla bieżące komunikaty i status postępu pochodzące bezpośrednio z silnika backendowego.

### Dokumentacja Implementacyjna (Java)

#### Integracja z silnikiem C
Java komunikuje się z backendem przy użyciu klasy ProcessBuilder. Odpowiada ona za asynchroniczne odpalanie silnika C z odpowiednimi flagami CLI (np. -a spectral -v) oraz przechwytywanie standardowego wyjścia (stdout) i strumienia błędów (stderr).

#### Architektura (MVC)
Kod zorganizowany jest zgodnie ze wzorcem architektonicznym *MVC (Model-View-Controller)* w pakiecie pl.edu.pw.grafy (zgodnie ze standardem "Reverse Domain Name" Politechniki Warszawskiej):
* model/ : Reprezentacja matematyczna topologii (klasy Graph, Vertex, Edge).
* gui/ : Klasy okna głównego (MainFrame) i komponentu rysującego (GraphPanel).
* io/ : Obsługa odczytu danych plików i parsowania ostatecznych wyników liczbowych.
* algorithms/ : Warstwa sterująca procesami zewnętrznymi i wymianą danych między Javą a programem w języku C.

#### Obsługa błędów
Aplikacja w sposób bezpieczny obsługuje wyjątkowe sytuacje. Przechwytuje kody powrotu silnika C i w razie wystąpienia błędu (np. błąd logiczny struktury grafu) wyświetla czytelne komunikaty ostrzegawcze zamiast zawieszać cały interfejs graficzny.

---

## Autorzy
* *Silnik w C:* Maja Biały, Aleksander Powierza
* *Interfejs GUI (Java):* Liwia Wojtachnik, Karolina Kość
