"""
@author : Antoine JAMIN
@date : mars 2022

L1 - TP3 : Carré magique

Consignes : Réalisez un programme permettant de générer un carré magique pour une taille donnée.

Conseils/Infos :
    -> Ce code est directement testable en l'état (si pycsp3 et tkinter sont installés). Vous pouvez tester différentes
        génération d'ordre n en modifiant la variable taille du main (l.108).
    -> La résolution du carré bimagique n'a pas été testée car trop de temps de calcul.
        Pour ce faire permetre la résolution par le solveur dans un temps "testable" on pourrait casser plein de
        symétries en préfixant certaines cases.
    -> Une solution pour tester le premier bimagique (n=8) a été proposée en s'appuyant sur la solution de Pfeffermann
"""
import random
import glob
from pycsp3 import *
from typing import List
from tkinter import *


def generer_carre_magique(n: int = 3, doublement: bool = False) -> List:
    """
    Fonction permettant de générer un carré magique
    :param n: taille du carré magique (>3)
    :param doublement: vrai si génération d'un carré doublement magique
    :return: list contenant le carré magique généré (si pas de solution retourne une liste vide)
    """
    if n == 2:
        return list()
    constante_magique: int = int(n * (n * n + 1) / 2)

    # Tableau initial
    x = VarArray(size=[n, n], dom=range(1, n * n + 1))

    # Contraintes
    # Valeurs uniques au sein du carré
    satisfy(AllDifferent(x))
    # somme de chaque ligne = constante magique
    for i in range(n):
        satisfy(Sum(x[i][j] for j in range(n)) == constante_magique)
        satisfy(Sum(x[j][i] for j in range(n)) == constante_magique)
    # somme de chaque diagonale = constante magique
    satisfy(Sum(diagonal_down(x, 0)) == constante_magique, Sum(diagonal_up(x, 0)) == constante_magique)
    # Si carré bimagique
    if doublement:
        val: int = int(n * (n * n + 1) * (2 * n * n + 1) / 6)
        # la somme des valeurs élevées au carré de chaque ligne (et colonne) est égale à val
        for i in range(n):
            satisfy(Sum(x[i][j] * x[i][j] for j in range(n)) == val)
            satisfy(Sum(x[j][i] * x[j][i] for j in range(n)) == val)
        # la somme des valeurs élevées au carré de chaque diagonale est égale à val
        satisfy(Sum(x[i, i] * x[i, i] for i in range(n)) == val)
        satisfy(Sum(x[i, n - 1 - i] * x[i, n - 1 - i] for i in range(n)) == val)
        # Pour une taille de 8 on va fixer quelques valeurs (3-4 par ligne de la solution de Pfeffermann) pour que
        # le calcul puisse être résolu en un temps raisonable
        if n == 8:
            clues: List = [
                [56, None, 8, None, 18, None, 9, None],
                [None, 20, None, None, 7, None, None, 10],
                [26, None, 13, 23, None, 38, None, None],
                [None, None, 35, 30, None, 12, None, 60],
                [None, 25, None, None, 41, None, 50, None],
                [None, None, 17, None, 36, None, 32, None],
                [None, 16, None, 52, None, 1, None, None],
                [44, None, 28, 37, None, None, 21, None]
            ]
            for i in range(n):
                for j in range(n):
                    if clues[i][j] is not None:
                        satisfy(x[i][j] == clues[i][j])
        # Retour de la première solution pour gain de temps
        if solve() is SAT:
            return values(x)
        else:
            return list()
    else:
        # Retour 1 solution parmi les solutions possibles (10 max pour gain de temps)
        if solve(sols=10) is SAT:
            return values(x, sol=random.randint(0, n_solutions() - 1))
        else:
            return list()


def afficher_carre_magique(carre_magique: List, titre: str) -> None:
    """
    Fonction permettant d'afficher un carré magique transmis sous forme de tableau
    :param carre_magique: List contenant le carré magique
    :return: None
    """
    fenetre = Tk()  # Initialisation de la fenêtre
    fenetre.minsize(220, 220)  # Taille minimale de la fenêtre au démarrage
    fenetre.resizable(False, False)  # Suppression du bouton maximiser la fenêtre
    fenetre.title(titre)  # Titre de la fenêtre
    lignes: List = []
    for i, r in enumerate(carre_magique):
        colonne: List = []
        for j, c in enumerate(r):
            lab = Label(text=c, font=("", 12))  # Label de police 12 contenant l'élément du carré magique
            lab.grid(row=i, column=j)  # Placement sur la grille de la fenêtre
            colonne.append(lab)
            # configuration de la colonne de la grille de la fenêtre (grid) pour homogénéiser le placement des chiffres
            fenetre.columnconfigure(j, minsize=len(carre_magique) * 10, weight=2)
        lignes.append(colonne)
        # configuration de la ligne de la grille de la fenêtre (grid) pour homogénéiser le placement des chiffres
        fenetre.rowconfigure(i, minsize=len(carre_magique) * 10, weight=2)
    fenetre.mainloop()  # Lancement de la boucle principale de l'élément fenêtre tourne jusqu'à fermeture


def carre_magique_is_ok(carre_magique: List) -> int:
    """
    Fonction permettant de tester la validité d'un carré magique
    :param carre_magique: List contenant un carré magique
    :return: 0 pas magique, 1 magique, 2 bimagique
    """
    n: int = len(carre_magique)
    # Le carré est-il carré ?
    if n == 0 or n != len(carre_magique[0]):
        return False
    constante_magique: int = int(n * (n * n + 1) / 2)
    val: int = int(n * (n * n + 1) * (2 * n * n + 1) / 6)
    # Vérification somme de chaque ligne et de chaque colonne
    for i in range(n):
        if sum(carre_magique[i][:]) != constante_magique:
            return 0
        if sum(carre_magique[:][i]) != constante_magique:
            return 0
    # Vérification somme de chaque diagonale
    if sum(carre_magique[i][i] for i in range(n)) != constante_magique:
        return 0
    if sum(carre_magique[i][n - 1 - i] for i in range(n)) != constante_magique:
        return 0
    # Vérification bimagique
    bimagique: bool = True
    # Vérification somme de chaque ligne et de chaque colonne
    for i in range(n):
        if sum(carre_magique[i][j] * carre_magique[i][j] for j in range(n)) != val:
            bimagique = False
        if sum(carre_magique[j][i] * carre_magique[j][i] for j in range(n)) != val:
            bimagique = False
    # Vérification somme de chaque diagonale
    if sum(carre_magique[i][i] * carre_magique[i][i] for i in range(n)) != val:
        bimagique = False
    if sum(carre_magique[i][n - 1 - i] * carre_magique[i][n - 1 - i] for i in range(n)) != val:
        bimagique = False
    if bimagique:
        return 2
    return 1


def clean() -> None:
    """
    Fonction permettant du supprimer les fichiers générés par le solveur
    :return: None
    """
    for file in glob.glob("*.log") + glob.glob("*.xml"):
        os.remove(file)


if __name__ == '__main__':
    taille: int = 3
    cm: List = generer_carre_magique(taille)
    if carre_magique_is_ok(cm) > 0:
        if carre_magique_is_ok(cm) == 1:
            afficher_carre_magique(cm, titre="Mon Carré Magique")
        elif carre_magique_is_ok(cm) == 2:
            afficher_carre_magique(cm, titre="Mon Carré Bimagique")
    clean()
