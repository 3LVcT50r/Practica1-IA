import IA.Bicing.*;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.framework.Successor;
import aima.search.informed.HillClimbingSearch;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import java.util.*;

public class MainExperimento1 {

    private  static HashMap<String,Integer>  setInitializeSet1() {
        HashMap<String, Integer> s = new HashMap<>();
        s.put("change_stop1", 0);
        s.put("swap", 0);
        s.put("add_station", 0);
        s.put("pick_up", 0);
        s.put("drop", 0);
        s.put("add_stop", 0);
        s.put("profit", 0);
        s.put("change_stop2", 0);
        s.put("delete_stop", 0);
        s.put("swap", 0);
        s.put("add_stop", 0);
        return  s;
    }
    private  static HashMap<String,Integer>  setInitializeSet2() {
        HashMap<String, Integer> s = new HashMap<>();
        s.put("profit", 0);
        s.put("add_station", 0);
        s.put("add_stop", 0);
        s.put("pick_up", 0);
        s.put("swap", 0);
        s.put("change_stop1", 0);
        s.put("change_stop2", 0);
        s.put("switch", 0);
        s.put("add_stop2", 0);
        s.put("change_stops", 0);
        s.put("drop", 0);
        return  s;
    }

    private static void printActions(List actions, HashMap<String,Integer> m) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            String stripped = action.replaceAll("\\(.*\\)", "");

            if (m.containsKey(stripped)) {
                m.put(stripped, m.get(stripped)+1);
            }
            else {
                m.put(stripped, 1);
            }

