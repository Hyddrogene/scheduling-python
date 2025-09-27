from graphviz import Digraph

# Créer un objet Digraph
dot = Digraph(comment='REDOSPLAT Workflow', format='png')

# Déclaration du problème en syntaxe REDOSPLAT
dot.node('A', 'Déclaration du problème en \n syntaxe REDOSPLAT', shape='box', style='filled', color='lightblue', width='3', height='1.3', fixedsize='true', fontsize='16')

# Analyse comme une Grammaire via COCO/R en C++
dot.node('C', 'Analyse comme une Grammaire\n via COCO/R en C++', shape='box', style='filled', color='lightyellow', width='3', height='1.3', fixedsize='true', fontsize='16')

# Structure intermédiaire
dot.node('D', 'Structure Intermédiaire', shape='box', style='filled', color='green', width='3', height='1.3', fixedsize='true', fontsize='16')

# Possibilités à partir de la structure intermédiaire
dot.node('E1', 'Vérification de Solution', shape='box', style='filled', color='lightpink', width='3', height='1.3', fixedsize='true', fontsize='16')
dot.node('E2', 'Résolution \n ------------\n ILP \n COP \n ...\n', shape='box', style='filled', color='lightpink', width='3', height='1.3', fixedsize='true', fontsize='16')
dot.node('E3', 'Conversion \n ------------\n XHSTT \n ITC 2007 \n ...', shape='box', style='filled', color='lightpink', width='3', height='1.3', fixedsize='true', fontsize='16')

# Ajout des connexions
dot.edge('A', 'C')
dot.edge('C', 'D')
dot.edge('D', 'E1')
dot.edge('D', 'E2')
dot.edge('D', 'E3')

# Afficher le graphique
dot.render('redosplat_workflow_colored', view=True)
