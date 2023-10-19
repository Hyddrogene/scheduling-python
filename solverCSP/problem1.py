from solver_csp import solver
from global_constraint.lessThan import lessThan

variables = {"x1": {1, 2, 3},
             "x2": {1, 2, 3}}
c1 = lessThan("x1", "x2")
scp = solver(variables,[c1])
print("it finds", scp.enumerate(variables, c1), "solutions")