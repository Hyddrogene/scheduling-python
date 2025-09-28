#const n = 3.         % Nombre de tâches
#const capacity = 10. % Capacité maximale de la ressource

% Durées des tâches
duration(1, 3).
duration(2, 2).
duration(3, 4).

% Consommations des ressources
resource(1, 4).
resource(2, 5).
resource(3, 3).

% Horizon temporel (le pire cas où toutes les tâches s'exécutent séquentiellement)
#const horizon = 10.

% Variables pour les temps de début des tâches
1 { start(T, S) : S = 0..horizon } 1 :- T = 1..n.

% Calcul du temps de fin des tâches
finish(T, F) :- start(T, S), duration(T, D), F = S + D.

% Contraintes cumulatives
:- time(T), #sum { R, T : start(T, S), duration(T, D),resource(T, R) } > capacity.

% Minimiser le makespan (temps total requis pour exécuter toutes les tâches)
%#minimize { F : finish(T, F) }.

% Temps utilisé
time(T) :- T = 0..horizon.

session(se1).
session(se2).
session(se3).

sess(2).

&dom{0..1} = s(S) :- session(S).
&dom{2..7} = n(N) :- sess(N).



&sum{ s(se1)*s(se2);s(se3)} = n(N ):- sess(N).

% Afficher les résultats
#show start/2.
