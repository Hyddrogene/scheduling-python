s(1..4).
a.
{p(N):s(N)}=1.
{q(N):s(N)}=1.

&diff {p(N)-q(N1)} >= 2 :- a,p(N),q(N1).
#show p/1.
#show q/1.