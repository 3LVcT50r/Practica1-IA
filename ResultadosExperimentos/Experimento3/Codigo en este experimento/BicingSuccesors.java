package IA.Bicing;


import java.util.ArrayList;
import java.util.List;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
/**
 * @author Ravi Mohan
 *
 */
public class BicingSuccesors implements SuccessorFunction {

    public List getSuccessors(Object aState) {
        ArrayList retVal= new ArrayList();
        BicingBoard board=(BicingBoard) aState;

        for (int i =0; i < board.getVans(); ++i) {
            for (int j =0; j <= board.getTbic(i); ++j) {
                if (board.canPickUp(i, j)) {
                    BicingBoard newBoard = new BicingBoard(board);
                    newBoard.operatorPickUp(i,j);
                    String S=new String("pick_up("+i+","+j+")");
                    //S = S + newBoard.getTotalWaste();
                    retVal.add(new Successor(S,newBoard));
                }
            }
        }

        for (int i =0; i < board.getVans(); ++i) {
            for (int j = 0; j < board.getTbic(i); ++j) {
                if (board.canDrop(i, j)) {
                    BicingBoard newBoard = new BicingBoard(board);
                    newBoard.operatorDrop(i,j);
                    String S=new String("drop("+i+","+j+")");
                    //S = S + newBoard.getTotalWaste();
                    retVal.add(new Successor(S,newBoard));
                }
            }
        }

        for (int i =0; i < board.getVans(); ++i) {
            for (int j = 0; j < board.getStations(); ++j) {
                if (board.canAddStation(i, j)) {
                    BicingBoard newBoard = new BicingBoard(board);
                    newBoard.operatorAddStation(i,j);
                    String S=new String("add_station("+i+","+j+")");
                    //S = S + newBoard.getTotalWaste();
                    retVal.add(new Successor(S,newBoard));
                }
            }
        }

        for (int i =0; i < board.getVans(); ++i) {
            for (int j = 0; j < board.getVans(); ++j) {
                for (int k = 0; k < 2; ++k) {
                    for (int l = 0; l < 2; ++l) {
                        if (board.canSwap(i, j, k, l)) {
                            BicingBoard newBoard = new BicingBoard(board);
                            newBoard.operatorSwap(i, j, k, l);
                            String S=new String("swap("+i+","+j+","+k+","+l+")");
                            //S = S + newBoard.getTotalWaste();
                            retVal.add(new Successor(S,newBoard));
                        }
                    }
                }
            }
        }

        for (int i =0; i < board.getVans(); ++i) {
            for (int j = 0; j < board.getStations(); ++j) {
                for (int k=0; k < 2; ++k) {
                    if (board.canAddStop(i, j)) {
                        BicingBoard newBoard = new BicingBoard(board);
                        newBoard.operatorAddStop(i, j);
                        String S = new String("add_stop(" + i + "," + j + ")");
                        //S = S + newBoard.getTotalWaste();
                        retVal.add(new Successor(S, newBoard));
                    }
                }
            }
        }

        for (int i = 0; i < board.getVans(); ++i) {
            for (int j = 0; j < board.getStations(); ++j) {
                if (board.canChangeStop1(i, j)) {
                    BicingBoard newBoard = new BicingBoard(board);
                    newBoard.operatorChangeStop1(i, j);
                    String S=new String("change_stop1("+i+","+j+")");
                    //S = S + newBoard.getTotalWaste();
                    retVal.add(new Successor(S,newBoard));
                }
            }
        }
        for (int i = 0; i < board.getVans(); ++i) {
            for (int j = 0; j < board.getStations(); ++j) {
                if (board.canChangeStop2(i, j)) {
                    BicingBoard newBoard = new BicingBoard(board);
                    newBoard.operatorChangeStop2(i, j);
                    String S=new String("change_stop2("+i+","+j+")");
                    //S = S + newBoard.getTotalWaste();
                    retVal.add(new Successor(S,newBoard));
                }
            }
        }


        //board.print();
        return retVal;
    }

}