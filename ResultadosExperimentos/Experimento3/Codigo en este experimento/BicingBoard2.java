package IA.Bicing;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;

public class BicingBoard2 {

// ****************************************
// Initialization
// ****************************************
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

        public BicingBoard2(Estaciones est, int van, String type) {
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

    public BicingBoard2(BicingBoard2 board) {
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
        System.out.println(getRealProfit() + " Distance: " + getDist1()+getDist2());
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


    public void operatorAddStation(int vn, int st, int npick, int stp) {
        state[vn][START] = st;
        state[vn][TBIC] = npick;
        state[vn][STOP1] = stp;
        state[vn][BIC1] = npick/2;
    }
    public boolean canAddStation(int vn, int st, int ntbic, int stp) {
        for (int i=0; i < van; ++i) {
            if (state[i][START] == st)
                return false;
        }

        return vanBound(vn) && stationBound(st) && state[vn][START] == -1 &&
                ntbic <= est.get(st).getNumBicicletasNoUsadas() &&
                ntbic >= 0 && ntbic <= 30 && state[vn][STOP1] == -1 && state[vn][START] != stp;
    }

    public boolean canAddStop(int vn, int st, int ntbic) {
        boolean cond = false;
        if (state[vn][START] == -1) return false;

        if (state[vn][STOP1] == -1) {
            cond = true;
        }
        else if (state[vn][STOP2] == -1) {
            cond = state[vn][STOP1] != -1 && state[vn][START] != -1 && ntbic <= est.get(state[vn][START]).getNumBicicletasNoUsadas();
        }
        return cond && vanBound(vn) && stationBound(st) && state[vn][START] != st &&
                ntbic >= 0 && ntbic <= 30;
    }
    public void operatorAddStop(int vn, int st, int ntbic) {
        if (state[vn][STOP1] == -1) {
            state[vn][STOP1] = st;
            state[vn][BIC1] = ntbic;
        }
        else if (state[vn][STOP2] == -1) state[vn][STOP2] = st;
    }

    public void operatorAddStop2(int vn, int st, int nbic) {
        state[vn][STOP2] = st;
        state[vn][BIC1] -= nbic;
    }
    public boolean canAddStop2(int vn, int st, int nbic) {
        return vanBound(vn) && stationBound(st) && state[vn][STOP1] != -1 &&
                nbic <= state[vn][TBIC];
    }

    public void operatorPickUp(int vn, int ntbic) {
        state[vn][TBIC] = ntbic;
        state[vn][BIC1] = ntbic;
    }
    public boolean canPickUp(int vn, int ntbic) {
        return (vanBound(vn) && state[vn][START] != -1 && ntbic <= est.get(state[vn][START]).getNumBicicletasNoUsadas() && ntbic >= 0 && ntbic <= 30);
    }



}
