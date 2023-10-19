import pulp

# Création du problème
prob = pulp.LpProblem("CB-CTT", pulp.LpMinimize)

# Ensembles de cours, jours, plages horaires, salles de classe et enseignants
C = ["C1", "C2", "C3"]  # Exemple de cours
D = ["Monday", "Tuesday", "Wednesday"]  # Jours de la semaine
T = ["Morning", "Afternoon"]  # Plages horaires
R = ["Room1", "Room2", "Room3"]  # Salles de classe
P = ["Teacher1", "Teacher2", "Teacher3"]  # Enseignants

# Variables de décision
x = pulp.LpVariable.dicts("x", [(c, d, t, r) for c in C for d in D for t in T for r in R], cat=pulp.LpBinary)
y = pulp.LpVariable.dicts("y", [(p, d, t) for p in P for d in D for t in T], cat=pulp.LpBinary)

# Objectif : par exemple, minimiser le nombre de violations de contraintes

# Contraintes
# 1. Chaque cours doit être planifié exactement une fois
for c in C:
    prob += pulp.lpSum(x[(c, d, t, r)] for d in D for t in T for r in R) == 1

# 2. Aucun cours ne peut se chevaucher dans le temps
for d in D:
    for t in T:
        for r in R:
            prob += pulp.lpSum(x[(c, d, t, r)] for c in C) <= 1

# 3. Aucune salle de classe ne peut être occupée simultanément par deux cours
for c in C:
    prob += pulp.lpSum(x[(c, d, t, r)] for d in D for t in T) <= 1

# 4. Les cours doivent être enseignés par un enseignant disponible
for c in C:
    for d in D:
        for t in T:
            for r in R:
                for p in P:
                    prob += y[(p, d, t)] >= x[(c, d, t, r)]

# 5. Chaque cours doit être enseigné exactement une fois par un enseignant
for p in P:
    prob += pulp.lpSum(y[(p, d, t)] for d in D for t in T) == 1

# Résoudre le problème
prob.solve()

# Afficher la solution
for c in C:
    for d in D:
        for t in T:
            for r in R:
                if x[(c, d, t, r)].varValue == 1:
                    print(f"Cours {c} le {d} à {t} dans {r}.")

# Statut de la résolution
print("Statut de la résolution:", pulp.LpStatus[prob.status])
