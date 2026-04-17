#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "pob_dane.h"
#include "log.h"
char *algorithms[]={"spectral","triangulacja", NULL};

void help(){
  printf("Uruchomienie programu: ./graf <plik_wejściowy> [flagi]\n");
  printf("Flagi:\n-h - wyświetla menu pomocy opisujące działanie programu\n-v - uruchamia tryb verbose\n");
  printf("-a <nazwa_algorytmu> - zmienia algorytm; algorytmy: triangulacja/spectral, domyslnie triangulacja\n");
  printf("-w <plik_wyjsciowy> - zapisuje wynik do podanego pliku\n");
}

int find_algorithm(char *a){
  char **iter=algorithms;
  while(*iter)
    if(strcmp(a,*iter++)==0)
      return 0;
  return 1;
}

int pobierz_dane(int argc, char** argv, char a[], FILE** out){
for (int j = 2; j < argc; j++) { //wczytywanie z flagami
  if (strcmp(argv[j], "-w") != 0 &&
        strcmp(argv[j], "-a") != 0 &&
        strcmp(argv[j], "-h") != 0 &&
        strcmp(argv[j], "-v") != 0) {
        fprintf(stderr,"Nieznana flaga: %s\n", argv[j]);
        return 1;
    }
    if (strcmp(argv[j], "-v") == 0) { //tryb verbose
        verbose_activate();
    }
    if (strcmp(argv[j], "-h") == 0) { //tryb help
        return 1;
    }
    if (strcmp(argv[j], "-a") == 0) { //zmiana na inny algorytm
        j++;
        if (j < argc){
            if(sscanf(argv[j], "%49s", a) != 1) {
              fprintf(stderr,"Bledny argument dla -a\n");
              return 1;
            }
            if(find_algorithm(a)!=0){
              fprintf(stderr,"Niepoprawna nazwa algorytmu: -a %s\n", a);
              return 1;
            }
        }
            
    }
    if (strcmp(argv[j], "-w") == 0) { //otworzenie pliku z wynikiem
        j++;
        if (j < argc){
            *out = fopen(argv[j], "a");
          if (!*out) {
            fprintf(stderr, "Nie mozna otworzyc pliku wyjsciowego");
            return 1;
          }
        }
    }
  }
  return 0;
}
