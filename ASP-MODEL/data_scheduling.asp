weeks(12).
days(5).
slot_per_day(1440).

courses(1).
parts(2).
classes(4).
sessions(36).

room("AMPHI-A",90).
room("AMPHI-B",286).
room("H001",20).
room("H002",42).
room("H003",38).


course("Web-Development").
%id,nr_sessions,length, day,week,nr rooms,nr teachers min, nrteachers max
part("Web-Development-Lec",12,120,5,12,1,1).
class("Web-Development-Lec-1",80).

part("Web-Development-Pra",8,120,5,12,1,1).
class("Web-Development-Pra-1",25).
class("Web-Development-Pra-2",25).
class("Web-Development-Pra-3",25).

teacher("DL").
teacher("AJ").
teacher("ML").

%%COURSE

course_part("Web-Development","Web-Development-Lec").
course_part("Web-Development","Web-Development-Pra").

%%PART
part_class("Web-Development-Lec","Web-Development-Lec-1").

part_class("Web-Development-Pra","Web-Development-Pra-1").
part_class("Web-Development-Pra","Web-Development-Pra-2").
part_class("Web-Development-Pra","Web-Development-Pra-3").

part_teacher("Web-Development-Lec","DL").
part_teacher("Web-Development-Pra","ML").
part_teacher("Web-Development-Pra","ML").
part_teacher("Web-Development-Pra","AJ").

part_room("Web-Development-Lec","AMPHI-A").
part_room("Web-Development-Lec","AMPHI-B").

part_room("Web-Development-Pra","H001").
part_room("Web-Development-Pra","H002").
part_room("Web-Development-Pra","H003").

part_days("Web-Development-Lec",1..5).
part_days("Web-Development-Pra",1..5).

part_weeks("Web-Development-Pra",1..12).
part_weeks("Web-Development-Pra",1..12).

part_slots("Web-Development-Lec",(480;570;660;750;840;930;1020;1110)).
part_slots("Web-Development-Pra",(480;570;660;750;840;930;1020;1110)).

part_slot(P,S) :- part_days(P,D),part_weeks(P,W),part_slots(P,SP),days(DPD),slot_per_day(SPD),S = (W-1)*DPD*SPD + (D-1) * SPD  + SP.

%%CLASS


class_sessions("Web-Development-Lec-1",1..12).
class_sessions("Web-Development-Pra-1",13..20).
class_sessions("Web-Development-Pra-2",21..28).
class_sessions("Web-Development-Pra-3",29..36).



%SOLUTION : GROUP
group("1-L3-info",20).
group("2-L3-info",10).
group("3-L3-info",10).
group("4-L3-info",20).


class_group("Web-Development-Pra-1","1-L3-info").
class_group("Web-Development-Pra-2","2-L3-info").
class_group("Web-Development-Pra-2","3-L3-info").
class_group("Web-Development-Pra-3","4-L3-info").

class_group("Web-Development-Lec-1",("1-L3-info";"2-L3-info";"13-L3-info";"4-L3-info")).

%#show part_slot/2.


%aff("data").
sequenced(3,(13;21;29)).

sequenced(S1,S2) :- part(P,_,_,_,_,_,_),part_class(P,C),class_sessions(C,S1),class_sessions(C,S2),S1+1 = S2.
periodic(S1,S2,7200) :- part(P,_,_,_,_,_,_),part_class(P,C),class_sessions(C,S1),class_sessions(C,S2),S1+1 = S2.
disjunctive_room(S1,S2) :- part(P,_,_,_,_,_,_), part_class(P,C), class_sessions(C,S1),class_sessions(C,S2),S1+1 = S2.
disjunctive_teacher(S1,S2) :- part(P,_,_,_,_,_,_), part_class(P,C), class_sessions(C,S1),class_sessions(C,S2),S1+1 = S2.
%#show sequenced/2. 
%#show periodic/3.