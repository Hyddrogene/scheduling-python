import networkx as nx
import community  # c'est le module python-louvain
import matplotlib.pyplot as plt
import json
from collections import defaultdict
from collections import Counter


def apply_louvain(G):
    """
    Applique Louvain et retourne un dictionnaire : variable → communauté
    """
    partition = community.best_partition(G, weight='weight', resolution=0.3)
    #partition = community.best_partition(G, weight='weight', threshold=0.001)

    return partition


# === CHARGEMENT DU JSON ====

#def load_constraints(json_data):
#    constraints = json_data.get("CONSTRAINTS", [])
#    return constraints

def load_resource_data(filename):

    with open(filename, "r") as f:
        data = json.load(f)

    constriants =  data.get("CONSTRAINTS", [])
    part_rooms = data["DATA"].get("part_rooms", [])
    part_teachers = data["DATA"].get("part_teachers", [])
    part_name = data["DATA"].get("part_name", [])
    part_classes = data["DATA"].get("part_classes", [])
    part_nr_sessions = data["DATA"].get("part_nr_sessions", [])
    nr_sessions = data["DATA"].get("nr_sessions", 0)
    part_label = data["DATA"].get("part_label",[])
    label_name = data["DATA"].get("label_name",[])

    return constriants,part_rooms, part_teachers, part_classes, part_nr_sessions, nr_sessions,part_name,part_label,label_name



# ==== CONVERSION EN CONTRAINTES RCPSP ====



def convert_constraints_to_graph2(G, constraints):
    

    for c in constraints:
        elements = c.get("elements", [])
        sessions = c.get("sessions", [])[0].get("set")
        weight = len(sessions)



        # Si plus d'un élément, créer les arêtes pondérées
        if len(sessions) >= 1:
            for i in range(len(sessions)):
                for j in range(i + 1, len(sessions)):
                    u = sessions[i]
                    v = sessions[j]
                    if G.has_edge(u, v):
                        G[u][v]["weight"] += weight
                    else:
                        G.add_edge(u, v, weight=weight)

    return G


def print_communities_by_group(partition,part_name, session_to_part):
    grouped = defaultdict(list)
    for node, community_id in partition.items():
        grouped[community_id].append(node)

    print("\n🧩 Communautés détectées :")
    for i, (comm_id, nodes) in enumerate(grouped.items(), 1):
        if len(nodes) > 2 :
            print(f"Communauté {i} ({comm_id}) :")
            print(f"  {sorted(nodes)}\n")




def print_communities_by_group_with_parts(partition, part_name, session_to_part):
    grouped = defaultdict(list)
    for node, community_id in partition.items():
        grouped[community_id].append(node)

    print("\n🧩 Communautés détectées :")
    nodes2 = []
    for i, (comm_id, nodes) in enumerate(grouped.items(), 1):
        if len(nodes) > 2:
            print(f"Communauté {i} ({comm_id}) :")
            print(f"  Sessions : {sorted(nodes)}")

            # Récupérer les indices de parts associés à ces sessions
            part_indices = set()
            for node in nodes:
                part_idx = session_to_part.get(node)
                if part_idx is not None:
                    part_indices.add(part_idx)

            # Affichage des noms de parts uniques
            part_labels = [part_name[idx] for idx in sorted(part_indices) if idx < len(part_name)]
            print(f"  Parts impliquées : {part_labels}\n")
        else:
            nodes2.append(nodes[0])
        
    print(f"Communauté unique ({comm_id+1}) :")
    print(f"  Sessions : {sorted(nodes2)}")

    # Récupérer les indices de parts associés à ces sessions
    part_indices = set()
    for node in nodes2:
        part_idx = session_to_part.get(node)
        if part_idx is not None:
            part_indices.add(part_idx)

    # Affichage des noms de parts uniques
    part_labels = [part_name[idx] for idx in sorted(part_indices) if idx < len(part_name)]
    print(f"  Parts impliquées : {part_labels}\n")

# Exemple d'appel (remplace les variables par tes données réelles)
# print_communities_by_group_with_parts(partition, part_name, session_to_part)




