# pip install pycsp3
import pycsp3

print("Résolution du carré magique !")

# taille de la grille
taille = 3

# Matrice d'indices pour résoudre le sudoku
indices = [[0] * 9 for _ in range(taille)]
# le 1er chiffre de la première colonne doit être un 9
# indices[0][0] = 9

# grille représente toutes les possibilités pour notre sudoku, pour une matrice de 9 par 9 (size=[9,9])
# chaque case de la matrice (grille[i][j]) a pour valeurs possibles un chiffre entre 1 et 9 (dom=range(1,10))
grille = pycsp3.VarArray(size=[taille, taille], dom=range(1, ???))

# Pour donner des contraintes à notre problème nous utilisons la fonction satisfy, que nous pouvons appeler plusieurs fois

# À changer pour le carré magique

# # Chaque ligne doit contenir des chiffres différents
# for ligne in range(9):
#     pycsp3.satisfy(pycsp3.AllDifferent(grille[ligne][colonne] for colonne in range(9)))

# # Chaque colonne doit contenir des chiffres différents
# for colonne in range(9):
#     pycsp3.satisfy(pycsp3.AllDifferent(grille[ligne][colonne] for ligne in range(9)))

# # Dans chaque bloc de 3 par 3, les chiffres doivent être différents
# for block_i in [0, 3, 6]:
#     for block_j in [0, 3, 6]:
#         pycsp3.satisfy(
#             pycsp3.AllDifferent(
#                 grille[i][j]
#                 for i in range(block_i, block_i + 3)
#                 for j in range(block_j, block_j + 3)
#             )
#         )


# # On impose les indices à être placés sur le plateau, que dans le cas où on a donné un indice
# for i in range(9):
#     for j in range(9):
#         if indices[i][j] > 0:
#             pycsp3.satisfy(grille[i][j] == indices[i][j])


# Résolution du problème (solve())
# Si solve est SAT (satifiable) alors il a trouvé une solution
if pycsp3.solve() is pycsp3.SAT:
    # affichage de chaque ligne du résultat

    # À changer aussi pour l'affichage
    for i in range(9):
        print(pycsp3.values(grille[i]))
else:
    # en ajoutant :
    # indices[0][1] = 9
    # indices[0][1] = 9
    # Comme indices, il n'y a plus de solutions possibles
    # et on se retrouve dans ce cas
    print("Pas de solution trouvée !")
