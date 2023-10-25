package IA.Bicing;


import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Ravi Mohan
 *
 */
public class BicingSuccesors2AS implements SuccessorFunction {

    private int getRandom(int max) {
        Random ran = new Random();
        return ran.nextInt(1000)%(max+1);
    }

    public List getSuccessors(Object aState) {
        ArrayList retVal= new ArrayList();
        BicingBoard2 board=(BicingBoard2) aState;

        boolean operationMade = false;
        while (!operationMade) {

            int randomOperation = getRandom(3);
            //System.out.println(randomOperation);

            if (randomOperation == 0) {
                int f  = getRandom(board.getVans()-1);
                int sp1 = getRandom(board.getStations()-1);
                int tbic = getRandom(30);
                if (board.canAddStop(f, sp1, tbic)) {
                    BicingBoard2 newBoard = new BicingBoard2(board);
                    newBoard.operatorAddStop(f, sp1, tbic);
                    String S = new String("add_stop(" + f + "," + sp1 + "," + tbic + ")");
                    //S = S + newBoard.getRealProfit() + " " + newBoard.getTotalWaste();
                    retVal.add(new Successor(S, newBoard));
                }
            }
            else if (randomOperation == 1) {
                int f  = getRandom(board.getVans()-1);
                int sp1 = getRandom(board.getStations()-1);
                int tbic = getRandom(30);
                if (board.canAddStop2(f, sp1, tbic)) {
                    BicingBoard2 newBoard = new BicingBoard2(board);
                    newBoard.operatorAddStop2(f, sp1, tbic);
                    String S = new String("add_stop2(" + f + "," + sp1 + "," + tbic +")");
                    //S = S + newBoard.getRealProfit() + " " + newBoard.getTotalWaste();
                    retVal.add(new Successor(S, newBoard));
                }
            }
            else if (randomOperation == 2){         // add_station
                int f  = getRandom(board.getVans()-1);
                int st1= getRandom(board.getStations()-1);
                int sp1 = getRandom(board.getStations()-1);
                int tbic = getRandom(30);
                if (board.canAddStation(f, st1, tbic, sp1)) {
                    BicingBoard2 newBoard = new BicingBoard2(board);
                    newBoard.operatorAddStation(f, st1, tbic, sp1);
                    String S = new String("add_station(" + f + "," + st1 + "," + sp1 + "," + tbic+ ")");
                    retVal.add(new Successor(S, newBoard));
                }
            }
            else { // pick_up
                int f  = getRandom(board.getVans()-1);
                int tbic = getRandom(30);
                if (board.canPickUp(f, tbic)) {
                    BicingBoard2 newBoard = new BicingBoard2(board);
                    newBoard.operatorPickUp(f,tbic);
                    String S=new String("pick_up("+f+","+tbic+")");
                    retVal.add(new Successor(S,newBoard));
                }
            }

            if (retVal.size() > 0)
                operationMade = true;
        }

        return retVal;
    }
}
