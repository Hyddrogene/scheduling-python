import numpy as np
#from pymoo.model.problem import Problem
#from pymoo.core.problem import Problem
from pymoo.core.problem import ElementwiseProblem

from pymoo.algorithms.moo.nsga2 import NSGA2
from pymoo.algorithms.soo.nonconvex.ga import GA

from pymoo.problems import get_problem
from pymoo.optimize import minimize
from pymoo.operators.sampling.rnd import FloatRandomSampling
from pymoo.operators.sampling.rnd import PermutationRandomSampling

from pymoo.operators.crossover.sbx import SBX
from pymoo.operators.mutation.pm import PM


from pymoo.visualization.scatter import Scatter

problem = get_problem("zdt1")


class NQueensProblem(ElementwiseProblem):
    def __init__(self, n_queens):
        super().__init__(n_var=n_queens, 
                         n_obj=1, 
                         n_constr=0, 
                         xl=1, 
                         xu=n_queens, 
                         vtype=int)#,
                         #elementwise_evaluation=True)

    def _evaluate(self, x, out, *args, **kwargs):
        conflicts = 0
        n = len(x)
        for i in range(n):
            for j in range(i + 1, n):
                if (x[i] == x[j]) or (abs(x[i] - x[j]) == (j - i)):
                    conflicts += 1
        out["F"] = sum(np.array([conflicts]))



# Création de l'instance du problème ITC-2007
problem = NQueensProblem(4)


algorithm = GA(pop_size=100,
                eliminate_duplicates=True,
                sampling=PermutationRandomSampling()
                )



res = minimize(problem,
               algorithm,
               ('n_gen', 200),
               seed=1,
               verbose=True)

plot = Scatter()
plot.add(problem.pareto_front(), plot_type="line", color="black", alpha=0.7)
plot.add(res.F, color="red")
plot.show()

print("Solution: ", res.X)
print("Number of conflicts: ", res.F[0])