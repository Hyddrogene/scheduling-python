def reduire_lignes(matrice):
    """
    Réduit chaque ligne en soustrayant le plus petit élément de chaque ligne à tous ses éléments.
    """
    for i in range(len(matrice)):
        min_ligne = min(matrice[i])
        matrice[i] = [x - min_ligne for x in matrice[i]]
    return matrice


def reduire_colonnes(matrice):
    """
    Réduit chaque colonne en soustrayant le plus petit élément de chaque colonne à tous ses éléments.
    """
    for j in range(len(matrice[0])):
        min_colonne = min(matrice[i][j] for i in range(len(matrice)))
        for i in range(len(matrice)):
            matrice[i][j] -= min_colonne
    return matrice


def couvrir_zeros(matrice):
    """
    Couvre tous les zéros de la matrice avec le minimum de lignes et colonnes.
    Retourne les lignes et colonnes couvertes.
    """
    taille = len(matrice)
    lignes_couvertes = set()
    colonnes_couvertes = set()
    zeros_couverts = [[False] * taille for _ in range(taille)]

    for i in range(taille):
        for j in range(taille):
            if matrice[i][j] == 0 and i not in lignes_couvertes and j not in colonnes_couvertes:
                zeros_couverts[i][j] = True
                lignes_couvertes.add(i)
                colonnes_couvertes.add(j)

    return lignes_couvertes, colonnes_couvertes


def ajuster_matrice(matrice, lignes_couvertes, colonnes_couvertes):
    """
    Ajuste la matrice en fonction des lignes et colonnes couvertes :
    - Soustrait le plus petit élément des éléments non couverts.
    - Ajoute ce même élément aux intersections des lignes et colonnes couvertes.
    """
    non_couverts = [
        matrice[i][j]
        for i in range(len(matrice))
        for j in range(len(matrice[0]))
        if i not in lignes_couvertes and j not in colonnes_couvertes
    ]

    if non_couverts:
        min_non_couvert = min(non_couverts)
        for i in range(len(matrice)):
            for j in range(len(matrice[0])):
                if i not in lignes_couvertes and j not in colonnes_couvertes:
                    matrice[i][j] -= min_non_couvert
                elif i in lignes_couvertes and j in colonnes_couvertes:
                    matrice[i][j] += min_non_couvert

    return matrice


def modifier_pour_jours_consecutifs(matrice):
    """
    Modifie la matrice pour interdire les affectations de jours consécutifs.
    """
    inf = float('inf')
    n = len(matrice)

    # Ajouter un coût infini pour interdire les jours consécutifs
    for i in range(n):
        for j in range(1, n):
            matrice[i][j - 1] = inf  # Par exemple, si i fait j, i ne peut pas faire j+1

    return matrice


def algorithme_hongrois_contrainte_jours(matrice):
    """
    Implémente l'algorithme hongrois en intégrant la contrainte des jours consécutifs.
    """
    # Modifier la matrice pour interdire les jours consécutifs
    matrice = modifier_pour_jours_consecutifs(matrice)

    # Étape 1 : Réduction des lignes
    matrice = reduire_lignes(matrice)

    # Étape 2 : Réduction des colonnes
    matrice = reduire_colonnes(matrice)

    # Étape 3 : Couvrir les zéros et ajuster la matrice jusqu'à une solution optimale
    while True:
        lignes_couvertes, colonnes_couvertes = couvrir_zeros(matrice)
        total_couvertures = len(lignes_couvertes) + len(colonnes_couvertes)

        if total_couvertures >= len(matrice):
            break  # Solution optimale trouvée

        matrice = ajuster_matrice(matrice, lignes_couvertes, colonnes_couvertes)

    # Étape 4 : Trouver les affectations optimales
    affectations = []
    lignes_utilisées = set()
    colonnes_utilisées = set()
    for i in range(len(matrice)):
        for j in range(len(matrice[0])):
            if matrice[i][j] == 0 and i not in lignes_utilisées and j not in colonnes_utilisées:
                affectations.append((i, j))
                lignes_utilisées.add(i)
                colonnes_utilisées.add(j)

    return affectations


# Exemple d'utilisation
if __name__ == "__main__":
    matrice_de_couts = [
        [4, 1, 3, 2],
        [2, 0, 5, 3],
        [3, 2, 2, 4]
    ]

    affectations = algorithme_hongrois_contrainte_jours(matrice_de_couts)
    cout_total = sum(matrice_de_couts[i][j] for i, j in affectations)

    print("Affectations optimales :", affectations)
    print("Coût total optimal :", cout_total)
