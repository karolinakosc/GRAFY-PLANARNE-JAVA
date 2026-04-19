#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "pob_dane.h"
#include "log.h"
char *algorithms[]={"spectral","triangulation", NULL};

void help(){
  printf("Program usage: ./graph <input_file> [flags]\n");
  printf("Flags:\n");
  printf("-h - displays help menu describing program functionality\n");
  printf("-v - enables verbose mode\n");
  printf("-a <algorithm_name> - selects algorithm; available: triangulation/spectral, default: triangulation\n");
  printf("-w <output_file> - saves output to the specified file\n");
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
        fprintf(stderr,"Unknown flag: %s\n", argv[j]);
        return 1;
    }
    if (strcmp(argv[j], "-v") == 0) { //tryb verbose
        verbose_activate();
    }
    if (strcmp(argv[j], "-h") == 0) { //tryb help
        help();
    }
    if (strcmp(argv[j], "-a") == 0) { //zmiana na inny algorytm
        j++;
        if (j < argc){
            if(sscanf(argv[j], "%49s", a) != 1) {
              fprintf(stderr,"Wrong argument for -a\n");
              return 1;
            }
            if(find_algorithm(a)!=0){
              fprintf(stderr,"Unknown algorithm name: -a %s\n", a);
              return 1;
            }
        }
            
    }
    if (strcmp(argv[j], "-w") == 0) { //otworzenie pliku z wynikiem
        j++;
        if (j < argc){
            *out = fopen(argv[j], "a");
          if (!*out) {
            fprintf(stderr, "Cannot open output file.\n");
            return 1;
          }
        }
    }
  }
  return 0;
}
