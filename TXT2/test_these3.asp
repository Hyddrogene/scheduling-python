% espace de travail privé (affecte’)
affecte_(C,S)  :- cours(C), dispo(S), not naffecte_(C,S).
naffecte_(C,S) :- cours(C), dispo(S), not affecte_(C,S).
some(C) :- affecte_(C,S).
:- cours(C), not some(C).
:- affecte_(C,S1), affecte_(C,S2), S1 != S2.
:- affecte_(C1,S), affecte_(C2,S), C1 != C2.

% export propre
affecte(C,S) :- affecte_(C,S).
