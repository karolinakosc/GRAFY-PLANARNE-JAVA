# Wizualizacja Grafów Planarnych

Celem projektu jest stworzenie dwóch współpracujących ze sobą aplikacji – pierwszej w języku C (silnik obliczeniowy), a drugiej w języku Java (nakładka graficzna / GUI) – służących do wyznaczania współrzędnych węzłów w celu estetycznej wizualizacji grafu planarnego. 

System działa w architekturze wielowarstwowej, gdzie Java odpowiada za interfejs użytkownika i prezentację danych, a zoptymalizowany silnik w C jest wywoływany jako proces w tle i wykonuje operacje matematyczne.

---

## 1. Silnik Obliczeniowy - C
Program konsolowy odpowiedzialny za przetwarzanie struktury grafu i wyznaczanie współrzędnych kartezjańskich wierzchołków.

### Zaimplementowane Algorytmy
W ramach modułu C zaimplementowano dwa algorytmy rozmieszczania:
1. *Algorytm triangulacji* (Domyślny) – wyznacza pozycje kolejnych wierzchołków na podstawie znanych odległości (wag krawędzi) i punktów przecięcia okręgów.
2. *Algorytm spektralnego rozmieszczenia* – wykorzystuje wartości i wektory własne macierzy Laplace'a grafu do minimalizacji sumy odległości między połączonymi węzłami.

### Dokumentacja Funkcjonalna (C)
#### Instrukcja obsługi (CLI)
Program uruchamiany jest z poziomu terminala według schematu:
`./graf <plik_wejściowy> [flagi]`

*Dostępne flagi:*
* `-h` : Wyświetla menu pomocy z opisem flag.
* `-v` : Uruchamia tryb verbose (szczegółowe logi operacji).
* `-a <nazwa>` : Wybór algorytmu: triangulacja (domyślny) lub spectral.
* `-w <plik>` : Zapisuje wynik do wskazanego pliku (zamiast na standardowe wyjście).

### Dokumentacja Implementacyjna (C)
#### Format danych
* **Wejście:** Plik tekstowy, każda linia: `<nazwa_krawędzi> <wierzchołek_1> <wierzchołek_2> <waga_krawędzi>`
* **Wyjście:** Współrzędne w formacie: `<nr_wierzchołka> <współrzędna_x> <współrzędna_y>`

#### Budowanie projektu
* `cd c_app` - Przejście do folderu z silnikiem C
* `make` - Kompilacja projektu (narzędzie GCC)
* `make clean` - Usunięcie plików binarnych

---

## 2. Interfejs Graficzny - JAVA
Aplikacja okienkowa zbudowana w bibliotece *Java Swing*, pełniąca funkcję nowoczesnej nakładki graficznej na silnik obliczeniowy.

### Dokumentacja Funkcjonalna (Java)
#### Instrukcja obsługi (GUI)
Aplikacja składa się z trzech głównych stref:
1. **Panel Sterowania:** Umożliwia wczytanie pliku grafu, wybór algorytmu oraz uruchomienie procesu obliczeniowego.
2. **Obszar Roboczy (Canvas):** Interaktywne płótno, które automatycznie skaluje i renderuje wierzchołki oraz krawędzie po otrzymaniu współrzędnych z silnika C.
3. **Konsola Logów:** Zablokowane pole tekstowe wyświetlające status obliczeń, czas wykonania algorytmów oraz komunikaty o błędach.

#### Zarządzanie stanem i obsługa błędów
* **Walidacja danych:** System weryfikuje poprawność pliku przed uruchomieniem procesu, zapobiegając błędom.
* **Obsługa wyjątków:** Zamiast przerywania programu w konsoli, błędy (np. niepoprawny format pliku) wyświetlane są w graficznych oknach dialogowych (`JOptionPane`).
* **Responsywność:** Aplikacja zachowuje responsywność podczas pracy silnika w tle (blokowanie przycisków sterujących).
* **Interakcja:** Użytkownik posiada możliwość skalowania widoku (zoom) oraz przesuwania (panning) grafu na płótnie roboczym.

---

## Autorzy
* **Interfejs GUI (Java):** Liwia Wojtachnik, Karolina Kość
