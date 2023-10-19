 
include "alldifferent.mzn";

int : n = 4;
set of int : A = 1..30;

array[size] of var A : X_tab;

set of int : size = 1..2*n;

constraint forall(i in 1..n)(all_different([X_tab[i],X_tab[(2*n)+1-i]]));
constraint forall(i,j in 1..n where i<j)(X_tab[i]> X_tab[j] /\ X_tab[(2*n+1) -i]<X_tab[(2*n+1) -j]);
constraint all_different(X_tab);