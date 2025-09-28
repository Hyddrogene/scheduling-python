pref(X,Y,T) :- pref(X,Y,Z), pref(X,Z,T).
room(X,Y) :- person(X), person(Y), X!=Y, not room(X,Z1) : pref(X,Z1,Y); not room(Y,Z2) : pref(Y,Z2,X).
room(Y,X) :- room(X,Y).

person(X) :- pref(X,_,_).

%#show room/2.
hide(1).
#show hide/1.