def build_community_graph(G, partition):
    community_edges = defaultdict(float)
    community_graph = nx.Graph()

    for u, v, data in G.edges(data=True):
        cu = partition[u]
        cv = partition[v]
        if cu == cv:
            continue  # interne à la communauté
        key = tuple(sorted((cu, cv)))
        community_edges[key] += data.get("weight", 1.0)

    for (cu, cv), w in community_edges.items():
        community_graph.add_edge(cu, cv, weight=w)

    return community_graph







# Ajoute des arêtes pondérées entre toutes les paires de sessions associées à un même enseignant
def add_teacher_edges_from_sessions(G, teacher_to_sessions, nr_parts, weit ):
    print(weit)
    
    for teacher_id, sessions in teacher_to_sessions.items():

        weight = weit[teacher_id]
        weight = 1
        # Ajouter des arêtes entre toutes les paires de sessions associées
        for i in range(len(sessions)):
            for j in range(i + 1, len(sessions)):
                u, v = sessions[i], sessions[j]
                if G.has_edge(u, v):
                    G[u][v]["weight"] += weight
                else:
                    G.add_edge(u, v, weight=weight)

    return G





# Simulation d'une structure similaire à celle chargée depuis un fichier
def generate_session_to_part_map(part_classes, part_nr_sessions):
    session_to_part = {}
    session_id = 1

    for part_index, part in enumerate(part_classes):
        num_sessions = part_nr_sessions[part_index]
        num_classes = len(part["set"])
        total_sessions = num_sessions * num_classes

        for _ in range(total_sessions):
            session_to_part[session_id] = part_index
            session_id += 1

    return session_to_part


# Génère le mapping inverse : part_index -> liste des sessions associées
def generate_part_to_sessions_map(part_classes, part_nr_sessions):
    part_to_sessions = {}
    session_id = 1

    for part_index, part in enumerate(part_classes):
        num_sessions = part_nr_sessions[part_index]
        num_classes = len(part["set"])
        total_sessions = num_sessions * num_classes

        part_to_sessions[part_index] = [i for i in range(session_id, session_id + total_sessions)]
        session_id += total_sessions

    print("session_id",session_id)
    return part_to_sessions


def generate_teacher_to_parts_map(part_teachers):
    teacher_to_parts = {}

    for part_index, teacher_group in enumerate(part_teachers):
        for teacher_id in teacher_group["set"]:
            if teacher_id not in teacher_to_parts:
                teacher_to_parts[teacher_id] = []
            teacher_to_parts[teacher_id].append(part_index)

    return teacher_to_parts

# Utilise teacher_to_parts et part_to_sessions pour générer teacher -> sessions
def generate_teacher_to_sessions_map(teacher_to_parts, part_to_sessions):
    teacher_to_sessions = {}

    for teacher, parts in teacher_to_parts.items():
        sessions = []
        for part in parts:
            sessions.extend(part_to_sessions.get(part, []))
        teacher_to_sessions[teacher] = sessions

    return teacher_to_sessions



def generate_teacher_weights(teacher_to_parts, part_to_sessions, part_teachers):
    teacher_weights = {}

    for teacher_id, parts in teacher_to_parts.items():
        total_sessions = 0
        total_teachers = 0

        for part_index in parts:
            sessions = part_to_sessions.get(part_index, [])
            total_sessions += len(sessions)

            teachers_in_part = len(part_teachers[part_index]["set"])
            total_teachers += teachers_in_part if teachers_in_part > 0 else 1  # éviter division par 0

        # poids = sessions / enseignants (plus il y a de sessions et moins il y a d'enseignants, plus c’est "lourd")
        weight = total_sessions / total_teachers if total_teachers > 0 else 0
        teacher_weights[teacher_id] = weight

    return teacher_weights




# -----------------------------------

def apply_louvain_with_info(G):
    """
    Applique Louvain et retourne :
    - partition : dict node → communauté
    - nb_communities : nombre total de communautés détectées
    - community_sizes : dict communauté → taille
    - modularity : score de modularité global
    - community_density : dict communauté → densité (arêtes / max arêtes)
    """
    partition = community.best_partition(G, weight='weight')

    # Nombre total de communautés
    nb_communities = len(set(partition.values()))

    # Taille de chaque communauté
    community_sizes = defaultdict(int)
    for node, comm in partition.items():
        community_sizes[comm] += 1

    # Modularity
    modularity = community.modularity(partition, G, weight='weight')

    # Densité des communautés
    community_subgraphs = defaultdict(list)
    for node, comm in partition.items():
        community_subgraphs[comm].append(node)

    community_density = {}
    for comm, nodes in community_subgraphs.items():
        subg = G.subgraph(nodes)
        num_edges = subg.number_of_edges()
        max_edges = len(nodes) * (len(nodes) - 1) / 2 if len(nodes) > 1 else 1
        community_density[comm] = num_edges / max_edges

    return partition, {
        "nb_communities": nb_communities,
        "community_sizes": dict(community_sizes),
        "modularity": modularity,
        "community_density": community_density
    }

