%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% DONNEES D'INSTANCE : DIMENSIONS
int: nb_sommets;
int: nb_couleurs;
int: nb_cliques;
int: score_max;

% DONNEES D'INSTANCE : CONTENEURS
array[1..nb_sommets] of set of 0..nb_sommets-1: voisins;
array[1..nb_sommets] of int: poids;
array[1..nb_cliques] of set of 0..nb_sommets-1: cliques;


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% DONNEES DE TRAVAIL

% plage des couleurs
set of int: COLOR = 1..nb_couleurs;
% plage des sommets réels
set of int: R_VERTEX = 1..nb_sommets;
% plage des sommets virtuels : 1 par couleur d'identifiant nb_sommets+i (i=1..nb_couleurs) et de poids nul
set of int: V_VERTEX = nb_sommets+1..nb_sommets+nb_couleurs;
% plage des sommets virtuels ou non avec incrémentation des ids pour les sommets non virtuels
set of int: VERTEX = 1..max(V_VERTEX);
% plage des poids
set of int: WEIGHT = 0..max(poids);
% plage des cliques
set of int: CLIQUE = 1..nb_cliques;

% voisins des sommets virtuels ou non : les sommets virtuels sont déconnectés
array[VERTEX] of set of VERTEX: neighbours = [{j+1 | j in voisins[i]} | i in index_set(voisins)] ++ [{} | i in V_VERTEX];
% poids des sommets virtuels ou non : les sommets virtuels ont un poids nul
% NB. le vecteur des poids en entrée est décroissant donc weight est décroissant
array[VERTEX] of WEIGHT: weight = [poids[i] | i in index_set(poids)] ++ [0 | i in V_VERTEX];
% cliques
array[CLIQUE] of set of VERTEX: clique = [{i+1 | i in cliques[c]} | c in index_set(cliques)];

% plage du score
set of int: SCORE = max(weight)..sum(weight);

% densité du graphe
float: density = sum(i in R_VERTEX)(card(neighbours[i])) / max({1,(card(R_VERTEX)*(card(R_VERTEX)-1))});


