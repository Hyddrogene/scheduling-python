import numpy as np
import matplotlib.pyplot as plt
import matplotlib.animation as animation

# Taille de l'image et domaine complexe
width, height = 800, 800
xmin, xmax, ymin, ymax = -2, 2, -2, 2
max_iter = 300

# Création de la grille complexe
x = np.linspace(xmin, xmax, width)
y = np.linspace(ymin, ymax, height)
X, Y = np.meshgrid(x, y)

# Préparation de la figure
fig, ax = plt.subplots(figsize=(6,6))
img = ax.imshow(np.zeros((width, height)), extent=(xmin, xmax, ymin, ymax), cmap="inferno")

# Fonction d'animation
def update(frame):
    # Variation progressive du paramètre C
    c_real = np.cos(frame / 20) * 0.8  # Variation douce entre -0.8 et 0.8
    c_imag = np.sin(frame / 20) * 0.8  # Variation douce entre -0.8 et 0.8
    C = complex(c_real, c_imag)

    # Initialisation du calcul
    Z = X + 1j * Y
    julia_set = np.zeros(Z.shape, dtype=int)

    # Calcul de l'ensemble de Julia
    for i in range(max_iter):
        mask = np.abs(Z) < 2
        Z[mask] = Z[mask]**2 + C
        julia_set[mask] = i

    # Mise à jour de l'image
    img.set_array(julia_set)
    ax.set_title(f"Fractale de Julia (C = {C:.3f})")
    return [img]

# Création de l'animation
ani = animation.FuncAnimation(fig, update, frames=200, interval=50, blit=False)

# Affichage
plt.show()
 
