package IA.Bicing;

import aima.search.framework.HeuristicFunction;

public class BicingHeuristic implements HeuristicFunction {
    public double getHeuristicValue(Object state) {
        BicingBoard board = (BicingBoard) state;
        return -1*(board.getProfit()*10 + board.lowDemandStart()/10.0 + board.bonuStop()/10.0);
    }
}

