from pulp import LpProblem, LpVariable, lpSum, LpBinary, LpStatus, LpMinimize

# Initialiser le problème MIP
problem = LpProblem("Sudoku", sense=LpMinimize) 

# Variables de décision : x[i][j][k] est binaire, 1 si la case (i, j) contient le chiffre k
x = [[[LpVariable(f"x_{i}_{j}_{k}", cat=LpBinary) for k in range(1, 10)] for j in range(9)] for i in range(9)]

# Contraintes
# Chaque cellule contient exactement un chiffre
for i in range(9):
    for j in range(9):
        problem += lpSum(x[i][j][k] for k in range(9)) == 1

# Chaque chiffre apparaît une fois par ligne
for i in range(9):
    for k in range(9):
        problem += lpSum(x[i][j][k] for j in range(9)) == 1

# Chaque chiffre apparaît une fois par colonne
for j in range(9):
    for k in range(9):
        problem += lpSum(x[i][j][k] for i in range(9)) == 1

# Chaque chiffre apparaît une fois par bloc 3x3
for block_row in range(3):
    for block_col in range(3):
        for k in range(9):
            problem += lpSum(
                x[i][j][k]
                for i in range(block_row * 3, (block_row + 1) * 3)
                for j in range(block_col * 3, (block_col + 1) * 3)
            ) == 1

# Contraintes pour les valeurs initiales du Sudoku
# Exemple : une grille partiellement remplie
initial_grid = [
    [5, 3, 0, 0, 7, 0, 0, 0, 0],
    [6, 0, 0, 1, 9, 5, 0, 0, 0],
    [0, 9, 8, 0, 0, 0, 0, 6, 0],
    [8, 0, 0, 0, 6, 0, 0, 0, 3],
    [4, 0, 0, 8, 0, 3, 0, 0, 1],
    [7, 0, 0, 0, 2, 0, 0, 0, 6],
    [0, 6, 0, 0, 0, 0, 2, 8, 0],
    [0, 0, 0, 4, 1, 9, 0, 0, 5],
    [0, 0, 0, 0, 8, 0, 0, 7, 9]
]

for i in range(9):
    for j in range(9):
        if initial_grid[i][j] != 0:
            k = initial_grid[i][j] - 1  # Convertir en index (0-8)
            problem += x[i][j][k] == 1

# Résoudre le problème
problem.solve()

# Afficher le statut de la solution
print("Status:", LpStatus[problem.status])

# Extraire la solution et l'afficher
solution = [[0] * 9 for _ in range(9)]
for i in range(9):
    for j in range(9):
        for k in range(9):
            if x[i][j][k].value() == 1:
                solution[i][j] = k + 1

for row in solution:
    print(row)
 
