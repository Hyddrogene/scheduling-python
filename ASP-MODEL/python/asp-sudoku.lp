% Définition des lignes, colonnes et valeurs possibles
row(1..9).   % Lignes de 1 à 9
col(1..9).   % Colonnes de 1 à 9
val(1..9).   % Valeurs possibles de 1 à 9

% Chaque cellule contient exactement une valeur
1 { cell(R, C, V) : val(V) } 1 :- row(R), col(C).

% Une valeur ne peut apparaître qu'une seule fois par ligne
:- row(R), val(V), #count { C : cell(R, C, V) } > 1.

% Une valeur ne peut apparaître qu'une seule fois par colonne
:- col(C), val(V), #count { R : cell(R, C, V) } > 1.

% Définition des blocs 3x3
block(RB, CB, R, C) :- row(R), col(C), RB = (R-1)/3+1, CB = (C-1)/3+1.

% Une valeur ne peut apparaître qu'une seule fois par bloc 3x3
:- val(V), block(RB, CB, R1, C1), block(RB, CB, R2, C2), R1 != R2, C1 != C2, cell(R1, C1, V), cell(R2, C2, V).

% Grille donnée : fixer les cellules déjà connues
cell(R, C, V) :- given(R, C, V).
:- given(R, C, V), not cell(R, C, V).
