package IA.Bicing;

import IA.Bicing.*;
import java.util.*;
import java.lang.Math;

public class BicingBoard {

    static final int START=0;
    static final int STOP1=1;
    static final int STOP2=2;
    static final int TBIC=3;
    static final int BIC1=4;

    //private Vector<Vector<Integer>> state;
    private int state[][];
    private int van;
    private int stations;
    private Estaciones est;
    //
    public BicingBoard(Estaciones est, int van, String type) {
        state = new int[van][5];
        this.van = van;
        this.stations = est.size();
        this.est = est;

        if (type.equals("Greedy")) {

        }
        else if (type.equals("Mixed")) {
            // Not greedy not basic, a middle approach
            // We initialize vans assigned to random stations
            // we take the bicycles not being use and split into other two random stations, not used yet

            //state = new Vector<Vector<Integer>>(van, new vector<Integer>(6,0));

            // Random list to randomize initial state
            List<Integer> randStations = new ArrayList<>();
            for (int i=0; i < est.size(); ++i)
                randStations.add(i);
            Collections.shuffle(randStations);
            int randomIndex=0;

            for (int i=0; i < van; i++) {
                // start station
                state[i][START] = randStations.get(randomIndex);
                // number of bicycles not used
                int noBic=est.get(state[i][START]).getNumBicicletasNoUsadas();
                state[i][TBIC] = noBic;
                ++randomIndex;
                // we continue only if there are bicycles not used
                if (state[i][TBIC] > 1) {
                    double half = noBic/2.0;
                    state[i][BIC1] = (int)Math.ceil(half);
                    state[i][STOP1] = randStations.get(randomIndex);
                    ++randomIndex;
                    state[i][STOP2] = randStations.get(randomIndex);
                }
                else if (state[i][TBIC] == 1) {
                    state[i][BIC1] = 1;
                    state[i][STOP1] = randStations.get(randomIndex);
                    state[i][STOP2] = -1;
                }
                else  {
                    state[i][STOP1] = -1;
                    state[i][STOP2] = -1;
                }
                ++randomIndex;
            }
        }
        else {
            for (int i=0; i < van; ++i) {
                for (int j=0; j < 3; ++j) {
                    state[i][START+j]=-1;
                }
            }

        }
    }
    public void print() {
        for(int i=0;i<van;i++){
            System.out.print(" VanId: ");
            System.out.print(i);
            System.out.print(" Start: ");
            System.out.print(state[i][START]);
            System.out.print(" Station1: ");
            System.out.print(state[i][STOP1]);
            System.out.print(" Station2: ");
            System.out.print(state[i][STOP2]);
            System.out.print(" PickedUpBic: ");
            System.out.print(state[i][TBIC]);
            System.out.print(" BicStation1: ");
            System.out.println(state[i][BIC1]);
        }
    }

    public int getVans() {
        return van;
    }

    public int getTbic(int vn) {
        if (vanBound(vn) &&  state[vn][START] != -1)
            return est.get(state[vn][START]).getNumBicicletasNoUsadas();
        else return 0;
    }

    public boolean vanBound(int v) {
        return (v >= 0 && v < van);
    }

    public boolean stationBound(int s) {
        return (s >= 0 && s < stations);
    }

    //( 0 <= v < this.van | sp = {0,1} )

    //Swap stops between vans v1 and v2
    //pre: v1 and v2 exist, and v1 stops in sp1 and v2 stops in sp2
    //post: v1 stops in sp2 and v2 stops in sp1
    public void operatorSwap(int v1, int v2, int sp1, int sp2) {
        int aux = state[v1][STOP1+sp1];
        state[v1][STOP1+sp1] = state[v2][STOP1+sp2];
        state[v2][STOP1+sp2] = aux;
    }

    public boolean canSwap(int v1, int v2, int sp1, int sp2) {
        return (vanBound(v1) && vanBound(v2) && stationBound(sp1) &&
                stationBound(sp2) && v1 != v2 && sp1 != sp2 && state[v1][sp1] != -1 && state[v2][sp2] != -1);
    }

    //Delete stop sp from van vn
    //pre: vn exists, and stops in sp
    //post: if vn has two stops and delete stop1, stop1 = stop2 and stop2 = -1 and b1=bt
    //else stop1=-1 and b1=0;
    public void operatorDeleteStop(int vn, int sp) {
        if (sp == 2) {
            state[vn][STOP1] = state[vn][STOP2];
            state[vn][STOP2] = -1;
            state[vn][BIC1] = state[vn][TBIC];
        }
        else {
            state[vn][STOP1] = -1;
            state[vn][BIC1] = 0;
        }
    }

    public boolean canDeleteStop(int vn, int sp) {
        return (vanBound(vn) && stationBound(sp) && state[vn][sp] != -1);
    }

    //Change station
    //pre: vn and vnc exists
    //post: vn = vnc
    public void operatorChangeStation(int vn, int vnc) {
        int aux[] = state[vnc];
        state[vnc] = state[vn];
        state[vn] = aux;
    }

    public boolean canChangeStation(int vn, int vnc) {
        return (vanBound(vn) && vanBound(vnc) && vn != vnc && state[vn][START] != -1 && state[vnc][START] != -1);
    }

    //Modify PickUp Bicycles
    //pre: vn exists and  0 < TBIC <= bicnotused in vn
    //post: TBIC = ntbic | 0 < ntbic <= bicnotused in vn
    public void operatorPickUp(int vn, int ntbic) {
        state[vn][TBIC] = ntbic;
        if (ntbic == 0) {
            operatorDeleteStop(vn, state[vn][STOP2]);
            operatorDeleteStop(vn, state[vn][STOP1]);
        }
    }
    public boolean canPickUp(int vn, int ntbic) {
        int tbic = est.get(state[vn][START]).getNumBicicletasNoUsadas();
        return (ntbic <= tbic && ntbic >= 0 && vanBound(vn) && state[vn][START] != -1);
    }

    //Modify Drop Bicycles
    //pre: vn exists and 0 < TBIC <= bicnotused in vn | 0 < BIC1 < TBIC
    //post: BIC1 = nbic1 | 0 < nbic1 < TBIC
    public void operatorDrop(int vn, int nbic1) {
        int tbic = est.get(state[vn][START]).getNumBicicletasNoUsadas();
        state[vn][BIC1] = nbic1;
        if (tbic == nbic1)
            state[vn][STOP2] = -1;
    }
    public boolean canDrop(int vn, int nbic1) {
        int tbic = est.get(state[vn][START]).getNumBicicletasNoUsadas();
        return (nbic1 <= tbic && nbic1 >= 0 && vanBound(vn) && state[vn][START] != -1);
    }
}
