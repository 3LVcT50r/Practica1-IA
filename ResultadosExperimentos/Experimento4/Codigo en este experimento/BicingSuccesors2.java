package IA.Bicing;


import java.util.ArrayList;
import java.util.List;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
/**
 * @author Ravi Mohan
 *
 */
public class BicingSuccesors2 implements SuccessorFunction {

    public List getSuccessors(Object aState) {
        ArrayList retVal= new ArrayList();
        BicingBoard2 board=(BicingBoard2) aState;


        for (int i = 0; i < board.getVans(); ++i) {
            for (int j = 0; j < board.getStations(); ++j) {
                for (int k = 0; k <= 30; ++k) {
                    //System.out.println("HOLA5");
                    if (board.canAddStop(i, j, k)) {
                        BicingBoard2 newBoard = new BicingBoard2(board);
                        newBoard.operatorAddStop(i, j, k);
                        String S = new String("add_stop(" + i + "," + j + "," + k + ")");
                        //S = S + newBoard.getRealProfit() + " " + newBoard.getTotalWaste();
                        retVal.add(new Successor(S, newBoard));
                    }
                }
            }
        }


        for (int i = 0; i < board.getVans(); ++i) {
            for (int j = 0; j < board.getStations(); ++j) {
                for (int k = 0; k <= 30; ++k) {
                    if (board.canAddStop2(i, j, k)) {
                        BicingBoard2 newBoard = new BicingBoard2(board);
                        newBoard.operatorAddStop2(i, j, k);
                        String S=new String("add_stop2("+i+","+j+")");
                        //S = S + newBoard.getRealProfit() + " " + newBoard.getTotalWaste();
                        retVal.add(new Successor(S,newBoard));
                    }
                }
            }
        }

        for (int i = 0; i < board.getVans(); ++i) {
            for (int j = 0; j < board.getStations(); ++j) {
                for (int k = 0; k < board.getStations(); ++k) {
                        for (int l = 0; l <= 30; ++l) {
                            if (board.canAddStation(i, j, l, k)) {
                                BicingBoard2 newBoard = new BicingBoard2(board);
                                newBoard.operatorAddStation(i, j, l, k);
                                String S = new String("add_station(" + i + "," + j + "," + l + "," + k + ")");
                                //S = S + newBoard.getRealProfit() + " " + newBoard.getTotalWaste();
                                retVal.add(new Successor(S, newBoard));
                            }
                        }
                }
            }
        }

        for (int i =0; i < board.getVans(); ++i) {
            for (int j =0; j <= board.getTbic(i); ++j) {
                if (board.canPickUp(i, j)) {
                    BicingBoard2 newBoard = new BicingBoard2(board);
                    newBoard.operatorPickUp(i,j);
                    String S=new String("pick_up("+i+","+j+")");
                    //S = S + newBoard.getRealProfit() + " " + newBoard.getTotalWaste();
                    retVal.add(new Successor(S,newBoard));
                }
            }
        }


        return retVal;
    }
}
