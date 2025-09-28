cours(math).
cours(informatique).
salle(salle1).
salle(salle2).
salle(salle3).
indisponible(salle3).


affecte(C,S) :- cours(C), salle(S), not autre(C,S).
autre(C,S) :-  salle(S),salle(S2), affecte(C,S2), S2 != S.


:- affecte(C1,S), affecte(C2,S), C1 != C2.
:- affecte(C,S), indisponible(S).

%#show affecte/2.
