room(1).
room(2).

cake(minou).
{assigned(X,M):room(X)} :- cake(M).
%assigned(X,M) :- cake(M),room(X).

%:- cake(M), not assignedP(M).
%assignedP(X) :- assigned(X,V),cake(X),room(V).

caca(1).

:- caca(N), N > #count{M,X:assigned(X,M)} .

:- cake(M), assigned(X1,M), assigned(X2,M), X1!=X2.