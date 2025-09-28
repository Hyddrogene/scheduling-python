%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% MWSSP+WVCP HEURISTICS

include "mwssp_heuristics.mzn";
include "wvcp_heuristics.mzn";
include "mwssp_wvcp.mzn";


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% SEARCH STRATEGIES

enum MWSSP_WVCP_SEARCH = {
  MWSSP,
  WVCP
};


MWSSP_WVCP_SEARCH: MWSSP_WVCP_SEARCH_STRATEGY;


function ann: mwssp_wvcp_search() =
  if      MWSSP_WVCP_SEARCH_STRATEGY=MWSSP
  then    mwssp_search()
  else    wvcp_search()
  endif;


function ann: mwssp_wvcp_restart() =
  if      MWSSP_WVCP_SEARCH_STRATEGY=MWSSP
  then    mwssp_restart()
  else    wvcp_restart()
  endif;
