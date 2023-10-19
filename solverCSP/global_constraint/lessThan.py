from .constraint import constraint

class lessThan(constraint):
    def __init__(self,v1,v2):
        self.v1 = v1
        self.v2 = v2

    def filter(self, variables):
        d1 = variables[self.v1] # get current domain of "x1"
        d2 = variables[self.v2] # get current domain of "x2"
        # filtering according to the 1st rule
        d1 = [v for v in d1 if v < max(d2)] 
        # filtering according to the 2nd rule
        d2 = [v for v in d2 if v > min(d1)]
        variables[self.v1] = d1 # update the domain of "x1"
        variables[self.v2] = d2 # update the domain of "x2"	