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

public class MainTest {

    //Funcion que muestra que operador escogio
    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }
    //Funcion que muestra cuantos nodos se expandieron
    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();

        while(keys.hasNext()) {
            String key = (String)keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
    }
    //Funcion que dado un problema p, te devuelve el estado final de un algoritmo hill
    public static Object hill(Problem p) throws Exception {
        Search s = new HillClimbingSearch();
        SearchAgent agent = new SearchAgent(p, s);

        //Funciones para poder observar los pasos del algoritmo.
        //System.out.println(agent.getActions().size());
        //printActions(agent.getActions());
        //printInstrumentation(agent.getInstrumentation());

        return s.getGoalState();
    }
    //Funcion que dado un problema p, un stiter, un k y un lambda, te devuelve el estado final de un algoritmo SA
    public static Object sim(Problem p, int stiter, int k, double lambda) throws Exception {
        //Cantidad de steps que queremos usar
        int steps = 10000;
        Search s = new SimulatedAnnealingSearch(steps, stiter, k, lambda);
        SearchAgent agent = new SearchAgent(p, s);

        return s.getGoalState();
    }


    public static void main(String[] args) throws Exception {
        //La cantidad de estaciones del problema
        int estaciones=25;
        //La cantidad de bicicletas del problema
        int bicicletas=1250;
        //La cantidad de furgonetas del problema
        int furgonetas=5;
        //Tipo de acercamiento de la solucion inicial "Basic" = vacio | "Mixed" = aleatorio | ->
        // ->  "Greedy" = maximiza la ganancia por bicicletas
        String solucionInicial="Basic";
        //El valor del stiter para el simmulated
        int stiter=100;
        //El valor de k para el simmulated
        int k=1;
        //El valor de lambda para el simmulated
        double lambda=0.0001;
        Random ran = new Random();
        //Semilla generada de manera "aleatoria"
        int x = ran.nextInt(1000);
        System.out.println("Seed: " + x);

        //Si queremos analizar un problema en hora punta hay que reempalzar EQUILIBRIUM por RUSH_HOUR
        Estaciones est = new Estaciones(estaciones, bicicletas, Estaciones.EQUILIBRIUM, x);

        //HC + BICIS
        System.out.println("HC + BICIS ////////////////////////////////////////////////////////////////////");
        BicingBoard2 InitialState1 = new BicingBoard2(est, furgonetas, solucionInicial);
        Problem p1 = new Problem(InitialState1, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic3());
        BicingBoard2 finalState1 = (BicingBoard2) hill(p1);
        finalState1.print();

        //HC + GANANCIA TOTAL
        System.out.println("HC + GANANCIA TOTAL ////////////////////////////////////////////////////////////");
        BicingBoard2 InitialState2 = new BicingBoard2(est, furgonetas, solucionInicial);
        Problem p2 = new Problem(InitialState2, new BicingSuccesors2(), new BicingTest(), new BicingHeuristic4());
        BicingBoard2 finalState2 = (BicingBoard2) hill(p2);
        finalState2.print();

        //SA + BICIS
        System.out.println("SA + BICIS ////////////////////////////////////////////////////////////////////");
        BicingBoard2 InitialState3 = new BicingBoard2(est, furgonetas, solucionInicial);
        Problem p3 = new Problem(InitialState3, new BicingSuccesors2AS(), new BicingTest(), new BicingHeuristic3());
        BicingBoard2 finalState3 = (BicingBoard2) sim(p3, stiter, k, lambda);
        finalState3.print();

        //SA + GANANCIA TOTAL
        System.out.println("SA + GANANCIA TOTAL ////////////////////////////////////////////////////////////");
        BicingBoard2 InitialState4 = new BicingBoard2(est, furgonetas, solucionInicial);
        Problem p4 = new Problem(InitialState4, new BicingSuccesors2AS(), new BicingTest(), new BicingHeuristic4());
        BicingBoard2 finalState4 = (BicingBoard2) sim(p4, stiter, k, lambda);
        finalState1.print();

    }
}
