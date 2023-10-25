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

        Estaciones est = new Estaciones(25, 1250, Estaciones.EQUILIBRIUM, 1234);
        BicingBoard InitialState= new BicingBoard(est, 5, "Greedy" );
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
                double best = -1000;
                int bestState = 0;
                for (int i = 0; i < a.size(); ++i) {
                    BicingBoard newState = (BicingBoard) a.get(i).getState();
                    double value = new BicingHeuristic().getHeuristicValue(newState);
                    if (best < value) {
                        best = value;
                        bestState = i;
                    }
                    System.out.println("Est: " + i + " Profit: " + value);
                }
                System.out.println("BestState: " + bestState + " Profit: " + best);
            }
            else if (input.equals("successors")) {
                List<Successor> a = new BicingSuccesors().getSuccessors(InitialState);
                System.out.println(a.size());
            }
            else if (input.equals("hill")) {
                //El hill por defecto dentro de su implementacion busca el menor sucesor
                Problem p = new Problem(InitialState, new BicingSuccesors(), new BicingTest(), new BicingHeuristic());
                Search s = new HillClimbingSearch();
                SearchAgent agent = new SearchAgent(p, s);

                System.out.println(agent.getActions().size());
                printActions(agent.getActions());
                printInstrumentation(agent.getInstrumentation());

                Object o = s.getGoalState();
                BicingBoard finalState = (BicingBoard) o;
                System.out.println("Heuristico total: " + -1*new BicingHeuristic().getHeuristicValue(o));
                System.out.print("Waste: " + finalState.getTotalWaste() + " ProfitBic: " + finalState.getProfit()+ " Distance: " + finalState.getDist1()+finalState.getDist2());
                System.out.println(" RealProfit: " + finalState.getRealProfit());
                finalState.print();
                //printAllStations(est, finalState);
            }
            else if (input.equals("hill2")) {
                //El hill por defecto dentro de su implementacion busca el menor sucesor
                Problem p = new Problem(InitialState, new BicingSuccesors(), new BicingTest(), new BicingHeuristic2());
                Search s = new HillClimbingSearch();
                SearchAgent agent = new SearchAgent(p, s);

                System.out.println(agent.getActions().size());
                printActions(agent.getActions());
                printInstrumentation(agent.getInstrumentation());

                Object o = s.getGoalState();
                BicingBoard finalState = (BicingBoard) o;
                System.out.println("Heuristico total: " + -1*new BicingHeuristic2().getHeuristicValue(o));
                System.out.print("Waste: " + finalState.getTotalWaste() + " ProfitBic: " + finalState.getProfit()+ " Distance: " + finalState.getDist1()+finalState.getDist2());
                System.out.println(" RealProfit: " + finalState.getRealProfit());
                finalState.print();
            }
            else if (input.equals("fake_hill")) {
                for (int j=0; j < 10000; ++j) {
                    List<Successor> a = new BicingSuccesors().getSuccessors(InitialState);
                    //System.out.println(a.size());
                    double best = -1000;
                    int bestState = 0;
                    for (int i = 0; i < a.size(); ++i) {
                        BicingBoard newState = (BicingBoard) a.get(i).getState();
                        double value = -1*new BicingHeuristic().getHeuristicValue(newState);
                        if (best < value) {
                            best = value;
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
//Nicolas2