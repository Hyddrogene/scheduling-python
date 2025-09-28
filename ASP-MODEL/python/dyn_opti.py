def minimize_cost_with_choices(costs):
    if not costs:
        return 0, []

    n = len(costs)
    dp = [[0] * 3 for _ in range(n)]
    choices = [[-1] * 3 for _ in range(n)]  # Stocker les choix

    # Initialiser le premier jour
    dp[0] = costs[0]

    # Calculer les coûts et stocker les choix pour les jours suivants
    for i in range(1, n):
        for j in range(3):  # Pour chaque vêtement
            # Trouver le coût minimum des autres vêtements
            min_cost = float('inf')
            chosen_prev = -1
            for k in range(3):
                if k != j and dp[i-1][k] < min_cost:
                    min_cost = dp[i-1][k]
                    chosen_prev = k
            dp[i][j] = costs[i][j] + min_cost
            choices[i][j] = chosen_prev  # Stocker le choix précédent

    # Trouver le coût minimum et son index dans la dernière ligne
    min_cost = min(dp[-1])
    last_choice = dp[-1].index(min_cost)

    # Reconstruire les choix à rebours
    path = []
    current_choice = last_choice
    for i in range(n-1, -1, -1):
        path.append(current_choice)
        current_choice = choices[i][current_choice]
    path.reverse()  # Remettre dans l'ordre chronologique

    return min_cost, path

# Exemple d'utilisation
costs = [
    [100, 20, 30],
    [20, 30, 10],
    [40, 30, 50],
    [30, 10, 20]
]
a = "’"
min_cost, path = minimize_cost_with_choices(costs)
print("Coût minimum:", min_cost)
print("Choix des vêtements par jour:", path)
