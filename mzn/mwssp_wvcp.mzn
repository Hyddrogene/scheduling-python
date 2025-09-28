include "wvcp.mzn";
include "mwssp.mzn";


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% VARIABLES

% 
array[R_VERTEX] of var 0..1: y_dominant;


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% CONSTRAINTS

% 
constraint
forall(i in R_VERTEX)( 
  y_dominant[i] = bool2int(i == x_dominant[x_color[i]])
);

% 
constraint
forall(i in R_VERTEX where outs[i]!={})( 
  card(x_vertices[x_color[i]]) > 2 -> y_dominant[i] = max(ij in outs[i])(y_arc[ij])
); 

% 
constraint
x_score + y_score = sum(weight);