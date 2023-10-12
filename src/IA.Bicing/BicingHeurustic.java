package IA.Bicing;

import aima.search.framework.HeuristicFunction;

public class BicingHeurustic implements HeuristicFunction {
    public Integer getHeuristicValiue(Object state) {
        BicingBoard estat = (BicingBoard) state;
        return estat.getDinero();
    }
}
