from graphviz import Digraph

# Définir les couleurs
color_teacher1 = "#FF6347"
color_teacher2 = "#4682B4"
color_algorithmic = "#8B4513"
color_room1 = "#2E8B57"
color_room2 = "#DA70D6"
color_room3 = "#8B0000"

algo_lec_colors = ["#FF4500", "#FFA07A", "#FFD700", "#FFFACD"]
algo_lab_colors = ["#006400", "#32CD32", "#7FFF00", "#ADFF2F"]

nodes = {
    "Teacher1": (color_teacher1, "oval", "black", "2"),
    "Teacher2": (color_teacher2, "oval", "black", "2"),
    "AlgoLec": (algo_lec_colors[0], "box", "red", "4"),
    "AlgoLab": (algo_lab_colors[0], "box", "black", "2"),
    "Algorithmic": (color_algorithmic, "box", "black", "2"),
    "AlgoLec1": (algo_lec_colors[1], "box", "black", "2"),
    "AlgoLec1-1": (algo_lec_colors[2], "box", "black", "2"),
    "AlgoLec1-2": (algo_lec_colors[2], "box", "black", "2"),
    "AlgoLec1-3": (algo_lec_colors[2], "box", "black", "2"),
    "AlgoLec1-4": (algo_lec_colors[2], "box", "black", "2"),
    "AlgoLab1": (algo_lab_colors[1], "box", "black", "2"),
    "AlgoLab2": (algo_lab_colors[1], "box", "black", "2"),
    "AlgoLab3": (algo_lab_colors[1], "box", "black", "2"),
    "AlgoLab1-1": (algo_lab_colors[2], "box", "black", "2"),
    "AlgoLab1-2": (algo_lab_colors[2], "box", "black", "2"),
    "AlgoLab2-1": (algo_lab_colors[2], "box", "black", "2"),
    "AlgoLab2-2": (algo_lab_colors[2], "box", "black", "2"),
    "AlgoLab3-1": (algo_lab_colors[2], "box", "black", "2"),
    "AlgoLab3-2": (algo_lab_colors[2], "box", "black", "2"),
    "Room 1 Lab": (color_room1, "oval", "black", "2"),
    "Room 2 Lab": (color_room2, "oval", "black", "2"),
    "Room 3 Lec": (color_room3, "oval", "red", "4"),
}

# Créer le graphique
dot = Digraph()

# Ajuster les paramètres globaux pour réduire les espaces par défaut
dot.attr(nodesep='0', ranksep='0.5', pad='0.5')

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


#dot.node("Invisible6", style="invis")
dot.node('Invisible6', shape='point', width='0.5', height='0.5', style='invis')


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
    ("Invisible6", "AlgoLab1-2"),
    ("Invisible6", "AlgoLab2-1"),
    
    ("Algorithmic", "Invisible3")
]

for edge in invisible_edges:
    dot.edge(edge[0], edge[1], style="invis")



# Coller deux noeuds ensemble en utilisant le même rang
with dot.subgraph() as s:
    s.attr(rank='same')
    s.node('Invisible6')
    s.node('AlgoLab1-2')
    s.node('AlgoLab2-1')
    
with dot.subgraph() as s:
    s.attr(rank='same')
    s.node('Teacher1')
    s.node('Teacher2')

# Ajouter un sous-graphe avec un plus grand espace vertical
with dot.subgraph() as s:
    s.attr(rank='same', ranksep='2')
    s.node('AlgoLec')
    s.node('AlgoLab')

# Ajouter plus de noeuds collés si nécessaire avec des espaces verticaux différents
with dot.subgraph() as s:
    s.attr(rank='same', ranksep='3')
    s.node('Room 1 Lab')
    s.node('Room 2 Lab')
    


# Visualiser le graphique
dot.render('graph', format='png', view=True)
