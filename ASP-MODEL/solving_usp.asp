%set of session
session(1..S):-sessions(S).
%set of week
w(1..W) :- weeks(W).
%set of day
d(1..D) :- days(D).
%set of slot perday
spd(1..P) :- slot_per_day(P).
%create possible date
pg :- grid_exist(1).
n_pg :-not grid_exist(_).
%part_slot(P,S) :- n_pg,part_days(P,D),part_weeks(P,W),part_slots(P,SP),days(DPD),slot_per_day(SPD),S = (W-1)*DPD*SPD + (D-1) * SPD  + SP.

part_slot(P,S) :- pg,part(P,_,_,_,_,_,_),part_days(P,D),part_weeks(P,W),part_slots(P,SP),days(DPD),grid(_,_,SPD),S = (W-1)*DPD*SPD + (D-1) * SPD  + SP.
%part_days(P,D),part_weeks(P,W),grid(_,_,X),part_slots(P,SP),days(DPD),S = (W-1)*DPD*X + (D-1) + SP.

%parts nr_sessions by part
part_nrsessions(P,N) :- part(P,N,_,_,_,_,_).

%%%% session_duration
session_duration(S,D) :- pg, session(S),session_part(S,P), part_grids(P,_,D,_).
%session_duration(S,D) :- n_pg, session(S),session_part(S,P), part(P,_,D,_,_,_,_).

%horizon :
%slots(1..H) :-weeks(W),days(DPD),slot_per_day(SPD), H = W*DPD*SPD.

nr_slots(H) :- weeks(W),days(DPD),grid(_,_,SPD), H = W*DPD*SPD.

%slots(S):-n_pg,part_slot(_,S).
slots(1..H):-pg,nr_slots(H).

%session_group
session_group(S,G) :- class_sessions(C,S),class_group(C,G).


session_class(S,C) :-  class_sessions(C,S).

class_part(C,P) :- part_class(P,C). 

session_part(S,P) :- session(S),session_class(S,C),class_part(C,P).
%session_slot(S,SL) :- session(S),session_part(S,P),part_slot(P,SL).
session_teacher(S,T) :- session(S),session_part(S,P),part_teacher(P,T).
session_room(S,R) :- session(S),session_part(S,P),part_room(P,R).

%Assign time to S
%1{assigned(S,SL):slots(SL)}1 :- session(S).

%session_slot(S,SL) :- session(S),session_part(S,P),part_slot(P,SL).
%1{assigned(S,SL):session_slot(S,SL)}1 :- session(S).
1{assigned(S,SL):session_part(S,P),part_slot(P,SL)}1:- session(S).
:-not {assigned(S,SL):session(S)}1,session_part(S,P),part_slots(P,SL),slots(SL).

%Assign T to session

1{assigned(S,SL,T):session_teacher(S,T)}1 :- assigned(S,SL).
%1{assigned(S,SL,T):teacher(T)}1 :- assigned(S,SL).


%Assign R to session
1{assigned(S,SL,T,R):session_room(S,R)}1 :- assigned(S,SL,T).

%Assigned group

%ensure disjunctive teacher
%:- not {assigned(S,SL) : session(S) } 1, session_part(S,P),part_slot(P,SL).
:- not {assigned(S,SL,T) : session(S) } 1, session_part(S,P),part_slot(P,SL),part_teacher(P,T).
:- not {assigned(S,SL,T,R) : session(S) } 1,session_part(S,P),part_slot(P,SL),part_teacher(P,T),part_room(P,R). 

%%%%% periodic
:- periodic(S1,S2,N),assigned(S1,SL1),assigned(S2,SL2),SL2 != SL1+N.
%%%%% sequenced
%:- sequenced(S1,S2),assigned(S1,SL1),session_part(S1,P),part(P,_,N,_,_,_,_),assigned(S2,SL2),SL1+N> SL2.
:- sequenced(S1,S2),assigned(S1,SL1),session_part(S1,P),part_grids(P,_,N,_),assigned(S2,SL2),SL1+N > SL2.

sequenced(S1,S2):-part(P,_,_,_,_,_,_),part_class(P,C),class_sessions(C,S1),class_sessions(C,S2),S1+1 = S2.


%%%%% forbidden_period

%%%%% allowed periodic

%%%%% same_slot

:-same_slot(S1,S2),assigned(S1,SL1),assigned(S2,SL2),SL1 != SL2.

%%%%% same_week

%:-same_slot(S1,S2),assigned(S1,SL1),assigned(S2,SL2),SL1 / != SL2.

%%%%% same_weekDay

%%%%% same_weeklySlot

%%%%% same_teachers
 
:-same_teachers(S1,S2),assigned(S1,_,T1),assigned(S2,_,T2), T1 != T2.

%%%%% same_rooms

:-same_rooms(S1,S2),assigned(S1,_,_,R1),assigned(S2,_,_,R2), R1 != R2.

%%%%% assign_rooms

%:- assign_rooms(S1,R1),assigned(S1,_,_,R2), R1 != R2.

%%%%% assign_teachers

%:- assign_teachers(S1,T1),assigned(S1,_,T2), T1 != T2;.

%%%%% disjunctive_teacher
:-disjunctive_teacher(S1,S2), assigned(S1,SL1,T),assigned(S2,SL2,T),SL1 == SL2, not disjunctive_group(S1,S2), not sequenced(S1,S2).
%:-disjunctive_teacher(S1,S2), assigned(S1,SL1,T),assigned(S2,SL2,T),SL1 == SL2, not disjunctive_group(S1,S2).


%%%%% disjunctive_room 
%:-disjunctive_room(S1,S2), assigned(S1,SL1,_,R),assigned(S2,SL2,_,R),SL1 == SL2, not disjunctive_group(S1,S2).
:-disjunctive_room(S1,S2), assigned(S1,SL1,_,R),assigned(S2,SL2,_,R),SL1 == SL2, not disjunctive_group(S1,S2), not sequenced(S1,S2).
:-disjunctive_group(S1,S2),session_group(S1,G),session_group(S2,G), assigned(S1,SL1),assigned(S2,SL2),SL1 == SL2, not sequenced(S1,S2).
%:-disjunctive_group(S1,S2),session_group(S1,G),session_group(S2,G), assigned(S1,SL1),assigned(S2,SL2),SL1 == SL2.

%%%%% disjunctive_group

%%%%% service_teacher
service_teacher("teacher1","cours-1-pCM",5).
service_teacher("teacher2","cours-1-pCM",5).

%:- service_teacher(T,P,N), K = { assigned(S,_,T) : part_sessions(P,S)}, K!= N.
:- service_teacher(T,P,N), #count { S,T :assigned(S,_,T),part_sessions(P,S)} != N.

%%%%% size_multiroom

%%%%% Implicite sequenced session

%%%% workload

%assigned(1,480).


%:- periodic(S1,S2,_), not ponct(S1,S2,_,_).
%#show assigned/2.
%#show assigned/3.
#show assigned/4.
%#show slots/1.
%#show ponct/4.
%#show class_sessions/2. 
%#show part_slot/2.
%#show nr_slots/1.