# Exemple d'utilisation :
# info = apply_louvain_with_info(G)
# print(info["modularity"], info["nb_communities"], info["community_sizes"])



def afficher_communautes_ordre_croissant(part_community_label):
    # Compter les occurrences de chaque communauté
    compteur = Counter(part_community_label.values())
    # Trier les communautés par nombre d’occurrences (ordre croissant)
    communautes_tries = sorted(compteur.items(), key=lambda x: x[1])
    # Afficher les résultats
    print("Classement des communautés par taille croissante :\n")
    for communaute, count in communautes_tries:
        print(f"{communaute} : {count} membres")


def add_label_coommu(partition,part_to_sessions,label_name,part_label,filename):



    # Ajout du label de communauté pour chaque part
    part_community_label = {}
    print("part_label",part_label)
    print("label_name",label_name)
    for part_id, sessions in part_to_sessions.items():
        community_counts = defaultdict(int)
        for session in sessions:
            community_id = partition.get(session)  # cast en str si les clés sont des strings
            if community_id is not None:
                community_counts[community_id] += 1
        if community_counts:
            major_community = max(community_counts.items(), key=lambda x: x[1])[0]
            part_community_label[part_id] = f"Community_{major_community}"
        else:
            part_community_label[part_id] = "Unknown"
            
    print()
    print("partition ",partition)
    print()
    print("part_community_label ",part_community_label)
    print()
    afficher_communautes_ordre_croissant(part_community_label)
    print()
    # Ajout des nouveaux labels de communauté à label_name et mise à jour de part_label
    for part_id, community_label in part_community_label.items():
        if community_label not in label_name:
            label_name.append(community_label)
        community_label_index = label_name.index(community_label)
        
        part_label[part_id]["set"].append(community_label_index)

    # Sauvegarde mise à jour
    updated_data = {
        "label_name": label_name,
        "part_label": part_label
    }
    
    print(updated_data)
    # Sauvegarde dans le fichier
        
    with open(filename, "r") as f:
        data = json.load(f)

    
    data["DATA"]["label_name"] = label_name
    data["DATA"]["part_label"] = part_label

    with open(filename, 'w') as f:
        json.dump(data, f, indent=2)
    
    print("✅ Fichier mis à jour avec les labels de communauté.")
    
    
    
    
import re









def extract_part_sequence_from_file(file_path, session_to_part,part_name):
    session_ids = set()

    # Lecture du fichier
    with open(file_path, "r") as f:
        for line in f:
            # Recherche des motifs x_slot_XXX où XXX est un entier
            matches = re.findall(r'x_slot_(\d+)', line)
            for match in matches:
                session_ids.add(int(match))

    # Conversion sessions → parts (évite doublons de part)
    seen_parts = set()
    part_sequence = []
    for session_id in session_ids:
        part_id = session_to_part.get(session_id)
        if part_id is not None and part_id not in seen_parts:
            seen_parts.add(part_id)
            part_sequence.append(part_name[part_id])

    return part_sequence




def listdepart(session_to_part, part_name):

    # Extraction des sessions à partir du texte
    pattern = r"x_slot_(\d+)"
    matches = re.findall(pattern, txt_content)

    # Conversion en entiers
    #sessions = sorted(set(int(m) for m in matches))

    # Création d'une liste ordonnée des parties de cours associées
    ordered_parts = [part_name[session_to_part[s]] for s in sessions if s in session_to_part]
    
    print(ordered_parts)
    return ordered_parts





from infomap import Infomap

def apply_infomap(G):
    im = Infomap()
    
    for u, v, data in G.edges(data=True):
        weight = data.get("weight", 1)
        im.add_link(u, v, weight)

    im.run()

    partition = {}
    for node in im.nodes:
        partition[node.node_id] = node.module_id

    return partition



