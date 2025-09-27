def all_different(values):
    """ Check if all elements in the list are unique. """
    return len(values) == len(set(values))

def all_equal(values):
    """ Check if all elements in the list are equal. """
    return 1 == len(set(values))

class Variable:
    def __init__(self, name, domain):
        self.name = name
        self.domain = list(domain)  # Assurez-vous que le domaine est une liste modifiable.

    def reduce_domain(self, value):
        if value in self.domain:
            self.domain.remove(value)
        return self.domain

class Constraint:
    def __init__(self, variables, function):
        self.variables = variables
        self.function = function

    def is_satisfied(self, assignment):
        # Extrait les valeurs assignées aux variables de la contrainte
        values = [assignment[var.name] for var in self.variables if var.name in assignment]
        # Si toutes les variables de la contrainte ont des valeurs assignées, vérifie le prédicat
        if len(values) == len(self.variables):
            return self.function(*values)
        return True  # Si la contrainte n'est pas complètement applicable, ne l'empêche pas

    def is_satisfied(self, assignment):
        # Extrait les valeurs assignées aux variables de la contrainte
        values = [assignment[var.name] for var in self.variables if var.name in assignment]
        # Si toutes les variables de la contrainte ont des valeurs assignées, vérifie le prédicat
        if len(values) == len(self.variables):
            return self.function(*values)
        return True  # Si la contrainte n'est pas complètement applicable, ne l'empêche pas

def forward_check(variable, variables, assignment, constraints):
    for other_var in variables:
        if other_var.name != variable.name and other_var.name not in assignment:
            original_domain = other_var.domain[:]
            for value in original_domain:
                test_assignment = assignment.copy()
                test_assignment[other_var.name] = value
                if not all(constraint.is_satisfied(test_assignment) for constraint in constraints if variable.name in [v.name for v in constraint.variables] or other_var.name in [v.name for v in constraint.variables]):
                    other_var.domain.remove(value)


def backtrack(assignment, variables, constraints):
    print("Current assignment:", assignment)
    if len(assignment) == len(variables):
        return assignment

    unassigned = [v for v in variables if v.name not in assignment]
    first = unassigned[0]

    for value in first.domain[:]:
        print(f"Trying {first.name} = {value}")
        local_assignment = assignment.copy()
        local_assignment[first.name] = value
        if all(constraint.is_satisfied(local_assignment) for constraint in constraints):
            result = backtrack(local_assignment, variables, constraints)
            if result is not None:
                return result
    return None




def solve_csp(variables, constraints):
    assignment = {}
    return backtrack(assignment, variables, constraints)

"""
# Exemple d'utilisation
variables = [
    Variable('X', [1, 2, 3]),
    Variable('Y', [1, 2, 3])
]

constraints = [
    Constraint([variables[0], variables[1]], lambda x, y: x == y),
    Constraint([variables[0], variables[1]], lambda x, y: x + y > 3)
]

"""
variables = [
    Variable('X', [1, 2, 3]),
    Variable('Y', [2, 3, 4]),
    Variable('Z', [1, 3, 4])
]

constraints = [
    #Constraint(variables, lambda x, y, z: x != y and y != z and x != z),
    Constraint([variables[0], variables[1]], lambda x, y: x + y > 3),
    Constraint(variables, lambda *args: all_different(args)),
    Constraint(variables, lambda *args: all_equal(args))
]


solution = solve_csp(variables, constraints)

if solution != None :
    print(solution)
else :
    print("UNSAT")
