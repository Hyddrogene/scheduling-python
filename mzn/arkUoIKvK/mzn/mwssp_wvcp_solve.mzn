include "mwssp_wvcp_heuristics.mzn";


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% RESOLUTION

solve :: mwssp_wvcp_search() :: mwssp_wvcp_restart()
%   seq_search([
%    int_search(x_color,first_fail,indomain_min),
%    int_search(y_arc,input_order,indomain_max)
%   ])
  minimize(x_score);
%  maximize(y_score);



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
++  [" + " ++ "MWSSP_WVCP_SEARCH_STRATEGY=" ++ show(MWSSP_WVCP_SEARCH_STRATEGY)]
++  ["\n"]
++  [" + " ++ "WVCP_SEARCH_STRATEGY=" ++ show(WVCP_SEARCH_STRATEGY) ++ " + " ++ "WVCP_SEARCH_RESTART=" ++ show(WVCP_SEARCH_RESTART)]
++  ["\n"]
++  [" + " ++ "WVCP_SEARCH_VARIABLES_VERTICES=" ++ show(WVCP_SEARCH_VARIABLES_VERTICES) ++ " + " ++ "WVCP_SEARCH_DOMAIN_VERTICES=" ++ show(WVCP_SEARCH_DOMAIN_VERTICES)]
++  ["\n"]
++  [" + " ++ "WVCP_SEARCH_VARIABLES_DOMINANTS=" ++ show(WVCP_SEARCH_VARIABLES_DOMINANTS) ++ " + " ++ "WVCP_SEARCH_DOMAIN_DOMINANTS=" ++ show(WVCP_SEARCH_DOMAIN_DOMINANTS)]
++  ["\n"]
++  [" + " ++ "WVCP_SEARCH_VARIABLES_COLORS=" ++ show(WVCP_SEARCH_VARIABLES_COLORS) ++ " + " ++ "WVCP_SEARCH_DOMAIN_COLORS=" ++ show(WVCP_SEARCH_DOMAIN_COLORS)]
++  ["\n"]
++  [" + " ++ "MWSSP_SEARCH_STRATEGY=" ++ show(MWSSP_SEARCH_STRATEGY) ++ " + " ++ "MWSSP_SEARCH_RESTART=" ++ show(MWSSP_SEARCH_RESTART)]
++  ["\n"]
++  [" + " ++ "MWSSP_SEARCH_VARIABLES_ARCS=" ++ show(MWSSP_SEARCH_VARIABLES_ARCS) ++ " + " ++ "MWSSP_SEARCH_DOMAIN_ARCS=" ++ show(MWSSP_SEARCH_DOMAIN_ARCS)]
++  ["\n"]
% ++  ["\n**************************"]
% ++  ["\n"]
% ++  ["COLORATION\t"] ++ [show(v) ++ "=" ++ show(x_color[v]) ++ " " | v in VERTEX]
% ++  ["\n"]
% ++  ["PLACEMENT\t"] ++ [show(c) ++ "=" ++ show(x_vertices[c]) ++ " " | c in COLOR]
% ++  ["\n"]
% ++  ["DOMINANTS\t"] ++ [show(c) ++ "=" ++ show(x_dominant[c]) ++ " " | c in COLOR]
% ++  ["\n"]
% ++  ["OUVERTES\t"] ++ [show(round(100*fix(x_nr_opened/card(R_VERTEX)))) ++ "%"]
% ++  ["\n"]
% ++  ["ETOILES \t"] ++ [if fix(y_arc[e])=1 then "(" ++ show(tail[e]) ++ "," ++ show(head[e]) ++ ") " else "" endif | e in ARC]
% ++  ["\n"]
++  ["alpha(G)\t" ++ show(x_score)]
++  ["\n"]
++  ["chi(-G)\t" ++ show(y_score)]
++  ["\n"]
% ++  ["(" ++ show(tail[e]) ++ "," ++ show(head[e]) ++ ") " | e in sort_arcs(set2array(index_set(ARC)))]
;
