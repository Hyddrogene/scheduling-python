import matplotlib.pyplot as plt
import numpy as np

# Création de l'axe du temps
t0 = 0  # date de début
tn = 10  # date de fin
grain = 1  # Grain standard

# Points sur l'axe temporel
time_points = np.arange(t0, tn + grain, grain)
time_pointsStr = ["t"+str(i) for i in time_points] 
"""
# Création du graphique avec des personnalisations
plt.figure(figsize=(12, 3))

# Dessiner l'axe du temps avec un style personnalisé
plt.hlines(1, t0, tn, colors='darkgreen', linewidth=3, linestyles='dashed')

# Ajouter des marqueurs personnalisés pour les dates de début, fin et créneaux horaires
for t, label in zip(time_points, time_pointsStr):
    plt.vlines(t, 0.95, 1.05, colors='red', linewidth=2, linestyles='solid')
    plt.text(t, 1.05, label, ha='center', va='bottom', fontsize=12, fontweight='bold', color='blue')

# Ajouter les étiquettes de la date de début et de fin avec une police personnalisée
plt.text(t0, 0.85, 'Date de Début (t0=0)', ha='center', va='bottom', fontsize=10, fontstyle='italic', color='purple')
plt.text(tn, 0.85, f'Date de Fin (tn={tn})', ha='center', va='bottom', fontsize=10, fontstyle='italic', color='purple')

# Enlever les axes inutiles
plt.yticks([])
plt.xticks([])

# Titre et légende
plt.title("Personnalisation d'un Horizon Temporel avec un Grain de 1 Unité de Temps", fontsize=16, color='darkblue', pad=20)

# Affichage du graphique
plt.show()
"""

# Création du graphique avec ajustement pour rapprocher les étiquettes de début et de fin
plt.figure(figsize=(12, 3))

# Dessiner l'axe du temps avec un style personnalisé
plt.hlines(1, t0, tn, colors='darkgreen', linewidth=3, linestyles='dashed')

# Ajouter des marqueurs personnalisés pour les dates de début, fin et créneaux horaires
for t, label in zip(time_points, time_pointsStr):
    plt.vlines(t, 0.95, 1.05, colors='red', linewidth=2, linestyles='solid')
    plt.text(t, 1.05, label, ha='center', va='bottom', fontsize=12, fontweight='bold', color='blue')

# Ajustement des étiquettes de début et de fin pour les rapprocher des barres
plt.text(t0, 0.92, 'Date de Début (t0=0)', ha='center', va='top', fontsize=10, fontstyle='italic', color='purple')
plt.text(tn, 0.92, f'Date de Fin (tn={tn})', ha='center', va='top', fontsize=10, fontstyle='italic', color='purple')

# Enlever les axes inutiles
plt.yticks([])
plt.xticks([])

# Titre et légende
plt.title("Horizon Temporel avec un Grain de 1 Unité de Temps", fontsize=16, color='darkblue', pad=20)

# Affichage du graphique
plt.show()
