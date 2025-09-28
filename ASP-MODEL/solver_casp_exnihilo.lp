#const begin = 1.
#const end = 2.

%class(math1;math2;computerScience;physics).
class(math1;math2;physics).
rooms(1..2).


&dom{begin..end} = x(C) :- class(C).
1{ room(C,S) : rooms(S) }1 :- class(C).

% two classes cannot take place in the same room

:- room(C1,S), room(C2,S), C1 != C2, &sum{x(C2)- x(C1)} = 0.

% maths1 happens before maths2

&sum{x(math1) - x(math2)} < 0. 

% physics ne peut pas avoir lieu en première heure

&sum{ x(physics)}>1.

% physics must be in room 1

:- not room(physics,1).

&show { x/1 }.
#show room/2.