from pycsp3 import *

def solve_sudoku(grid):
    n = 9  # Taille du Sudoku

    # Variables : x[i][j] est la valeur de la case (i, j) avec des valeurs possibles de 1 à 9
    x = VarArray(size=[n, n], dom=range(1, n + 1))

    # Contraintes : chaque ligne doit contenir tous les chiffres de 1 à 9
    satisfy(
        [AllDifferent(row) for row in x],

        # Contraintes : chaque colonne doit contenir tous les chiffres de 1 à 9
        [AllDifferent(col) for col in columns(x)],

        # Contraintes : chaque bloc 3x3 doit contenir tous les chiffres de 1 à 9
        [AllDifferent([x[i][j] for i in range(bi, bi + 3) for j in range(bj, bj + 3)])
         for bi in range(0, n, 3) for bj in range(0, n, 3)],

        # Contraintes : les valeurs déjà placées doivent être respectées
        [x[i][j] == grid[i][j] for i in range(n) for j in range(n) if grid[i][j] != 0]
    )

    # Résoudre le problème
    if solve():
        # Extraire et afficher la solution
        solution = [[x[i][j].value for j in range(n)] for i in range(n)]
        print("Solution trouvée :")
        for row in solution:
            print(row)
    else:
        print("Pas de solution trouvée.")


# Exemple de grille Sudoku (0 représente une case vide)
grid = [
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

# Résolution
solve_sudoku(grid)
