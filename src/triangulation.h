#ifndef _TRIANGULACJA_H_
#define _TRIANGULACJA_H_
#include"graf.h"

double odl (double x1, double y1, double x2, double y2);
void check(graf* g, int a, int b, int c, int ac, int bc);
int find_pkt_pos(graf* g, double x, double y, int n);
int find_link(graf* g, int a, int b);
void triangulation(graf* g, char* out);
void switch_pkt(graf* g, int m);
#endif
