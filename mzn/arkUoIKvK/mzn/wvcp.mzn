include "globals.mzn";
include "data.mzn";


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% CONSTRAINT MODEL FLAGS

bool: WVCP_SCORE; % to use best known score in constraints
bool: WVCP_CLIQUES; % to use cliques for better propagation


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% VARIABLES

% la coloration des sommets
array[VERTEX] of var COLOR: x_color;

% les sommets par couleur
array[COLOR] of var set of VERTEX: x_vertices;

% par couleur, le sommet d'id minimum parmi ceux de poids maximum dans la couleur
array[COLOR] of var VERTEX: x_dominant;

% ouverture de couleur
array[COLOR] of var bool: x_opened = array1d(COLOR,[card(x_vertices[c])!=1 | c in COLOR]);
var COLOR: x_nr_opened = sum(c in COLOR)(bool2int(x_opened[c]));

% le score
var SCORE: x_score;


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% CONTRAINTES

% couplage
constraint
int_set_channel(x_color,x_vertices);

% coloration des sommets virtuels (compatible avec règle d'élimination de symétries)
constraint
forall(v in V_VERTEX)( 
  x_color[v] = v - min(V_VERTEX) + 1
);

% sommets adjacents de couleurs différentes
constraint
forall(i in VERTEX, j in neighbours[i] where i<j)(
  (x_color[i] != x_color[j])
);

% sommets de clique de couleurs différentes
constraint
forall(k in CLIQUE where WVCP_CLIQUES)( 
  alldifferent([x_color[i] | i in clique[k]]) 
);

% dominants
constraint
forall(c in COLOR)(
  x_dominant[c] = min(x_vertices[c]) % correct car sommets numérotés par poids décroissant
  %arg_min(i in VERTEX)(weight[i]*bool2int(x_color[i]=c)) % formulation sans hypothèse de tri sur les poids
);

% élimination de symétries
constraint
strictly_decreasing([max(VERTEX)*(1+weight[x_dominant[c]]) - x_dominant[c] | c in COLOR]);

% élimination de symétries : monotonicité de l'ouverture de couleurs
constraint
forall(c in COLOR where c<max(COLOR))(
  card(x_vertices[c])=1 -> card(x_vertices[c+1])=1 % valeur 1 du fait de la présence d'1 sommet virtuel par couleur
);

% score
constraint
x_score = sum([weight[x_dominant[c]] | c in COLOR]);

% meilleur score connu
constraint
if (WVCP_SCORE==true)
then (x_score <= score_max)
endif;

% borne supérieure induite par les sommets de poids max
constraint
x_score <= sum([weight[i] | i in min(VERTEX)..max(COLOR)]);

% borne inférieure induite par les cliques
constraint
forall(n in 1..1 where WVCP_CLIQUES /\ nb_cliques>0)(
  x_score >= max(k in CLIQUE)(sum([weight[i] | i in clique[k]]))
);
