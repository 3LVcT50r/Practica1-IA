import IA.Bicing.*;
import java.util.Random;
import java.util.Scanner;

import IA.Bicing.BicingBoard;

public class Main {
    static public void printStation(Estaciones est, int i) {
        Estacion st = est.get(i);
        System.out.print(" X: " + st.getCoordX() + " Y: " + st.getCoordY() + " Demand " + st.getDemanda());
        System.out.println(" BicNotUsed: " + st.getNumBicicletasNoUsadas());
    }

    public static void main(String[] args) {

        String input;
        Scanner sc = new Scanner(System.in);

        Random ran = new Random();
        int x = ran.nextInt(100);

        System.out.println("IntialState type (Greedy, Mixed, Basic)");
        input = sc.next();


        Estaciones est = new Estaciones(25, 1250, Estaciones.EQUILIBRIUM, x);
        BicingBoard InitialState= new BicingBoard(est, 5, input);


        while (sc.hasNext()) {
            input = sc.next();
            if (input.equals("print_state")) {
                InitialState.print();
            }
            else if (input.equals("print_station")) {
                int station = sc.nextInt();
                printStation(est, station);
            }
            else if (input.equals("change_station")) {
                int st1 = sc.nextInt();
                int st2 = sc.nextInt();
                InitialState.operatorChangeStation(st1,st2);
            }
            else if (input.equals("delete_stop")) {
                int vn = sc.nextInt();
                int sp = sc.nextInt();
                InitialState.operatorDeleteStop(vn, sp);
            }
            else if (input.equals("pick_up")) {
                int vn = sc.nextInt();
                int ntbic = sc.nextInt();
                InitialState.operatorPickUp(vn, ntbic);
            }
            else if (input.equals("drop")) {
                int vn = sc.nextInt();
                int nbic1 = sc.nextInt();
                InitialState.operatorDrop(vn,nbic1);
            }
            else if (input.equals("swap")) {
                int v1 = sc.nextInt();
                int v2 = sc.nextInt();
                int sp1 = sc.nextInt();
                int sp2 = sc.nextInt();
                InitialState.operatorSwap(v1,v2,sp1,sp2);
            }
        }

        sc.close();

    }
}