def summary_from_partition(partition):
    communities = defaultdict(list)
    for node, comm in partition.items():
        communities[comm].append(node)

    print(f"Nombre de communautés : {len(communities)}")
    for i, (cid, members) in enumerate(communities.items()):
        print(f" Communauté {i} (taille {len(members)})")

import statistics

def display_infomap_partition(partition):
    communities = defaultdict(list)
    for node, comm_id in partition.items():
        communities[comm_id].append(node)

    sizes = [len(members) for members in communities.values()]
    print(f"\nNombre total de communautés détectées par Infomap : {len(communities)}")

    for i, (comm_id, members) in enumerate(sorted(communities.items(), key=lambda x: -len(x[1]))):
        print(f"\nCommunauté {comm_id} (taille {len(members)}) :")
        print(f"  Extrait : {sorted(members)[:10]}{' ...' if len(members) > 10 else ''}")

    print("\n📈 Statistiques sur la taille des communautés :")
    print(f"  Min   : {min(sizes)}")
    print(f"  Max   : {max(sizes)}")
    print(f"  Moyenne : {statistics.mean(sizes):.2f}")
    print(f"  Médiane : {statistics.median(sizes)}")




def apply_infomap2(G, trials=10, seed=42, two_level=False, silent=False):
    """
    Applique l'algorithme Infomap à un graphe pondéré.
    
    Paramètres :
    - G : graphe networkx
    - trials : nombre d'essais pour l'optimisation (par défaut 10)
    - seed : graine pour la reproductibilité
    - two_level : désactive la hiérarchie si True
    - silent : si True, n'affiche rien

    Retourne :
    - partition : dictionnaire {node_id: community_id}
    - stats : dictionnaire contenant modularité, nb communautés, densité par communauté...
    """

    im = Infomap(f"--seed {seed} --num-trials {trials}" + (" --two-level" if two_level else ""))

    for u, v, data in G.edges(data=True):
        weight = data.get("weight", 1)
        im.add_link(u, v, weight)

    im.run()
    
    # Création du dictionnaire de partition
    partition = {}
    for node in im.nodes:
        partition[node.node_id] = node.module_id

    # Statistiques
    community_sizes = defaultdict(int)
    communities = defaultdict(set)
    for node_id, comm_id in partition.items():
        community_sizes[comm_id] += 1
        communities[comm_id].add(node_id)

    nb_communities = len(community_sizes)
    
    # Calcul des densités internes par communauté
    community_density = {}
    for comm_id, nodes in communities.items():
        subgraph = G.subgraph(nodes)
        n = subgraph.number_of_nodes()
        m = subgraph.number_of_edges()
        max_edges = n * (n - 1) / 2 if n > 1 else 1
        density = m / max_edges if max_edges > 0 else 0
        community_density[comm_id] = density

    if not silent:
        print(f"🔍 Résultats Infomap :")
        print(f" - Nombre de communautés : {nb_communities}")
        print(f" - Taille moyenne des communautés : {sum(community_sizes.values()) / nb_communities:.2f}")
        print(f" - Communautés principales (top 5 tailles) :")
        top_comms = sorted(community_sizes.items(), key=lambda x: x[1], reverse=True)[:5]
        for cid, size in top_comms:
            print(f"   > Communauté {cid} : {size} nœuds | Densité : {community_density[cid]:.3f}")

    stats = {
        "nb_communities": nb_communities,
        "community_sizes": dict(community_sizes),
        "community_density": dict(community_density)
    }

    return partition, stats




