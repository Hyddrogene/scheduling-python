from pulp import LpProblem, LpVariable, lpSum, LpStatus, LpMinimize

# Initialiser le problème MIP
problem = LpProblem("Sudoku", sense=LpMinimize)  # Correction : définir un sens pour le problème

# Variables de décision : x[i][j] est un entier entre 1 et 9 qui représente la valeur de la case (i, j)
x = [[LpVariable(f"x_{i}_{j}", lowBound=1, upBound=9, cat='Integer') for j in range(9)] for i in range(9)]

# Contraintes
# Chaque ligne contient des valeurs différentes
for i in range(9):
    for j1 in range(9):
        for j2 in range(j1 + 1, 9):
            problem += x[i][j1] != x[i][j2]

# Chaque colonne contient des valeurs différentes
for j in range(9):
    for i1 in range(9):
        for i2 in range(i1 + 1, 9):
            problem += x[i1][j] != x[i2][j]

# Chaque bloc 3x3 contient des valeurs différentes
for block_row in range(3):
    for block_col in range(3):
        cells = [
            x[i][j]
            for i in range(block_row * 3, (block_row + 1) * 3)
            for j in range(block_col * 3, (block_col + 1) * 3)
        ]
        for c1 in range(len(cells)):
            for c2 in range(c1 + 1, len(cells)):
                problem += cells[c1] != cells[c2]

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
            problem += x[i][j] == initial_grid[i][j]

# Résoudre le problème
problem.solve()

# Afficher le statut de la solution
print("Status:", LpStatus[problem.status])

# Extraire la solution et l'afficher
solution = [[0] * 9 for _ in range(9)]
for i in range(9):
    for j in range(9):
        if x[i][j].value() is not None:
            solution[i][j] = int(x[i][j].value())
        else:
            solution[i][j] = 0  # Si aucune valeur n'est attribuée, mettre 0

for row in solution:
    print(row)

