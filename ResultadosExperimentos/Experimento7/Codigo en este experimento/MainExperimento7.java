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

public class MainExperimento7 {

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
        double ganancia1 = 0, ganancia2=0;
        System.setOut(new PrintStream(new FileOutputStream("Experimento7.txt")));

        for (int j =0; j < 5; ++j) {
            int furgonetas=5+j*5;
            System.out.print("Furgonetas: " + furgonetas);
            for (int i = 0; i < 10; ++i) {
                int x = ran.nextInt(1000);
                //System.out.println("Seed: " + x);


                //HC + BICIS
                Estaciones est = new Estaciones(25, 1250, Estaciones.EQUILIBRIUM, x);
                BicingBoard2 InitialState1 = new BicingBoard2(est, furgonetas, "Basic");
                Problem p1 = new Problem(InitialState1, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic3());
                BicingBoard2 finalState1 = (BicingBoard2) hill(p1);
                ganancia1 += finalState1.getProfit();

                //HC + BICIS
                Estaciones est2 = new Estaciones(25, 1250, Estaciones.RUSH_HOUR, x);
                BicingBoard2 InitialState2 = new BicingBoard2(est2, furgonetas, "Basic");
                Problem p2 = new Problem(InitialState2, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic3());
                BicingBoard2 finalState2 = (BicingBoard2) hill(p1);
                ganancia2 += finalState2.getProfit();
            }
            ganancia1 = ganancia1 / 10;
            ganancia2 = ganancia2 / 10;
            System.out.println(" Gnancia Equilibrado: " + ganancia1 + " Gnancia hora punta " + ganancia2);
        }
    }
}
