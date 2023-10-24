import IA.Bicing.*;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class MainExperimento4 {

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


    public static void main(String[] args) throws Exception {
        Random ran = new Random();

       System.setOut(new PrintStream(new FileOutputStream("Experimento4.txt")));

        long startTime, endTime;
        double timeElapsed;

        for(int j=0;  j <= 5; ++j) {
            double time=0;
            int estaciones=0, bicicletas=0, furgonetas=0;
            for(int i=0;  i < 10; ++i) {
                int x = ran.nextInt(1000);
                //System.out.println("Seed: " + x);
                estaciones=25+25*j;
                bicicletas=1250+j*25*50;
                furgonetas=estaciones/5;

                Estaciones est = new Estaciones(estaciones, bicicletas, Estaciones.EQUILIBRIUM, x);
                Problem p;
                //Solucion basic
                BicingBoard2 InitialState2 = new BicingBoard2(est, furgonetas, "Basic");

                startTime = System.nanoTime();
                //Tipo: Hill; Heuristico: Maximización de lo que obtenemos por los traslados de las bicicletas;
                //Operadores: "Complejos" (Set2)
                //System.out.println("Set Complejo Heuristico Base ///////////////////////////////////////");
                p = new Problem(InitialState2, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic3());
                BicingBoard2 finalState2 = (BicingBoard2) hill(p);
                endTime = System.nanoTime();
                timeElapsed = (endTime - startTime)/1000000.0;
                time += timeElapsed;
                System.out.println(timeElapsed);

            }
            time = time/10.0;
            System.out.println("Estaciones: " + estaciones + " Bicicletas: " + bicicletas + " Furgonetas: " + furgonetas +" Promedio: " + time);
        }
    }
}
