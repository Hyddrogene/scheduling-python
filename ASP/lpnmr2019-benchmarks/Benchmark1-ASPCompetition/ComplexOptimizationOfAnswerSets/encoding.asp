eleb(P) :- rule(_,pos(P)).

nhold(conjunction(S)) :- eleb(conjunction(S)), not hold(P), set(S,pos(P)).
nhold(conjunction(S)) :- eleb(conjunction(S)),     hold(P), set(S,neg(P)).
 hold(conjunction(S)) :- eleb(conjunction(S)), not nhold(conjunction(S)).

hold(atom(A))                     :- rule(pos(atom(A)),   pos(B)), hold(B).
                                  :- rule(pos(false),     pos(B)), hold(B).
{ hold(P) : wlist(S,_,pos(P),_) } :- rule(pos(sum(_,S,_)),pos(B)), hold(B).

elem(E) :- eleb(E).
elem(E) :- rule(pos(E),_).
elem(P) :- rule(pos(sum(_,S,_)),_), wlist(S,_,pos(P),_).
elem(P) :- minimize(J,S),           wlist(S,_,pos(P),W).

supp(atom(A),B) :- rule(pos(atom(A)),   pos(B)).
supp(atom(A),B) :- rule(pos(sum(_,S,_)),pos(B)), wlist(S,_,pos(atom(A)),_).

supp(atom(A))   :- supp(atom(A),B).

set(S) :- set(S,E).
fact(atom(A))   :- rule(pos(atom(A)),pos(conjunction(S))), not set(S).

true(atom(A))                 :- fact(atom(A)).
true(atom(A)) | fail(atom(A)) :- supp(atom(A)), not fact(atom(A)).
                fail(atom(A)) :- elem(atom(A)), not supp(atom(A)).

fail(false).

sett(S,0,P)    :- set(S,P).
sett(S,N+1,P2) :- sett(S,N,P1), sett(S,N,P2), P1 < P2.

setn(S,N,P) :- sett(S,N,P), not sett(S,N+1,P).
setn(S,N)  :- setn(S,N,_).

true(conjunction(S),N+1) :- elem(conjunction(S)), setn(S,N), not setn(S,N+1).
true(conjunction(S),N)   :- elem(conjunction(S)), true(P), setn(S,N,pos(P)), true(conjunction(S),N+1).
true(conjunction(S),N)   :- elem(conjunction(S)), fail(P), setn(S,N,neg(P)), true(conjunction(S),N+1).

true(conjunction(S)) :- true(conjunction(S),0).
fail(conjunction(S)) :- elem(conjunction(S)), set(S,pos(P)), fail(P).
fail(conjunction(S)) :- elem(conjunction(S)), set(S,neg(N)), true(N).

suppt(S,0,P)    :- supp(S,P).
suppt(S,N+1,P2) :- suppt(S,N,P1), suppt(S,N,P2), P1 < P2.

suppn(S,N,P) :- suppt(S,N,P), not suppt(S,N+1,P).
suppn(S,N)  :- suppn(S,N,_).

suppf(S,N+1) :- suppn(S,N), not suppn(S,N+1).
suppf(S,N)   :- fail(P), suppn(S,N,P), suppf(S,N+1).

bot :- true(atom(A)), suppf(atom(A),0).
bot :- rule(pos(H),pos(B)), true(B), fail(H).

true(atom(A)) :- supp(atom(A)), not fact(atom(A)), bot.
fail(atom(A)) :- supp(atom(A)), not fact(atom(A)), bot.

target(P,N) :- minimize(_,S), wlist(S,N,pos(P),_).
target(N) :- target(P,N).

equal(N+1) :- target(N), not target(N+1).
equal(N) :- target(P,N), true(P), equal(N+1).
equal(N) :- target(P,N), not hold(P), equal(N+1).

bot :- equal(0).
bot :- target(P,_), true(P), not hold(P).

:- not bot.
