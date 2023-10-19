class solver :

    def __init__(self,variables, constraints):
        self.variables = variables
        self.constraints = constraints

    def create_decision(self,variables):
        # find a variable with at least two values in its domain
        var, dom = next(filter(lambda x: len(x[1]) > 1, \
                variables.items()), (None, None))
        if var is not None:
            # it true, returns the decision
            return var, min(dom)
        else:
            # otherwise, all variables are instantiated
            return None, None


    def copy_domains(self,variables):
        # returns a deep copy of the dictionnary
        return {var: dom.copy() for var, dom in variables.items()}
    

    def propagate(self,variables, var, val, apply, constraint):
        # makes a backup of the variables
        c_variables = self.copy_domains(variables)
        # applies the decision or rebuts is
        c_variables[var] = [x for x in c_variables[var] if apply is (x == val)]
        return enumerate(c_variables, constraint[0])



    def enumerate(self,variables, constraint):
        constraint.filter(variables)
        var, val = self.create_decision(variables)
        if var is None:
            print(variables) # prints the solution
            return 1
        else:
            n = self.propagate(variables, var, val, True, constraint)
            n += self.propagate(variables, var, val, False, constraint)
        return n
