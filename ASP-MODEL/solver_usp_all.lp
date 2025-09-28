pg :- grid_exist(1).
n_pg :-not grid_exist(1).


session_duration(S,D) :- pg, session(S),session_part(S,P), part_grids(P,_,D,_).

session_group(S,G) :- class_sessions(C,S),class_group(C,G).

session_class(S,C) :-  class_sessions(C,S).

class_part(C,P) :- part_class(P,C). 

session_part(S,P) :- session(S),session_class(S,C),class_part(C,P).
session_teacher(S,T) :- session(S),session_part(S,P),part_teacher(P,T).
session_room(S,R) :- session(S),session_part(S,P),part_room(P,R).

1{assigned(S,SL):session_part(S,P),part_slot(P,SL)}1:- session(S).

nrPositionTeacher(S,2..N):- session(S),nrteacherMax(S,N).
nrteacherMax(S,N):- session(S),session_part(S,P),part(P,_,_,_,_,_,N), N > 1.
sessionTeacherFix(S) :- session(S) ,not nrteacherMax(S,_).
partTeacherFix(P) :- part_sessions(P,S), sessionTeacherFix(S).
partTeacherMulti(P) :- part_sessions(P,S),nrteacherMax(S,N).

1{assignedtk(S,SL,I):nrPositionTeacher(S,I)}K:-assigned(S,SL),nrteacherMax(S,K).

1{assignedt(S,SL,T,K):session_teacher(S,T)}1 :- assignedtk(S,SL,K).
1{assignedt(S,SL,T,"S"):session_teacher(S,T)}1 :- assigned(S,SL),sessionTeacherFix(S).


nrPositionRoom(S,1..N):- session(S),nrRoomMax(S,N).
nrRoomMax(S,N):- session(S),session_part(S,P),part(P,_,_,_,_,N,_), N > 1.
sessionRoomFix(S) :- session(S) ,not nrRoomMax(S,_).
partRoomFix(P) :- part_sessions(P,S), sessionRoomFix(S).
partRoomMulti(P) :- part_sessions(P,S),nrRoomMax(S,N).

1{assignedrk(S,SL,I):nrPositionRoom(S,I)}K:-assigned(S,SL),nrRoomMax(S,K).
1{assignedr(S,SL,R,I):session_room(S,R),nrPositionRoom(S,I)}1 :- assignedrk(S,SL,I).
1{assignedr(S,SL,R,"S"):session_room(S,R)}1 :- assigned(S,SL),sessionRoomFix(S).

:- nrRoomMax(S,_),session_class(S,C),class_headcount(C,N), N > #sum{V,I:assignedr(S,_,R,I),room(R,V)}.%
roomOrdered(S,1..N):- session(S),same_rooms(S,_),nrRoomMax(S,K),N = #count{S,I:assignedr(S,_,_,I)}.
roomOrdered(S,1..N):- session(S),same_rooms(_,S),nrRoomMax(S,K),N = #count{S,I:assignedr(S,_,_,I)}.
:- roomOrdered(S,I), not assignedr(S,_,_,I).

%1{assignedr(S,SL,R):session_room(S,R)}1 :- assigned(S,SL).
:- nrRoomMax(S,_),session_class(S,C),class_headcount(C,N), N > #sum{V,I:assignedr(S,_,R,I),room(R,V)}.%

:- assignedr(S,SL,R,1),room(R,C1),sessionRoomFix(S),session_class(S,C),class_headcount(C,N), N > C1.
:- assignedr(S,SL,R),room(R,C1),session_class(S,C),class_headcount(C,N), N > C1.

%:- not {assigned(S,SL):session(S),disjunctive_group(S,G)}1,group(G,_),slots(SL).

%:- not {assignedt(S,SL,T,K) : session(S),disjunctive_teacher(T,S),nrPositionTeacher(S,K) } 1, slots(SL),teacher(T).
%:- not {assignedt(S,SL,T,"S") : session(S),disjunctive_teacher(T,S) } 1, slots(SL),teacher(T).

%:- not {assignedr(S,SL,R,K) : session(S),disjunctive_room(R,S),nrPositionRoom(S,K) } 1, slots(SL),room(R,_).
%:- not {assignedr(S,SL,R,"S") : session(S),disjunctive_room(R,S) } 1, slots(SL),room(R,_).

