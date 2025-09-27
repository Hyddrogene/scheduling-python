fruits = {"pomme", "banane", "orange", "cerise"}

# Affichage du set
print(fruits)  



import numpy as np
import matplotlib.pyplot as plt

# Générer des données de positions (latitude, longitude)
np.random.seed(42)
latitudes = np.random.uniform(low=-90, high=90, size=100)
longitudes = np.random.uniform(low=-180, high=180, size=100)

# Types de roches (basalte, granite, calcaire)
types_roches = np.random.choice(['Basalte', 'Granite', 'Calcaire'], size=100)

# Attribuer des couleurs aux types de roches
couleurs = {'Basalte': 'red', 'Granite': 'blue', 'Calcaire': 'green'}
couleurs_roches = [couleurs[roche] for roche in types_roches]

# Créer la carte
plt.figure(figsize=(12, 6))
plt.scatter(longitudes, latitudes, c=couleurs_roches, label=types_roches, alpha=0.6, edgecolors='w', s=100)

# Ajouter des légendes
plt.title('Carte de distribution des types de roches')
plt.xlabel('Longitude')
plt.ylabel('Latitude')
plt.grid(True)

# Créer une légende personnalisée
for roche, couleur in couleurs.items():
    plt.scatter([], [], c=couleur, label=roche)

plt.legend(title="Types de roches")
plt.show()
