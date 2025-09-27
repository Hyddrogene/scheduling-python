from graphviz import Digraph


    #4682B4 (SteelBlue)
    #8B4513 (SaddleBrown)
    #FF6347 (Tomato)
    #2E8B57 (SeaGreen)
    #DA70D6 (Orchid)


# Définir les couleurs dans des variables
color_teacher1 = "#FF4500"    # Orange Red
color_teacher1 = "#4682B4"    #(SteelBlue)

color_teacher2 = "#32CD32"    # Lime Green
color_teacher2 = "#8B4513"    # SaddleBrown

color_algolec = "#DC143C"     # Crimson
color_algolab = "#8A2BE2"     # Blue Violet
color_algorithmic = "#1E90FF" # Dodger Blue
color_algolec1 = "#FF7F7F"    # Light Coral
color_algolab1 = "#9370DB"    # Medium Purple

color_room1 = "#FFFF00"       # Yellow
color_room1 = "#FF6347"       # Tomato

color_room2 = "#00CED1"       # Dark Turquoise
color_room2 = "#00CED1"       # SeaGreen

color_room3 = "#FF6347"       # Tomato
color_room3 = "#DA70D6"       # Orchid

# Dégradé pour AlgoLec et ses enfants
algo_lec_colors = ["#DC143C", "#E57373", "#FFB6C1", "#FFDAB9"]
algo_lab_colors = ["#8A2BE2", "#9370DB", "#BA68C8", "#D8BFD8"]

algo_lec_colors = ["#DC143C", "#FF4500", "#FF6347", "#FFA07A"]
algo_lab_colors = ["#6A5ACD", "#7B68EE", "#9370DB", "#BA55D3"]

algo_lec_colors = ["#FF6347", "#FF7F50", "#FF8C00", "#FFA500"]
algo_lab_colors = ["#483D8B", "#6A5ACD", "#7B68EE", "#8A2BE2"]

algo_lec_colors = ["#FF0000", "#FF4500", "#FF6347", "#FF7F50"]
algo_lab_colors = ["#4B0082", "#6A5ACD", "#7B68EE", "#9370DB"]

algo_lec_colors = ["#E9967A", "#FA8072", "#FFA07A", "#FFB6C1"]
algo_lab_colors = ["#8A2BE2", "#9370DB", "#BA55D3", "#DDA0DD"]

algo_lec_colors = ["#8B0000", "#FF4500", "#FF7F50", "#FFD700"]
algo_lab_colors = ["#2E0854", "#4B0082", "#663399", "#8A2BE2"]

algo_lec_colors = ["#A52A2A", "#D2691E", "#FF7F50", "#FFDAB9"]
algo_lab_colors = ["#2E0854", "#4B0082", "#6A5ACD", "#9370DB"]


algo_lec_colors = ["#800000", "#B22222", "#DC143C", "#FA8072"]
algo_lab_colors = ["#3A005F", "#4B0082", "#6A5ACD", "#8A2BE2"]

algo_lec_colors = ["#FF4500", "#FFA07A", "#FFD700", "#FFFACD"]
algo_lab_colors = ["#006400", "#32CD32", "#7FFF00", "#ADFF2F"]


# Créer le graphe
dot = Digraph()

# Ajouter les nœuds avec des couleurs spécifiques et formes
nodes = {
    "Teacher1": (color_teacher1, "oval", "black", "1"),
    "Teacher2": (color_teacher2, "oval", "black", "1"),
    "AlgoLec": (algo_lec_colors[0], "box", "red", "4"),
    "AlgoLab": (algo_lab_colors[0], "box", "black", "1"),
    "Algorithmic": (color_algorithmic, "box", "black", "1"),
    "AlgoLec1": (algo_lec_colors[1], "box", "black", "1"),
    "AlgoLec1-1": (algo_lec_colors[2], "box", "black", "1"),
    "AlgoLec1-2": (algo_lec_colors[2], "box", "black", "1"),
    "AlgoLec1-3": (algo_lec_colors[2], "box", "black", "1"),
    "AlgoLec1-4": (algo_lec_colors[2], "box", "black", "1"),
    "AlgoLab1": (algo_lab_colors[1], "box", "black", "1"),
    "AlgoLab2": (algo_lab_colors[1], "box", "black", "1"),
    "AlgoLab3": (algo_lab_colors[1], "box", "black", "1"),
    "AlgoLab1-1": (algo_lab_colors[2], "box", "black", "1"),
    "AlgoLab1-2": (algo_lab_colors[2], "box", "black", "1"),
    "AlgoLab2-1": (algo_lab_colors[2], "box", "black", "1"),
    "AlgoLab2-2": (algo_lab_colors[2], "box", "black", "1"),
    "AlgoLab3-1": (algo_lab_colors[2], "box", "black", "1"),
    "AlgoLab3-2": (algo_lab_colors[2], "box", "black", "1"),
    "Room 1 Lab": (color_room1, "oval", "black", "1"),
    "Room 2 Lab": (color_room2, "oval", "black", "1"),
    "Room 3 Lec": (color_room3, "oval", "red", "4"),
}

