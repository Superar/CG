package preenchimentoPoligonos;

import java.util.ArrayList;
import java.util.LinkedList;

public class Scanline {

    public ArrayList<LinkedList<Node>> ET;
    public ArrayList<LinkedList<Node>> AET;

    public Scanline(){
        ET = new ArrayList<LinkedList<Node>>();
        AET = new ArrayList<LinkedList<Node>>();
    }

    void leftEdgeScan(int xmin, int ymin, int xmax, int ymax, int valor) {
        int x, y;

        x = xmin;
        y = ymin;

        int numerador = xmax - xmin;
        int denominador = ymax - ymin;
        int incremento = denominador;

        for (y = ymin; y <= ymax; y++) {
            pintaPixel(x,y,valor);

            incremento += numerador;

            if (incremento > denominador){
                x++;
                incremento -= denominador;
            }
        }
    }

    void pintaPixel(int x, int y, int valor) {

    }

    class Node{
        int ymax, x0, inclinacao;
    }
}
