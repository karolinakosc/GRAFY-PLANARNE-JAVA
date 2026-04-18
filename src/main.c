#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <stdio.h>
#include <string.h>
#include "pob_dane.h"
#include "graf.h"
#include "spectral.h"
#include "planar.h"

int main(int argc, char** argv)
{
  if(argc == 1){ //Sprawdzanie ilosci argumentow
      fprintf(stderr,"Nie podano zadnych argumentow.\nProgram wymaga podania przynajmniej sciezki do pliku z grafem.\n");
      help();
      return 1;
  }

  graf *g = load_graf(argv[1]); //wczytanie pliku z grafem
  char a[50] = "triangulacja"; //Bazowo bez podania flagi argumentu bedzie triangulacja
  FILE* out = stdout;		//zmienilem z NULL na stdout		
  if (pobierz_dane(argc, argv, a, &out) != 0) {
        help();
        return 2;
  }
  if(checkPlanar(g)==-1)
    fprintf(stderr,"Graf nie jest planarny.\n");
  if (strcmp(a,"spectral") == 0)
    if(SpectralLayoutAlgorithm(g)!=-1) {
      printf("TEST algorytm zakonczony pomyslnie\n");
    }

  //wlaczanie funkcji posrednich
  //do zrobienia
  //zapisywanie wyniku
  //do zrobienia
  
  free_graf(g);
  if(out) fclose(out);
  return 0;
}
