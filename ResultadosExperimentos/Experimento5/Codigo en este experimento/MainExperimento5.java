import IA.Bicing.*;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class MainExperimento5 {

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            //System.out.println(action);
        }
    }

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();

        while(keys.hasNext()) {
            String key = (String)keys.next();
            String property = properties.getProperty(key);
            //System.out.println(key + " : " + property);
        }
    }

    public static Object hill(Problem p) throws Exception {
        Search s = new HillClimbingSearch();
        SearchAgent agent = new SearchAgent(p, s);

        //System.out.println(agent.getActions().size());
        printActions(agent.getActions());
        printInstrumentation(agent.getInstrumentation());

        return s.getGoalState();
    }

    public static Object sim(Problem p, int stiter, int k, double lambda) throws Exception {
        int steps = 10000;
        Search s = new SimulatedAnnealingSearch(steps, stiter, k, lambda);
        SearchAgent agent = new SearchAgent(p, s);

        //printActions(agent.getActions());
        //printInstrumentation(agent.getInstrumentation());
        return s.getGoalState();
    }


    public static void main(String[] args) throws Exception {
        Random ran = new Random();

        double ganancia[] = {0,0,0,0};
        double distancia[] = {0,0,0,0};
        double tiempo[] = new double[4];
        long startTime, endTime;

        for (int i =0; i < 10; ++i) {
            int x = ran.nextInt(1000);
            //System.out.println("Seed: " + x);
            Estaciones est = new Estaciones(25, 1250, Estaciones.EQUILIBRIUM, x);
            BicingBoard2 InitialState1 = new BicingBoard2(est, 5, "Basic");
            BicingBoard2 InitialState2 = new BicingBoard2(est, 5, "Basic");
            BicingBoard2 InitialState3 = new BicingBoard2(est, 5, "Basic");
            BicingBoard2 InitialState4 = new BicingBoard2(est, 5, "Basic");

            //HC + BICIS
            startTime = System.nanoTime();
            Problem p1 = new Problem(InitialState1, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic3());
            BicingBoard2 finalState1 = (BicingBoard2) hill(p1);
            endTime = System.nanoTime();
            ganancia[0] += finalState1.getProfit();
            distancia[0] += finalState1.getDist();
            tiempo[0] += (endTime - startTime)/1000000.0;

            // HC + GANANCIA TOTAL
            startTime = System.nanoTime();
            Problem p2 = new Problem(InitialState2, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic4());
            BicingBoard2 finalState2 = (BicingBoard2) hill(p2);
            endTime = System.nanoTime();
            ganancia[1] += finalState2.getProfit();
            distancia[1] += finalState2.getDist();
            tiempo[1] += (endTime - startTime)/1000000.0;

            //SA + BICIS
            startTime = System.nanoTime();
            Problem p3 = new Problem(InitialState3, new BicingSuccesors2AS(), new BicingTest(), new BicingHeuristic3());
            BicingBoard2 finalState3 = (BicingBoard2) sim(p3, 100, 1, 0.0001);
            endTime = System.nanoTime();
            ganancia[2] += finalState3.getProfit();
            distancia[2] += finalState3.getDist();
            tiempo[2] += (endTime - startTime)/1000000.0;

            //SA + GANANCIA TOTAL
            startTime = System.nanoTime();
            Problem p4 = new Problem(InitialState4, new BicingSuccesors2AS(), new BicingTest(), new BicingHeuristic4());
            BicingBoard2 finalState4 = (BicingBoard2) sim(p4, 100, 1, 0.0001);
            endTime = System.nanoTime();
            ganancia[3] += finalState4.getProfit();
            distancia[3] += finalState4.getDist();
            tiempo[3] += (endTime - startTime)/1000000.0;
        }
        ganancia[0] = ganancia[0]/10;
        ganancia[1] = ganancia[1]/10;
        ganancia[2] = ganancia[2]/10;
        ganancia[3] = ganancia[3]/10;
        distancia[0] = distancia[0]/10;
        distancia[1] = distancia[1]/10;
        distancia[2] = distancia[2]/10;
        distancia[3] = distancia[3]/10;
        tiempo[0] = tiempo[0]/10;
        tiempo[1] = tiempo[1]/10;
        tiempo[2] = tiempo[2]/10;
        tiempo[3] = tiempo[3]/10;

        System.setOut(new PrintStream(new FileOutputStream("Experimento5.txt")));

        System.out.println("HC + BICIS");
        System.out.println("Ganancia: "+ganancia[0]+" distancia: "+ distancia[0]+ " tiempo: "+tiempo[0]);
        System.out.println();
        System.out.println("HC + GANANCIA TOTAL");
        System.out.println("Ganancia: "+ganancia[1]+" distancia: "+ distancia[1]+ " tiempo: "+tiempo[1]);
        System.out.println();
        System.out.println("SA + BICIS");
        System.out.println("Ganancia: "+ganancia[2]+" distancia: "+ distancia[2]+ " tiempo: "+tiempo[2]);
        System.out.println();
        System.out.println("SA + GANANCIA TOTAL");
        System.out.println("Ganancia: "+ganancia[3]+" distancia: "+ distancia[3]+ " tiempo: "+tiempo[3]);

    }
}