:- assignedt(S,_,T,K2),assignedt(S,_,T,K1), K1 != K2.
:- assignedr(S,_,R,K2),assignedr(S,_,R,K1), K1 != K2.

:- periodic(S1,S2,N),assigned(S1,SL1),assigned(S2,SL2),SL2 != SL1+N.
:- sequenced(S1,S2),assigned(S1,SL1),session_part(S1,P),part_grids(P,_,N,_),assigned(S2,SL2),SL1+N > SL2.
sequenced(S1,S2):-part(P,_,_,_,_,_,_),part_class(P,C),class_sessions(C,S1),class_sessions(C,S2),S1+1 = S2.
:-same_slot(S1,S2),assigned(S1,SL1),assigned(S2,SL2),SL1 != SL2.

:-same_teachers(S1,S2),assignedt(S1,_,T1,K),assignedt(S2,_,T2,K), T1 != T2.

:-same_rooms(S1,S2),assignedr(S1,_,R1,K),assignedr(S2,_,R2,K), R1 != R2.
:-same_rooms(S1,S2),#count{S2,K2 : assignedr(S2,_,_,K2)} =N2 ,#count{S1,K1 :assignedr(S1,_,_,K1)} =N1, N1 != N2.
:- same_rooms(S,S2),nrRoomMax(S,_),not nrRoomMax(S2,_).

num(1).
num(0).
%&diff{K2-K1} >= N:- sequenced_ch(S1,S2,0),assigned(S2,K2),assigned(S1,K1),session_part(S1,P),part_grids(P,_,N,_).

%&diff{K1-K2} >= N:- sequenced_ch(S1,S2,1),assigned(S2,K2),assigned(S1,K1),session_part(S2,P),part_grids(P,_,N,_).

:- sequenced_ch(S1,S2,0),assigned(S2,K2),assigned(S1,K1),session_part(S1,P),part_grids(P,_,N,_),K2-K1 < N.
:- sequenced_ch(S1,S2,1),assigned(S2,K2),assigned(S1,K1),session_part(S2,P),part_grids(P,_,N,_),K1-K2 < N.

%:-disjunctive_group(S1,G),disjunctive_group(S2,G),assigned(S2,K2),assigned(S1,K1),session_part(S2,P),part_grids(P,_,N,_),K1 >= K2 , K1 < K2+N.


%&disjoint{xslot(S1)@D1:disjunctive_teacher(T,S1),assignedt(S1,T,_),session_duration(S1,D1)} :-teacher(T).
%&disjoint{xslot(S1)@D1:disjunctive_room(S1,T),assignedr(S1,T,_),session_duration(S1,D1)} :-room(T,N).
1{sequenced_ch(S1,S2,X):num(X)}1 :- disjunctive_group(S1,G),disjunctive_group(S2,G),assignedr(S1,SL1),assignedr(S2,SL2), S1 < S2,not sequenced(S1,S2),not sequenced(S2,S1).
1{sequenced_ch(S1,S2,X):num(X)}1 :- disjunctive_room(R,S1),disjunctive_room(R,S2),assignedr(S1,R,_),assignedr(S2,R,_), S1 < S2,not sequenced(S1,S2),not sequenced(S2,S1).
1{sequenced_ch(S1,S2,X):num(X)}1 :- disjunctive_teacher(T,S1),disjunctive_teacher(T,S2),assignedt(S1,T,_),assignedt(S2,T,_), S1 < S2,not sequenced(S1,S2),not sequenced(S2,S1).

:- assign_rooms(S1,R1),assignedr(S1,_,R2,_), R1 != R2.
:- assign_teachers(S1,T1),assignedt(S1,_,T2,_), T1 != T2.

:- serviceTeacher(T,P,N),#count { S,T :assignedt(S,_,T,_),part_sessions(P,S)} != N .%,partTeacherFix(P).
%:- serviceTeacher(T,P,N),partTeacherMulti(P), #count { S,T :assignedt(S,_,T,_),part_sessions(P,S)} != N.
%same_rooms((11..29),(12..30)).
%same_slot(11,21).

%#show rere/3. 
#show assignedt/4.
#show assignedt/3.

#show assignedr/4.
#show assignedr/3.


