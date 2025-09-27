class Variable:
    def __init__(self, name, domain):
        self.name = name
        self.domain = domain

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

def backtrack(assignment, variables, constraints):
    if len(assignment) == len(variables):
        return assignment

    unassigned = [v for v in variables if v.name not in assignment]
    first = unassigned[0]

    for value in first.domain:
        local_assignment = assignment.copy()
        local_assignment[first.name] = value
        # Si la valeur courante est consistante avec les contraintes, continuez
        if all(constraint.is_satisfied(local_assignment) for constraint in constraints if first in constraint.variables):
            result = backtrack(local_assignment, variables, constraints)
            if result is not None:
                return result
    return None


def solve_csp(variables, constraints):
    assignment = {}
    return backtrack(assignment, variables, constraints)

# Exemple d'utilisation
variables = [
    Variable('X', [1, 2, 3]),
    Variable('Y', [1, 2, 3])
]

constraints = [
    Constraint([variables[0], variables[1]], lambda x, y: x == y),
    Constraint([variables[0], variables[1]], lambda x, y: x + y > 3)


]

solution = solve_csp(variables, constraints)
print(solution)
