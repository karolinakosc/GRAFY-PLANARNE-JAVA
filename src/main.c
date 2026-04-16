#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <stdio.h>
#include <string.h>
#include "pob_dane.h"
#include "graf.h"
#include "spectral.h"

int output(graf *g, FILE *out){
	for(int i=0; i<g->l_pkt; i++)
		if(fprintf(out,"%d:  %lf  %lf\n",g->punkty[i].n, g->punkty[i].x, g->punkty[i].y)!=3)
			return -1;
	return 0;
}

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
  if (strcmp(a,"spectral") && SpectralLayoutAlgorithm(g)!=-1) {
	  if(!output(g,out))
		  fprintf(stderr,"Blad wypisywania grafu.\n");
  }

  //wlaczanie funkcji posrednich
  //do zrobienia
  //zapisywanie wyniku
  //do zrobienia
  
  if(out) fclose(out);
  return 0;
}
