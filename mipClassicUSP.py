from parserUSP import instanceUSP
from mip import Model, xsum, maximize, BINARY, INTEGER, OptimizationStatus



class mipClassicVars:
    def __init__(self, instanceUTP, solutionFile):
        self.usp  = instanceUTP
        self.solutionFile = solutionFile
        self.model        = Model("Scheduling")
        #self.initVariables()
        self.initVars()
        self.constraintBasis()

    def initVars(self) :
        nr_sessions = self.usp.nr_sessions 
        nr_week = self.usp.nr_weeks
        nr_days = self.usp.nr_days_per_week
        nr_dailySlot = self.usp.grid[2]

        self.x_slot = []
        j = 0
        for i in range(0,nr_sessions) :
            p = self.usp.session_part[i]-1
            nr_week1 = max(self.usp.part_weeks[p])
            nr_abstract_slots = (nr_week1 * nr_days * nr_dailySlot)
            tmp = []
            for r in range(0,len(self.usp.part_rooms[p])):
                for t in range(0,len(self.usp.part_teachers[p])):
                    for i in range(0,nr_abstract_slots):
                        tmp.append(self.model.add_var(var_type=BINARY))
                        j+=1
            self.x_slot.append(tmp)
           
        print("Vars x_slot = ",j)
        print("Total = ",(j))

    def cardinal_rooms(self):
        for s in range(0,self.usp.nr_sessions):
            self.model += xsum(self.x_slot[s]) == 1

    def sequenced(self,constraint):
        for session1 in constraint.sessions[0]:
            p = self.usp.session_part[session1]-1
            for session2 in constraint.sessions[1] :
                p1 = self.usp.session_part[session1-1]-1
                p2 = self.usp.session_part[session2-1]-1
                nr_abstract_slots = self.usp.nr_days_per_week * self.usp.grid[2] * min(max(self.usp.part_weeks[p1]),max(self.usp.part_weeks[p2]))
                SLOT = range(0,nr_abstract_slots)
                if session1 != session2 :
                    #for t in SLOT:
                    self.model += xsum([t*self.x_slot[session1][t] for t in SLOT]) + self.usp.part_abstract_grid[p1][1] <=  xsum([t*self.x_slot[session2][t] for t in SLOT])
                    #self.model += xsum(self.x_slot[session1][t]) - xsum(self.x_slot[session2][t+1:SLOT[-1]])
                    #self.model += (self.x_slot[session1-1] +self.usp.part_abstract_grid[p][2]) <= self.x_slot[session2-1]

    def implicite_sequenced(self):
        for p in range(0,self.usp.nr_parts):
            for c in self.usp.part_classes[p]:
                print("classe ", c)
                for s1 in range(0,len(self.usp.class_sessions[c-1])):
                    for s2 in range(s1,len(self.usp.class_sessions[c-1])):
                        if s1 != s2 :
                            session1 = self.usp.class_sessions[c-1][s1] -1
                            session2 = self.usp.class_sessions[c-1][s2] -1
                            p1 = self.usp.session_part[session1-1]-1
                            p2 = self.usp.session_part[session2-1]-1
                            w1 = max(self.usp.part_weeks[p1])
                            w2 = max(self.usp.part_weeks[p2])
                            m  = min([w1,w2])
                            nr_abstract_slots = self.usp.nr_days_per_week * self.usp.grid[2] * m
                            SLOT = range(0,nr_abstract_slots)
                            max_slot = max(SLOT)
                            print("session1 ",session1," session2 ",session2)
                            print("len(SLOT) ",len(SLOT)," len(x_slot()) ",len(self.x_slot[session1]))
                            self.model += (xsum([(t+1)*self.x_slot[session1][t] for t in SLOT]) + self.usp.part_abstract_grid[p1][1]) <=  xsum([(t+1)*self.x_slot[session2][t] for t in SLOT])

                                   # self.model += self.x_slot[session2][t] - xsum(self.x_slot[session1][(t+1):SLOT[-1]]) == 0
                            #self.model += self.x_slot[self.usp.class_sessions[c-1][s2]-1] >= (self.x_slot[self.usp.class_sessions[c-1][s1]-1]+self.usp.part_abstract_grid[p][1])
                            self.usp.session_session_sequenced[session1][session2] = 1
        #exit(0)

    def periodic(self,constraint):#TODO
        nr_days = self.usp.nr_days_per_week
        nr_dailySlot = self.usp.grid[2]
        d_week = nr_days * nr_dailySlot
        for i in range(1,len(constraint.sessions[0])):

            self.model += self.x_slot[constraint.sessions[0][i]-1] + (i*d_week) == self.x_slot[constraint.sessions[0][0]-1]

    def same_slot(sel,constraint):
        for s1 in constraint.sessions[0]:
            for s2 in constraint.sessions[0]:
                if s1 != s2 :
                        session1 = s1 -1
                        session2 = s2 -1
                        p1 = self.usp.session_part[session1-1]-1
                        p2 = self.usp.session_part[session2-1]-1
                        nr_abstract_slots = self.usp.nr_days_per_week * self.usp.grid[2] * min(max(self.usp.part_weeks[p1]),max(self.usp.part_weeks[p2]))
                        nr_abstract_slots_max = self.usp.nr_days_per_week * self.usp.grid[2] * max(max(self.usp.part_weeks[p1]),max(self.usp.part_weeks[p2]))

                        SLOT = range(0,nr_abstract_slots)
                        for t in SLOT:
                            self.model += self.x_slot[session1][t] == self.x_slot[session2][t]
                        for t in range(nr_abstract_slots,nr_abstract_slots_max):
                            if len(self.x_slot[session1]) >t:
                                self.model += self.x_slot[session1][t] == 0
                            if len(self.x_slot[session2]) >t:
                                self.model += self.x_slot[session2][t] == 0

    def fusion_rooms(self,session1,session2):
        p1 = self.usp.session_part[session1-1]#getPositionTeacher
        p2 = self.usp.session_part[session2-1] 
        if p1 == p2 :
            return self.usp.part_rooms[p1-1]
        else :
            return [value for value in self.part_rooms[p1-1] if value in self.part_rooms[p2-1]]
        
    def fusion_teachers(self,session1,session2):
        p1 = self.usp.session_part[session1-1]#getPositionTeacher
        p2 = self.usp.session_part[session2-1] 
        if p1 == p2 :
            return self.usp.part_teachers[p1-1]
        else :
            return [value for value in self.part_teachers[p1-1] if value in self.part_teachers[p2-1]]

    def same_rooms(self,constraint) :
        for session1 in constraint.sessions[0]:
            for session2 in constraint.sessions[0]:
                for r in self.fusion_rooms(session1,session2):
                    if session1 != session2 :
                        self.model += self.x_room[session1-1][self.usp.getPositionRoom(session1-1, r)] == self.x_room[session2-1][self.usp.getPositionRoom(session2-1, r)]

    def same_teachers(self,constraint):
        for session1 in constraint.sessions[0]:
            for session2 in constraint.sessions[0]:
                for r in self.fusion_teachers(session1,session2):
                    if session1 != session2 :
                        self.model += self.x_teacher[session1-1][self.usp.getPositionTeacher(session1-1, r)] == self.x_teacher[session2-1][self.usp.getPositionTeacher(session2-1, r)]

    def cardinal_teachers(self):
        for s in range(0,len(self.x_teacher)):
            self.model += xsum(self.x_slot[s]) == 1

    def cardinal_slots(self):
        for s in range(0,len(self.x_slot)):
            self.model += xsum(self.x_slot[s]) == 1

    def service_teacher(self):
        nr_week = 0
        nr_days = self.usp.nr_days_per_week
        nr_dailySlot = self.usp.grid[2]
        for p in range(0,self.usp.nr_parts):
            for t in self.usp.part_teachers[p]:
                print("parts : ",self.usp.part_name[p]," nr teachers ",len(self.usp.part_teachers[p])," service ", sum(self.usp.part_teacher_sessions_count[p])," ssess * class ",(len(self.usp.part_classes[p]) * self.usp.part_nr_sessions[p]) )
                tmp = []
                p_t = self.usp.getPositionTeacher(self.usp.part_sessions[p][0]-1, t)
                nr_week = max(self.usp.part_weeks[p])
                nr_abstract_slots = (nr_week * nr_days * nr_dailySlot)
                p_sl = p_t * nr_abstract_slots
                self.model += xsum([xsum(self.x_slot[s-1][p_sl:(p_sl+nr_abstract_slots)]) for s in self.usp.part_sessions[p]]) == self.usp.part_teacher_sessions_count[p][t-1]
                #self.model += xsum([self.x_teacher[s-1][self.usp.getPositionTeacher(s-1, t)] for s in self.usp.part_sessions[p]]) == self.usp.part_teacher_sessions_count[p][t-1]

    def disjunctive_groupe(self):
        nr_sessions = self.usp.nr_sessions
        l = 0
        M = 3000
        y1 = []
        y2 = []
        for g in range(0,self.usp.nr_groups):
            for i in range(0,len(self.usp.group_sessions[g])):
                s1 = self.usp.group_sessions[g][i]-1
                for j in range(i,len(self.usp.group_sessions[g])):
                    s2 = self.usp.group_sessions[g][j]-1
                    if s1 != s2 :
                        if self.usp.session_session_sequenced[s1][s2] != 1 :
                            y1.append(self.model.add_var(var_type=BINARY))
                            y2.append(self.model.add_var(var_type=BINARY))

                            self.usp.session_session_sequenced[s1][s2] = y1[l]
                            self.usp.session_session_sequenced[s2][s1] = y2[l]

                            p1 = self.usp.session_part[s1]-1
                            p2 = self.usp.session_part[s2]-1
                            self.model += (y1[l] + y2[l]) == 1
                            nr_abstract_slots = self.usp.nr_days_per_week * self.usp.grid[2] * min(max(self.usp.part_weeks[p1]),max(self.usp.part_weeks[p2]))
                            nr_abstract_slots_max = self.usp.nr_days_per_week * self.usp.grid[2] * max(max(self.usp.part_weeks[p1]),max(self.usp.part_weeks[p2]))

                            SLOT = range(0,nr_abstract_slots)
                            self.model += (xsum([(t+1)*self.x_slot[s2][t] for t in SLOT]))  >=  xsum([(t+1)*self.x_slot[s1][t] for t in SLOT]) + (self.usp.part_abstract_grid[p1][1]+M)*y1[l]-M

                            self.model += (xsum([(t+1)*self.x_slot[s1][t] for t in SLOT]))  >=  xsum([(t+1)*self.x_slot[s2][t] for t in SLOT]) + (self.usp.part_abstract_grid[p2][1]+M)*y2[l]-M

                            l+=1

    def disjunctive_teacher(self):
        nr_sessions = self.usp.nr_sessions
        l = 0
        M = 3000
        y1 = []
        y2 = []
        self.y1y2 = []
        for t in range(1,self.usp.nr_teachers+1):
            if len(self.usp.teacher_parts[t-1])>0 :
                for i in range(0,len(self.usp.teacher_sessions[t-1])):
                    s1 = self.usp.teacher_sessions[t-1][i]-1
                    t1 = self.usp.getPositionTeacher(s1, t)
                    for j in range(i,len(self.usp.teacher_sessions[t-1])):
                        s2 = self.usp.teacher_sessions[t-1][j]-1
                        t2 = self.usp.getPositionTeacher(s2, t)
                        if s1!= s2 :
                            if self.usp.session_session_sequenced[s1][s2] == 0 :
                                #print("==0 s1 = ",s1,"s2 = ",s2)

                                self.y1y2.append(self.model.add_var(var_type=BINARY))
                                y1.append(self.model.add_var(var_type=BINARY))
                                y2.append(self.model.add_var(var_type=BINARY))


                                self.usp.session_session_sequenced[s1][s2] = y1[l]
                                self.usp.session_session_sequenced[s2][s1] = y2[l]

                                self.model += self.y1y2[l] <= self.x_teacher[s1][t1]
                                self.model += self.y1y2[l] <= self.x_teacher[s2][t2]
                                self.model += self.y1y2[l] >= (self.x_teacher[s1][t1]+self.x_teacher[s2][t2]-1)
                                self.model += self.y1y2[l] >= y1[l]
                                self.model += self.y1y2[l] >= y2[l]

                                self.model += self.y1y2[l] == (y1[l] + y2[l])
                                #self.model += (y1[l] + y2[l]) <= 1
                                p1 = self.usp.session_part[s1]-1
                                p2 = self.usp.session_part[s2]-1
                                #self.model += (y1[l] + y2[l]) == 1
                                nr_abstract_slots = self.usp.nr_days_per_week * self.usp.grid[2] * min(max(self.usp.part_weeks[p1]),max(self.usp.part_weeks[p2]))
                                nr_abstract_slots_max = self.usp.nr_days_per_week * self.usp.grid[2] * max(max(self.usp.part_weeks[p1]),max(self.usp.part_weeks[p2]))
                                SLOT = range(0,nr_abstract_slots)
                                self.model += (xsum([(t+1)*self.x_slot[s2][t] for t in SLOT]))  >=  xsum([(t+1)*self.x_slot[s1][t] for t in SLOT]) + (self.usp.part_abstract_grid[p1][1]+M)*y1[l]-M

                                self.model += (xsum([(t+1)*self.x_slot[s1][t] for t in SLOT]))  >=  xsum([(t+1)*self.x_slot[s2][t] for t in SLOT]) + (self.usp.part_abstract_grid[p2][1]+M)*y2[l]-M


                            elif self.usp.session_session_sequenced[s1][s2] == 1:
                                #print("==1  s1 = ",s1,"s2 = ",s2)
                                y1.append(0)
                                y2.append(0)
                                self.y1y2.append(-1)
                                pass
                            else :
                                #print("!=0  s1 = ",s1,"s2 = ",s2)
                                self.y1y2.append(self.model.add_var(var_type=BINARY))
                                #y1.append(self.model.add_var(var_type=BINARY))
                                y1.append(self.usp.session_session_sequenced[s1][s2])
                                y2.append(self.usp.session_session_sequenced[s2][s1])
                            l+=1

    def disjunctive_rooms(self):
        nr_sessions = self.usp.nr_sessions
        l = 0
        M = 3000
        y1 = []
        y2 = []
        self.r1r2 = []
        for t in range(1,self.usp.nr_rooms+1):
            if len(self.usp.room_parts[t-1])>0 :
                for i in range(0,len(self.usp.room_sessions[t-1])):
                    s1 = self.usp.room_sessions[t-1][i]-1
                    t1 = self.usp.getPositionRoom(s1, t)
                    for j in range(i,len(self.usp.room_sessions[t-1])):
                        s2 = self.usp.room_sessions[t-1][j]-1
                        t2 = self.usp.getPositionRoom(s2, t)
                        if s1!= s2 :
                            if self.usp.session_session_sequenced[s1][s2] == 0 :
                                #print("==0 s1 = ",s1,"s2 = ",s2)

                                self.r1r2.append(self.model.add_var(var_type=BINARY))
                                y1.append(self.model.add_var(var_type=BINARY))
                                y2.append(self.model.add_var(var_type=BINARY))


                                self.usp.session_session_sequenced[s1][s2] = y1[l]
                                self.usp.session_session_sequenced[s2][s1] = y2[l]

                                self.model += self.r1r2[l] <= self.x_room[s1][t1]
                                self.model += self.r1r2[l] <= self.x_room[s2][t2]
                                self.model += self.r1r2[l] >= (self.x_room[s1][t1]+self.x_room[s2][t2]-1)
                                self.model += self.r1r2[l] >= y1[l]
                                self.model += self.r1r2[l] >= y2[l]

                                self.model += self.r1r2[l] == (y1[l] + y2[l])
                                #self.model += (y1[l] + y2[l]) <= 1
                                p1 = self.usp.session_part[s1]-1
                                p2 = self.usp.session_part[s2]-1
                    
                                nr_abstract_slots = self.usp.nr_days_per_week * self.usp.grid[2] * min(max(self.usp.part_weeks[p1]),max(self.usp.part_weeks[p2]))
                                nr_abstract_slots_max = self.usp.nr_days_per_week * self.usp.grid[2] * max(max(self.usp.part_weeks[p1]),max(self.usp.part_weeks[p2]))
                                SLOT = range(0,nr_abstract_slots)
                                self.model += (xsum([(t+1)*self.x_slot[s1][t] for t in SLOT]))  >=  xsum([(t+1)*self.x_slot[s2][t] for t in SLOT]) + (self.usp.part_abstract_grid[p1][1]+M)*y1[l]-M

                                self.model += (xsum([(t+1)*self.x_slot[s2][t] for t in SLOT]))  >=  xsum([(t+1)*self.x_slot[s1][t] for t in SLOT]) + (self.usp.part_abstract_grid[p2][1]+M)*y2[l]-M

                            elif self.usp.session_session_sequenced[s1][s2] == 1:
                                #print("==1  s1 = ",s1,"s2 = ",s2)
                                y1.append(0)
                                y2.append(0)
                                self.r1r2.append(-1)
                                pass
                            else :
                                y1.append(0)
                                y2.append(0)
                                self.r1r2.append(-1)

                            l+=1


    def constraintBasis(self):
        self.cardinal_slots()
        self.service_teacher()
        #self.implicite_sequenced()
        #self.disjunctive_groupe()
        #self.disjunctive_teacher()
        #self.disjunctive_rooms()
        return
        for cons in self.usp.constraints:
            if cons.constraint == "sequenced" :
                self.sequenced(cons)
                pass
            elif cons.constraint == "periodic":
                #self.periodic(cons)
                pass
            elif cons.constraint == "sameSlot" or cons.constraint == "sameSlots":
                self.same_slot(cons)
                pass
            elif cons.constraint == "sameRooms":
                self.same_rooms(cons)
                pass
            elif cons.constraint == "sameTeachers":
                self.same_teachers(cons)
                pass
            else :
                pass

    def solve(self):
        self.model.optimize()

    def solver(self):
        self.model.max_gap = 0.05
        status = self.model.optimize(max_seconds=300)
        if status == OptimizationStatus.OPTIMAL:
            print('optimal solution cost {} found'.format(self.model.objective_value))
        elif status == OptimizationStatus.FEASIBLE:
            print('sol.cost {} found, best possible: {}'.format(self.model.objective_value, self.model.objective_bound))
        elif status == OptimizationStatus.NO_SOLUTION_FOUND:
            print('no feasible solution found, lower bound is: {}'.format(self.model.objective_bound))
        if status == OptimizationStatus.OPTIMAL or status == OptimizationStatus.FEASIBLE:
            for i in range(0,len(self.x_slot)):
                #t = [self.x_teacher[i][j].x for j in range(0,len(self.x_teacher[i]))]
                #r = [self.x_room[i][j].x for j in range(0,len(self.x_room[i]))]
                s = [self.x_slot[i][j].x for j in range(0,len(self.x_slot[i]))]
                #y = t.index(1.0)
                #z = r.index(1.0)
                w = s.index(1.0)
               # t_1 = self.usp.part_teachers[self.usp.session_part[i]-1][y]
                #r_1 = self.usp.part_rooms[self.usp.session_part[i]-1][z]
  
    def println(self):
        print([int(self.x_slot[i].x) for i in range(0,len(self.x_slot))])
        for i in range(0,len(self.x_teacher)):
            y = self.x_teacher.index(1)
            print(self.usp.part_teachers[self.usp.session_part[i]][y],end=" ")

filename = "/home/etud/timetabling/src/ExperimentGenerator/tmp/experiment_18-09-23/instanceUSP_generated_180923153321_realistic_extension_v2.json"
filename = "/home/etud/minizincPython/example_extension_v2.json"
usp = instanceUSP(filename)
mip = mipClassicVars(usp,"solution.xml")
mip.solver()
#mip.println()