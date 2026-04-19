#include "matrix_operations.h"
#include <stdlib.h>
#include <math.h>
#include "utlis.h"

int find_index(graf *g, int vertex){
  for(int i=0;i<g->l_pkt;i++)
    if(g->punkty[i].n == vertex)
      return i;
  return -1;
}

int matrix_cpy(Matrix *dest, Matrix *source){
    if(!dest || !source)
        return -1;
    for(int i=0;i<dest->size * dest->size; i++)
        dest->data[i] = source->data[i];
    return 0;
}

Matrix *allocate_matrix(int size){
    Matrix *M = malloc(sizeof(Matrix));
    if(!M)
        return NULL;
    M->size = size;
    M->data = calloc(size * size,sizeof(double));
    if(!M->data){
        free(M);
        return NULL;
    }
    return M;
}

Vector *allocate_vector(int size){
    Vector *V = malloc(sizeof(Vector));
    if(!V)
        return NULL;
    V->size = size;
    V->data = calloc(size,sizeof(double));
    if(!V->data){
        free(V);
        return NULL;
    }
    return V;
}

Matrix *create_adjacency_matrix(graf *g){
    int size = g->l_pkt;
    Matrix *adj = allocate_matrix(size);
    if(!adj){
      fprintf(stderr,"Error while allocating adjacency matrix\n");
      return NULL;
    }
    for(int i=0; i<g->l_l; i++){
        int a=find_index(g, g->linki[i].a);
        int b=find_index(g, g->linki[i].b);
        if(a==-1 || b==-1){
          fprintf(stderr,"Vertex not found.\n");
          return NULL;
        }
        double weight = g->linki[i].waga;
        MAT(adj,a,b) = weight;
        MAT(adj,b,a) = weight;
    }
    return adj;
}

void free_matrix(Matrix *M){
    free(M->data);
    free(M);
}

void free_vec(Vector *V){
    free(V->data);
    free(V);
}

Vector* create_degree_vector(Matrix *M){
    Vector *degree_vector = allocate_vector(M->size);
    for(int i=0;i<M->size;i++){
        for(int j=0;j<M->size;j++){
          VEC(degree_vector,i) +=MAT(M,i,j);
        }
    }
    return degree_vector;
}

void adjacency_to_laplacian_matrix(Matrix* adj_matrix, Vector* deg_matrix){    

    for(int i=0;i<adj_matrix->size;i++){
        for(int j=0;j<adj_matrix->size;j++){
            if(i==j)
                MAT(adj_matrix,i,j) += VEC(deg_matrix,i);
            else 
                MAT(adj_matrix,i,j) = -MAT(adj_matrix,i,j);
        }
    }
}
//LU matrix -> A = L * U
//in order to save memory, L and U matrix are stored in A matrix
void LU_decompose(Matrix *A, int *P){    //implementation of Gaussian elimination in order to get LU matrix
    double *tmp_row = malloc(sizeof(double) * A->size);
    if(!tmp_row)
	    return;
    //searching for max_row in order to minimalize rounding errors
    for(int k=0; k<A->size; k++){
        int max_row = k;
        for(int i = k+1; i<A->size; i++)
            if(fabs(MAT(A,i,k)) > fabs(MAT(A,max_row,k)))
                max_row = i;
        if(max_row != k){
            int tmp = P[k];
            P[k] = P[max_row];
            P[max_row] = tmp;

            for(int j=0; j<A->size; j++){
                tmp_row[j] = MAT(A, k,j);
                MAT(A,k,j) = MAT(A, max_row,j);
                MAT(A,max_row,j) = tmp_row[j];
            }

        }
        if(is_zero(MAT(A,k,k))){               //check if A[k][k] ~ 0
            continue;
        }
        for(int i=k+1; i<A->size; i++){            //Gaussian elimination - building LU matrix
            double factor = MAT(A,i,k) / MAT(A,k,k);  //A[i][k] elimination factor
            MAT(A,i,k) = factor;                   //to have L and U in one matrix store factor in eliminated position                  
            for(int j = k+1; j < A->size; j++)
                MAT(A,i,j) -= factor * MAT(A,k,j);
        }
    }
    free(tmp_row);
}


int LU_solve(Matrix *A, int *P, Vector *current, Vector *res, Vector *help_vec){  //A * w = v
    //U - upper
    //L - lower
    //A * res = current
    //L * U * res = current
    //helper: help_vec = U * res
    //L * help_vec = current
    //U * res = help_vec
    for(int i=0; i<A->size; i++){
        VEC(help_vec, i) = VEC(current, P[i]); //VEC(current, P[i]) to get valid index after changing lines in LU_decompose
        for(int j=0; j<i; j++){                 //L * help_vec = v
            VEC(help_vec, i) -= MAT(A, i, j) * VEC(help_vec, j);
        }
    //no dividing as A[i][i] contains diagonal of U matrix
    }
    for(int i=A->size-1; i>=0; i--){               //U * w = help_vec
        VEC(res, i) = VEC(help_vec, i);
        for(int j=i+1; j<A->size; j++){
            VEC(res, i) -= MAT(A, i, j) * VEC(res, j);
        }
        if(is_zero(MAT(A, i, i)))
            return -1;
        VEC(res, i) /= MAT(A, i, i);
    }
    return 0;
}

double scalar_product(Vector *a, Vector *b){
    double scalar=0;
    for(int i=0;i<a->size;i++)
        scalar += VEC(a,i)*VEC(b,i);
    return scalar;
}

double squared_length(Vector *v){
    double s_len=0;
    for(int i=0;i<v->size;i++)
        s_len += pow(VEC(v,i),2);
    return s_len;
}

void scaling(Vector *v, double scalar){
    for(int i=0;i<v->size;i++)
        VEC(v,i) *= scalar;
}

int vector_orthagonalization(Vector *result, Vector *component){
    double scalar = scalar_product(result,component);
    double squared_len = squared_length(component);
    if(fabs(squared_len) < EPS)
        return -1;
    double coeff = scalar / squared_len;
    for(int i=0;i<result->size; i++)
        VEC(result,i) -= coeff * VEC(component,i);
    return 0;
}

double vector_norm(Vector *v){
    return sqrt(squared_length(v));
}

int vector_normalize(Vector *v){
    double norm = vector_norm(v);
    if(is_zero(norm))
        return -1;
    for(int i=0;i<v->size;i++)
        VEC(v,i) /= norm;
    return 0;
}

