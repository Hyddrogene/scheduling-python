package louvain;

import java.util.*;

public class Louvain {

    // ==== Classe de base pour le graphe ====
    static class Graph {
        Map<Integer, Map<Integer, Double>> adjacency = new HashMap<>();

        void addEdge(int u, int v, double weight) {
            adjacency.computeIfAbsent(u, k -> new HashMap<>()).put(v, weight);
            adjacency.computeIfAbsent(v, k -> new HashMap<>()).put(u, weight);
        }

        double totalWeight() {
            double total = 0.0;
            for (var entry : adjacency.entrySet()) {
                for (double w : entry.getValue().values()) {
                    total += w;
                }
            }
            return total / 2.0;
        }

        Set<Integer> getNeighbors(int node) {
            return adjacency.getOrDefault(node, Collections.emptyMap()).keySet();
        }

        double getEdgeWeight(int u, int v) {
            return adjacency.getOrDefault(u, Collections.emptyMap()).getOrDefault(v, 0.0);
        }

        Set<Integer> getNodes() {
            return adjacency.keySet();
        }

        double getNodeDegree(int node) {
            return adjacency.getOrDefault(node, Collections.emptyMap())
                            .values().stream().mapToDouble(Double::doubleValue).sum();
        }
    }

    // ==== Implémentation simplifiée de Louvain ====
    static class LouvainInterne {
        Graph graph;
        Map<Integer, Integer> nodeToCommunity = new HashMap<>();
        Map<Integer, Set<Integer>> communityToNodes = new HashMap<>();

        LouvainInterne(Graph g) {
            this.graph = g;
        }

        public void execute() {
            int commId = 0;
            for (int node : graph.getNodes()) {
                nodeToCommunity.put(node, commId++);
            }

            boolean changed = true;
            while (changed) {
                changed = false;
                for (int node : graph.getNodes()) {
                    int bestCommunity = nodeToCommunity.get(node);
                    double bestDeltaQ = 0.0;

                    Map<Integer, Double> neighborCommunities = new HashMap<>();

                    for (int neighbor : graph.getNeighbors(node)) {
                        int community = nodeToCommunity.get(neighbor);
                        double weight = graph.getEdgeWeight(node, neighbor);
                        neighborCommunities.put(community, neighborCommunities.getOrDefault(community, 0.0) + weight);
                    }

                    double m2 = graph.totalWeight() * 2;
                    double nodeDeg = graph.getNodeDegree(node);

                    for (var entry : neighborCommunities.entrySet()) {
                        int community = entry.getKey();
                        double tot = totalDegree(community);
                        double k_i_in = entry.getValue();

                        double deltaQ = k_i_in - (tot * nodeDeg / m2);
                        if (deltaQ > bestDeltaQ) {
                            bestDeltaQ = deltaQ;
                            bestCommunity = community;
                        }
                    }

                    if (bestCommunity != nodeToCommunity.get(node)) {
                        nodeToCommunity.put(node, bestCommunity);
                        changed = true;
                    }
                }
            }

            // Construire les communautés
            for (var entry : nodeToCommunity.entrySet()) {
                communityToNodes.computeIfAbsent(entry.getValue(), k -> new HashSet<>()).add(entry.getKey());
            }
        }

        double totalDegree(int community) {
            return nodeToCommunity.entrySet().stream()
                .filter(e -> e.getValue() == community)
                .mapToDouble(e -> graph.getNodeDegree(e.getKey()))
                .sum();
        }

        public void printCommunities() {
            System.out.println("Communautés détectées :");
            for (var entry : communityToNodes.entrySet()) {
                System.out.println("Communauté " + entry.getKey() + " : " + entry.getValue());
            }
        }
    }
    // ========================================
    // ==== Exemple de graphe et exécution ====
 // ========================================
    public static void main(String[] args) {
        Graph g = new Graph();
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        g.addEdge(3, 4, 1);
        g.addEdge(4, 5, 1);
        g.addEdge(5, 3, 1);
        g.addEdge(2, 3, 0.1); // lien faible entre deux communautés

        LouvainInterne algo = new LouvainInterne(g);
        algo.execute();
        algo.printCommunities();
    }
}
