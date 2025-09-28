a(X) : X=1..n.
a(X) :- a(Y), X=Y+1, X <= n.
a(1) :- a(n).
