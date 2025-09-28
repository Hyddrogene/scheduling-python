from scipy.optimize import linear_sum_assignment
import numpy as np

def minimize_distribution_cost(capacities, demands, cost_matrix):
    num_centrals = len(capacities)
    num_clients = len(demands)
    
    # Verify if the problem is feasible (total capacity >= total demand)
    if sum(capacities) < sum(demands):
        return "Impossible to satisfy all demands"
    
    # Create a cost matrix compatible with the Hungarian algorithm
    extended_cost_matrix = []
    for central_idx in range(num_centrals):
        extended_cost_matrix.append(cost_matrix[central_idx] + [float('inf')] * (num_clients - len(cost_matrix[central_idx])))
    
    # Convert to numpy array
    cost_matrix_np = np.array(extended_cost_matrix)
    
    # Solve the assignment problem
    row_ind, col_ind = linear_sum_assignment(cost_matrix_np)
    total_cost = cost_matrix_np[row_ind, col_ind].sum()
    print(row_ind, col_ind)
    
    return total_cost

# Input example
capacities = [100, 150]
demands = [50, 60, 80]
cost_matrix = [
    [2, 4, 5],
    [3, 1, 2]
]


capacities = [1,1,1,1,1]
demands = [1,1,1,1,1]
cost_matrix = [
    [6,5,6,3,2],
    [2,3,7,2,2],
    [3,5,4,3,4],
    [7,7,8,8,5],
    [6,7,7,5,3]
]


cost_matrix = [
    [17,15,9,5,12],
    [16,16,10,5,10],
    [12,15,14,11,5],
    [4,8,14,17,13],
    [13,9,8,12,17]
]


result = minimize_distribution_cost(capacities, demands, cost_matrix)
print(result)


#############################################"
###### **Difficile : Gestion énergétique (Optimisation)**
#**Problème :**
#EDF souhaite optimiser la distribution d’énergie entre plusieurs centrales et clients. Chaque centrale a une capacité maximale et chaque client une demande fixe. Minimisez les coûts de distribution en assignant les centrales aux clients.
#
#**Input :**
#Une liste des capacités des centrales et des demandes des clients, avec un coût d’assignation par paire.
#
#Exemple :
#- Capacités des centrales : [100, 150]
#- Demandes des clients : [50, 60, 80]
#- Coût entre chaque paire (matrice) :
# 
#
#[
#    [2, 4, 5],
#    [3, 1, 2]
#  ]#
#
#
#
#**Output attendu :**
#
#Coût total minimal: 10
#
#**Objectifs :**
#- Implémenter un algorithme d’assignation optimal (ex. algorithme hongrois).
#- Manipuler des tableaux multidimensionnels.
