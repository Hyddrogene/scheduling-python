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
%assigned(S,&dom {1..H}) :- session(S),nr_slots(H).
%1{assigned(S,SL):session_part(S,P),part_slot(P,SL)}1:- session(S).
%:-not {assigned(S,SL):session(S)}1,session_part(S,P),part_slots(P,SL),slots(SL).

%%%%TEACHER
1{assigned(S,T):session_teacher(S,T)}1 :- session(S).
:- not {assigned(S,T) : session(S) } 1, session_part(S,P),part_slot(P,SL),part_teacher(P,T).
%&dom {1..H} = xslot(S) :- session(S),session_part(S),.

%%%% ROOMS
1{assigned(S,T,R):session_room(S,R)}1 :- assigned(S,T).
:- not {assigned(S,T,R) : session(S) } 1,session_part(S,P),part_teacher(P,T),part_room(P,R). 
%&dom {1..H} = xslot(S) :- session(S),nr_slots(H).

%%%%% CONSTRAINT
&sum { xslot(S1) +N } = xslot(S2):- periodic(S1,S2,N).
&sum { xslot(S1) +N } <= xslot(S2) :- sequenced(S1,S2),session_part(S1,P),part_grids(P,_,N,_).
sequenced(S1,S2):-part(P,_,_,_,_,_,_),part_class(P,C),class_sessions(C,S1),class_sessions(C,S2),S1+1 = S2.
xslot(S1) = xslot(S2) :-same_slot(S1,S2).

:-same_teachers(S1,S2),assigned(S1,T1),assigned(S2,T2),T1 != T2.

:-same_rooms(S1,S2),assigned(S1,_,R1),assigned(S2,_,R2), R1 != R2.
assigned(S1,T,R1) :- assign_rooms(S1,R1),assigned(S1,T),session_room(S1,R1).
assigned(S1,T):- assign_teachers(S1,T), session_teacher(S1,T).

%%%% DISJUNCTIVE Group
&disjoint {xslot(S1)@D1:disjunctive_group(S1,G),session_duration(S1,D1)} :-group(G,_).

%:- disjunctive_group(S1,S2), S1 != S2,session_duration(S1,D1),session_duration(S2,D2),&sum{xslot(S1)} <= xslot(S2), &sum{xslot(S2); -D1} < xslot(S1).
%&sum { xslot(S2) - xslot(S1)} != 0 :- disjunctive_teacher(S1,S2), assigned(S1,T),assigned(S2,T), not disjunctive_group(S1,S2), not sequenced(S1,S2).
%:-disjunctive_room(S1,S2), assigned(S1,SL1,_,R),assigned(S2,SL2,_,R),SL1 == SL2, not disjunctive_group(S1,S2), not sequenced(S1,S2).

%%%DISJUNCTIVE TEACHER
num(1).
num(0).
1{sequenced_ch(S1,S2,X):num(X)}1 :- disjunctive_teacher(S1,S2),assigned(S1,T),assigned(S2,T), not disjunctive_group(S1,S2), not sequenced(S1,S2),not sequenced(S2,S1).
:-not{sequenced_ch(S1,S2,X):num(X)}1,disjunctive_teacher(S1,S2),assigned(S1,T),assigned(S2,T), not disjunctive_group(S1,S2), not sequenced(S1,S2),not sequenced(S2,S1).

&sum{xslot(S2)- xslot(S1)} > 0 :- sequenced_ch(S1,S2,0),session_duration(S1,D1).
&sum{xslot(S2); -D1} >= xslot(S1):- sequenced_ch(S1,S2,0),session_duration(S1,D1).
&sum{xslot(S1)- xslot(S2)} > 0 :- sequenced_ch(S1,S2,1),session_duration(S1,D1).
&sum{xslot(S1); -D2} >= xslot(S2):- sequenced_ch(S1,S2,1),session_duration(S2,D2).

%sequenced(S1,S2);sequenced(S2,S1):- disjunctive_teacher(S1,S2), assigned(S1,_,T),assigned(S2,_,T),session_duration(S1,D1).
%:-sequenced(S1,S2),sequenced(S2,S1).
%&disjoint { xslot(S)@D1 : session_teacher(S,T),assigned(S,T),session_duration(S,D1)  } :-teacher(T).

%%%DISJUNCTIVE ROOM

num_r(1).
num_r(0).
1{sequenced_ch(S1,S2,X):num_r(X)}1 :- disjunctive_room(S1,S2),assigned(S1,T),assigned(S2,T), not disjunctive_group(S1,S2), not disjunctive_teacher(S1,S2), not sequenced(S1,S2),not sequenced(S2,S1).
:-not{sequenced_ch(S1,S2,X):num_r(X)}1,disjunctive_room(S1,S2),assigned(S1,T),assigned(S2,T), not disjunctive_group(S1,S2),not disjunctive_teacher(S1,S2), not sequenced(S1,S2),not sequenced(S2,S1).
%:- disjunctive_room(S1,S2), assigned(S1,_,T),assigned(S2,_,T),session_duration(S2,D2), &sum{xslot(S1)} >= xslot(S2), &sum{xslot(S1); -D2} < xslot(S2),not disjunctive_group(S1,S2), not sequenced(S1,S2).
%&sum { xslot(S1) - xslot(S2)} != 0 :- disjunctive_room(S1,S2), assigned(S1,_,T),assigned(S2,_,T), not disjunctive_group(S1,S2), not sequenced(S2,S1).
%:-disjunctive_group(S1,S2),session_group(S1,G),session_group(S2,G), assigned(S1,SL1),assigned(S2,SL2),SL1 == SL2, not sequenced(S1,S2).
%&sum { xslot(S2) - xslot(S1)} != 0 :- disjunctive_teacher(S1,S2),session_group(S1,G),session_group(S2,G), not sequenced(S1,S2).

:- serviceTeacher(T,P,N),K= #count { S:assigned(S,T),part_sessions(P,S)} ,K != N.
%toto(T,P,N,K):- serviceTeacher(T,P,N),K= #count { S:assigned(S,T),part_sessions(P,S)} ,K == N.

%tata(T,P,N,K):- serviceTeacher(T,P,N),K= #count { S:assigned(S,T),part_sessions(P,S)} ,K == N.


%&sum{1+xslot(S)-xSlot(S)} = N :- serviceTeacher(T,P,N),assigned(S,T),part_sessions(P,S).
%:-session(1),&sum{xslot(1)} !=1 .
%:-session(11),&sum{xslot(11)} !=2 .
%same_slot(1,11).
%sequenced_ch(1,11,1).
%assss(S,xslot(S),T,R):- assigned(S,T,R).
%assigned(11,"teacher1").

%#show serviceTeacher/3.
#show assigned/3.
%#show session_group/2.
%#show part_sessions/2.
%#show toto/4.
%#show session/1.
&show {xslot(X):session(X)}.
