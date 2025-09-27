import numpy as np
import matplotlib.pyplot as plt
from numba import cuda, jit

# Taille de l'image
width, height = 800, 800
xmin, xmax, ymin, ymax = -2, 2, -2, 2
max_iter = 300

# Création de la grille complexe
x = np.linspace(xmin, xmax, width)
y = np.linspace(ymin, ymax, height)
X, Y = np.meshgrid(x, y)
Z = X + 1j * Y

# Paramètre C pour Julia (fixe ou aléatoire)
C = complex(-0.7, 0.27015)

# ⬇️ Accélération GPU avec Numba CUDA
@cuda.jit
def julia_kernel(d_Z_real, d_Z_imag, d_C_real, d_C_imag, d_result, max_iter):
    """ Calcul de la fractale de Julia sur GPU """
    i, j = cuda.grid(2)  # Récupère l'index du thread dans la grille
    if i < d_Z_real.shape[0] and j < d_Z_real.shape[1]:
        zr, zi = d_Z_real[i, j], d_Z_imag[i, j]  # Partie réelle et imaginaire
        cr, ci = d_C_real[0], d_C_imag[0]
        iteration = 0

        while zr * zr + zi * zi < 4 and iteration < max_iter:
            new_zr = zr * zr - zi * zi + cr
            zi = 2 * zr * zi + ci
            zr = new_zr
            iteration += 1

        d_result[i, j] = iteration

# Allocation mémoire GPU
d_Z_real = cuda.to_device(X)
d_Z_imag = cuda.to_device(Y)
d_C_real = cuda.to_device(np.array([C.real], dtype=np.float64))
d_C_imag = cuda.to_device(np.array([C.imag], dtype=np.float64))
d_result = cuda.device_array((width, height), dtype=np.int32)

# Lancement du kernel CUDA avec un maillage optimal
threads_per_block = (16, 16)
blocks_per_grid_x = (width + threads_per_block[0] - 1) // threads_per_block[0]
blocks_per_grid_y = (height + threads_per_block[1] - 1) // threads_per_block[1]
blocks_per_grid = (blocks_per_grid_x, blocks_per_grid_y)

julia_kernel[blocks_per_grid, threads_per_block](d_Z_real, d_Z_imag, d_C_real, d_C_imag, d_result, max_iter)

# Récupération des résultats du GPU
julia_set = d_result.copy_to_host()

# Affichage du résultat
plt.figure(figsize=(8, 8))
plt.imshow(julia_set, extent=(xmin, xmax, ymin, ymax), cmap="inferno")
plt.colorbar()
plt.title(f"Fractale de Julia sur GPU (C = {C})")
plt.show()
 
