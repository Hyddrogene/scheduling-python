:- use_module(library(chr)).

% Déclaration des contraintes
:- chr_constraint neq/2.

% Règle de simplification : si X ≠ Y et Y ≠ X, ne garder quune seule contrainte
neq(X,Y) \ neq(Y,X) <=> X \== Y | true.

% Règle de propagation : si X ≠ Y et X = Y, cela entraîne un échec
neq(X,Y) <=> X == Y | false.

% Règle de transitivité : si X ≠ Y et Y ≠ Z, alors X ≠ Z
neq(X,Y), neq(Y,Z) ==> X \== Z | neq(X,Z).


