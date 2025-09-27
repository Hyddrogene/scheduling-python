import numpy as np
import matplotlib.pyplot as plt
import random

# Taille de l'image
width, height = 800, 800
xmin, xmax, ymin, ymax = -2, 2, -2, 2
max_iter = 300

# Génération aléatoire de C
c_real = random.uniform(-1, 1)
c_imag = random.uniform(-1, 1)
C = complex(c_real, c_imag)

# Création de la grille complexe
x = np.linspace(xmin, xmax, width)
y = np.linspace(ymin, ymax, height)
X, Y = np.meshgrid(x, y)
Z = X + 1j * Y

# Initialisation du tableau de résultats
julia_set = np.zeros(Z.shape, dtype=int)

# Calcul de l'ensemble de Julia
for i in range(max_iter):
    mask = np.abs(Z) < 2
    Z[mask] = Z[mask]**2 + C
    julia_set[mask] = i

# Affichage du résultat
plt.figure(figsize=(8, 8))
plt.imshow(julia_set, extent=(xmin, xmax, ymin, ymax), cmap="magma")
plt.colorbar()
plt.title(f"Fractale de Julia pour C = {C:.3f}")
plt.show()
 
