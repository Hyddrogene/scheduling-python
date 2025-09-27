import matplotlib.pyplot as plt
import networkx as nx

# Créer le graphe
G = nx.DiGraph()

# Ajouter les nœuds avec des couleurs spécifiques
nodes = {
    "Teacher1": "#FFB74D",
    "Teacher2": "#81C784",
    "AlgoLec": "#E57373",
    "AlgoLab": "#BA68C8",
    "Algorithmic": "#4FC3F7",
    "AlgoLec1": "#FFCDD2",
    "AlgoLec1:1": "#FFCDD2",
    "AlgoLec1:2": "#FFCDD2",
    "AlgoLec1:3": "#FFCDD2",
    "AlgoLec1:4": "#FFCDD2",
    "AlgoLab1": "#E1BEE7",
    "AlgoLab2": "#E1BEE7",
    "AlgoLab3": "#E1BEE7",
    "AlgoLab1:1": "#E1BEE7",
    "AlgoLab1:2": "#E1BEE7",
    "AlgoLab2:1": "#E1BEE7",
    "AlgoLab2:2": "#E1BEE7",
    "AlgoLab3:1": "#E1BEE7",
    "AlgoLab3:2": "#E1BEE7",
    "Room 1 Lab": "#FFF176",
    "Room 2 Lab": "#4DB6AC",
    "Room 3 Lec": "#FF8A65",
}

for node, color in nodes.items():
    G.add_node(node, color=color)

# Ajouter les arêtes
edges = [
    ("Teacher1", "AlgoLec"),
    ("Teacher2", "AlgoLec"),
    ("AlgoLec", "AlgoLec1"),
    ("AlgoLec", "AlgoLab"),
    ("AlgoLec1", "AlgoLec1:1"),
    ("AlgoLec1", "AlgoLec1:2"),
    ("AlgoLec1", "AlgoLec1:3"),
    ("AlgoLec1", "AlgoLec1:4"),
    ("AlgoLab", "AlgoLab1"),
    ("AlgoLab", "AlgoLab2"),
    ("AlgoLab", "AlgoLab3"),
    ("AlgoLab1", "AlgoLab1:1"),
    ("AlgoLab1", "AlgoLab1:2"),
    ("AlgoLab2", "AlgoLab2:1"),
    ("AlgoLab2", "AlgoLab2:2"),
    ("AlgoLab3", "AlgoLab3:1"),
    ("AlgoLab3", "AlgoLab3:2"),
    ("Algorithmic", "AlgoLec"),
    ("Algorithmic", "AlgoLab"),
    ("Room 1 Lab", "AlgoLab"),
    ("Room 2 Lab", "AlgoLab"),
    ("Room 3 Lec", "AlgoLec"),
]

G.add_edges_from(edges)

# Définir les positions des nœuds manuellement pour créer une structure pyramidale
pos = {
    "Algorithmic": (0, 3),
    "Teacher1": (-1, 2),
    "Teacher2": (1, 2),
    "AlgoLec": (0, 1.5),
    "Room 3 Lec": (0.5, 1.5),
    "AlgoLab": (0, 1),
    "Room 1 Lab": (-0.5, 1),
    "Room 2 Lab": (0.5, 1),
    "AlgoLec1": (-0.5, 0.5),
    "AlgoLab1": (0, 0.5),
    "AlgoLab2": (0.5, 0.5),
    "AlgoLab3": (1, 0.5),
    "AlgoLec1:1": (-0.75, 0),
    "AlgoLec1:2": (-0.5, 0),
    "AlgoLec1:3": (-0.25, 0),
    "AlgoLec1:4": (0, 0),
    "AlgoLab1:1": (0, 0),
    "AlgoLab1:2": (0.25, 0),
    "AlgoLab2:1": (0.5, 0),
    "AlgoLab2:2": (0.75, 0),
    "AlgoLab3:1": (1, 0),
    "AlgoLab3:2": (1.25, 0),
}

# Tracer les nœuds avec les couleurs spécifiées
node_colors = [G.nodes[node]['color'] for node in G.nodes]
nx.draw_networkx_nodes(G, pos, node_color=node_colors, node_size=2000)

# Tracer les arêtes
nx.draw_networkx_edges(G, pos, arrowstyle='-|>', arrowsize=20, edge_color="#455A64")

# Tracer les étiquettes
nx.draw_networkx_labels(G, pos, font_size=10, font_color="black")

# Afficher le graphe
plt.title("Schéma de la configuration de l'algorithme")
plt.axis('off')
plt.show()
