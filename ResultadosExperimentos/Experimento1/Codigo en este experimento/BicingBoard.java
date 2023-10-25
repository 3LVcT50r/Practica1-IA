package IA.Bicing;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;

public class BicingBoard  {
    private static final int START=0;
    private static final int STOP1=1;
    private static final int STOP2=2;
    private static final int TBIC=3;
    private static final int BIC1=4;
    private static Estaciones est;

    private int state[][];
    private int van;
    private int stations;
    private int dist1;
    private int dist2;

// ****************************************
// Constructor
// ****************************************

    public BicingBoard(Estaciones est, int van, String type) {
        state = new int[van][5];
        this.van = van;
        this.stations = est.size();
        this.est = est;

        //Basic initialization of the state
        for (int i = 0; i < van; ++i) {
            for (int j = 0; j < 3; ++j) {
                state[i][START + j] = -1;
            }
        }

        if (type.equals("Greedy")) {

            int dem, bicNext, bicNow, afterDem, valor = 0, n = est.size();
            int lista[][] = new int [n][2];
            for (int i = 0; i < n; ++i) {
                dem = est.get(i).getDemanda();
                bicNext = est.get(i).getNumBicicletasNext();
                bicNow = est.get(i).getNumBicicletasNoUsadas();
                afterDem = bicNext - dem;
                valor = Math.min(afterDem, bicNow);
                lista[i][0] = valor;
                lista[i][1]=i;

            }
            Arrays.sort(lista, new Comparator<int[]>() {
                @Override
                public int compare(int[] a, int[] b) {
                    return Integer.compare(a[0], b[0]);
                }
            });

            for(int i = 0; i < van; ++i) {
                state[i][START] = lista[n-1-i][1];
                state[i][TBIC] = lista[n-1-i][0];
                state[i][STOP1] = lista[i][1];
                state[i][BIC1] = Math.min(-lista[i][0], state[i][TBIC]);
            }

        }
        else if (type.equals("Mixed")) {
            // Not greedy not basic, a middle approach
            // We initialize vans assigned to random stations
            // we take the bicycles not being use and split into other two random stations, not used yet

            // Random list to randomize initial state
            List<Integer> randStations = new ArrayList<>();
            for (int i=0; i < est.size(); ++i)
                randStations.add(i);
            //Collections.shuffle(randStations);
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
    }

    public BicingBoard(BicingBoard board) {
        //2d array copy
        state = new int[board.state.length][board.state[0].length];
        for (int i=0; i < board.state.length; ++i)
            System.arraycopy(board.state[i], 0, state[i], 0, board.state[0].length);

        van = new Integer(board.van);
        stations = new Integer(board.stations);
    }

// ****************************************
// Util Functions
// ****************************************

    public void print() {
        for(int i=0;i<van;i++){
            System.out.print(" VanId: "+i+" Start: "+state[i][START]+" Station1: "+state[i][STOP1]+" Station2: "+state[i][STOP2]);
            System.out.print(" PickedUpBic: "+state[i][TBIC]+" BicStation1: "+state[i][BIC1]);
            System.out.println();
        }
        System.out.print(" Waste: "+getTotalWaste()+" Profit: " + getProfit()+" Total Profit: ");
        System.out.println(getRealProfit());
    }

    public int bicDropped(int station) {
        int total=0;
        for (int i=0; i < van; ++i) {
            if (state[i][STOP1] == station)
                total += state[i][BIC1];
            else if (state[i][STOP2] == station)
                total += state[i][TBIC] - state[i][BIC1];
        }
        return total;
    }

    public int getDist1() {
        return this.dist1;
    }
    public int getDist2() {
        return this.dist2;
    }

    public int bicPickUp(int station) {
        int total=0;
        for (int i=0; i < van; ++i) {
            if (state[i][START] == station)
                total += state[i][TBIC];
        }
        return total;
    }

    private int getWasteRow(int i) {
        int priceKm1 = (state[i][TBIC]+9)/10;
        int priceKm2 = ((state[i][TBIC]-state[i][BIC1])+9)/10;

        int startX=0;
        int startY=0;

        if (state[i][START] != -1) {
            startX = est.get(state[i][START]).getCoordX();
            startY = est.get(state[i][START]).getCoordY();
        }

        int stop1X=0;
        int stop1Y=0;
        if (state[i][STOP1] != -1) {
            stop1X = est.get(state[i][STOP1]).getCoordX();
            stop1Y = est.get(state[i][STOP1]).getCoordY();
        }

        int stop2X=0;
        int stop2Y=0;
        if (state[i][STOP2] != -1) {
            stop2X = est.get(state[i][STOP2]).getCoordX();
            stop2Y = est.get(state[i][STOP2]).getCoordY();
        }

        int distStartStop1=0;
        int distSp1Sp2=0;

        if (state[i][STOP1] != -1)
            distStartStop1 = (Math.abs(startX-stop1X)+Math.abs(startY-stop1Y))/1000;

        if (state[i][STOP1] != -1 && state[i][STOP2] != -1 )
            distSp1Sp2 = (Math.abs(stop2X-stop1X)+Math.abs(stop2Y-stop1Y))/1000;

        this.dist1 = distStartStop1;
        this.dist2 = distSp1Sp2;
        return distStartStop1*priceKm1+distSp1Sp2*priceKm2;
    }

    public int getTotalWaste() {
        int total=0;
        for (int i=0; i < van; ++i) {
            total += getWasteRow(i);
        }
        return total;
    }

    public int getProfit() {
        int profit=0;
        for (int i=0; i < stations; ++i) {
            profit += getProfitStation(i);
        }
        return profit;
    }

    public int getProfitStation(int i) {
        int dem = est.get(i).getDemanda();
        int bicNext = est.get(i).getNumBicicletasNext();
        int pickUp = bicPickUp(i);
        int dropped = bicDropped(i);
        int demSin = dem - bicNext;

        if(demSin >= 0) return Math.min((dropped-pickUp),demSin);
        else {
            if (dropped-pickUp < demSin) return (dropped-pickUp) + demSin;
            else return 0;
        }
    }

    public int getRealProfit() {
        return getProfit()-getTotalWaste();
    }

    public int lowDemandStart() {
        int points=0;
        for (int i=0; i < van; ++i) {
            if (state[i][START] != -1) {
                int dem = est.get(state[i][START]).getDemanda();
                int bicNext = est.get(state[i][START]).getNumBicicletasNext();
                points += bicNext - dem;
            }
        }
        return points;
    }

    public int bonuStop() {
        int points=0;
        for (int i=0; i < van; ++i) {
            if (state[i][STOP1] != -1) {
                int dem = est.get(state[i][STOP1]).getDemanda();
                int bicNext = est.get(state[i][STOP1]).getNumBicicletasNext();
                int pickUp = bicPickUp(state[i][STOP1]);
                int dropped = bicDropped(state[i][STOP1]);
                int realBic = bicNext+dropped-pickUp;
                points += dem - realBic;
            }
            if (state[i][STOP2] != -1) {
                int dem = est.get(state[i][STOP2]).getDemanda();
                int bicNext = est.get(state[i][STOP2]).getNumBicicletasNext();
                int pickUp = bicPickUp(state[i][STOP2]);
                int dropped = bicDropped(state[i][STOP2]);
                int realBic = bicNext+dropped-pickUp;
                points += dem - realBic;
            }
        }
        return points;
    }

    public int getVans() {
        return van;
    }

    public int getStations() {
        return stations;
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



// ****************************************
// Operators
// ****************************************

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
                stationBound(sp2) && v1 != v2 && sp1 != sp2 &&
                state[v1][STOP1+sp1] != -1 && state[v2][STOP1+sp2] != -1);
    }

