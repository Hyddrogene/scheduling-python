pg :- grid_exist(1).
n_pg :-not grid_exist(1).


session_duration(S,D) :- pg, session(S),session_part(S,P), part_grids(P,_,D,_).

session_group(S,G) :- class_sessions(C,S),class_group(C,G).

session_class(S,C) :-  class_sessions(C,S).

class_part(C,P) :- part_class(P,C). 

session_part(S,P) :- session(S),session_class(S,C),class_part(C,P).
session_teacher(S,T) :- session(S),session_part(S,P),part_teacher(P,T).
session_room(S,R) :- session(S),session_part(S,P),part_room(P,R).

{assigned(S,SL):session_part(S,P),part_slot(P,SL)}:- session(S).
{assigned(S,SL,T):session_teacher(S,T)} :- assigned(S,SL),session(S).
%{assignedr(S,SL,R):session_room(S,R)} :- assigned(S,SL).
{assignedr(S,SL,R):session_room(S,R)} :- assigned(S,SL),session(S).


:- periodic(S1,S2,N),assigned(S1,SL1),assigned(S2,SL2),SL2 != SL1+N.
:- sequenced(S1,S2),assigned(S1,SL1),session_part(S1,P),part_grids(P,_,N,_),assigned(S2,SL2),SL1+N > SL2.
sequenced(S1,S2):-part(P,_,_,_,_,_,_),part_class(P,C),class_sessions(C,S1),class_sessions(C,S2),S1+1 = S2.
:-same_slot(S1,S2),assigned(S1,SL1),assigned(S2,SL2),SL1 != SL2.
:-same_teachers(S1,S2),assigned(S1,_,T1),assigned(S2,_,T2), T1 != T2.
:-same_rooms(S1,S2),assignedr(S1,_,R1),assignedr(S2,_,R2), R1 != R2.

:- assign_rooms(S1,R1),assignedr(S1,_,R2), R1 != R2.
:- assign_teachers(S1,T1),assigned(S1,_,T2), T1 != T2.
:-disjunctive_teacher(S1,S2), assigned(S1,SL1,T),assigned(S2,SL2,T),SL1 == SL2, not disjunctive_group(S1,S2), not sequenced(S1,S2).
:-disjunctive_room(S1,S2), assignedr(S1,SL1,R),assignedr(S2,SL2,R),SL1 == SL2, not disjunctive_group(S1,S2), not sequenced(S1,S2).

:-disjunctive_group(S1,S2),session_group(S1,G),session_group(S2,G), assigned(S1,SL1),assigned(S2,SL2),SL1 == SL2, not sequenced(S1,S2).
:- serviceTeacher(T,P,N), #count { S,T :assigned(S,_,T),part_sessions(P,S)} != N.

:- assignedr(S,SL,R),room(R,C1),session_class(S,C),class_headcount(C,N), N > C1.

%:- session(S),slot(SL),  1 = #count { S,SL :assigned(S,SL)}.

:-  sessions(N),#count { S,R :assignedr(S,SL,R),session(S)} != N.

:- assignedr(S,SL1,T), assigned(S,SL2,R), SL1 != SL2.
%assignedr(31,"room1").

#show assignedr/3. 
#show assigned/3. 
%#show assigned/2.
