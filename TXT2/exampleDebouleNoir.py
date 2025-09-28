from graphviz import Digraph
from IPython.display import Image

# Créer un graphique
dot = Digraph()

# Ajouter un gros nœud rond noir
dot.node('', shape='circle', style='filled', fillcolor='black', width='1', height='1')

# Ajouter un nœud losange vide
#dot.node('', shape='diamond', style='filled', fillcolor='white', color='black', width='0.5', height='0.5')

# Enregistrer et afficher l'image
dot.render('nodes_graph2', format='png', view=False)

# Afficher l'image dans le notebook Jupyter
Image(filename='nodes_graph2.png')
