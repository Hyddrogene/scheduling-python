c(C):- course(C).
p(P):- part(P,_,_,_,_,_,_).
cl(C):- class(C,_).
%Room
r(R) :- room(R,_).
%Teacher
t(T):- teacher(T).
%Group
g(G):- class_group(_,G).
%Student
%u(U) :- student(U).
%set of session
session(1..S):-sessions(S).
%set of week
w(1..W) :- weeks(W).
%set of day
d(1..D) :- days(D).
%set of slot perday
spd(1..P) :- slot_per_day(P).
%parts nr_sessions by part
part_nrsessions(P,N) :- part(P,N,_,_,_,_,_).

%horizon :
%slots(1..H) :-weeks(W),days(DPD),slot_per_day(SPD), H = W*DPD*SPD.
slots(S):-part_slot(_,S).
%session_class
%O{class_sessions(C,S):session(S)}O :- cl(C),class_part(C,P),part_nrsessions(P,O).
%:- class_sessions(C1,S),class_sessions(C2,S),C1 != C2.


session_class(S,C) :-  class_sessions(C,S).

class_part(C,P) :- part_class(P,C). 

%N { assigned(S,W,D,H,R,T,G):d(D),spd(P)}N
%1{assigned(S,W,D,H,R,T,G):w(W),d(D),spd(P)}1 :- session(S),class_session(C,S),part_class(P,C),part_teacher(T),part_room(P,R),class_group(C,G).

session_part(S,P) :- session(S),session_class(S,C),class_part(C,P).
session_slot(S,SL) :- session(S),session_part(S,P),part_slot(P,SL).
%Assign time to S



%{assigned(S,SL):slots(SL)} = 1 :- session(S).

{assigned(S,SL):session_part(S,P),part_slot(P,SL)} = 1 :- session(S).




%:- assigned(S1,SL1),assigned(S2,SL2),SL1 != SL2,S1=S2.
%:- assigned(S,SL),session_part(S,P),not part_slot(P,SL).
%assigned(S,SL) :- session(S),session_part(S,P),part_slot(P,SL).
%:- assigned(S,SL),session_part(S,P),not part_slot(P,SL).
%:- not{assigned(S,SL):session(S)}1,session_part(S,P),part_slots(P,SL),slots(SL).
%:- assigned(S,SL2),assigned(S,SL1),SL1 != SL2.

%
%
%Assign T to session
%1{assigned(S,SL,T):t(T)}1 :- assigned(S,SL),session(S),session_part(S,P),part_teacher(P,T).

%
%
%Assign R to session
%1{assigned(S,SL,T,R):r(R)}1 :- assigned(S,SL,T),session(S),session_part(S,P),part_room(P,R).

%
%
%Assigned group
%assigned(S,SL,T,R,G) :- assigned(S,SL,T,R),class_sessions(S,C),class_group(C,G).

%ensure disjunctive teacher
%:- not {assigned(S,SL) : session(S) } 1, session_part(S,P),part_slot(P,SL).
%:- not {assigned(S,SL,T) : session(S) } 1, session_part(S,P),part_slot(P,SL),part_teacher(P,T).
%:- not {assigned(S,SL,T,R) : session(S) } 1,session_part(S,P),part_slot(P,SL),part_teacher(P,T),part_room(P,R). 
%ponct(S1,S2,SL1,SL2)

%%%%% periodic
%:- periodic(S1,S2,N),assigned(S1,SL1),assigned(S2,SL2),SL2 != SL1+N.
%%%%% sequenced
%:- sequenced(S1,S2),assigned(S1,SL1),session_part(S1,P),part(P,_,N,_,_,_,_),assigned(S2,SL2),SL1+N> SL2.

%%%%% forbidden_period

%%%%% allowed periodic

%%%%% same_slot

%:-same_slot(S1,S2),assigned(S1,SL1),assigned(S2,SL2),SL1 != SL2.

%%%%% same_week

:-same_slot(S1,S2),assigned(S1,SL1),assigned(S2,SL2),SL1 / != SL2.

%%%%% same_weekDay

%%%%% same_weeklySlot

%%%%% same_teachers
 
%:-same_teachers(S1,S2),assigned(S1,_,T1),assigned(S2,_,T2), T1 != T2.

%%%%% same_rooms

%:-same_rooms(S1,S2),assigned(S1,_,_,R1),assigned(S2,_,_,R2), R1 != R2.

%%%%% assign_rooms

%:- assign_rooms(S1,R1),assigned(S1,_,_,R2), R1 != R2.

%%%%% assign_teachers

%:- assign_teachers(S1,T1),assigned(S1,_,T2), T1 != T2;.

%%%%% disjunctive_teacher
         
%%%%% disjunctive_room 
       
%%%%% disjunctive_group

%%%%% service_teacher

%%%%% size_multiroom

%%%%% Implicite sequenced session

assigned(1,480).


%:- periodic(S1,S2,_), not ponct(S1,S2,_,_).
%#show assigned/2.
#show session_slot/2.
%#show ponct/4.
%#show class_sessions/2.