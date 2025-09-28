include "disjunctive.mzn";
int :nr_days_per_weeks = 5;
int :nr_weeks = 12;
int : nr_slots_per_day = 1440;
int : nr_slots = nr_days_per_weeks * nr_weeks * nr_slots_per_day;

set of int : SESSIONS = 1..200;
set of int : SLOTS = 1..nr_slots;
set of int : CLASSES = 1..20;

array[CLASSES] of set of SESSIONS : class_sessions = [{(i-1)*10+s | s in 1..10} | i in CLASSES];

array[SESSIONS] of var SLOTS : x_slot;
array[SESSIONS] of var SLOTS : y_slot;

constraint forall(c in CLASSES)(
  disjunctive([x_slot[s] | s in class_sessions[c]],[1 | i in class_sessions[c]])
);

constraint forall(i in class_sessions[2])( x_slot[i] -> (x_slot[i]+3)  mod nr_days_per_weeks);
solve satisfy;

output[show(x_slot)]