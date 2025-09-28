include "mwssp_heuristics.mzn";


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% RESOLUTION

solve :: mwssp_search() :: mwssp_restart()
  maximize(y_score);


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% AFFICHAGE

output
    ["COULEURS\t" ++ show(COLOR)]
++  ["\n"]
% ++  ["POIDS\t"] ++ [show(v) ++ "-" ++ show(weight[v]) ++ " " | v in VERTEX]
% ++  ["\n"]
% ++  ["VOISINS\t"] ++ [show(v) ++ "-" ++ show(neighbours[v]) ++ " " | v in VERTEX]
% ++  ["\n"]
% ++  ["CLIQUES\t" ++ show(clique)]
% ++  ["\n**************************"]
% ++  ["\n"]
++  ["#Sommets\t" ++ show(nb_sommets)]
++  ["\n"]
++  ["Densité\t" ++ show(round(100*density)) ++ "%"]
++  ["\n"]
++  ["Max poids\t" ++ show(max(poids))]
++  ["\n"]
++  ["Mean poids\t" ++ show(round(sum(poids)/nb_sommets))]
++  ["\n"]
++  ["WVCP*\t" ++ show(score_max)]
++  ["\n"]
++  ["MWSSP*\t" ++ show(sum(poids)-score_max)]
++  ["\n**************************"]
++  ["\n"]
++  
["FLAGS\t"] 
++  [" + " ++ "MWSSP_SEARCH_STRATEGY=" ++ show(MWSSP_SEARCH_STRATEGY) ++ " + " ++ "MWSSP_SEARCH_RESTART=" ++ show(MWSSP_SEARCH_RESTART)]
++  ["\n"]
++  [" + " ++ "MWSSP_SEARCH_VARIABLES_ARCS=" ++ show(MWSSP_SEARCH_VARIABLES_ARCS) ++ " + " ++ "MWSSP_SEARCH_DOMAIN_ARCS=" ++ show(MWSSP_SEARCH_DOMAIN_ARCS)]
++  ["\n"]
% ++  ["\n**************************"]
% ++  ["\n"]
% ++  ["ETOILES \t"] ++ [if fix(y_arc[e])=1 then "(" ++ show(tail[e]) ++ "," ++ show(head[e]) ++ ") " else "" endif | e in ARC]
% ++  ["\n"]
++  ["chi(-G)\t" ++ show(y_score)]
% ++  ["\n"]
% ++  ["(" ++ show(tail[e]) ++ "," ++ show(head[e]) ++ ") " | e in sort_arcs(set2array(index_set(ARC)))]
;
