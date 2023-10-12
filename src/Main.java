import IA.Bicing.*;
import java.util.Random;
import IA.Bicing.BicingBoard;

public class Main {
    public static void main(String[] args) {

        Random ran = new Random();
        int x = ran.nextInt(100);
        Estaciones est = new Estaciones(25, 1250, Estaciones.EQUILIBRIUM, x);
        BicingBoard InitialState= new BicingBoard(est, 5, "a");

        InitialState.print();


    }
}