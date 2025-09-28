pg :- grid_exist(1).
n_pg :-not grid_exist(1).

%DATA LINEARISATION
session_duration(S,D) :- pg, session(S),session_part(S,P), part_grids(P,_,D,_).
session_group(S,G) :- class_sessions(C,S),class_group(C,G).
session_class(S,C) :-  class_sessions(C,S).
class_part(C,P) :- part_class(P,C). 
session_part(S,P) :- session(S),session_class(S,C),class_part(C,P).
session_teacher(S,T) :- session(S),session_part(S,P),part_teacher(P,T).
session_room(S,R) :- session(S),session_part(S,P),part_room(P,R).
%%%%SLOT
&dom {1..H} = xslot(S) :- session(S),session_part(S,P),part_maxslot(P,H).
%%%%TEACHER
%K{assignedtk(S,I):nrPositionTeacher(S,I)}K:-session(S),nrteacherMax(S,K).
nrPositionTeacher(S,1..N):- session(S),nrteacherMax(S,N).
nrteacherMax(S,N):- session(S),session_part(S,P),part(P,_,_,_,_,_,N), N > 1.
sessionTeacherFix(S) :- session(S) ,not nrteacherMax(S,_).
partTeacherFix(P) :- part_sessions(P,S), sessionTeacherFix(S).
partTeacherMulti(P) :- part_sessions(P,S),nrteacherMax(S,N).
assignedtk(S,I):-nrPositionTeacher(S,I),session(S),nrteacherMax(S,K).
1{assignedt(S,T,K):session_teacher(S,T)}1 :- assignedtk(S,K).
1{assignedt(S,T,1):session_teacher(S,T)}1 :- session(S),sessionTeacherFix(S).
%%%% ROOMS
%K{assignedrk(S,I):nrPositionRoom(S,I)}K:-session(S),nrRoomMax(S,K).
nrPositionRoom(S,1..N):- session(S),nrRoomMax(S,N).
nrRoomMax(S,N):- session(S),session_part(S,P),part(P,_,_,_,_,N,_), N > 1.
sessionRoomFix(S) :- session(S) ,not nrRoomMax(S,_).
partRoomFix(P) :- part_sessions(P,S), sessionRoomFix(S).
partRoomMulti(P) :- part_sessions(P,S),nrRoomMax(S,N).
assignedrk(S,I):-session(S),nrRoomMax(S,K),nrPositionRoom(S,I).
1{assignedr(S,R,K):session_room(S,R)}1 :- assignedrk(S,K).
1{assignedr(S,R,1):session_room(S,R)}1 :- session(S),sessionRoomFix(S).
%%%%% CONSTRAINT

%%COMMENT
%:- nrRoomMax(S,_),session_class(S,C),class_headcount(C,N), N > #sum{V:assignedr(S,R,_),room(R,V)}.
%:- assignedr(S,R,_),room(R,C1),session_class(S,C),class_headcount(C,N), N > C1, not nrRoomMax(S,_).

&sum { xslot(S1) +N } = xslot(S2):- periodic(S1,S2,N).
&sum { xslot(S1) +N } <= xslot(S2) :- sequenced(S1,S2),session_part(S1,P),part_grids(P,_,N,_).
sequenced(S1,S2):-part(P,_,_,_,_,_,_),part_class(P,C),class_sessions(C,S1),class_sessions(C,S2),S1+1 = S2.
&sum{xslot(S1) - xslot(S2)} = 0 :-same_slot(S1,S2).
:-same_teachers(S1,S2),assigned(S1,T1,K),assigned(S2,T2,K),T1 != T2.
:-same_rooms(S1,S2),assigned(S1,R1,K),assigned(S2,R2,K), R1 != R2.
assigned(S1,T,R1) :- assign_rooms(S1,R1),assigned(S1,T),session_room(S1,R1).
assigned(S1,T):- assign_teachers(S1,T), session_teacher(S1,T).
%%%% DISJUNCTIVE Group
&disjoint {xslot(S1)@D1:disjunctive_group(S1,G),session_duration(S1,D1)} :-group(G,_).
%%%DISJUNCTIVE TEACHER & DISJUNCTIVE ROOM
num(1).
num(0).
&sum{xslot(S2)-xslot(S1)} >= N:- sequenced_ch(S1,S2,0),session_part(S1,P),part_grids(P,_,N,_).
&sum{xslot(S1)- xslot(S2) } >= N:- sequenced_ch(S1,S2,1),session_part(S2,P),part_grids(P,_,N,_).

%&disjoint{xslot(S1)@D1:disjunctive_teacher(T,S1),assignedt(S1,T,_),session_duration(S1,D1)} :-teacher(T).
%&disjoint{xslot(S1)@D1:disjunctive_room(S1,T),assignedr(S1,T,_),session_duration(S1,D1)} :-room(T,N).
1{sequenced_ch(S1,S2,X):num(X)}1 :- disjunctive_room(R,S1),disjunctive_room(R,S2),assignedr(S1,R,_),assignedr(S2,R,_), S1 < S2,not sequenced(S1,S2),not sequenced(S2,S1).
1{sequenced_ch(S1,S2,X):num(X)}1 :- disjunctive_teacher(T,S1),disjunctive_teacher(T,S2),assignedt(S1,T,_),assignedt(S2,T,_), S1 < S2,not sequenced(S1,S2),not sequenced(S2,S1).
:- serviceTeacher(T,P,N),K= #count { S:assignedt(S,T,_),part_sessions(P,S)} ,K != N.

#show assignedt/3.
#show assignedr/3.
#show class_headcount/2.
&show {xslot(X):session(X)}.
