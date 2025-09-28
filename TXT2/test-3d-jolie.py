import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
from mpl_toolkits.mplot3d import Axes3D
import imageio
import os

# Exemple de DataFrame
np.random.seed(0)
df = pd.DataFrame({
    'x': np.random.randn(100),
    'y': np.random.randn(100),
    'z': np.random.randn(100),
    'label': np.random.choice(['A', 'B', 'C'], 100)
})

# Dossier temporaire pour les images
os.makedirs('frames', exist_ok=True)

# Couleurs par classe
color_map = {'A': 'red', 'B': 'green', 'C': 'blue'}

# Générer des vues
for angle in range(0, 360, 5):
    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')

    for label, color in color_map.items():
        subset = df[df['label'] == label]
        ax.scatter(subset['x'], subset['y'], subset['z'], label=label, color=color)

    ax.view_init(30, angle)  # élévation, azimut
    ax.legend()
    plt.tight_layout()
    plt.savefig(f'frames/frame_{angle:03d}.png')
    plt.close()

# Créer le GIF
images = [imageio.imread(f'frames/frame_{angle:03d}.png') for angle in range(0, 360, 5)]
imageio.mimsave('animation.gif', images, fps=10)

# Nettoyage (optionnel)
import shutil
shutil.rmtree('frames')
