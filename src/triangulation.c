#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "triangulation.h"
#include "graf.h"

double odl (double x1, double y1, double x2, double y2) { //odleglosc punktow od siebie
  double dl = sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
  return dl;
}
void check(graf* g, int a, int b, int c, int ac, int bc){ //obliczenie punktow przeciecia sie dwoch okregow o srodkach sasiadow punktu c oraz promieniu odleglosci pomiedzy nimi a punktem c

  double d = odl(g->punkty[b].x, g->punkty[b].y, g->punkty[a].x, g->punkty[a].y);

  if(d == 0) return;

  double ra = g->linki[ac].waga;
  double rb = g->linki[bc].waga;

  double k = (ra*ra - rb*rb + d*d) / (2*d);

  double h = sqrt(ra*ra - k*k);

  double x = g->punkty[a].x + k*(g->punkty[b].x - g->punkty[a].x)/d;
  double y = g->punkty[a].y + k*(g->punkty[b].y - g->punkty[a].y)/d;

  double x1f = x + h*(g->punkty[b].y - g->punkty[a].y)/d;
  double y1f = y - h*(g->punkty[b].x - g->punkty[a].x)/d;

  double x2f = x - h*(g->punkty[b].y - g->punkty[a].y)/d;
  double y2f = y + h*(g->punkty[b].x - g->punkty[a].x)/d;

  g->punkty[c].x = x1f;
  g->punkty[c].y = y1f;
  g->punkty[c].op_x = x2f;
  g->punkty[c].op_y = y2f;
  verbose("Initial coordinate options for point %d: x = %lf, y = %lf, op_x = %lf, op_y = %lf\n", c, x1f, y1f, x2f, y2f);
}

void triangulation(graf* g){
  g->punkty[g->linki[0].b].x = g->linki[0].waga;
  g->punkty[g->linki[0].a].known = 1;
  g->punkty[g->linki[0].b].known = 1;

  int changed = 1;

  while(changed){
    changed = 0;

    for(int r = 0; r < g->l_pkt; r++){

      if(g->punkty[r].known)
        continue;

      int s = -1, t = -1;
      int sr = -1, tr = -1;

      for(int i = 0; i < g->l_l; i++){

        int a = g->linki[i].a;
        int b = g->linki[i].b;

        if(s == -1){
          if(find_link(g, r, a) != -1 && g->punkty[a].known){
            s = a;
            sr = find_link(g, r, s);
            continue;
          }
          if(find_link(g, r, b) != -1 && g->punkty[b].known){
            s = b;
            sr = find_link(g, r, s);
            continue;
          }
        }
        if(t == -1 && s != -1){
          if(find_link(g, r, a) != -1 && g->punkty[a].known){
            if(a == s) continue;
            t = a;
            tr = find_link(g, r, t);
            break;
          }
          if(find_link(g, r, b) != -1 && g->punkty[b].known){
            if(b == s) continue;
            t = b;
            tr = find_link(g, r, t);
            break;
          }
        }
      }

      if(s != -1 && t != -1 && sr != -1 && tr != -1){
        check(g, s, t, r, sr, tr);
        verbose("Checking coordinates for point %d, connected with point %d and %d, by links %s and %s", r, s, t, g->linki[sr].name, g->linki[tr].name);
        g->punkty[r].known = 1;
        changed = 1;
        break;
      }
    }
  }

  //wybór punktów!!!
  int m = 3;
  while(m < g->l_pkt && m >= 3){
    if(find_pkt_pos(g, g->punkty[m].x, g->punkty[m].y, m) == -1){
      m++;
      continue;
    }
    else switch_pkt(g, m);

    if(find_pkt_pos(g, g->punkty[m].x, g->punkty[m].y, m) != -1){
      switch_pkt(g, find_pkt_pos(g, g->punkty[m].x, g->punkty[m].y, m));
      if(find_pkt_pos(g, g->punkty[m].x, g->punkty[m].y, m) < m)
        m = find_pkt_pos(g, g->punkty[m].x, g->punkty[m].y, m);
      else m++;
    }
  }
  if(m <= 3) fprintf(stderr, "Graph is not planar!\n");
  
}

void switch_pkt(graf* g, int m){
  int tmpx = g->punkty[m].x;
  int tmpy = g->punkty[m].y;
  g->punkty[m].x = g->punkty[m].op_x;
  g->punkty[m].y = g->punkty[m].op_y;
  g->punkty[m].op_x = tmpx;
  g->punkty[m].op_y = tmpy;
}

int find_pkt_pos(graf* g, double x, double y, int n){
  for(int i = 0; i < g->l_pkt; i++){
    if(g->punkty[i].x == x && g->punkty[i].y == y && g->punkty[n].n != n)
      return g->punkty[i].n;
  }
  return -1;
}

int find_link(graf* g, int a, int b){
  for(int i = 0; i < g->l_l; i++){
    if((g->linki[i].a == a && g->linki[i].b == b) || (g->linki[i].a == b && g->linki[i].b == a))
      return i;
  }
  return -1;
}

