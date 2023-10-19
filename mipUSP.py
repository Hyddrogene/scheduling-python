from parserUSP import instanceUSP
from mip import Model, xsum, maximize, BINARY



class Scheduling_USP:
    def __init__(self, instanceUTP, solutionFile):
        self.usp  = instanceUTP
        self.solutionFile = solutionFile
        self.model        = Model("Scheduling")
        #self.initVariables()
        self.initVars()
        self.constraintBasis()
        
        
    def initVariables(self):
        size = 4
        self.x_slot    = [self.model.add_var(var_type=INTEGER) for i in range(size)]
        self.x_teacher = [[self.model.add_var(var_type=BINARY) for i in range(size)] for j in range(size)]
        self.x_room    = [self.model.add_var(var_type=INTEGER) for i in range(size)]

    
    def initVars(self) :
        nr_sessions = self.usp.nr_sessions 
        nr_week = self.usp.nr_weeks
        nr_days = self.usp.nr_days_per_week
        nr_dailySlot = self.usp.grid[2]

        #nr_abstract_slots = nr_week * nr_days * nr_dailySlot
        #SLOTS = range(0,nr_abstract_slots)
        i = 0
        #print(SLOTS)
        self.x_slot = []
        for s in range(0,self.usp.nr_sessions):
            p  = self.usp.session_part[s]-1
            tmp = []
            for r in range(0,len(self.usp.part_rooms[p])):
            #for t in range(0,len(self.usp.part_teachers[p])): 
                    w = max(self.usp.part_weeks[p])
                    for slt in range(0,w*nr_days*nr_dailySlot):
                        i+=1
                        #var_name = "X_"+str(s)+"_"+str(r)+"_"+str(t)+"_"+str(slt)
                        #tmp.append(self.model.add_var(name=var_name,var_type=BINARY))
                        tmp.append(self.model.add_var(var_type=BINARY))
            self.x_slot.append(tmp)
        print(i)
    
    def constraintBasis(self):
        #self.model += xsum(self.x_slot) >= 2
        for s in range(0,self.usp.nr_sessions):
            #p  = self.usp.session_part[s]-1
            #ag = w * nr_days * nr_dailySlot
            #for r in range(0,len(self.usp.part_rooms[p])):
            self.model += xsum(self.x_slot[s]) <= 1
        for r in range(0,self.usp.nr_rooms):
            if len(self.usp.room_parts[r])> 0:
                tmp = []
                for s in self.usp.room_sessions[r]:
                    for 
                    

                #for t in range(0,len(self.usp.part_teachers[p])): 
                    #for slt in SLOTS:
                        
               # if( i != j ):
               #     model += (x_room[i] - x_room[j])*(x_slot[i] - x_slot[j]) == 0
    
    def solve(self):
        self.model.optimize()
        
    def println(self):
        print([int(self.x_slot[i].x) for i in range(0,len(self.x_slot))])



filename = "/home/etud/timetabling/src/ExperimentGenerator/tmp/experiment_18-09-23/instanceUSP_generated_180923153321_realistic_extension_v2.json"
usp = instanceUSP(filename)
Scheduling_USP(usp,"solution.xml")
