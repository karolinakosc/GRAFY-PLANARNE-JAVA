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

int cmp_pkt(const void *a, const void *b){
    const pkt *pa = (const pkt *)a;
    const pkt *pb = (const pkt *)b;
    if (pa->n < pb->n) return -1;
    if (pa->n > pb->n) return 1;
    return 0;
}

int write_coordinates(FILE *out, graf *g){
  pkt *tmp_punkty = malloc(sizeof(pkt) * g->l_pkt);
  if(!tmp_punkty)
    return -1;
  for(int i=0;i<g->l_pkt;i++){
    tmp_punkty[i].x = g->punkty[i].x;
    tmp_punkty[i].op_x = g->punkty[i].op_x;
    tmp_punkty[i].y = g->punkty[i].y;
    tmp_punkty[i].op_y = g->punkty[i].op_y;
    tmp_punkty[i].n = g->punkty[i].n;
  }
  qsort(tmp_punkty, g->l_pkt, sizeof(pkt), cmp_pkt);
  for(int i=0;i<g->l_pkt;i++)
    fprintf(out,"%d %lf %lf\n",tmp_punkty[i].n, tmp_punkty[i].x, tmp_punkty[i].y);

  free(tmp_punkty);
  return 0;
}

int main(int argc, char** argv)
{
  if(argc == 1){ //Sprawdzanie ilosci argumentow
      fprintf(stderr,"No arguments were provided.\nThe program requires at least a path to a graph file.\n");
      help();
      return 1;
  }

  graf *g = load_graf(argv[1]); //wczytanie pliku z grafem
  if(!g){
    help();
    return -1;
  }
  char a[50] = "triangulacja"; //Bazowo bez podania flagi argumentu bedzie triangulacja
  FILE* out = stdout;
  if (pobierz_dane(argc, argv, a, &out) != 0) {
        help();
        return 2;
  }
  if(checkPlanar(g)==-1)
    fprintf(stderr,"The graph is not planar.\n");
  if (strcmp(a,"spectral") == 0){
    if(SpectralLayoutAlgorithm(g)==-1) {
      fprintf(stderr,"Error in spectral layout algorithm.\n");
      return -1;
    }
    else{
      if(write_coordinates(out,g)==-1)
        fprintf(stderr,"Error writing data.\n");
    }
  }

  //wlaczanie funkcji posrednich
  //do zrobienia
  
  free_graf(g);
  if(out) fclose(out);
  return 0;
}
