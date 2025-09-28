% Faits d'exemple
cours(math). cours(informatique).
salle(s1). salle(s2). salle(s3).
salle(s4).
salle(s5).
indisponible(s3).

% Salles dispo
dispo(S) :- salle(S), not indisponible(S).

% Guess binaire sur les couples cours-salle
affecte(C,S)  :- cours(C), dispo(S), not naffecte(C,S).
naffecte(C,S) :- cours(C), dispo(S), not affecte(C,S).

% Au moins une salle par cours
some(C) :- affecte(C,S).
:- cours(C), not some(C).

% Au plus une salle par cours
:- affecte(C,S1), affecte(C,S2), S1 != S2.

% Au plus un cours par salle (injectivité)
:- affecte(C1,S), affecte(C2,S), C1 != C2.

%#show affecte/2.
