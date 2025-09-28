%Définition des intervalles possibles pour les variables
table(0..3).
chair(0..10).

% Règle pour générer les combinaisons de production
1 { produce_table(T) : table(T) } 1.
1 { produce_chair(C) : chair(C) } 1.

% Contraintes
:- produce_table(T), produce_chair(C), 10*T + 3*C > 50.

% Fonction objectif
profit(P) :- produce_table(T), produce_chair(C), P = 20*T + 10*C.
#maximize { P : profit(P) }.
