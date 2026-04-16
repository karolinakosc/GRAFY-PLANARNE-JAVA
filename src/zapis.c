#include <stdio.h>
#include <stdlib.h>
#include "zapis.h"

void zapisz(char* file, double x, double y){
    FILE* out = fopen(file, "a");
    fprintf(out, "pozycje: %lf, %lf", x, y);  //zmienilem %d na %lf
    fclose(out); //zmienilem in na out
}
