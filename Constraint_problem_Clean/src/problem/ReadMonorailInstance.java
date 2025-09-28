package problem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadMonorailInstance {
	public String filename;
	
	public int[][] graphe;
	public ArrayList<ArrayList<Integer>> graphe_struct;
	public Map<Integer, List<Integer>> graphes;
	public ReadMonorailInstance(String filename) {
		this.filename = filename;
		
		try {
			graphes = parseGraphe(this.filename);
			System.out.println(graphes);
			System.out.println(this.graphe_struct);
			//ArrayList<Integer> maxSize = this.graphe_struct.stream().map(t->t.size()).collect(Collectors.toCollection(ArrayList<Integer>::new));
			//Optional<Integer> maxitaille = maxSize.stream().max(Integer::compare);
			graphe = creerMatriceAdjacence(graphes,graphes.size());
			afficherMatrice(graphe);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//FinMethod
	
	
    public Map<Integer, List<Integer>> parseGraphe(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        graphe_struct = new ArrayList<>();

        // Lecture du fichier
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }

        Map<Integer, List<Integer>> adj = new HashMap<>();
        Map<String, Integer> positionToNode = new HashMap<>();

        int nodeId = 1;

        // Repérer tous les nœuds et leur position (ligne, colonne)
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            ArrayList<Integer> tmp = new ArrayList<>();
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == 'X') {
                    positionToNode.put(i + "," + j, nodeId);
                    tmp.add(nodeId);
                    adj.put(nodeId, new ArrayList<>());
                    nodeId++;
                }
            }
            if(tmp.size() >0) {
            	this.graphe_struct.add(tmp);
            }
            
        }

        // Analyser les connexions (arêtes)
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == '-') {
                    // Cherche à gauche et à droite un X
                    Integer left = positionToNode.get(i + "," + (j - 1));
                    Integer right = positionToNode.get(i + "," + (j + 1));
                    if (left != null && right != null) {
                        adj.get(left).add(right);
                        adj.get(right).add(left);
                    }
                } else if (line.charAt(j) == '|') {
                    // Cherche en haut et en bas un X
                    Integer up = positionToNode.get((i - 1) + "," + j);
                    Integer down = positionToNode.get((i + 1) + "," + j);
                    if (up != null && down != null) {
                        adj.get(up).add(down);
                        adj.get(down).add(up);
                    }
                }
            }
        }

        return adj;
    }//FinMethod
    
    public static void afficherMatrice(int[][] matrice) {
        for (int[] ligne : matrice) {
            for (int val : ligne) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }//FinMethod

    
    public int[][] creerMatriceAdjacence(Map<Integer, List<Integer>> graphe, int N) {
        int[][] matrice = new int[N][N];

        for (int i = 1; i <= N; i++) {
            List<Integer> voisins = graphe.getOrDefault(i, new ArrayList<>());
            for (int v : voisins) {
                matrice[i - 1][v - 1] = 1; // i et v sont en base 1
            }
        }

        return matrice;
    }//FinMethod
    

	
}//FinClass
