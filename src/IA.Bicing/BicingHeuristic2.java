package IA.Bicing;

import aima.search.framework.HeuristicFunction;

public class BicingHeuristic2 implements HeuristicFunction {
    public double getHeuristicValue(Object state) {
        BicingBoard board = (BicingBoard) state;
        return board.getRealProfit();
    }
}
