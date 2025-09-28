# CP SOLVER FOR WVCP/MWSSP

Three models are provided
1. Stand-alone WVCP model - main file `wvcp_solver.mzn`
2. Stand-alone MWSSP model - main file `wssp_solver.mzn`
3. Combined WVCP and MWSSP model - main file `mwssp_wvcp_solver.mzn`

All models run on the same datasets (`.dzn` files).

All models may be run with Chuffed or GECODE.

Restart strategies may be used.

Warm-start strategies are not currently supported.


# FLAGS
Each model is configurable with flags.
The combined model must be run with both the flags of MWSSP and WVCP.


## WVCP flags

Search flags are cases of enumerations defined in `heuristics.mzn` (generic CP heuristics) and `wvcp_heuristics.mzn` (WVCP-specific heuristics).

- bool: WVCP_SCORE - whether models uses the provided best known score or not
- bool: WVCP_CLIQUES - whether models uses the provided cliques or not

- enum: WVCP_SEARCH_STRATEGY - variables to branch on
- enum: WVCP_SEARCH_RESTART - restart strategy or none
- enum: WVCP_SEARCH_VARIABLES_COLORS - variable heuristics on colors to open/close
- enum: WVCP_SEARCH_DOMAIN_COLORS - domain heuristics on openness/closure of color variables
- enum: WVCP_SEARCH_VARIABLES_DOMINANTS - variable heuristics on dominating vertices
- enum: WVCP_SEARCH_DOMAIN_DOMINANTS - domain heuristics on coloring of dominating vertex variables
- enum: WVCP_SEARCH_VARIABLES_VERTICES - variable heuristics on vertices to color
- enum: WVCP_SEARCH_DOMAIN_VERTICES - domain heuristics on coloring of vertex variables


## MWSSP flags

Search flags are cases of enumerations defined in `heuristics.mzn` (generic CP heuristics) and `mwssp_heuristics.mzn` (MWSSP-specific heuristics).

- enum: MWSSP_SEARCH_STRATEGY - variables to branch on
- enum: MWSSP_SEARCH_RESTART - restart strategy or none
- enum: MWSSP_SEARCH_VARIABLES_ARCS - variable heuristics on arc to include/exclude
- enum: MWSSP_SEARCH_DOMAIN_ARCS - domain heuristics on inclusion/exclusion of arc variables


## MWSSP+WVCP flags

Search flags are cases of enumerations defined in `mwssp_wvcp__heuristics.mzn` (MWSSP-WVCP-specific heuristics).

- enum: MWSSP_WVCP_SEARCH_STRATEGY - either branching on MWSSP variables or on WVCP variables


# COMMAND LINE EXAMPLES

Remarks :
1. solver flags: either `org.gecode.gecode` or `org.chuffed.chuffed`
2. timeout: given in milliseconds
3. the number of colors and the cliques may be set to 0 if not defined in the data set
4. all variable heuristic flags (`*_SEARCH_VARIABLES_*`) that are set to generic values (e.g. `INPUT_ORDER`) must be coerced with `WVCPSV` (e.g. `WVCPSV(INPUT_ORDER`)) if they are WVCP flags or with `MWSSPSV` if they are MWSSP flags: see examples below. They must NOT be coerced if they are WVCP- or MWSSP-specific flags (e.g. `DESC_WEIGHT_DEGREE`).

## WVCP

minizinc \
--solver org.gecode.gecode \
--time-limit 3600000 \
-s --solver-statistics --verbose-solving \
--intermediate \
--random-seed 0 \
-O1 \
-D nb_couleurs=nb_sommets \
-D nb_cliques=0 -D "cliques=[]" \
-D WVCP_SCORE=false \
-D WVCP_CLIQUES=false \
-D WVCP_SEARCH_STRATEGY=VERTICES_SPECIFIC \
-D WVCP_SEARCH_RESTART=RESTART_NONE \
-D "WVCP_SEARCH_VARIABLES_COLORS=WVCPSV(INPUT_ORDER)"    -D WVCP_SEARCH_DOMAIN_COLORS=INDOMAIN_MIN \
-D "WVCP_SEARCH_VARIABLES_DOMINANTS=WVCPSV(INPUT_ORDER)" -D WVCP_SEARCH_DOMAIN_DOMINANTS=INDOMAIN_MIN  \
-D "WVCP_SEARCH_VARIABLES_VERTICES=DESC_WEIGHT_DEGREE"   -D WVCP_SEARCH_DOMAIN_VERTICES=INDOMAIN_SPLIT \
wvcp_solve.mzn \
../../../graphes_dzn/GEOM30b.dzn


## MWSSP

minizinc \
--solver org.gecode.gecode \
--time-limit 3600000 \
-s --solver-statistics --verbose-solving \
--intermediate \
--random-seed 0 \
-O1 \
-D nb_couleurs=nb_sommets \
-D nb_cliques=0 -D "cliques=[]" \
-D MWSSP_SEARCH_STRATEGY=ARCS_SPECIFIC \
-D MWSSP_SEARCH_RESTART=RESTART_NONE \
-D "MWSSP_SEARCH_VARIABLES_ARCS=DESC_WEIGHT_TAIL" -D MWSSP_SEARCH_DOMAIN_ARCS=INDOMAIN_MAX \
mwssp_solve.mzn \
../../../graphes_dzn/GEOM30b.dzn


## MWSSP+WVCP

minizinc \
--solver org.gecode.gecode \
--time-limit 3600000 \
-s --solver-statistics --verbose-solving \
--intermediate \
--random-seed 0 \
-O1 \
-D nb_couleurs=nb_sommets \
-D nb_cliques=0 -D "cliques=[]" \
-D WVCP_SCORE=false \
-D WVCP_CLIQUES=false \
-D MWSSP_WVCP_SEARCH_STRATEGY=WVCP \
-D WVCP_SEARCH_STRATEGY=VERTICES_SPECIFIC \
-D WVCP_SEARCH_RESTART=RESTART_NONE \
-D "WVCP_SEARCH_VARIABLES_COLORS=WVCPSV(INPUT_ORDER)"    -D WVCP_SEARCH_DOMAIN_COLORS=INDOMAIN_MIN \
-D "WVCP_SEARCH_VARIABLES_DOMINANTS=WVCPSV(INPUT_ORDER)" -D WVCP_SEARCH_DOMAIN_DOMINANTS=INDOMAIN_MIN  \
-D "WVCP_SEARCH_VARIABLES_VERTICES=DESC_WEIGHT_DEGREE"   -D WVCP_SEARCH_DOMAIN_VERTICES=INDOMAIN_SPLIT \
-D MWSSP_SEARCH_STRATEGY=ARCS_SPECIFIC \
-D MWSSP_SEARCH_RESTART=RESTART_NONE \
-D "MWSSP_SEARCH_VARIABLES_ARCS=DESC_WEIGHT_TAIL" -D MWSSP_SEARCH_DOMAIN_ARCS=INDOMAIN_MAX \
mwssp_wvcp_solve.mzn \
../../../graphes_dzn/GEOM30b.dzn
