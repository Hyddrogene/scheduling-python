weeks(12).
day(5).
slot_per_day(8).

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
%id,nr_sessions,length, day,week,nr rooms,nr teacehers
part("Web-Development-Lec",12,1,5,12,1,1).
class("Web-Development-Lec-1",80).

part("Web-Development-Pra",8,1,5,12,1,1).
class("Web-Development-Pra-1",25).
class("Web-Development-Pra-2",25).
class("Web-Development-Pra-3",25).

teacher("DL").
teacher("AJ").
teacher("ML").

course_part("Web-Development","Web-Development-Lec").
course_part("Web-Development","Web-Development-Pra").

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
