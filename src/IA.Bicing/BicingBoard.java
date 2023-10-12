package IA.Bicing;

import IA.Bicing.*;
import java.util.*;
import java.lang.Math;


public class BicingBoard {

    //private Vector<Vector<Integer>> state;
    private int state[][];
    private int van;
    //
    public BicingBoard(Estaciones stations, int van, String type) {
        state = new int[van][6];
        this.van = van;

        if (type.equals("Basic")) {
            //Basic initialization - 0 vans assigned to stations
            //state = new Vector<Vector<Integer>>(van, new vector<Integer>(6,0));
        }
        else if (type.equals("Greedy")) {

        }
        else {
            // Not greedy not basic, a middle approach
            // We initialize vans assigned to random stations
            // we take the bicycles not being use and split into other two random stations, not used yet

            //state = new Vector<Vector<Integer>>(van, new vector<Integer>(6,0));

            // Random list to randomize initial state
            List<Integer> randStations = new ArrayList<>();
            for (int i=0; i < stations.size(); ++i)
                randStations.add(i);
            Collections.shuffle(randStations);
            int randomIndex=0;

            for (int i=0; i < van; i++) {
                // id van
                state[i][0] = i;
                // start station
                state[i][1] = randStations.get(randomIndex);
                // number of bicycles not used
                int noBic=stations.get(state[i][1]).getNumBicicletasNoUsadas();
                state[i][4] = noBic;
                ++randomIndex;
                // we continue only if there are bicycles not used
                if (state[i][4] > 1) {
                    double half = noBic/2.0;
                    state[i][5] = (int)Math.ceil(half);
                    state[i][2] = randStations.get(randomIndex);
                    ++randomIndex;
                    state[i][3] = randStations.get(randomIndex);
                }
                else if (state[i][4] == 1) {
                    state[i][5] = 1;
                    state[i][2] = randStations.get(randomIndex);
                    state[i][3] = -1;
                }
                else  {
                    state[i][2] = -1;
                    state[i][3] = -1;
                }
                ++randomIndex;
            }
        }

    }
    public void print() {
        for(int i=0;i<van;i++){
            System.out.print(" VanId: ");
            System.out.print(state[i][0]);
            System.out.print(" Start: ");
            System.out.print(state[i][1]);
            System.out.print(" Station1: ");
            System.out.print(state[i][2]);
            System.out.print(" Station2: ");
            System.out.print(state[i][3]);
            System.out.print(" NumberBicNotUsed: ");
            System.out.print(state[i][4]);
            System.out.print(" BicStation1: ");
            System.out.println(state[i][5]);
        }
    }

}
