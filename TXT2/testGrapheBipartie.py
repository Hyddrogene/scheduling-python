import networkx as nx
import matplotlib.pyplot as plt

# Création du graphe bipartite
B = nx.Graph()

# Liste des éléments (cours, étudiants, salles, enseignants)
cours = ["Cours 1", "Cours 3",  "Cours 5", "Cours 7"]
cours2 = [ "Cours 2", "Cours 4", "Cours 6"]
# Ajouter les noeuds (alternance entre groupes et autres informations)
for i in range(len(cours2)):
    # Ajouter les cours en tant que noeuds
    B.add_node(cours[i], bipartite=0)  # Côté gauche (cours)
    B.add_node(cours2[i], bipartite=1)   # Côté droit (salles)
    # Création des relations (arêtes)
B.add_node(cours[len(cours)-1], bipartite=0)  # Côté gauche (cours)
B.add_node("", bipartite=1)  # Côté gauche (cours)

B.add_edge(cours[0], cours2[0])
B.add_edge(cours2[0], cours[1])
B.add_edge(cours[1], cours2[1])


B.add_edge(cours[2], cours2[2])
B.add_edge(cours2[2], cours[3])

# Dessiner le graphe
left_nodes = [n for n, d in B.nodes(data=True) if d['bipartite'] == 0]
right_nodes = [n for n, d in B.nodes(data=True) if d['bipartite'] == 1]

# Positions pour un graphe bipartite
pos = {}
pos.update((node, (1, index)) for index, node in enumerate(left_nodes))  # Cours à gauche
pos.update((node, (2, index)) for index, node in enumerate(right_nodes))  # Groupes/Salles/Enseignants à droite

# Dessiner les noeuds et arêtes
nx.draw(B, pos, with_labels=True,node_color=['skyblue' if node in left_nodes  else 'lightgreen' for node in B.nodes()], node_size=[1600 for i in range(0,len(cours)+len(cours2)) ]+[0])

# Ajouter les labels "Gauche" et "Droite"
plt.text(1, len(left_nodes) + 0.5, 'Créneau 1', horizontalalignment='center', verticalalignment='center', fontsize=12, bbox=dict(facecolor='white', edgecolor='black', boxstyle='round,pad=0.5'))
plt.text(2, len(right_nodes) + 0.5, 'Créneau 2', horizontalalignment='center', verticalalignment='center', fontsize=12, bbox=dict(facecolor='white', edgecolor='black', boxstyle='round,pad=0.5'))


plt.xlim(0.7, 2.3)  # Ajuste selon le niveau de dézoom que tu veux
plt.ylim(-1, 5)  

plt.show()
