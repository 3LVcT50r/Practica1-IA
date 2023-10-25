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

public class MainExperimento3 {

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
        int stiter[] = {1, 50, 100, 150, 200};
        int k[] = {1,2,25,400,800};
        double lambda[] = {1,0.1,0.01,0.001,0.0001};
        double res[][] = new double[5][5];

       System.setOut(new PrintStream(new FileOutputStream("Experimento3.txt")));
       DecimalFormat formato = new DecimalFormat("0.#####");

        for(int i=0;  i < 5; ++i) {
            System.out.println("stiter: " + stiter[i]);
            for(int j=0;  j < 5; ++j) {
                for (int l = 0; l < 5; ++l) {
                    int suma=0;
                    for (int z =0; z < 10; ++z) {
                        int x = ran.nextInt(1000);
                        //System.out.println("Seed: " + x);
                        Estaciones est = new Estaciones(25, 1250, Estaciones.EQUILIBRIUM, x);
                        Problem p;
                        BicingBoard2 InitialState2 = new BicingBoard2(est, 5, "Basic");

                        Problem p2 = new Problem(InitialState2, new BicingSuccesors2AS(), new BicingTest(), new BicingHeuristic3());
                        BicingBoard2 finalState = (BicingBoard2) sim(p2, stiter[i], k[j], lambda[l]);
                        suma += finalState.getProfit();
                    }
                    suma = suma / 10;
                    res[j][l] = suma;
                    //double lambdaValue = lambda[l];
                    //System.out.print(formato.format(lambdaValue) + " profit: " + suma);
                    // System.out.println("sitter: " + stiter[i] + " k: " + k[j] + " Lambda: " + formato.format(lambdaValue) + " profit: " + suma);
                }
            }
                for(int m=0; m < 6; ++m) {
                    for(int n=0; n < 6; ++n) {
                        if (m == 0 && n > 0) {
                            double lambdaValue = lambda[n-1];
                            System.out.print(" " + formato.format(lambdaValue));
                        }
                        if (m > 0 && n == 0) {
                            System.out.print(k[m-1]);
                        }
                        if (n > 0 && m > 0) {
                            System.out.print(" " + res[m-1][n-1]);
                        }
                    }
                    System.out.println();
                }

        }
    }
}