    //Delete stop sp from van vn
    //pre: vn exists, and stops in sp
    //post: if vn has two stops and delete stop1, stop1 = stop2 and stop2 = -1 and b1=bt
    //else stop1=-1 and b1=0;
    public void operatorDeleteStop(int vn) {
        if (state[vn][STOP2] != -1) {
            state[vn][STOP2] = -1;
            state[vn][BIC1] = state[vn][TBIC];
        }
        else if (state[vn][STOP1] != -1) {
            state[vn][STOP1] = -1;
            state[vn][TBIC] = 0;
            state[vn][BIC1] = 0;
        }
    }

    public boolean canDeleteStop(int vn) {
        return (vanBound(vn) && (state[vn][STOP2] != -1 || state[vn][STOP1] != -1 ) );
    }

    //Add start
    //pre: vn and st exists
    //post: vn starts in st
    public void operatorAddStation(int vn, int st) {
        state[vn][START] = st;
    }

    public boolean canAddStation(int vn, int st) {
        for (int i=0; i < van; ++i) {
            if (state[i][START] == st)
                return false;
        }
        return (vanBound(vn) && stationBound(st) && state[vn][START] == -1);
    }

    //Add stop
    //pre: vn and sp exists
    //post: vn stops in sp
    public void operatorAddStop(int vn, int st) {
        if (state[vn][STOP1] == -1) state[vn][STOP1] = st;
        else if (state[vn][STOP2] == -1) state[vn][STOP2] = st;
    }

    public boolean canAddStop(int vn, int st) {
        boolean cond = (vanBound(vn) && stationBound(st) &&
                state[vn][START] != st && state[vn][START] != -1 &&
                (state[vn][STOP1] == -1 || state[vn][STOP2] == -1));
        if (state[vn][STOP1] == st) return false;
        return cond;
    }

    //Modify PickUp Bicycles
    //pre: vn exists and  0 < TBIC <= bicnotused in vn
    //post: TBIC = ntbic | 0 < ntbic <= bicnotused in vn
    public void operatorPickUp(int vn, int ntbic) {
        state[vn][TBIC] = ntbic;
        state[vn][BIC1] = ntbic;
        /*if (ntbic == 0) {
            operatorDeleteStop(vn, state[vn][STOP2]);
            operatorDeleteStop(vn, state[vn][STOP1]);
        }*/
    }
    public boolean canPickUp(int vn, int ntbic) {
        return (vanBound(vn) && state[vn][START] != -1 && ntbic <= est.get(state[vn][START]).getNumBicicletasNoUsadas() && ntbic >= 0 && ntbic <= 30);
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
        return (vanBound(vn) && state[vn][START] != -1 && nbic1 <= state[vn][TBIC] &&
                nbic1 >= 0 );
    }

    // pre:
    // post:

    public void operatorChangeStop1(int vn, int sp) {
        state[vn][STOP1] = sp;
    }
    public void operatorChangeStop2(int vn, int sp) {
        state[vn][STOP2] = sp;
    }
    public boolean canChangeStop1(int vn, int sp) {
        return (vanBound(vn) && state[vn][STOP1] != -1 &&  state[vn][STOP1] != sp);
    }
    public boolean canChangeStop2(int vn, int sp) {
        return vanBound(vn) && state[vn][STOP1] != -1 && state[vn][STOP2] != sp &&
                state[vn][STOP2] != -1 && state[vn][STOP1] != state[vn][STOP2];
    }


}
