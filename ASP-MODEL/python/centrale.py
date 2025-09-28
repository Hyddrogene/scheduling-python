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
##############################################################

def zero_consecutif(tab) :
    res = []
    for i in range(len(tab)):
        if tab[i] == 0 and i< len(tab):
            if tab[i+1] == 0:
                res.append(1)
            else :
                res.append(0)
                
    return res



def couvrir_zeros_ligne(matrice, nr_central, nr_days):
    """
    Couvre tous les zéros de la matrice avec le minimum de lignes et colonnes.
    Retourne les lignes et colonnes couvertes.
    """
    
    lignes_couvertes = set()
    colonnes_couvertes = set()
    zeros_couverts = [ [False] * taille for _ in range(taille) ]


    zero_couverts_ = []
    zero_barre = [[0 for jj in range(nr_central)] for ii in range(nr_days)]
    for i in range(nr_days):
        for j in range(nr_central):
            zero_couverts_ligne = []
            if matrice[i][j] == 0  :
                if j < range(nr_central):
                    if matrice[i][j+1] != 0 :
                        zero_couverts_ligne.append(1)
                    else :
                        zero_couverts_ligne.append(0)
                        for k in range(j,nr_central):
                            zero_barre[i][k] = 1
        zero_couverts_.append(zero_couverts_ligne)
        

    return zero_couverts_, zero_barre

def couvrir_zeros_colone(matrice, nr_central, nr_days):
    """
    Couvre tous les zéros de la matrice avec le minimum de lignes et colonnes.
    Retourne les lignes et colonnes couvertes.
    """
    
    lignes_couvertes = set()
    colonnes_couvertes = set()
    zeros_couverts = [ [False] * taille for _ in range(taille) ]


    zero_couverts_ = []
    zero_barre = [[0 for jj in range(nr_central)] for ii in range(nr_days)]
    for i in range(nr_central):
        for j in range(nr_days):
        zero_couverts_colone = []
        if matrice[j][i] == 0  :
            if j < range(nr_central):
                
                if matrice[i][j+1] != 0 :
                    zero_couverts_ligne.append(1)
                else :
                    zero_couverts_ligne.append(0)
                    for k in range(j,nr_central):
                        zero_barre[i][k] = 1
        zero_couverts_.append(zero_couverts_ligne)
        

    return zero_couverts_, zero_barre


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


def algorithme_hongrois(matrice):
    """
    Implémente l'algorithme hongrois sans bibliothèque externe.
    """
    n = len(matrice)

    # Étape 1 : Réduction des lignes
    matrice = reduire_lignes(matrice)

    # Étape 2 : Réduction des colonnes
    matrice = reduire_colonnes(matrice)

    # Étape 3 : Couvrir les zéros et ajuster la matrice jusqu'à une solution optimale
    while True:
        lignes_couvertes, colonnes_couvertes = couvrir_zeros(matrice)
        total_couvertures = len(lignes_couvertes) + len(colonnes_couvertes)

        if total_couvertures >= n:
            break  # Solution optimale trouvée

        matrice = ajuster_matrice(matrice, lignes_couvertes, colonnes_couvertes)

    # Étape 4 : Trouver les affectations optimales
    affectations = []
    lignes_utilisées = set()
    colonnes_utilisées = set()
    for i in range(n):
        for j in range(n):
            if matrice[i][j] == 0 and i not in lignes_utilisées and j not in colonnes_utilisées:
                affectations.append((i, j))
                lignes_utilisées.add(i)
                colonnes_utilisées.add(j)

    return affectations


# Exemple d'utilisation
if __name__ == "__main__":
    matrice_de_couts = [
        [4, 1, 3],
        [2, 0, 5],
        [3, 2, 2],
        [1, 7, 5]
    ]

    affectations = algorithme_hongrois(matrice_de_couts)
    cout_total = sum(matrice_de_couts[i][j] for i, j in affectations)

    print("Affectations optimales :", affectations)
    print("Coût total optimal :", cout_total)
