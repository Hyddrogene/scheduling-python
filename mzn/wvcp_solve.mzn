include "wvcp_heuristics.mzn";


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% RESOLUTION

solve :: wvcp_search() :: wvcp_restart()
  minimize(x_score);
%  satisfy;


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% AFFICHAGE

output
%     ["COULEURS\t" ++ show(COLOR)]
% ++  ["\n"]
% ++  ["POIDS\t"] ++ [show(v) ++ "-" ++ show(weight[v]) ++ " " | v in VERTEX]
% ++  ["\n"]
% ++  ["VOISINS\t"] ++ [show(v) ++ "-" ++ show(neighbours[v]) ++ " " | v in VERTEX]
% ++  ["\n"]
% ++  ["CLIQUES\t" ++ show(clique)]
% ++  ["\n**************************"]
% ++  ["\n"]
% ++  ["#Sommets\t" ++ show(nb_sommets)]
% ++  ["\n"]
% ++  ["Densité\t" ++ show(round(100*density)) ++ "%"]
% ++  ["\n"]
% ++  ["Max poids\t" ++ show(max(poids))]
% ++  ["\n"]
% ++  ["Mean poids\t" ++ show(round(sum(poids)/nb_sommets))]
% ++  ["\n"]
% ++  ["WVCP*\t" ++ show(score_max)]
% ++  ["\n"]
% ++  ["MWSSP*\t" ++ show(sum(poids)-score_max)]
% ++  ["\n**************************"]
% ++  ["\n"]
["FLAGS\t"] 
++  ["WVCP_SCORE=" ++ show(WVCP_SCORE) ++ " + " ++ "WVCP_CLIQUES=" ++ show(WVCP_CLIQUES)]
++  ["\n"]
++  [" + " ++ "WVCP_SEARCH_STRATEGY=" ++ show(WVCP_SEARCH_STRATEGY) ++ " + " ++ "WVCP_SEARCH_RESTART=" ++ show(WVCP_SEARCH_RESTART)]
++  ["\n"]
++  [" + " ++ "WVCP_SEARCH_VARIABLES_VERTICES=" ++ show(WVCP_SEARCH_VARIABLES_VERTICES) ++ " + " ++ "WVCP_SEARCH_DOMAIN_VERTICES=" ++ show(WVCP_SEARCH_DOMAIN_VERTICES)]
++  ["\n"]
++  [" + " ++ "WVCP_SEARCH_VARIABLES_DOMINANTS=" ++ show(WVCP_SEARCH_VARIABLES_DOMINANTS) ++ " + " ++ "WVCP_SEARCH_DOMAIN_DOMINANTS=" ++ show(WVCP_SEARCH_DOMAIN_DOMINANTS)]
++  ["\n"]
++  [" + " ++ "WVCP_SEARCH_VARIABLES_COLORS=" ++ show(WVCP_SEARCH_VARIABLES_COLORS) ++ " + " ++ "WVCP_SEARCH_DOMAIN_COLORS=" ++ show(WVCP_SEARCH_DOMAIN_COLORS)]
% ++  ["\n**************************"]
% ++  ["\n"]
% ++  ["COLORATION\t"] ++ [show(v) ++ "=" ++ show(x_color[v]) ++ " " | v in VERTEX]
% ++  ["\n"]
% ++  ["PLACEMENT\t"] ++ [show(c) ++ "=" ++ show(x_vertices[c]) ++ " " | c in COLOR]
% ++  ["\n"]
% ++  ["DOMINANTS\t"] ++ [show(c) ++ "=" ++ show(x_dominant[c]) ++ " " | c in COLOR]
++  ["\n"]
++  ["OUVERTES\t"] ++ [show(round(100*fix(x_nr_opened/card(R_VERTEX)))) ++ "%"]
++  ["\n"]
++  ["alpha(G)\t" ++ show(x_score)]
;
