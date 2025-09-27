import numpy as np
import numpy as np
from pymoo.algorithms.soo.nonconvex.ga import GA
from pymoo.core.repair import Repair
from pymoo.operators.crossover.hux import HUX
from pymoo.operators.mutation.bitflip import BitflipMutation
from pymoo.operators.sampling.rnd import BinaryRandomSampling
from pymoo.optimize import minimize
from pymoo.problems.single import create_random_knapsack_problem

from pymoo.core.problem import Problem


class Knapsack(Problem):
    def __init__(self,
                 n_items,  # number of items that can be picked up
                 W,  # weights for each item
                 P,  # profit of each item
                 C,  # maximum capacity
                 ):
        super().__init__(n_var=n_items, n_obj=1, n_ieq_constr=2, xl=0, xu=1, vtype=bool)

        self.W = W
        self.P = P
        self.C = C

    def _evaluate(self, x, out, *args, **kwargs):
        G1 = (np.sum(self.W * x, axis=1) - self.C)
        G2 = 1 - np.any(x, axis=1)

        out["F"] = -np.sum(self.P * x, axis=1)
        out["G"] = np.column_stack([G1, G2])

algorithm = GA(pop_size=200,
               sampling=BinaryRandomSampling(),
               crossover=HUX(),
               mutation=BitflipMutation(),
               eliminate_duplicates=True)


problem = Knapsack(n_items=3, P=[60, 100, 120], W=[10, 20, 30], C=5)
res = minimize(problem,
               algorithm,
               termination=('n_gen', 10),
               verbose=True)