if __name__ == "__main__":

    filename = "/home/etud/2023-L1-dev-web/LALA/LULU/instanceUSP_generated_290224203852_realistic_4067.json"
    #filename = "/home/etud/2023-L1-dev-web/LALA/LULULU/instance_exemple_20250423_002730.json"
    filename = "/home/etud/2023-L1-dev-web/LALA/LOLO/ua_l1_p1-p2_l3-info_2023.json"
    constraints,part_rooms, part_teachers, part_classes, part_nr_sessions, nr_sessions ,part_name,part_label,label_name = load_resource_data(filename)
    session_to_part = generate_session_to_part_map(part_classes, part_nr_sessions)
    part_to_sessions = generate_part_to_sessions_map(part_classes, part_nr_sessions)
    
    
      
      
    teacher_to_parts = generate_teacher_to_parts_map(part_teachers)
    teacher_to_sessions = generate_teacher_to_sessions_map(teacher_to_parts, part_to_sessions)
    
    room_to_parts = generate_teacher_to_parts_map(part_rooms)
    rooms_to_sessions = generate_teacher_to_sessions_map(room_to_parts, part_to_sessions)

    teacher_weitgh = generate_teacher_weights(teacher_to_parts,part_to_sessions,part_teachers )
    room_weitgh = generate_teacher_weights(room_to_parts,part_to_sessions,part_rooms )


    print("nr_sessions", nr_sessions)
    print("Constraints ",len(constraints))

    # Initialisation du graphe
    G = nx.Graph()
    for e in range(1, nr_sessions + 1):
        G.add_node(e)
    

    #G = convert_constraints_to_graph2(G, constraints)
    
    G = add_teacher_edges_from_sessions(G, teacher_to_sessions, len(part_to_sessions),teacher_weitgh)
    G = add_teacher_edges_from_sessions(G, rooms_to_sessions , len(part_to_sessions),room_weitgh)

    
    #partition = apply_louvain(G)
    partition,res= apply_louvain_with_info(G)

    print(G)
    print(res)
    # Affichage des communautés
    #print_communities_by_group(partition, part_name, session_to_part)
    print_communities_by_group_with_parts(partition, part_name, session_to_part)

    comm_G = build_community_graph(G, partition)
    pos = nx.spring_layout(comm_G, seed=42)
    nx.draw(comm_G, pos, with_labels=True, node_size=500, node_color='lightblue', edge_color='gray')
    labels = nx.get_edge_attributes(comm_G, 'weight')
    nx.draw_networkx_edge_labels(comm_G, pos, edge_labels={k: int(v) for k, v in labels.items()})
    plt.title("Graphes entre communautés (agrégation)")
    plt.show()
    
    
    
    
    
    print("==== FIN ======")
    if 1== 0:
        cliques = nx.find_cliques(G)
        kl =  sum(1 for c in cliques)  # The number of maximal cliques in G

        kl2 = max(cliques, key=len)  # The largest maximal clique in G
        
        print("kl",kl,"kl2",kl2)
    
    if 1 == 1:
        filename2 = "/home/etud/2023-L1-dev-web/LALA/LOLO/ua_l1_p1-p2_l3-info_2023_2.json"
        filename2 = "/home/etud/2023-L1-dev-web/LALA/LOLO/ua_l1_p1-p2_l3-info_2023_3.json"
        filename2 = "/home/etud/2023-L1-dev-web/LALA/LOLO/ua_l1_p1-p2_l3-info_2023-4.json"
        filename2 = "/home/etud/2023-L1-dev-web/LALA/LOLO/ua_l1_p1-p2_l3-info_2023-5.json"
        add_label_coommu(partition,part_to_sessions,label_name,part_label,filename2)
        
        
        exit(0)
        
        fichier = "decisionpath.txt"
        parties = extract_part_sequence_from_file(fichier, session_to_part,part_name)
        print("Ordre des parties :", parties)
        print("============")
        d = nx.degree_centrality(G)

        print("degree_centrality",d)
        # On filtre pour garder uniquement les clés numériques (int), optionnel
        d_filtered = {k: v for k, v in d.items() if isinstance(k, int)}

        # On trie par valeur décroissante
        sorted_dict = dict(sorted(d_filtered.items(), key=lambda item: item[1], reverse=True))

        # Affichage
        for key, value in sorted_dict.items():
            part = part_name[session_to_part[key]]
            print(f"{key}: {part} : {value}")
            
            
        # Écriture dans un fichier texte
        output_path = "sorted_dict_output.txt"
        with open(output_path, "w") as f:
            for key, value in sorted_dict.items():
                part = part_name[session_to_part[key]]
                f.write(f"{key}: {part} : {value}\n")
                #f.write(f"{key}: {value}\n")


    #p2 = apply_infomap(G)
    p2,ss = apply_infomap2(G)
    print(ss)
    summary_from_partition(p2)
    display_infomap_partition(p2)
    print_communities_by_group_with_parts(p2, part_name, session_to_part)


