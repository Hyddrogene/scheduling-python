% Faits
workload(teacher1, 1).        % Le seuil pour teacher1 est 2
workload(teacher2, 1).        % Le seuil pour teacher2 est 3

day(monday).                  % Définition des jours
day(tuesday).
day(friday).

slot_decomposition(slot1, 1, monday, week1).    % Décomposition des créneaux
slot_decomposition(slot2, 2, monday, week1).
slot_decomposition(slot3, 3, monday, week1).
slot_decomposition(slot4, 4, monday, week1).
slot_decomposition(slot5, 5, tuesday, week1).    % Décomposition des créneaux
slot_decomposition(slot6, 6, tuesday, week1).
slot_decomposition(slot7, 7, tuesday, week1).
slot_decomposition(slot8, 8, tuesday, week1).
slot_decomposition(slot9, 9, friday, week1).    % Décomposition des créneaux
slot_decomposition(slot10, 10, friday, week1).
slot_decomposition(slot11, 11, friday, week1).
slot_decomposition(slot12, 12, friday, week1).
slot_decomposition(slot13, 13, wednesday, week1).

slot_day(S,D) :- slot_decomposition(S,_,D,_).


&dom {1..16} = xSlot(S) :- session(S).
&dom {1..4} = xday(S) :- session(S).
&dom {1..1} = xweek(S) :- session(S).
&dom {1..4} = xdl(S) :- session(S).



&sum{xSlot(S) -  (xdl(S)+4*(xday(S)-1)+(xweek(S)-1)*16)} = 0:-session(S). 

workload(teacher1, 1).        % Le seuil pour teacher1 est 2


workload_session(teacher1, session1).          % Sessions pour teacher1
workload_session(teacher1, session2).
workload_session(teacher1, session3).          % Sessions pour teacher1
workload_session(teacher1, session4).

workload_session(teacher2, session5).          % Sessions pour teacher2
workload_session(teacher2, session6).
workload_session(teacher2, session7).          % Sessions pour teacher2
workload_session(teacher2, session8).


&distinct{xSlot(S) : workload_session(ID,S)} :- workload(ID,_).
&distinct{xday(S) : workload_session(ID,S)} :- workload(ID,_).

forbidden_slot(session1, 15).
&sum{xSlot(S)} != N :- forbidden_slot(S, N).



forbidden_slot(session1, 1).
forbidden_slot(session1, 2).
forbidden_slot(session1, 3).
forbidden_slot(session1, 4).

forbidden_slot(session1, 5).
forbidden_slot(session1, 6).
forbidden_slot(session1, 7).
forbidden_slot(session1, 8).


forbidden_slot(session1, 9).
forbidden_slot(session1, 10).
forbidden_slot(session1, 11).
forbidden_slot(session1, 12).


forbidden_slot(session1, 13).
forbidden_slot(session1, 14).
forbidden_slot(session1, 15).



&sum{xSlot(S)} != N :- forbidden_slot(S, N).





session(session1).
session(session2).
session(session3).
session(session4).
session(session5).
session(session6).
session(session7).
session(session8).

slot(slot1).
slot(slot2).
slot(slot3).
slot(slot4).
slot(slot5).
slot(slot6).
slot(slot7).
slot(slot8).
slot(slot9).
slot(slot10).
slot(slot11).
slot(slot12).
slot(slot13).




% Assignations (essayer différents scénarios pour tester la contrainte)
1{assigned(S,SL) : slot(SL)}1 :- session(S).



% Contraintes
%:- workload(ID, SEUIL),day(D), #count {S,SL: workload_session(ID, S), assigned(S, SL),slot_day(SL, D)} > SEUIL.
%:- workload(ID, SEUIL), #count {S,SL: workload_session(ID, S), assigned(S, SL),slot_day(SL, monday)} > SEUIL.
%:- workload(ID, SEUIL), #count {S,SL: workload_session(ID, S), assigned(S, SL),slot_day(SL, tuesday)} > SEUIL.
%:- workload(ID, SEUIL), #count {S,SL: workload_session(ID, S), assigned(S, SL),slot_day(SL, friday)} > SEUIL.
%:- workload(ID, SEUIL), #count {S,SL: workload_session(ID, S), assigned(S, SL),slot_day(SL, wednesday)} > SEUIL.




% Affichage des résultats
%#show assigned/2.
%#show slot_day/2.

