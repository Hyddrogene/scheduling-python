#Voici le programme PuLP correspondant à la fonction objective et aux contraintes 
# que vous avez fournies :

import pulp

# Création d'une instance du problème de minimisation
model = pulp.LpProblem("UCTTP", pulp.LpMinimize)

# Déclaration des variables binaires xchr
xchr = pulp.LpVariable.dicts("xchr", (C, H, R), cat=pulp.LpBinary)

# Déclaration des variables binaires ycd
ycd = pulp.LpVariable.dicts("ycd", (C, D), cat=pulp.LpBinary)

# Déclaration des variables binaires zc
zc = pulp.LpVariable.dicts("zc", C, cat=pulp.LpBinary)

# Fonction objective
model += (
    pulp.lpSum(
        W_CComp * vqh * xchr[c][h][r]
        + W_Dist * pulp.lpSum(distcd[c][d] for d in D if d != d5)
        + W_Mwd * zc[c]
        + W_R_Stb * pulp.lpSum(
            (d, h)
            for d in D
            for h in H
            if (d, h) not in L_H_P_M
            and abs(pulp.lpSum(xchr[c][h][r] for c in C[q]) - pulp.lpSum(xc[h + 1][r] for c in C[q])) > 0
        )
        for c in C
        for h in H
        for r in R
        for q in Q
    )
)

# Contraintes
for h in H - {F_H} - L_H_P_M:
    for q in Q:
        model += (
            pulp.lpSum(
                pulp.lpSum(xchr[c][h - 1][r] for r in R)
                - pulp.lpSum(xchr[c][h][r] for r in R)
                + pulp.lpSum(xchr[c][h + 1][r] for r in R)
                for c in C[q]
            ) - 1 <= vqh
        )

for h in F_H:
    for q in Q:
        model += (
            pulp.lpSum(
                pulp.lpSum(xchr[c][h][r] for r in R)
                - pulp.lpSum(xchr[c][h + 1][r] for r in R)
                for c in C[q]
            ) <= vqh
        )

for c in C:
    for d in D:
        if d != d5:
            model += ycd[c][d] + ycd[c][d + 1] - 1 <= distcd[c][d]

for c in C:
    for d in D:
        model += (
            pulp.lpSum(xchr[c][h][r] for h in H for r in R)
            >= ycd[c][d]
        )

for c in C:
    model += (
        pulp.lpSum(ycd[c][d] for d in D) + zc[c] >= mwdc[c]
    )

for c in C:
    model += (
        pulp.lpSum(xchr[c][h][r] for h in H for r in R) == lectc[c]
    )

for h in H:
    for q in Q:
        model += (
            pulp.lpSum(xchr[c][h][r] for c in C[q] for r in R) <= 1
        )

for h in H:
    for r in R:
        model += (
            pulp.lpSum(xchr[c][h][r] for c in C) <= 1
        )

for h in H:
    for t in T:
        model += (
            pulp.lpSum(xchr[c][h][r] for c in C[t] for r in R) <= 1
        )

for r in R:
    model += (
        pulp.lpSum(xchr[c][h][r] for c in C for h in H) == 0
    )

for c in C:
    for r in R:
        model += (
            pulp.lpSum(xchr[c][h][r] for h in H)
            - pulp.lpSum(xchr[c][h][r] for h in H)
            == 0
        )

for c in C:
    for r in R:
        model += (
            pulp.lpSum(xchr[c][h][r] for h in H)
            - pulp.lpSum(xchr[c][h][r] for h in H)
            == 0
        )

# Résolution du problème
model.solve()

# Affichage des résultats
for c in C:
    for h in H:
        for r in R:
            if pulp.value(xchr[c][h][r]) == 1:
                print(f"xchr[{c}][{h}][{r}] = 1")
