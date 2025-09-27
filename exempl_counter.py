from collections import Counter

def compte_valeurs_differentes(lst):
    # Compter l'occurrence de chaque valeur
    occurrences = Counter(lst)
    max_frequence = max(occurrences.values())
    val = sum([occurrences[i] for i in occurrences])-max_frequence
    print(occurrences)
    return val

# Exemple d'utilisation :
ma_liste = [203, 2, 145, 47, 204, 30, 77, 1, 160, 117, 198, 189, 105, 133, 140, 31, 86, 189, 52, 106, 165, 70, 132, 3, 104, 174, 168, 8, 16, 185, 22, 169, 97, 61, 160, 133, 160, 154, 100, 130, 76, 16, 10, 181, 40, 25, 199, 106]

resultat = compte_valeurs_differentes(ma_liste)
print(f"Nombre de valeurs différentes non maximales : {resultat}")
