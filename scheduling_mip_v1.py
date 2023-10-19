from mip import Model, xsum, maximize, BINARY,INTEGER

g = 0

#decision variable

class Scheduling_USP:
    def __init__(self, instanceUTP, solutionFile):
        self.instanceUTP  = instanceUTP
        self.solutionFile = solutionFile
        self.model        = Model("Scheduling")
        self.initVariables()
        self.constraintBasis()
        
    def initVariables(self):
        size = 4
        self.x_slot    = [self.model.add_var(var_type=INTEGER) for i in range(size)]
        self.x_teacher = [[self.model.add_var(var_type=BINARY) for i in range(size)] for j in range(size)]
        self.x_room    = [self.model.add_var(var_type=INTEGER) for i in range(size)]
    
    def constraintBasis(self):
        size = 4
        self.model += xsum(self.x_slot) >= 2
        for i in range(0,size):
            for j in range(i,size): 
                if( i != j ):
                    model += (x_room[i] - x_room[j])*(x_slot[i] - x_slot[j]) == 0
    
    def solve(self):
        self.model.optimize()
        
    def println(self):
        print([int(self.x_slot[i].x) for i in range(0,len(self.x_slot))])

s = Scheduling_USP(1,"sol0")
s.solve()
s.println()
