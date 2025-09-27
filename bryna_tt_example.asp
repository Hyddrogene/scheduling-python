%% Course

c(1..10). % on a 10 cours numéroté de 1 à 10.
r(1..2). % room

%% Seance

n(1..3). % 3 seance par cours

horizonTemporelWeek(1..45).%nombre d'ehure 
w(1..3). %nombre de semaines

sess(1..V) :- V = 10*3.

%s => course, seance, heure
%1{ s(X,Y,Z,W,R) : horizonTemporelWeek(Z), w(W), r(R)}1 :- c(X), n(Y).
1{ s(V,Z,W,R) : horizonTemporelWeek(Z), w(W), r(R)}1 :- sess(V).


%% precedence

% la séance Y1 est avant la séance Y2

:- s(X,Y1,Z1,W,_), s(X,Y2,Z2,W,_), Y1 < Y2, Z1 > Z2.
:- s(X,Y1,_,W1,_), s(X,Y2,_,W2,_), Y1 < Y2, W1 > W2.

%%recurrence

:- s(X,Y1,Z1,W1,_), s(X,Y2,Z2,W2,_), Y1 != Y2, Z1 != Z2, W1 != W2.

%% overlap

:- s(V1,Z,1,W,R), s(V2,Z2,W,R), Z1 + 1 <= Z2.
:- s(X,Y1,Z1,W,R1), s(X,Y2,Z2,W,R2), Z1 + 1 <= Z2.

%% One class par room

:- s(X1,Y2,Z,W,R), s(X2,Y2,Z,W,R), X1 != X2. 

#show s/5.
