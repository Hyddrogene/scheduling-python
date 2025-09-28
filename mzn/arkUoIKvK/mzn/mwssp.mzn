include "data.mzn";


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% DONNEES DE TRAVAIL

%% C is complementary graph of G
%% H is directed complementary graph of G

% nodes of H (excluding virtual vertices)
set of int: NODE = R_VERTEX;

% number of edges of C
int: nr_edges = (nb_sommets*(nb_sommets-1) - sum(i in R_VERTEX)(card(neighbours[i]))) div 2;

% number of arcs of H
int: nr_arcs = nr_edges;

% arcs of H
set of int: ARC = 1..nr_arcs;

% successors and predecessors in H
array[NODE] of set of NODE: successors = [{j | j in NODE where j>i} diff neighbours[i] | i in NODE];

% arc tails and heads
array[ARC] of NODE: tail = [i | i in NODE, j in successors[i]];
array[ARC] of NODE: head = [j | i in NODE, j in successors[i]];

% incoming/outgoing arcs per node
array[NODE] of set of ARC: outs = [{e | e in ARC where i=tail[e]} | i in NODE];
array[NODE] of set of ARC: ins = [{e | e in ARC where i=head[e]} | i in NODE];


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% VARIABLES

% 
array[ARC] of var 0..1: y_arc;

%
var int: y_score;


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% CONSTRAINTS

% % (7)
% constraint
% forall(v in NODE, vw in outs[v])( 
%   y_arc[vw] + sum(e in ins[v])(y_arc[e]) <= 1 
% );

% % (8)
% constraint
% forall(v in NODE, vu,vw in outs[v] where head[vu]<head[vw] /\ not(head[vw] in successors[head[vu]]))( 
%   y_arc[vu] + y_arc[vw] + sum(e in ins[v])(y_arc[e]) <= 1 
% );

% (11)
constraint
forall(vu,vw in ARC where vu<vw /\ tail[vu]=tail[vw] /\ not(head[vw] in successors[head[vu]]))( 
  y_arc[vu] + y_arc[vw] <= 1 
);

% (12)
constraint
forall(v in NODE, uv in ins[v], vw in outs[v])( 
  y_arc[uv] + y_arc[vw] <= 1 
);

% (13)
constraint
forall(v in NODE, tv,uv in ins[v] where tv<uv)( 
  y_arc[tv] + y_arc[uv] <= 1 
);

% Cost of MWSSP
constraint
y_score = sum(e in ARC)(weight[head[e]]*y_arc[e]);
