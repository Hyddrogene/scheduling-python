





c(C):- course(C,_,_,_,_,_).
p(P):- part(P).
cl(C):- class(C).
%Room
r(R) :- room(R,_).
%Teacher
t(T):-Teacher(T).
%Group
g(G):- class_group(_,G).
%Student
u(U) :- student(U).
%set of session
session(1..S):-sessions(S).
%set of week
w(1..W) :- weeks(W).
%set of day
d(1..D) :- days(D).
%set of slot perday
spd(1..P) :- slot_per_day(P)

%session_class


%N { assigned(S,W,D,H,R,T,G):d(D),spd(P)}N
%1{assigned(S,W,D,H,R,T,G):w(W),d(D),spd(P)}1 :- session(S),class_session(C,S),part_class(P,C),part_teacher(T),part_room(P,R),class_group(C,G).


%Assign time to S
1{assigned(S,W,D,H):w(W),d(D),spd(P)}1 :- session(S).


%Assign T to session
1{assigned(S,W,D,H,T):t(T)}1 :- assigned(S,W,D,H),session(S),class_session(C,S),part_class(P,C),part_teacher(T).

%Assign R to session
1{assigned(S,W,D,H,T,R):r(R)}1 :- assigned(S,W,D,H,T),session(S),class_session(C,S),part_class(P,C),part_room(R).

%Assigned group
assigned(S,W,D,H,T,R,G) :- assigned(S,W,D,H,T,R),class_session(C,S),class_group(C,G).

%ensure disjunctive teacher
:- not {assigned(S,W,D,H,T) : session(S) } 1, t(T),w(W),d(D), spd(H). 
