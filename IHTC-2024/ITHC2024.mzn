int : nr_days;
int : skill_lvls; 
int : nr_shifts_days;
int : nr_age_of_group;
 
%array[int] of int : weigths;
 
int : nr_occupants;
int : nr_patients;
int : nr_nurses;
int : nr_surgeons;
int : nr_operating_theater;
int : nr_rooms;

int : nr_nurses_all;

 %%%%%%%%%%%%%%%%
  array[int] of string : shifts_days_name;
  array[int] of string : age_groups_name;
 %%%%%%%%%%%%%%%%
 set of int : OCCUPANTS = 1..nr_occupants;
 set of int : PATIENTS = 1..nr_patients;
 set of int : NURSES = 1..nr_nurses;
 set of int : SURGEONS = 1..nr_surgeons;
 set of int : OPERATING_THEATERS = 1..nr_operating_theater;
 set of int : ROOMS = 1..nr_rooms;
 set of int : AGES = 0..nr_age_of_group;
 set of int : SHIFTS = 1..nr_shifts_days;
 
 set of int : NURSES_ALL = 1..nr_nurses_all;
 set of int : DAYS = 1..nr_days;

 %%%%%%%%%%%%%%%%
 
 
 array[PATIENTS] of string :patient_name;
 array[PATIENTS] of int :patient_gender;
 array[PATIENTS] of AGES :patient_age;
 array[PATIENTS] of int :patient_mandatory;
 array[PATIENTS] of int :patient_duration;
 array[PATIENTS] of int :patient_duration_surgeon;
 
 
 array[PATIENTS] of DAYS :patient_day_release;
 array[PATIENTS] of int  :patient_due_day;
 array[PATIENTS] of int :patient_due_day_surgeon;
 array[PATIENTS] of SURGEONS :patient_surgeon;
 array[int] of int :patient_workload;
 array[int] of int :patient_skill_needs;
 array[PATIENTS] of int :patient_position;
 array[PATIENTS] of set of ROOMS :patient_forbidden_room;
 
 
 
 %%%
 
 array[OCCUPANTS] of string :occupant_name;
 array[OCCUPANTS] of int :occupant_gender;
 array[OCCUPANTS] of int :occupant_age;
 array[OCCUPANTS] of int :occupant_duration;
 array[int] of int :occupant_workload; 
 array[OCCUPANTS] of ROOMS :occupant_room; 
 array[OCCUPANTS] of int :occupant_workload_len;
 array[OCCUPANTS] of int :occupant_workload_sum;
 
%%%
array[NURSES] of string: nurse_name;
array[NURSES] of int: nurse_skill;
array[int,int] of int : nurse_working_shift;
array[NURSES] of DAYS: nurse_working_day;
array[NURSES] of int: nurse_working_position;

%%%
array[SURGEONS] of string : surgeon_name;
array[SURGEONS,DAYS] of int : surgeon_max_workload;
%%%
array[OPERATING_THEATERS] of string : ot_name;
array[OPERATING_THEATERS,DAYS] of int : ot_available;
%%%
array[ROOMS] of string : room_name;
array[ROOMS] of int : room_capacity;


%%%%%%%%%%%%%%
array[PATIENTS] of var DAYS: x_day;
array[PATIENTS] of var ROOMS: x_room;
array[PATIENTS] of var OPERATING_THEATERS: x_ot;

array[NURSES_ALL] of var DAYS: x_nurse_days;
array[NURSES_ALL] of var ROOMS: x_nurse_rooms;

constraint 1!=0;

solve satisfy;

output[show(x_day)++"\n"++show(x_room)++"\n"++show(x_ot)++"\n"++show(x_nurse_days)++"\n"++show(x_nurse_rooms)]