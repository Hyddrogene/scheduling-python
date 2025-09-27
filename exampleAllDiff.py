import networkx as nx

def allDifferentRegin(variables):
    # Créer un graphe de compatibilité G
    G = nx.DiGraph()
    
    # Ajouter les nœuds et les arcs
    value_to_nodes = {}
    for vi, domain in variables.items():
        for val in domain:
            node = (vi, val)
            G.add_node(node)
            if val in value_to_nodes:
                for other_node in value_to_nodes[val]:
                    G.add_edge(node, other_node)
                    G.add_edge(other_node, node)
            if val not in value_to_nodes:
                value_to_nodes[val] = []
            value_to_nodes[val].append(node)
    
    # Trouver les composantes fortement connexes
    sccs = list(nx.strongly_connected_components(G))
    
    # Vérifier si une CFC contient plus d'un nœud pour la même variable
    for scc in sccs:
        if any(sum(1 for node in scc if node[0] == var) > 1 for var in variables):
            return False  # La contrainte est violée
    
    # Filtrer les domaines
    valid_values = {vi: set() for vi in variables}  # Initialiser avec des ensembles vides
    for scc in sccs:
        for vi in variables:
            vi_nodes = [(v, val) for v, val in scc if v == vi]
            if len(vi_nodes) == 1:  # S'assurer qu'une seule valeur pour vi est dans cette CFC
                _, val = vi_nodes[0]
                valid_values[vi].add(val)

    for vi in variables:
        variables[vi] = list(valid_values[vi])

    return True  # Toutes les variables ont des valeurs distinctes valides

# Exemple d'utilisation
variables = {
    'x': [1],
    'y': [1, 2, 3],
    'z': [1, 2, 3]
}

result = allDifferentRegin(variables)
print("Résultat de la contrainte allDifferent:", result)
print("Domaines filtrés:", variables)
