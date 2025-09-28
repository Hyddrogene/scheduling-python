weeks(12). days(5). slot_per_day(1440).
grid(480,90,7).courses(1). parts(2). 
classes(3). sessions(28).
room("Salle-1",40). room("Salle-2",20). 
room("Salle-3",20). course("math").
teacher("teacher1").teacher("teacher2").
%%COURS
part("math-CM",12,120,5,12,1,1).
class("math-CM-1",80). part("math-TP",8,120,5,12,1,1).
class("math-TP-1",25).class("math-TP-2",25).
course_part("math","math-CM").
course_part("math","math-TP").
part_class("math-CM","math-CM-1").
part_class("math-TP","math-TP-1").
part_class("math-TP","math-TP-2").
class_sessions("math-CM-1",1..12).
class_sessions("math-TP-1",13..20).
class_sessions("math-TP-2",21..28).
%%RESSOURCE
part_teacher("math-CM","teacher-1").
part_teacher("math-TP",("teacher-1";"teacher-2")).
part_room("math-CM","salle-1").
part_room("math-TP",("salle-1";"salle-2";"salle-3")).
%%GRILLE HORAIRE
part_days("math-CM",1..5). part_weeks("math-CM",1..12).
part_days("math-TP",1..5). part_weeks("math-TP",1..12).
part_slots(("math-CM";"math-TP"),
(480;570;660;750;840;930)).
part_grids("cours-1-pTP",1,1,6).
part_grids("cours-1-pTP",1,1,6).
part_slot(P,S) :- part_days(P,D),part_weeks(P,W),
part_slots(P,SP),days(DPD),slot_per_day(SPD),
S = (W-1)*DPD*SPD + (D-1) * SPD  + SP.
%%GROUPES
group("group-1",20).
group("group-2",20).
class_group("math-CM-1",("group-1";"group-2")).
class_group("math-TP-1","group-1").
class_group("math-TP-2","group-2").
%%CONTRAINTEs
sequenced(3,(13;21;29)).
periodic(S1,S2,7200) :- part(P,_,_,_,_,_,_),part_class(P,C),
class_sessions(C,S1),class_sessions(C,S2),S1+1 = S2.
disjunctive_room(S1,S2) :- part(P,_,_,_,_,_,_), 
part_class(P,C), class_sessions(C,S1),
class_sessions(C,S2),S1+1 = S2.
disjunctive_teacher(S1,S2) :- part(P,_,_,_,_,_,_), 
part_class(P,C), class_sessions(C,S1),
class_sessions(C,S2),S1+1 = S2.

grid_exist(1).
session(1..28). 