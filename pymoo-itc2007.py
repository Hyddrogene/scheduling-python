import numpy as np
from pymoo.model.problem import Problem
from pymoo.factory import get_sampling, get_crossover, get_mutation
from pymoo.factory import get_termination
from pymoo.algorithms.so_genetic_algorithm import GA
from pymoo.optimize import minimize
from pymoo.visualization.scatter import Scatter

# Définition de la classe du problème ITC-2007
class ITC2007(Problem):
    def __init__(self):
        super().__init__(n_var=10, n_obj=2, n_constr=2, xl=0, xu=1)

    def _evaluate(self, x, out, *args, **kwargs):
        # Calcul des fonctions objectifs et des contraintes spécifiques à ITC-2007
        # À adapter en fonction des spécifications du problème

        out["F"] = [f1, f2]  # Valeurs des fonctions objectifs
        out["G"] = [g1, g2]  # Valeurs des contraintes


class NQueensProblem(Problem):
    def __init__(self, n_queens):
        super().__init__(n_var=n_queens, 
                         n_obj=1, 
                         n_constr=0, 
                         xl=0, 
                         xu=n_queens-1, 
                         elementwise_evaluation=True)

    def _evaluate(self, x, out, *args, **kwargs):
        conflicts = 0
        n = len(x)
        for i in range(n):
            for j in range(i + 1, n):
                if x[i] == x[j] or abs(x[i] - x[j]) == j - i:
                    conflicts += 1
        out["F"] = np.array([conflicts])



# Création de l'instance du problème ITC-2007
problem = ITC2007()
problem = NQueensProblem(4)

# Configuration de l'algorithme génétique (GA)
algorithm = GA(
    pop_size=100,
    sampling=get_sampling("real_random"),
    crossover=get_crossover("real_sbx", prob=0.9, eta=15),
    mutation=get_mutation("real_pm", eta=20),
    eliminate_duplicates=True
)

# Configuration du critère d'arrêt (termination)
termination = get_termination("n_gen", 100)

# Exécution de l'optimisation
res = minimize(problem,
               algorithm,
               termination,
               seed=1,
               save_history=True,
               verbose=True)

# Affichage des résultats
Scatter().add(res.F, s=30, facecolors='none', edgecolors='r').show()



def _evaluate(self, x, out, *args, **kwargs):
    # Calcul des fonctions objectifs et des contraintes spécifiques à ITC-2007
    # À adapter en fonction des spécifications du problème

    # Exemple de calcul des fonctions objectifs f1 et f2 (à remplacer par vos propres calculs)
    f1 = sum(x)  # Exemple de fonction objectif f1 (somme des variables de décision)
    f2 = max(x)  # Exemple de fonction objectif f2 (maximum des variables de décision)

    # Exemple de calcul des contraintes g1 et g2 (à remplacer par vos propres calculs)
    g1 = sum(x) - 100  # Exemple de contrainte g1 (somme des variables de décision doit être inférieure à 100)
    g2 = max(x) - 50   # Exemple de contrainte g2 (maximum des variables de décision doit être inférieur à 50)

    # Assignation des valeurs calculées aux sorties de la méthode
    out["F"] = [f1, f2]  # Valeurs des fonctions objectifs
    out["G"] = [g1, g2]  # Valeurs des contraintes