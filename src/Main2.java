import IA.Bicing.*;

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

    public static Object hill(Problem p) throws Exception {
        Search s = new HillClimbingSearch();
        SearchAgent agent = new SearchAgent(p, s);

        System.out.println(agent.getActions().size());
        printActions(agent.getActions());
        printInstrumentation(agent.getInstrumentation());

        return s.getGoalState();
    }


    public static void main(String[] args) throws Exception {
        Random ran = new Random();
        int x = ran.nextInt(100);

        Estaciones est = new Estaciones(50, 2500, Estaciones.EQUILIBRIUM, 1234);
        BicingBoard InitialState = new BicingBoard(est, 10, "Basic" );
        BicingBoard2 InitialState2 = new BicingBoard2(est, 10, "Basic" );
        InitialState.print();
        Problem p;

        /*//Tipo: Hill; Heuristico: Maximización de lo que obtenemos por los traslados de las bicicletas + ponderacion;
        //Operadores: "Simples" (Set1)
        System.out.println("Heuristico 1 ///////////////////////////////////////");
        p = new Problem(InitialState, new BicingSuccesors(), new BicingTest(), new BicingHeuristic());
        BicingBoard finalState = (BicingBoard) hill(p);
        finalState.print();

        //Tipo: Hill; Ganancia total + ponderacion; Operadores: "Simples" (Set1)
        System.out.println("Heuristico 2 ///////////////////////////////////////");
        p = new Problem(InitialState, new BicingSuccesors(), new BicingTest(), new BicingHeuristic2());
        finalState = (BicingBoard) hill(p);
        finalState.print();
        */
        //Tipo: Hill; Heuristico: Maximización de lo que obtenemos por los traslados de las bicicletas;
        //Operadores: "Complejos" (Set2)
        System.out.println("Heuristico 3 ///////////////////////////////////////");
        p = new Problem(InitialState2, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic3());
        BicingBoard2 finalState2 = (BicingBoard2) hill(p);
        finalState2.print();

        //Tipo: Hill; Ganancia total; Operadores: "Complejos" (Set2)
        System.out.println("Heuristico 4 ///////////////////////////////////////");
        p = new Problem(InitialState2, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic4());
       finalState2 = (BicingBoard2) hill(p);
        finalState2.print();

        //printAllStations(est, finalState);
    }
}
