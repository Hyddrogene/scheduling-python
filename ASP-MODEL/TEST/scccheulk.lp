#const n = 4.         % Nombre de tâches
#const m = 1.         % Nombre de machines
#const horizon = 10.  % Horizon temporel (maximum)

% Données des tâches : durée et consommation électrique
duration(1, 3). consumption(1, 4).
duration(2, 2). consumption(2, 5).
duration(3, 4). consumption(3, 3).
duration(4, 1). consumption(4, 2).

% Capacité électrique des machines
capacity(1, 8).
capacity(2, 10).
capacity(3, 6).

% Variables : assignation des tâches
1 { assign(T, M) : machine(M) } 1 :- task(T).
machine(M) :- M = 1..m.
task(T) :- T = 1..n.

% Variables : temps de début des tâches
&dom{0..horizon} = start(T) :- task(T).
&dom{0..horizon} = finish(T) :- task(T). 

% Temps de fin des tâches
&sum{start(T)+D} = finish(T) :- duration(T, D).

% Contrainte cumulative sur la capacité électrique des machines
active(T, start(T), M) :- assign(T, M), duration(T, D).
&sum { (T) : active(T, S, M), consumption(T, C) } <= Cmax :- machine(M), capacity(M, Cmax), S = 0..horizon.

% Objectif : Minimiser le makespan
%makespan(F) :- finish(T), task(T).
%#minimize { F : makespan(F) }.

% Afficher les résultats
#show assign/2.
#show start/2.
#show finish/2.
