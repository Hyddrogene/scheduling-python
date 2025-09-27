Le recuit simulé (Simulated Annealing, SA) est une technique d'optimisation qui s'inspire du processus de refroidissement contrôlé des métaux. Il est particulièrement utile pour trouver des solutions approximatives à des problèmes d'optimisation combinatoire complexes, tels que la planification de cours (comme dans le problème ITC-2007). Voici comment vous pourriez implémenter un algorithme de recuit simulé en Python pour aborder un problème de planification de cours simplifié.

### Étapes de base de l'algorithme de recuit simulé:
1. **Initialisation** : Commencer avec une solution initiale générée aléatoirement.
2. **Choix d'une nouvelle solution** : Perturber légèrement la solution actuelle pour en créer une nouvelle.
3. **Évaluation de l'énergie** : Évaluer une "fonction d'énergie" ou fonction coût qui indique la qualité de la solution.
4. **Acceptation de la nouvelle solution** : Décider si la nouvelle solution doit remplacer l'actuelle, en fonction d'une probabilité qui dépend de la différence d'énergie entre les deux solutions et d'un paramètre de "température" qui décroît progressivement.
5. **Refroidissement** : Réduire la température selon un schéma prédéfini.

### Code Python pour le Recuit Simulé

```python
import math
import random

def initial_solution():
    # Générer une solution initiale aléatoire
    return [random.randint(0, 9) for _ in range(10)]

def cost(solution):
    # Fonction coût simple pour l'exemple: maximiser la somme des éléments
    return -sum(solution)

def perturb(solution):
    """ Perturber légèrement la solution actuelle """
    new_solution = solution[:]
    idx = random.randint(0, len(solution) - 1)
    new_solution[idx] = random.randint(0, 9)  # Change one element randomly
    return new_solution

def acceptance_probability(old_cost, new_cost, temperature):
    """ Calculer la probabilité d'acceptation d'une nouvelle solution """
    if new_cost < old_cost:
        return 1.0
    else:
        return math.exp((old_cost - new_cost) / temperature)

def simulated_annealing():
    current_solution = initial_solution()
    current_cost = cost(current_solution)
    temperature = 1.0
    cooling_rate = 0.99
    min_temperature = 0.01

    while temperature > min_temperature:
        new_solution = perturb(current_solution)
        new_cost = cost(new_solution)
        if acceptance_probability(current_cost, new_cost, temperature) > random.random():
            current_solution, current_cost = new_solution, new_cost
        
        temperature *= cooling_rate

    return current_solution, current_cost

# Exécuter l'algorithme
best_solution, best_cost = simulated_annealing()
print("Best solution:", best_solution)
print("Best cost:", best_cost)
```

### Explications du Code
- **initial_solution** : Crée une solution initiale aléatoire. Dans cet exemple, il s'agit d'une liste de 10 nombres aléatoires.
- **cost** : Une fonction coût simpliste. Ici, elle est conçue pour être minimisée.
- **perturb** : Modifie aléatoirement un élément de la solution pour explorer le voisinage de la solution actuelle.
- **acceptance_probability** : Calcule la probabilité d'accepter ou non la nouvelle solution, en fonction de la différence de coût et de la température actuelle.
- **simulated_annealing** : Implémente l'algorithme de recuit simulé, ajustant la température jusqu'à ce qu'elle atteigne une valeur minimale.

### Adaptation à votre problème spécifique
Pour adapter cet algorithme à un problème de planification comme ITC-2007, vous devriez:
- Redéfinir la **fonction coût** pour évaluer la qualité d'un emploi du temps en fonction des contraintes du problème.
- Adapter la fonction **perturb** pour qu'elle génère des modifications valides de l'emploi du temps (par exemple, échanger des créneaux entre cours, ou changer l'heure ou la salle d'un cours).
- Éventuellement ajuster les paramètres de **température** et de **taux de refroidissement** selon la complexité de votre problème et les résultats des tests préliminaires.

Cette base fournit une structure pour implémenter un recuit simulé qui peut

 être étendue et adaptée aux détails spécifiques et aux exigences de contraintes du problème ITC-2007 ou de tout autre problème d'optimisation similaire.





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

# Création de l'instance du problème ITC-2007
problem = ITC2007()

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