#for node, (color, shape) in nodes.items():
#    dot.node(node, style="filled", fillcolor=color, shape=shape)
# Ajouter les noeuds
for node, (color, shape, border_color, penwidth) in nodes.items():
    dot.node(node, shape=shape, style='filled', fillcolor=color, color=border_color, penwidth=penwidth)


# Ajouter les arêtes
edges = [
    ("Teacher1", "AlgoLec"),
    ("Teacher1", "AlgoLab"),
    ("Teacher2", "AlgoLab"),
    ("Algorithmic", "AlgoLec"),
    ("Algorithmic", "AlgoLab"),
    ("AlgoLec", "AlgoLec1"),
    ("AlgoLec1", "AlgoLec1-1"),
    ("AlgoLec1", "AlgoLec1-2"),
    ("AlgoLec1", "AlgoLec1-3"),
    ("AlgoLec1", "AlgoLec1-4"),
    ("AlgoLab", "AlgoLab1"),
    ("AlgoLab", "AlgoLab2"),
    ("AlgoLab", "AlgoLab3"),
    ("AlgoLab1", "AlgoLab1-1"),
    ("AlgoLab1", "AlgoLab1-2"),
    ("AlgoLab2", "AlgoLab2-1"),
    ("AlgoLab2", "AlgoLab2-2"),
    ("AlgoLab3", "AlgoLab3-1"),
    ("AlgoLab3", "AlgoLab3-2"),
    ("Room 1 Lab", "AlgoLab"),
    ("Room 2 Lab", "AlgoLab"),
    ("Room 3 Lec", "AlgoLec"),
]

for edge in edges:
    dot.edge(*edge)

# Ajouter un nœud invisible pour alignement
dot.node("Invisible", style="invis")
dot.node("Invisible2", style="invis")
dot.node("Invisible3", style="invis")
dot.node("Invisible4", style="invis")
dot.node("Invisible5", style="invis")


# Ajouter des arêtes invisibles pour alignement
invisible_edges = [
    ("Teacher1", "Algorithmic"),
    ("Teacher2", "Algorithmic"),
    ("Room 1 Lab", "Algorithmic"),
    ("Room 2 Lab", "Algorithmic"),
    ("Room 3 Lec", "Algorithmic"),
    ("Invisible", "Algorithmic"),
    ("Algorithmic", "Invisible2"),
    ("Algorithmic", "Invisible4"),
    ("Algorithmic", "Invisible5"),

    ("Algorithmic", "Invisible3")
]

for edge in invisible_edges:
    dot.edge(edge[0], edge[1], style="invis")

# Sous-graphe pour forcer l'alignement horizontal
with dot.subgraph() as s:
    s.attr(rank='same')
    s.node('AlgoLec')
    s.node('AlgoLab')
    

dot.attr(nodesep='0.1', ranksep='0.5', pad='0.5')

with dot.subgraph() as s:
    s.attr(nodesep='0', ranksep='0', pad='0')    
    s.node('AlgoLab1-1')
    s.node('AlgoLab1-2')


#dot.edge('AlgoLab1-1', 'AlgoLab1-2', constraint='false')
# Ajouter les noeuds
#for node, (color, shape) in nodes.items():
#    border_color = 'red' if color == '#FF4500' else 'black'
#    penwidth = '4' if border_color == 'red' else '2'
#    dot.node(node, shape=shape, style='filled', fillcolor=color, color=border_color, penwidth=penwidth)

#dot.attr(nodesep='0', ranksep='0.5', pad='0.5')
# Alignement en mode pyramide
dot.attr(rankdir='TB') # De haut en bas

# Sauvegarder et afficher le graphe
dot.render("pyramidal_graph", format="png", cleanup=True)
dot.view()
