#include "graf.h" 
#include <stdio.h> 
#include <stdlib.h> 
#include <string.h> 

#define BUFSIZE 8192 

graf* create_graf (){ 

    graf* g = (graf*)malloc(sizeof(graf)); 

    if(!g){ 
        fprintf(stderr, "Failed to allocate memory for the graph.\n"); 
        return NULL; 
    }

    g->punkty = NULL; 
    g->linki = NULL; 
    g->l_pkt = 0; 
    g->l_l = 0; 
    g->l_pkt_capacity = 1;
    g->l_l_capacity = 1;
    g->punkty = malloc(g->l_pkt_capacity * sizeof(pkt));
    g->linki  = malloc(g->l_l_capacity * sizeof(link));

    if (!g->punkty || !g->linki) {
        fprintf(stderr, "Failed to allocate memory for the graph arrays.\n");
        free(g->punkty);
        free(g->linki);
        free(g);
        return NULL;
    }
    
    return g; 
} 

void free_graf(graf* g) { 
    if (!g) return; 
    free(g->punkty); 
    free(g->linki); 
    free(g); 
} 

graf* load_graf(char* argv){ 
    FILE* in = fopen(argv, "r"); 
    if(in == NULL){ 
        fprintf(stderr, "Failed to open input file: %s\n", argv); 
        return NULL; 

    }
    graf* g = create_graf();
    if (!g) { 
        fprintf(stderr, "Failed to create graph.\n"); 
        fclose(in);
        return NULL; 
    }

    char buf[BUFSIZE]; 
    
    while(fgets(buf, BUFSIZE, in)){ 
        
        int a, b; 
        char name[50]; 
        double waga; 

        if(sscanf(buf, "%49s %d %d %lf", name, &a, &b, &waga) != 4){ 
            fprintf(stderr, "Error while reading input file line.\n"); 
            free_graf(g);
            fclose(in);
            return NULL; 
        } 

        if(find_pkt(g, a) == -1)
            if(add_pkt(g, a) == -1) {
                fprintf(stderr, "Failed to add point.\n");
                free_graf(g);
                fclose(in);
                return NULL;
            }
        if(find_pkt(g, b) == -1)
            if(add_pkt(g, b) == -1) {
                fprintf(stderr, "Failed to add point.\n");
                free_graf(g);
                fclose(in);
                return NULL;
            }
        if(add_link(g, name, a, b, waga) == -1) {
            fprintf(stderr, "Failed to add link.\n");
            free_graf(g);
            fclose(in);
            return NULL;
        }
    } 
    fclose(in);
    return g; 
} 

int find_pkt(graf* g, int n) { 
    if(!g) return -1;
    for (int i = 0; i < g->l_pkt; i++)
        if (g->punkty[i].n == n)
            return i; 
    return -1;
} 

int add_link(graf* g, const char* name, int a, int b, double waga) {
    if(!g) return -1;
    if(g->l_l >= g->l_l_capacity) {
      link *l = realloc(g->linki, 2 * g->l_l_capacity * sizeof(link));
      if (!l) {
        fprintf(stderr, "Failed to allocate link.\n");
        return -1;
      }
      g->linki = l;
      g->l_l_capacity *= 2;
    }

    g->linki[g->l_l] = (link){ .a = a, .b = b, .waga = waga };
    strncpy(g->linki[g->l_l].name, name, 49);
    g->linki[g->l_l].name[49] = '\0';

    g->l_l++;
    return 0;
}

int add_pkt(graf* g, int n) {
    if(!g) return -1;
    if(g->l_pkt >= g->l_pkt_capacity){
      pkt* p = realloc(g->punkty, 2 * g->l_pkt_capacity * sizeof(pkt));
      if (!p) {
        fprintf(stderr, "Failed to allocate point.\n");
        return -1;
      }
      g->punkty = p;
      g->l_pkt_capacity *= 2;
    }
    g->punkty[g->l_pkt] = (pkt){0, 0, 0, 0, n}; 

    g->l_pkt++; 
    return 0;
}
