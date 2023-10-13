import IA.Bicing.*;

import java.util.*;

import IA.Bicing.*;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.framework.Successor;
import aima.search.informed.HillClimbingSearch;

public class Main {
    static public void printStation(Estaciones est, BicingBoard b, int i) {
        Estacion st = est.get(i);
        int realBic = st.getNumBicicletasNext() + b.bicDropped(i) - b.bicPickUp(i);
        System.out.print("Station: " + i + " X: " + st.getCoordX() + " Y: " + st.getCoordY() + " Demand " + st.getDemanda());
        System.out.print(" BicNotUsed: " + st.getNumBicicletasNoUsadas() + " BicNext: " + st.getNumBicicletasNext());
        System.out.print(" BicDropped: " + b.bicDropped(i) + " BicPickUp: " + b.bicPickUp(i));
        System.out.println(" RealBic: " + realBic + " Profit: " + b.getProfitStation(i));
    }

    static public void printAllStations(Estaciones est, BicingBoard b) {
        for (int i=0; i < est.size(); ++i) {
            printStation(est,b,i);
        }
    }

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();

        while(keys.hasNext()) {
            String key = (String)keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }

    }

    public static void main(String[] args) throws Exception {

        String input;
        Scanner sc = new Scanner(System.in);

        Random ran = new Random();
        int x = ran.nextInt(100);

        System.out.println("IntialState type (Greedy, Mixed, Basic)");
        //input = sc.next();


        Estaciones est = new Estaciones(25, 1250, Estaciones.EQUILIBRIUM, x);
        BicingBoard InitialState= new BicingBoard(est, 5, "Mixed" /*input*/);
        InitialState.print();

        while (sc.hasNext()) {
            input = sc.next();
            if (input.equals("print_state")) {
                InitialState.print();
            }
            else if (input.equals("print_station")) {
                int station = sc.nextInt();
                printStation(est, InitialState, station);
            }
            else if (input.equals("print_all_station")) {
                printAllStations(est, InitialState);
            }
            else if (input.equals("add_station")) {
                int st1 = sc.nextInt();
                int st2 = sc.nextInt();
                InitialState.operatorAddStation(st1,st2);
            }
            else if (input.equals("delete_stop")) {
                int vn = sc.nextInt();
                int sp = sc.nextInt();
                InitialState.operatorDeleteStop(vn, sp);
            }
            else if (input.equals("pick_up")) {
                int vn = sc.nextInt();
                int ntbic = sc.nextInt();
                InitialState.operatorPickUp(vn, ntbic);
            }
            else if (input.equals("drop")) {
                int vn = sc.nextInt();
                int nbic1 = sc.nextInt();
                InitialState.operatorDrop(vn,nbic1);
            }
            else if (input.equals("swap")) {
                int v1 = sc.nextInt();
                int v2 = sc.nextInt();
                int sp1 = sc.nextInt();
                int sp2 = sc.nextInt();
                InitialState.operatorSwap(v1,v2,sp1,sp2);
            }
            else if (input.equals("successors+")) {
                List<Successor> a = new BicingSuccesors().getSuccessors(InitialState);
                for (int j=0; j < 100; ++j) {
                    int best = -1000, bestState = 0;
                    for (int i = 0; i < a.size(); ++i) {
                        BicingBoard newState = (BicingBoard) a.get(i).getState();
                        if (best < newState.getRealProfit()) {
                            best = newState.getRealProfit();
                            bestState = i;
                        }
                        System.out.println("Est: " + i + " Profit: " + newState.getRealProfit());
                    }
                    System.out.println("BestState: " + bestState + " Profit: " + best);
                }
            }
            else if (input.equals("successors")) {
                List<Successor> a = new BicingSuccesors().getSuccessors(InitialState);
                System.out.println(a.size());
            }
            else if (input.equals("hill")) {
                Problem p = new Problem(InitialState, new BicingSuccesors(), new BicingTest(), new BicingHeuristic());
                Search s = new HillClimbingSearch();
                SearchAgent agent = new SearchAgent(p, s);

                System.out.println(agent.getActions().size());
                printActions(agent.getActions());
                printInstrumentation(agent.getInstrumentation());

                Object o = s.getGoalState();
                //System.out.println("Beneficio total: " + new BicingHeurustic().getHeuristicValue(o));
                System.out.println("Beneficio total: " + new BicingHeuristic().getHeuristicValue(o));
            }
            else if (input.equals("fake_hill")) {
                for (int j=0; j < 10000; ++j) {
                    List<Successor> a = new BicingSuccesors().getSuccessors(InitialState);
                    //System.out.println(a.size());
                    int best = -1000, bestState = 0;
                    for (int i = 0; i < a.size(); ++i) {
                        BicingBoard newState = (BicingBoard) a.get(i).getState();
                        if (best < newState.getRealProfit()) {
                            best = newState.getRealProfit();
                            bestState = i;
                        }
                        //System.out.println("Est: " + i + " Profit: " + newState.getRealProfit());
                    }
                    //int num = sc.nextInt();
                    InitialState = (BicingBoard) a.get(bestState).getState();
                }
            }
        }

        sc.close();

    }
}