package IA.Bicing;

import aima.search.framework.HeuristicFunction;

public class BicingHeuristic3 implements HeuristicFunction {
    public double getHeuristicValue(Object state) {
        BicingBoard2 board = (BicingBoard2) state;
        return -1*(board.getProfit());
    }
}
