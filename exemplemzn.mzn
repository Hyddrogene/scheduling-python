 
include "alldifferent.mzn";

int : n = 4;
set of int : A = 1..30;

array[size] of var A : A_tab;
array[size] of var A : B_tab;


set of int : size = 1..n;

constraint forall(i in size)(all_different([A_tab[i],B_tab[i]]));
constraint all_different(A_tab);
constraint all_different(A_tab++B_tab);
constraint all_different(B_tab);