            //System.out.println(stripped);
            System.out.println(action);
        }
    }

    private static void printInstrumentation(Properties properties, HashMap<String,Integer> m) {
        Iterator keys = properties.keySet().iterator();

        while(keys.hasNext()) {
            String key = (String)keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
    }

    public static Object hill(Problem p, HashMap<String,Integer> m) throws Exception {
        Search s = new HillClimbingSearch();
        SearchAgent agent = new SearchAgent(p, s);

        System.out.println(agent.getActions().size());
        printActions(agent.getActions(), m);
        printInstrumentation(agent.getInstrumentation(), m);

        return s.getGoalState();
    }


    public static void main(String[] args) throws Exception {
        System.setOut(new PrintStream(new FileOutputStream("Experimento1.txt")));
        HashMap<String, Integer> Set1 = new HashMap<>();
        Set1 = setInitializeSet1();
        HashMap<String, Integer> Set2 = new HashMap<>();
        Set2 = setInitializeSet2();
        HashMap<String, Integer> Set3 = new HashMap<>();
        Set3 = setInitializeSet1();
        HashMap<String, Integer> Set4 = new HashMap<>();
        Set4 = setInitializeSet2();
        HashMap<String, Integer> Set5 = new HashMap<>();
        Set5 = setInitializeSet1();
        HashMap<String, Integer> Set6 = new HashMap<>();
        Set6 = setInitializeSet2();

        Random ran = new Random();

        for(int i=0;  i < 20; ++i) {
            int x = ran.nextInt(100);
            System.out.println("Seed: " + x);

            Estaciones est = new Estaciones(25, 1250, Estaciones.EQUILIBRIUM, x);

            //Solucion basica
            BicingBoard InitialState = new BicingBoard(est, 5, "Basic");
            BicingBoard2 InitialState2 = new BicingBoard2(est, 5, "Basic");
            System.out.println("Profit: " + InitialState.getProfit());
            Problem p;

            //Tipo: Hill; Heuristico: Maximización de lo que obtenemos por los traslados de las bicicletas + ponderacion;
            //Operadores: "Simples" (Set1)
            System.out.println("Set Simple Heuristico Ponderado  ///////////////////////////////////////");
            p = new Problem(InitialState, new BicingSuccesors(), new BicingTest(), new BicingHeuristic());
            BicingBoard finalState = (BicingBoard) hill(p, Set1);
            System.out.println("Profit: " + finalState.getProfit());
            Set1.put("profit",Set1.get("profit")+finalState.getProfit());

            //Tipo: Hill; Heuristico: Maximización de lo que obtenemos por los traslados de las bicicletas;
            //Operadores: "Complejos" (Set2)
            System.out.println("Set Complejo Heuristico Base ///////////////////////////////////////");
            p = new Problem(InitialState2, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic3());
            BicingBoard2 finalState2 = (BicingBoard2) hill(p, Set2);
            System.out.println("Profit: " + finalState2.getProfit());
            Set2.put("profit",Set2.get("profit")+finalState2.getProfit());

            //Solucion mixed
            InitialState = new BicingBoard(est, 5, "Mixed");
            InitialState2 = new BicingBoard2(est, 5, "Mixed");
            System.out.println("Profit: " + InitialState.getProfit());

            //Tipo: Hill; Heuristico: Maximización de lo que obtenemos por los traslados de las bicicletas + ponderacion;
            //Operadores: "Simples" (Set1)
            System.out.println("Set Simple Heuristico Ponderado  ///////////////////////////////////////");
            p = new Problem(InitialState, new BicingSuccesors(), new BicingTest(), new BicingHeuristic());
            finalState = (BicingBoard) hill(p,Set3);
            System.out.println("Profit: " + finalState.getProfit());
            Set3.put("profit",Set3.get("profit")+finalState.getProfit());

            //Tipo: Hill; Heuristico: Maximización de lo que obtenemos por los traslados de las bicicletas;
            //Operadores: "Complejos" (Set2)
            System.out.println("Set Complejo Heuristico Base ///////////////////////////////////////");
            p = new Problem(InitialState2, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic3());
            finalState2 = (BicingBoard2) hill(p,Set4);
            System.out.println("Profit: " + finalState2.getProfit());
            Set4.put("profit",Set4.get("profit")+finalState2.getProfit());

            //Solucion greedy
            InitialState = new BicingBoard(est, 5, "Greedy");
            InitialState2 = new BicingBoard2(est, 5, "Greedy");
            System.out.println("Profit: " + InitialState.getProfit());

            //Tipo: Hill; Heuristico: Maximización de lo que obtenemos por los traslados de las bicicletas + ponderacion;
            //Operadores: "Simples" (Set1)
            System.out.println("Set Simple Heuristico Ponderado  ///////////////////////////////////////");
            p = new Problem(InitialState, new BicingSuccesors(), new BicingTest(), new BicingHeuristic());
            finalState = (BicingBoard) hill(p, Set5);
            System.out.println("Profit: " + finalState.getProfit());
            Set5.put("profit",Set5.get("profit")+finalState.getProfit());

            //Tipo: Hill; Heuristico: Maximización de lo que obtenemos por los traslados de las bicicletas;
            //Operadores: "Complejos" (Set2)
            System.out.println("Set Complejo Heuristico Base ///////////////////////////////////////");
            p = new Problem(InitialState2, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic3());
            finalState2 = (BicingBoard2) hill(p, Set6);
            System.out.println("Profit: " + finalState2.getProfit());
            Set6.put("profit",Set1.get("profit")+finalState2.getProfit());
        }

        System.out.println("Set1+Basic");
        for (Map.Entry<String, Integer> entrada : Set1.entrySet()) {
            System.out.println("Operador " + entrada.getKey() + " Valor " + entrada.getValue());
        }
        System.out.println("Set2+Basic");
        for (Map.Entry<String, Integer> entrada : Set2.entrySet()) {
            System.out.println("Operador " + entrada.getKey() + " Valor " + entrada.getValue());
        }
        System.out.println();
        System.out.println("Set1+Mixed");
        for (Map.Entry<String, Integer> entrada : Set3.entrySet()) {
            System.out.println("Operador " + entrada.getKey() + " Valor " + entrada.getValue());
        }
        System.out.println("Set2+Mixed");
        for (Map.Entry<String, Integer> entrada : Set4.entrySet()) {
            System.out.println("Operador " + entrada.getKey() + " Valor " + entrada.getValue());
        }
        System.out.println();
        System.out.println("Set1+Greedy");
        for (Map.Entry<String, Integer> entrada : Set5.entrySet()) {
            System.out.println("Operador " + entrada.getKey() + " Valor " + entrada.getValue());
        }
        System.out.println("Set2+Greedy");
        for (Map.Entry<String, Integer> entrada : Set6.entrySet()) {
            System.out.println("Operador " + entrada.getKey() + " Valor " + entrada.getValue());
        }
    }
}
