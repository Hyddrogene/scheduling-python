from collections import Counter





def count_unequal_pairs(array):
    """
    Compte le nombre de paires dans un tableau où les valeurs sont différentes.

    Args:
        array (list): Le tableau d'entrée.

    Returns:
        int: Le nombre de paires avec des valeurs différentes.
    """
    count = 0
    n = len(array)

    # Parcours de toutes les paires possibles
    for i in range(n):
        for j in range(i + 1, n):  # Commencer à j = i+1 pour éviter les doublons
            if array[i] != array[j]:
                count += 1

    return count


def count_unequal_pairs3(array):
    """
    Compte le nombre de paires dans un tableau où les valeurs sont différentes,
    en optimisant avec la fréquence des éléments.

    Args:
        array (list): Le tableau d'entrée.

    Returns:
        int: Le nombre de paires avec des valeurs différentes.
    """
    n = len(array)
    total_pairs = n * (n - 1) // 2  # Total de paires possibles
    
    # Compter les fréquences des éléments
    freq = Counter(array)
    
    # Calculer le nombre de paires égales
    equal_pairs = sum(count * (count - 1) // 2 for count in freq.values())
    
    # Les paires différentes sont le complément des paires égales
    return total_pairs - equal_pairs



def count_unequal_pairs2(array):
    """
    Compte le nombre total de paires où les valeurs sont différentes.

    Args:
        array (list): Le tableau d'entrée.

    Returns:
        int: Le nombre de paires avec des valeurs différentes.
    """
    n = len(array)
    total_pairs = n * (n - 1) // 2  # Toutes les paires possibles
    equal_pairs = sum(array.count(value) * (array.count(value) - 1) // 2 for value in set(array))
    return total_pairs - equal_pairs

# Exemple d'utilisation
#tableau = [1, 2, 2, 3, 4, 4, 4, 5]
tableau = [1, 2, 3, 4, 5, 6, 7, 5]
tableau = [5,6,6,6,6,7]
nombre_paires_differentes = count_unequal_pairs2(tableau)
print(f"Le nombre de paires différentes est : {nombre_paires_differentes}")
