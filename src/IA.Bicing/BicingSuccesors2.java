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

        for (int i =0; i < board.getVans(); ++i) {
            for (int j = 0; j < board.getVans(); ++j) {
                for (int k = 0; k < 2; ++k) {
                    for (int l = 0; l < 2; ++l) {
                        //System.out.println("HOLA1");
                        if (board.canSwap(i, j, k, l)) {
                            //System.out.println("HOLA1");
                            BicingBoard2 newBoard = new BicingBoard2(board);
                            newBoard.operatorSwap(i, j, k, l);
                            String S=new String("swap("+i+","+j+","+k+","+l+")");
                            S = S + newBoard.getTotalWaste();
                            retVal.add(new Successor(S,newBoard));
                        }
                    }
                }
            }
        }

        for (int i = 0; i < board.getVans(); ++i) {
            for (int j = 0; j < board.getStations(); ++j) {
                //System.out.println("HOLA2");
                for (int k = 0; k <= 30; ++k) {
                    if (board.canChangeStop1(i, j, 0)) {
                        //System.out.println("HOLA2");
                        BicingBoard2 newBoard = new BicingBoard2(board);
                        newBoard.operatorChangeStop1(i, j, k);
                        String S = new String("change_stop1(" + i + "," + j + ")");
                        S = S + newBoard.getTotalWaste();
                        retVal.add(new Successor(S, newBoard));
                    }
                }
            }
        }
        for (int i = 0; i < board.getVans(); ++i) {
            for (int j = 0; j < board.getStations(); ++j) {
                //System.out.println("HOLA3");
                if (board.canChangeStop2(i, j)) {
                    BicingBoard2 newBoard = new BicingBoard2(board);
                    newBoard.operatorChangeStop2(i, j);
                    String S=new String("change_stop2("+i+","+j+")");
                    S = S + newBoard.getTotalWaste();
                    retVal.add(new Successor(S,newBoard));
                }
            }
        }

        for (int i = 0; i < board.getVans(); ++i) {
            //System.out.println("HOLA?");
            for (int j = 0; j < board.getStations(); ++j) {
                //System.out.println("HOLA??");
                for (int k = 0; k < board.getStations(); ++k) {
                    //if (k != j) {
                    //System.out.println("HOLA???");
                        for (int l = 0; l <= 30; ++l) {
                            //System.out.println("HOLA4");
                            if (board.canAddStation(i, j, l, k)) {
                                BicingBoard2 newBoard = new BicingBoard2(board);
                                newBoard.operatorAddStation(i, j, l, k);
                                String S = new String("add_station(" + i + "," + j + "," + l + "," + k + ")");
                                S = S + newBoard.getTotalWaste();
                                retVal.add(new Successor(S, newBoard));
                            }
                        }
                    //}
                }
            }
        }

        for (int i = 0; i < board.getVans(); ++i) {
            for (int j = 0; j < board.getStations(); ++j) {
                for (int k = 0; k <= 30; ++k) {
                    //System.out.println("HOLA5");
                    if (board.canAddStop(i, j, k)) {
                        BicingBoard2 newBoard = new BicingBoard2(board);
                        newBoard.operatorAddStop(i, j, k);
                        String S = new String("add_stop(" + i + "," + j + "," + k + ")");
                        S = S + newBoard.getTotalWaste();
                        retVal.add(new Successor(S, newBoard));
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
                    S = S + newBoard.getTotalWaste();
                    retVal.add(new Successor(S,newBoard));
                }
            }
        }

        for (int i =0; i < board.getVans(); ++i) {
            for (int j = 0; j < board.getTbic(i); ++j) {
                if (board.canDrop(i, j)) {
                    BicingBoard2 newBoard = new BicingBoard2(board);
                    newBoard.operatorDrop(i,j);
                    String S=new String("drop("+i+","+j+")");
                    S = S + newBoard.getTotalWaste();
                    retVal.add(new Successor(S,newBoard));
                }
            }
        }

        for (int i = 0; i < board.getVans(); ++i) {
            for (int j = -30; j <= 30; ++j) {
                if (board.canSwitchBicis(i, j)) {
                    BicingBoard2 newBoard = new BicingBoard2(board);
                    newBoard.operatorSwitchBicis(i,j);
                    String S=new String("switch("+i+","+j+")");
                    S = S + newBoard.getTotalWaste();
                    retVal.add(new Successor(S,newBoard));
                }
            }
        }

        return retVal;
    }
}
