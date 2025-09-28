from graphviz import Digraph

# Créer un nouveau graphe dirigé
dot = Digraph(comment='Simplified Ontology Schema for GTP')

# Ajouter des nœuds pour les Constructs
constructs = {
    "Entities": "Entities\n(Type, Other attributes)",
    "Resources": "Resources\n(Type, Other attributes)",
    "Activities": "Activities\n(Type, Other attributes)",
    "E-meshes": "E-meshes\n(Mesh cell structure)",
    "R-meshes": "R-meshes\n(Mesh cell structure)",
    "Rules": "Rules\n(Business, Physical)"
}

for key, label in constructs.items():
    dot.node(key, label, shape='box', style='filled', fillcolor='lightgrey')

# Ajouter des arêtes pour les Relationships
relationships = [
    ("Entities", "Resources", "seize"),
    ("Entities", "Activities", "perform"),
    ("Activities", "E-meshes", "map to"),
    ("Activities", "R-meshes", "map to"),
    ("Activities", "Rules", "follow")
]

for start, end, label in relationships:
    dot.edge(start, end, label=label)

# Ajouter des symboles de direction et de concurrence
dot.attr('edge', arrowhead='none')
dot.edge("Entities", "Activities", label="", arrowtail="dot", dir="both")
dot.edge("Resources", "Activities", label="", arrowtail="dot", dir="both")
dot.edge("E-meshes", "Activities", label="", arrowtail="dot", dir="both")
dot.edge("R-meshes", "Activities", label="", arrowtail="dot", dir="both")

# Sauvegarder et afficher le graphe
dot.render('detailed_simplified_ontology', format='png', cleanup=True)
dot.view()
