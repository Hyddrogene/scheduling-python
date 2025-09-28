import clingo

# Fonction pour générer les faits ASP à partir de la grille donnée
def generate_facts(grid):
    facts = ""
    for r in range(9):
        for c in range(9):
            if grid[r][c] != 0:
                facts += f"given({r+1},{c+1},{grid[r][c]}).\n"
    return facts

# Fonction pour afficher la solution
def display_solution(model):
    grid = [[0 for _ in range(9)] for _ in range(9)]
    for atom in model.symbols(atoms=True):
        if atom.name == "cell":
            r, c, v = atom.arguments
            grid[int(r.number)-1][int(c.number)-1] = int(v.number)
    print("\nSolution :")
    for row in grid:
        print(" ".join(map(str, row)))

# Fonction principale pour résoudre le Sudoku
def solve_sudoku(grid):
    # Charger le modèle ASP
    asp_model = open("asp-sudoku.lp").read()

    # Ajouter les faits ASP pour la grille donnée
    asp_facts = generate_facts(grid)

    # Combiner modèle et faits
    program = asp_model + "\n" + asp_facts

    # Résolution avec clingo
    ctl = clingo.Control()
    ctl.add("base", [], program)
    ctl.ground([("base", [])])
    solution_found = False

    def on_model(model):
        nonlocal solution_found
        solution_found = True
        display_solution(model)

    ctl.solve(on_model=on_model)

    if not solution_found:
        print("Aucune solution trouvée.")

# Exemple de grille (0 représente une cellule vide)
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

# Appel de la fonction pour résoudre le Sudoku
solve_sudoku(grid)
 
