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
                    retVal.add(new Successor(S,newBoard));
                }
            }
        }

        for (int i =0; i < board.getVans(); ++i) {
            for (int j = 2; j > 0; --j) {
                if (board.canDeleteStop(i, j)) {
                    BicingBoard newBoard = new BicingBoard(board);
                    newBoard.operatorDeleteStop(i,j);
                    String S=new String("delete_stop("+i+","+j+")");
                    retVal.add(new Successor(S,newBoard));
                }
            }
        }



        return retVal;
    }

}