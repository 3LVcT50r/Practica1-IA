import IA.Bicing.*;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

public class MainExperimento2 {

    // 0 Basic 1 Mixed 2 Greedy
    public static int[][] profitSet1 = new int[3][20];
    public static int[][] profitSet2 = new int[3][20];
    public static String[][] nodeSet1 = new String[3][20];
    public static String[][] nodeSet2 = new String[3][20];
    public static long[][] timeSet1 = new long[3][20];
    public static long[][] timeSet2 = new long[3][20];


    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            //System.out.println(action);
        }
    }

    private static void printInstrumentation(Properties properties, int set, int type, int index ) {
        Iterator keys = properties.keySet().iterator();

        while(keys.hasNext()) {
            String key = (String)keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
            if (set == 0) nodeSet1[type][index] = property;
            else nodeSet2[type][index] = property;
        }
    }

    public static Object hill(Problem p,  int set, int type, int index) throws Exception {
        Search s = new HillClimbingSearch();
        SearchAgent agent = new SearchAgent(p, s);

        System.out.println(agent.getActions().size());
        printActions(agent.getActions());
        printInstrumentation(agent.getInstrumentation(), set, type, index);

        return s.getGoalState();
    }


    public static void main(String[] args) throws Exception {
        Random ran = new Random();
        long startTime, endTime, timeElapsed;

        for(int i=0;  i < 20; ++i) {
            int x = ran.nextInt(100);
            //System.out.println("Seed: " + x);

            Estaciones est = new Estaciones(25, 1250, Estaciones.EQUILIBRIUM, x);

            //Solucion basica
            BicingBoard InitialState = new BicingBoard(est, 5, "Basic");
            BicingBoard2 InitialState2 = new BicingBoard2(est, 5, "Basic");
            Problem p;

            startTime = System.nanoTime();
            //Tipo: Hill; Heuristico: Maximización de lo que obtenemos por los traslados de las bicicletas + ponderacion;
            //Operadores: "Simples" (Set1)
            //System.out.println("Set Simple Heuristico Ponderado  ///////////////////////////////////////");
            p = new Problem(InitialState, new BicingSuccesors(), new BicingTest(), new BicingHeuristic());
            BicingBoard finalState = (BicingBoard) hill(p,  0, 0, i);
            endTime = System.nanoTime();
            profitSet1[0][i] = finalState.getProfit();
            timeElapsed = (endTime - startTime)/1000000;
            timeSet1[0][i] = timeElapsed;

            startTime = System.nanoTime();
            //Tipo: Hill; Heuristico: Maximización de lo que obtenemos por los traslados de las bicicletas;
            //Operadores: "Complejos" (Set2)
            //System.out.println("Set Complejo Heuristico Base ///////////////////////////////////////");
            p = new Problem(InitialState2, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic3());
            BicingBoard2 finalState2 = (BicingBoard2) hill(p,  1, 0, i);
            endTime = System.nanoTime();
            profitSet2[0][i] = finalState2.getProfit();
            timeElapsed = (endTime - startTime)/1000000;
            timeSet2[0][i] = timeElapsed;

            //Solucion mixed
            InitialState = new BicingBoard(est, 5, "Mixed");
            InitialState2 = new BicingBoard2(est, 5, "Mixed");

            startTime = System.nanoTime();
            //Tipo: Hill; Heuristico: Maximización de lo que obtenemos por los traslados de las bicicletas + ponderacion;
            //Operadores: "Simples" (Set1)
            //System.out.println("Set Simple Heuristico Ponderado  ///////////////////////////////////////");
            p = new Problem(InitialState, new BicingSuccesors(), new BicingTest(), new BicingHeuristic());
            finalState = (BicingBoard) hill(p, 0, 1, i);
            endTime = System.nanoTime();
            profitSet1[1][i] = finalState.getProfit();
            timeElapsed = (endTime - startTime)/1000000;
            timeSet1[1][i] = timeElapsed;

            startTime = System.nanoTime();
            //Tipo: Hill; Heuristico: Maximización de lo que obtenemos por los traslados de las bicicletas;
            //Operadores: "Complejos" (Set2)
            //System.out.println("Set Complejo Heuristico Base ///////////////////////////////////////");
            p = new Problem(InitialState2, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic3());
            finalState2 = (BicingBoard2) hill(p, 1, 1, i);
            endTime = System.nanoTime();
            profitSet2[1][i] = finalState2.getProfit();
            timeElapsed = (endTime - startTime)/1000000;
            timeSet2[1][i] = timeElapsed;

            //Solucion greedy
            InitialState = new BicingBoard(est, 5, "Greedy");
            InitialState2 = new BicingBoard2(est, 5, "Greedy");

            startTime = System.nanoTime();
            //Tipo: Hill; Heuristico: Maximización de lo que obtenemos por los traslados de las bicicletas + ponderacion;
            //Operadores: "Simples" (Set1)
            //System.out.println("Set Simple Heuristico Ponderado  ///////////////////////////////////////");
            p = new Problem(InitialState, new BicingSuccesors(), new BicingTest(), new BicingHeuristic());
            finalState = (BicingBoard) hill(p,  0, 2, i);
            endTime = System.nanoTime();
            profitSet1[2][i] = finalState.getProfit();
            timeElapsed = (endTime - startTime)/1000000;
            timeSet1[2][i] = timeElapsed;

            startTime = System.nanoTime();
            //Tipo: Hill; Heuristico: Maximización de lo que obtenemos por los traslados de las bicicletas;
            //Operadores: "Complejos" (Set2)
            //System.out.println("Set Complejo Heuristico Base ///////////////////////////////////////");
            p = new Problem(InitialState2, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic3());
            finalState2 = (BicingBoard2) hill(p,  1, 2, i);
            endTime = System.nanoTime();
            profitSet2[2][i] = finalState2.getProfit();
            timeElapsed = (endTime - startTime)/1000000;
            timeSet2[2][i] = timeElapsed;

        }
        System.setOut(new PrintStream(new FileOutputStream("Experimento2.txt")));
        System.out.println("Set1");

        System.out.println("Nodos");
        for (int j=0; j < 3; ++j) {
            if (j == 0 ) System.out.print("Basic");
            else if (j == 1 ) System.out.print("Mixed");
            else  System.out.print("Greedy");

            for (int i = 0; i < 20; ++i) {
                System.out.print(" " + nodeSet1[j][i]);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Ganancia");

        for (int j=0; j < 3; ++j) {
            if (j == 0 ) System.out.print("Basic");
            else if (j == 1 ) System.out.print("Mixed");
            else  System.out.print("Greedy");

            for (int i = 0; i < 20; ++i) {
                System.out.print(" " + profitSet1[j][i]);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Tiempo");

        for (int j=0; j < 3; ++j) {
            if (j == 0 ) System.out.print("Basic");
            else if (j == 1 ) System.out.print("Mixed");
            else  System.out.print("Greedy");

            for (int i = 0; i < 20; ++i) {
                System.out.print(" " + timeSet1[j][i]);
            }
            System.out.println();
        }

        System.out.println();
        System.out.println();

        System.out.println("Set2");

        System.out.println("Nodos");
        for (int j=0; j < 3; ++j) {
            if (j == 0 ) System.out.print("Basic");
            else if (j == 1 ) System.out.print("Mixed");
            else  System.out.print("Greedy");

            for (int i = 0; i < 20; ++i) {
                System.out.print(" " + nodeSet2[j][i]);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Ganancia");

        for (int j=0; j < 3; ++j) {
            if (j == 0 ) System.out.print("Basic");
            else if (j == 1 ) System.out.print("Mixed");
            else  System.out.print("Greedy");

            for (int i = 0; i < 20; ++i) {
                System.out.print(" " + profitSet2[j][i]);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Tiempo");

        for (int j=0; j < 3; ++j) {
            if (j == 0 ) System.out.print("Basic");
            else if (j == 1 ) System.out.print("Mixed");
            else  System.out.print("Greedy");

            for (int i = 0; i < 20; ++i) {
                System.out.print(" " + timeSet2[j][i]);
            }
            System.out.println();
        }
    }
}
