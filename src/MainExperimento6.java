import IA.Bicing.*;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class MainExperimento6 {

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
        double tiempo = 0;
        long startTime, endTime;

        for (int i =0; i < 10; ++i) {
            int x = ran.nextInt(1000);
            //System.out.println("Seed: " + x);
            Estaciones est = new Estaciones(25, 1250, Estaciones.EQUILIBRIUM, x);
            BicingBoard2 InitialState1 = new BicingBoard2(est, 5, "Basic");

            //HC + BICIS
            startTime = System.nanoTime();
            Problem p1 = new Problem(InitialState1, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic3());
            BicingBoard2 finalState1 = (BicingBoard2) hill(p1);
            endTime = System.nanoTime();
            tiempo += (endTime - startTime)/1000000.0;

        }
        tiempo = tiempo/10;
        System.setOut(new PrintStream(new FileOutputStream("Experimento6.txt")));
        System.out.println("tiempo: " + tiempo);


    }
}
