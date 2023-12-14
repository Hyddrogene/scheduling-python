from parserUSP import instanceUSP

from mip import Model, xsum, maximize, BINARY, INTEGER, OptimizationStatus,GRB,CBC



class mipContinusVars:
    def __init__(self, instanceUTP, solutionFile):
        self.usp  = instanceUTP
        self.solutionFile = solutionFile
        self.model        = Model("Scheduling",solver_name=GRB)
        #self.initVariables()
        self.initVars()
        self.constraintBasis()

    def initVars(self) :
        nr_sessions = self.usp.nr_sessions 
        nr_week = self.usp.nr_weeks
        nr_days = self.usp.nr_days_per_week
        nr_dailySlot = self.usp.grid[2]
        self.nr_abstract_slots = (nr_week * nr_days * nr_dailySlot)+1
        self.SLOTS = range(1,self.nr_abstract_slots)

        self.x_slot = []
        for i in range(0,nr_sessions) :
            p = self.usp.session_part[i]-1
            nr_week1 = max(self.usp.part_weeks[p])
            nr_abstract_slots = (nr_week1 * nr_days * nr_dailySlot)+1
            print(nr_abstract_slots)
            nr_abstract_slots = 2000
            self.x_slot.append(self.model.add_var(var_type=INTEGER,lb=1,ub=nr_abstract_slots))
        #exit(0)
        print("Vars x_slot = ",nr_sessions)
        i = 0
        self.x_room = []
        for s in range(0,self.usp.nr_sessions):
            p  = self.usp.session_part[s]-1
            tmp = []
            for r in range(0,len(self.usp.part_rooms[p])):
                    #w = max(self.usp.part_weeks[p])
                    i+=1
                    tmp.append(self.model.add_var(var_type=BINARY))
            self.x_room.append(tmp)
        print("Vars x_room = ",i)
        j = 0
        self.x_teacher = []
        for s in range(0,self.usp.nr_sessions):
            p  = self.usp.session_part[s]-1
            tmp = []
            for r in range(0,len(self.usp.part_teachers[p])):
                    j += 1
                    tmp.append(self.model.add_var(var_type=BINARY))
            self.x_teacher.append(tmp)

        print("Vars x_teacher = ",j)
        print("Total = ",(i+j+nr_sessions))

    def cardinal_rooms(self):
        for s in range(0,self.usp.nr_sessions):
            self.model += xsum(self.x_room[s]) == 1

    def sequenced(self,constraint):
        if len(constraint.sessions) == 2 :
            for session1 in constraint.sessions[0]:
                p = self.usp.session_part[session1]-1
                for session2 in constraint.sessions[1] :
                    if session1 != session2 :
                        self.model += (self.x_slot[session1-1] +self.usp.part_abstract_grid[p][2]) <= self.x_slot[session2-1]
        else :
            for s1 in range(1,len(constraint.sessions[0])):
                session1 = constraint.sessions[0][s1]
                p = self.usp.session_part[session1-1]-1
                
                for session2 in constraint.sessions[0] :
                    if session1 < session2 :
                        self.model += (self.x_slot[session1-1] +self.usp.part_abstract_grid[p][2]) <= self.x_slot[session2-1]

    def implicite_sequenced(self):
        for p in range(0,self.usp.nr_parts):
            for c in self.usp.part_classes[p]:
                print("classe ", c)
                for s1 in range(0,len(self.usp.class_sessions[c-1])):
                    for s2 in range(s1,len(self.usp.class_sessions[c-1])):
                        if s1 != s2 :
                            #print("s1 = ",(self.usp.class_sessions[c-1][s1]-1)," s2 = ",(self.usp.class_sessions[c-1][s2]-1))
                            self.model += self.x_slot[self.usp.class_sessions[c-1][s2]-1] >= (self.x_slot[self.usp.class_sessions[c-1][s1]-1]+self.usp.part_abstract_grid[p][1])
                            self.usp.session_session_sequenced[self.usp.class_sessions[c-1][s1]-1][self.usp.class_sessions[c-1][s2]-1] = 1
        

    def periodic(self,constraint):
        nr_days = self.usp.nr_days_per_week
        nr_dailySlot = self.usp.grid[2]
        d_week = nr_days * nr_dailySlot
        print(d_week)
        for i in range(1,len(constraint.sessions[0])):
            print("(i*d_week)=",(i*d_week))
            self.model += self.x_slot[constraint.sessions[0][i]-1] - (i*d_week) == self.x_slot[constraint.sessions[0][0]-1]
    def same_slot(sel,constraint):
        for session1 in constraint.sessions[0]:
            for session2 in constraint.sessions[0]:
                if session1 != session2 :
                    self.model += self.x_slot[session1-1] == self.x_slot[session2-1]

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
            self.model += xsum(self.x_teacher[s]) == 1

    def service_teacher(self):
        for p in range(0,self.usp.nr_parts):
            for t in self.usp.part_teachers[p]:
                print("parts : ",self.usp.part_name[p]," nr teachers ",len(self.usp.part_teachers[p])," service ", sum(self.usp.part_teacher_sessions_count[p])," ssess * class ",(len(self.usp.part_classes[p]) * self.usp.part_nr_sessions[p]) )
                tmp = []
                p_t = self.usp.getPositionTeacher(self.usp.part_sessions[p][0]-1, t)
                for s in self.usp.part_sessions[p]:
                    tmp.append(self.x_teacher[s-1][p_t])
                #print(self.usp.part_teacher_sessions_count[p][t-1])
                self.model += xsum(tmp) == self.usp.part_teacher_sessions_count[p][t-1]
                #self.model += xsum([self.x_teacher[s-1][self.usp.getPositionTeacher(s-1, t)] for s in self.usp.part_sessions[p]]) == self.usp.part_teacher_sessions_count[p][t-1]

    def disjunctive_groupe(self):
        nr_sessions = self.usp.nr_sessions
        #val = int(nr_sessions**2 - (nr_sessions**2+nr_sessions)/2)
        #print(val)
        #exit(0)
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

                            self.model += (y1[l] + y2[l]) == 1
                            self.model += self.x_slot[s1] >= self.x_slot[s2] +(1+M)*y1[l] -M
                            self.model += self.x_slot[s2] >= self.x_slot[s1] +(1+M)*y2[l] -M
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

                                self.model += self.x_slot[s1] >= self.x_slot[s2]+(( self.usp.part_abstract_grid[p2][1] +M)*y2[l])-M
                                self.model += self.x_slot[s2] >= self.x_slot[s1]+(( self.usp.part_abstract_grid[p1][1] +M)*y1[l])-M

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
                            l+1
                            #self.model += xsum(y1[l] *  self.x_teacher[i][k] for k in range(0,2))
                            #z.append(self.model.add_var(var_type=BINARY))
                            #self.model += -M*(1-y1[l])<= self.x_slot[i] <= 1+M*(1-y1[l])
                            #self.model += -M*(1-y1[l])<= self.x_slot[j] <= 1+M*(1-y1[l])
                            #self.model += -M*(1-y2[l])<= self.x_slot[i] <= 1+M*(1-y2[l])
                            #self.model += -M*(1-y2[l])<= self.x_slot[j] <= 1+M*(1-y2[l])
                            #self.model += -M*(1-y1[i])<= y1[i] <= 1+M*(1-y1[i])
                            #self.model += y[l] == 2-self.x_teacher[i][t]-self.x_teacher[j][t]
                            #self.model += (self.x_slot[self.usp.teacher_sessions[t][i]-1]-self.x_slot[self.usp.teacher_sessions[t][j]-1]) <= -1 + 3000*y[l]
                            #self.model += self.x_slot[self.usp.teacher_sessions[t][i]-1] >= self.x_slot[self.usp.teacher_sessions[t][j]-1]+1 -3000*(1-y[l]) 

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

                                self.model += self.x_slot[s1] >= self.x_slot[s2]+(( self.usp.part_abstract_grid[p2][1] +M)*y2[l])-M
                                self.model += self.x_slot[s2] >= self.x_slot[s1]+(( self.usp.part_abstract_grid[p1][1] +M)*y1[l])-M

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
        self.cardinal_rooms()
        self.cardinal_teachers()
        self.service_teacher()
        self.implicite_sequenced()
        self.disjunctive_groupe()
        self.disjunctive_teacher() 
        self.disjunctive_rooms()
        for cons in self.usp.constraints:
            if cons.constraint == "sequenced" :
                self.sequenced(cons)
                pass
            elif cons.constraint == "periodic":
                self.periodic(cons)
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
        #self.model += xsum(self.x_slot) >= 2
        #for s in range(0,self.usp.nr_sessions):
            #p  = self.usp.session_part[s]-1
            #ag = w * nr_days * nr_dailySlot
            #for r in range(0,len(self.usp.part_rooms[p])):
           # self.model += xsum(self.x_slot[s]) <= 1
        #for r in range(0,self.usp.nr_rooms):
            #if len(self.usp.room_parts[r])> 0:
                #tmp = []
                #for s in self.usp.room_sessions[r]:
                    #print(0) 
                    

                #for t in range(0,len(self.usp.part_teachers[p])): 
                    #for slt in SLOTS:
                        
               # if( i != j ):
               #     model += (x_room[i] - x_room[j])*(x_slot[i] - x_slot[j]) == 0
    
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
            print('solution:')
            #for v in self.x_slot:
                #if abs(v.x) > 1e-6: # only printing non-zeros
                    #print('{} : {}'.format(v.name, v.x),end=" ")
            #print(sorted([self.x_slot[i].x for i in range(0,len(self.x_slot))]))
            print(sorted([self.x_slot[i].x for i in range(0,len(self.x_slot))]))
            for i in range(0,len(self.x_teacher)):
                t = [self.x_teacher[i][j].x for j in range(0,len(self.x_teacher[i]))]
                r = [self.x_room[i][j].x for j in range(0,len(self.x_room[i]))]
                y = t.index(1.0)
                z = r.index(1.0)
                t_1 = self.usp.part_teachers[self.usp.session_part[i]-1][y]
                r_1 = self.usp.part_rooms[self.usp.session_part[i]-1][z]
                
                print("session ",i," rank ",self.usp.session_rank[i]," slot ",self.x_slot[i].x," room ",r_1," teacher ",t_1," group ",self.usp.session_group[i])
                #print("t = ", self.usp.part_teachers[self.usp.session_part[i]-1][y],end=" ")
                #print(t)
            #print([self.y1y2[i].x for i in range(0,len(self.y1y2))])
        
    def println(self):
        print([int(self.x_slot[i].x) for i in range(0,len(self.x_slot))])
        for i in range(0,len(self.x_teacher)):
            y = self.x_teacher.index(1)
            print(self.usp.part_teachers[self.usp.session_part[i]][y],end=" ")
            print()

        #print([[self.x_teacher[i][j].x for j in range(0,len(self.x_teacher[i]))] for i in range(0,len(self.x_teacher))])
        #rint([self.usp.room_name[int(self.x_room[i][[self.x_room[i][k].x for k in range(0,len(self.x_room[i]))].index(max([self.x_room[i][k].x for k in range(0,len(self.x_room[i]))]))].x)-1] for i in range(0,len(self.x_slot))])



filename = "/home/etud/timetabling/src/ExperimentGenerator/tmp/experiment_18-09-23/instanceUSP_generated_180923153321_realistic_extension_v2.json"
filename = "/home/etud/minizincPython/example_extension_v2.json"
filename = "/home/etud/timetabling/tools/tools_php/tmp/experiment_07-12-23/instanceUSP_generated_071223111836_realistic_example1.json"
usp = instanceUSP(filename)
mip = mipContinusVars(usp,"solution.xml")
mip.solver()
#mip.println()