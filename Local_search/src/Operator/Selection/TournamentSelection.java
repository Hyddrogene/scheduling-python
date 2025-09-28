package Operator.Selection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import GA.SolutionGA;
import utils.ComparisonFunction;

public class TournamentSelection extends Selection {

    private final int pressure;
    private final ComparisonFunction funcComp;

    //public TournamentSelection(ComparisonFunction funcComp, int pressure, SelectionParameters params) {
    public TournamentSelection(ComparisonFunction funcComp, int pressure) {

        super();
        this.funcComp = funcComp;
        this.pressure = pressure;
    }

    @Override
    protected ArrayList<SolutionGA> doSelection(ArrayList<SolutionGA> parents,ArrayList<SolutionGA> childs, int poolSize) {
        int nRandom = poolSize;//nSelect * nParents * pressure;
        int nPerms = (int) Math.ceil((double) nRandom / childs.size());
        int nSelect = (int) Math.ceil((double) nRandom / childs.size());
        int nParents = parents.size();
        

        //int[] P = randomPermutations(nPerms, childs.size()).subList(0, nRandom).stream().mapToInt(i -> i).toArray();

        int[][] selectedIndices = new int[nSelect][nParents];
        for (int i = 0; i < nSelect; i++) {
            int[] tournament = new int[] {};//Arrays.copyOfRange(P, i * nParents * pressure, (i + 1) * nParents * pressure);
            int[] winners = new int[] {};//compare(childs, tournament);
            System.arraycopy(winners, 0, selectedIndices[i], 0, nParents);
        }

        return null;//Arrays.stream(selectedIndices).mapToInt(u->Arrays.stream(u).max().getAsInt()).max().getAsInt();
    }

    private int[] compare(int[][] pop, int[] indices) {
        int[] winners = new int[indices.length / pressure];
        int winnerIndex = 0;

        for (int i = 0; i < indices.length; i += pressure) {
            int winner = indices[i];
            int winnerValue = 0;//evaluate(pop[winner]);

            for (int j = 1; j < pressure; j++) {
                int competitor = indices[i + j];
                int competitorValue = 0;//evaluate(pop[competitor]);

                if (funcComp.compare(winnerValue, competitorValue)) {
                    winner = competitor;
                    winnerValue = competitorValue;
                }
            }

            winners[winnerIndex++] = winner;
        }

        return winners;
    }

}