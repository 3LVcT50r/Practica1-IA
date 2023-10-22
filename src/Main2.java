import IA.Bicing.BicingBoard2;
import IA.Bicing.BicingSuccesors2;
import IA.Bicing.BicingTest;
import IA.Bicing.BicingHeuristic3;
import IA.Bicing.BicingHeuristic4;

import IA.Bicing.Estacion;
import IA.Bicing.Estaciones;
import IA.Bicing.TestBicing;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.framework.Successor;
import aima.search.informed.HillClimbingSearch;

import java.util.*;

public class Main2 {
    static public void printStation(Estaciones est, BicingBoard2 b, int i) {
        Estacion st = est.get(i);
        int realBic = st.getNumBicicletasNext() + b.bicDropped(i) - b.bicPickUp(i);
        System.out.print("Station: " + i + " X: " + st.getCoordX() + " Y: " + st.getCoordY() + " Demand " + st.getDemanda());
        System.out.print(" BicNotUsed: " + st.getNumBicicletasNoUsadas() + " BicNext: " + st.getNumBicicletasNext());
        System.out.print(" BicDropped: " + b.bicDropped(i) + " BicPickUp: " + b.bicPickUp(i));
        System.out.println(" RealBic: " + realBic + " Profit: " + b.getProfitStation(i));
    }

    static public void printAllStations(Estaciones est, BicingBoard2 b) {
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
        BicingBoard2 InitialState= new BicingBoard2(est, 5, "Mixed" );
        InitialState.print();

        //Hill1
        Problem p = new Problem(InitialState, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic3());
        Search s = new HillClimbingSearch();
        SearchAgent agent = new SearchAgent(p, s);

        System.out.println(agent.getActions().size());
        printActions(agent.getActions());
        printInstrumentation(agent.getInstrumentation());

        Object o = s.getGoalState();
        BicingBoard2 finalState = (BicingBoard2) o;
        System.out.println("Heuristico total: " + -1*new BicingHeuristic3().getHeuristicValue(o));
        System.out.print("Waste: " + finalState.getTotalWaste() + " ProfitBic: " + finalState.getProfit() + " Distance: " + finalState.getDist1()+finalState.getDist2());
        System.out.println(" RealProfit: " + finalState.getRealProfit());
        finalState.print();

        //Hill2
        Problem p1 = new Problem(InitialState, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic4());
        Search s1 = new HillClimbingSearch();
        SearchAgent agent1 = new SearchAgent(p1, s1);

        System.out.println(agent1.getActions().size());
        printActions(agent1.getActions());
        printInstrumentation(agent1.getInstrumentation());

        Object o1 = s1.getGoalState();
        BicingBoard2 finalState1 = (BicingBoard2) o1;
        System.out.println("Heuristico total: " + -1*new BicingHeuristic4().getHeuristicValue(o1));
        System.out.print("Waste: " + finalState1.getTotalWaste() + " ProfitBic: " + finalState1.getProfit()+ " Distance: " + finalState1.getDist1()+finalState1.getDist2());
        System.out.println(" RealProfit: " + finalState1.getRealProfit());
        finalState1.print();
        //printAllStations(est, finalState);

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
            else if (input.equals("successors+")) {
                List<Successor> a = new BicingSuccesors2().getSuccessors(InitialState);
                double best = -1000;
                int bestState = 0;
                for (int i = 0; i < a.size(); ++i) {
                    BicingBoard2 newState = (BicingBoard2) a.get(i).getState();
                    double value = new BicingHeuristic3().getHeuristicValue(newState);
                    if (best < value) {
                        best = value;
                        bestState = i;
                    }
                    System.out.println("Est: " + i + " Profit: " + value);
                }
                System.out.println("BestState: " + bestState + " Profit: " + best);
            }
            else if (input.equals("successors")) {
                List<Successor> a = new BicingSuccesors2().getSuccessors(InitialState);
                System.out.println(a.size());
            }
            /*
            else if (input.equals("hill")) {
                Problem p = new Problem(InitialState, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic3());
                Search s = new HillClimbingSearch();
                SearchAgent agent = new SearchAgent(p, s);

                System.out.println(agent.getActions().size());
                printActions(agent.getActions());
                printInstrumentation(agent.getInstrumentation());

                Object o = s.getGoalState();
                BicingBoard2 finalState = (BicingBoard2) o;
                System.out.println("Heuristico total: " + -1*new BicingHeuristic3().getHeuristicValue(o));
                System.out.print("Waste: " + finalState.getTotalWaste() + " ProfitBic: " + finalState.getProfit());
                System.out.println(" RealProfit: " + finalState.getRealProfit());
                finalState.print();
                //printAllStations(est, finalState);
            }
            else if (input.equals("hill2")) {
                Problem p = new Problem(InitialState, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic4());
                Search s = new HillClimbingSearch();
                SearchAgent agent = new SearchAgent(p, s);

                System.out.println(agent.getActions().size());
                printActions(agent.getActions());
                printInstrumentation(agent.getInstrumentation());

                Object o = s.getGoalState();
                BicingBoard2 finalState = (BicingBoard2) o;
                System.out.println("Heuristico total: " + -1*new BicingHeuristic4().getHeuristicValue(o));
                System.out.print("Waste: " + finalState.getTotalWaste() + " ProfitBic: " + finalState.getProfit());
                System.out.println(" RealProfit: " + finalState.getRealProfit());
                finalState.print();
                //printAllStations(est, finalState);
            }
            */
        }
    }
}
