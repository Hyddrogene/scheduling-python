package FeatureModel;

public class SommeTableau2D {
    public static void main(String[] args) {
        int[][] tableau = {
            {1, 2, 3},
            {4, 5,4},
            {6,0,0}
        };

        int somme = java.util.Arrays.stream(tableau)       // Stream de lignes (int[])
                         .flatMapToInt(java.util.Arrays::stream) // Aplatir en un seul IntStream
                         .sum();                            // Faire la somme

        System.out.println("Somme totale : " + somme);
        
        int a= 1;
        boolean b = a==1? true: false;
        
        System.out.println("b : " + b);
        
        
    }
}
