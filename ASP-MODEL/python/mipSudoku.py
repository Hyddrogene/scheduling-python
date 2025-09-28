from mip import Model, xsum, BINARY, OptimizationStatus, CBC

def solve_sudoku(grid):
    n = 9
    model = Model(solver_name=CBC)

    # Variables : x[i][j][k] = 1 si la case (i, j) contient le chiffre k+1
    x = [[[model.add_var(var_type=BINARY) for k in range(n)] for j in range(n)] for i in range(n)]

    # Contraintes : chaque case contient exactement un chiffre
    for i in range(n):
        for j in range(n):
            model += xsum(x[i][j][k] for k in range(n)) == 1

    # Contraintes : chaque chiffre apparaît exactement une fois par ligne
    for i in range(n):
        for k in range(n):
            model += xsum(x[i][j][k] for j in range(n)) == 1

    # Contraintes : chaque chiffre apparaît exactement une fois par colonne
    for j in range(n):
        for k in range(n):
            model += xsum(x[i][j][k] for i in range(n)) == 1

    # Contraintes : chaque chiffre apparaît exactement une fois par bloc 3x3
    for bi in range(3):
        for bj in range(3):
            for k in range(1, n+1):
                model += xsum(x[3*bi + di][3*bj + dj][k-1] for di in range(3) for dj in range(3)) == 1
                
    """for bi in range(0, n, 3):
        for bj in range(0, n, 3):
            for k in range(n):
                model += xsum(x[i][j][k] for i in range(bi, bi+3) for j in range(bj, bj+3)) == 1



    """

    # Contraintes : respecter les chiffres déjà placés
    for i in range(n):
        for j in range(n):
            if grid[i][j] != 0:
                model += x[i][j][grid[i][j]-1] == 1

    # Résolution
    print("Résolution en cours...")
    status = model.optimize()

    if status == OptimizationStatus.OPTIMAL:
        # Extraire la solution
        solution = [[0]*n for _ in range(n)]
        for i in range(n):
            for j in range(n):
                for k in range(n):
                    if x[i][j][k].x >= 0.99:
                        solution[i][j] = k + 1
        return solution
    else:
        print(f"Pas de solution trouvée. Statut : {status}")
        return None


# Exemple de grille Sudoku
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

solution = solve_sudoku(grid)
if solution:
    print("Solution trouvée :")
    for row in solution:
        print(row)
