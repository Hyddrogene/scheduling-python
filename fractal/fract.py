import numpy as np
import matplotlib.pyplot as plt

# Paramètres de l'image
width, height = 800, 800
xmin, xmax, ymin, ymax = -2, 1, -1.5, 1.5
max_iter = 100

# Création de la grille de pixels
x = np.linspace(xmin, xmax, width)
y = np.linspace(ymin, ymax, height)
X, Y = np.meshgrid(x, y)
C = X + 1j * Y
Z = np.zeros_like(C, dtype=complex)
mandelbrot = np.zeros(C.shape, dtype=int)

# Calcul de l'ensemble de Mandelbrot
for i in range(max_iter):
    mask = np.abs(Z) < 2
    Z[mask] = Z[mask]**2 + C[mask]
    mandelbrot[mask] = i

# Affichage
plt.imshow(mandelbrot, extent=(xmin, xmax, ymin, ymax), cmap='inferno')
plt.colorbar()
plt.title("Fractale de Mandelbrot")
plt.show()
