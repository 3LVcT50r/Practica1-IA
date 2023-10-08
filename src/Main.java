import IA.Bicing.*;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        Random ran = new Random();
        int x = ran.nextInt(100);
        Estaciones epa = new Estaciones(25, 1250, Estaciones.EQUILIBRIUM, x);
        int bicis=0;

        for(int i=0;i<epa.size();i++){
            System.out.print(" X= ");
            System.out.print(epa.get(i).getCoordX());
            System.out.print(" Y= ");
            System.out.print(epa.get(i).getCoordY());
            System.out.print(" Bicis= ");
            System.out.print(epa.get(i).getNumBicicletasNoUsadas());
            bicis += epa.get(i).getNumBicicletasNoUsadas();
            System.out.print(" Demanda= ");
            System.out.println(epa.get(i).getDemanda());
            //bicis += epa.get(i).getDemanda();
        }
        System.out.println(bicis);

    }
}