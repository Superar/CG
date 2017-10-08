package preenchimentoPoligonos;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class Scanline {

    private HashMap<Integer, LinkedList<Node>> et;
    private LinkedList<Node> aet;

    public Scanline() {
        et = new HashMap<Integer, LinkedList<Node>>();
    }

    public void addAresta(Ponto ponto1, Ponto ponto2) {
        if (!(ponto1.getY() - ponto2.getY() == 0)) {

            Ponto p1, p2;

            if (ponto1.getX() > ponto2.getX()) {
                p1 = ponto2;
                p2 = ponto1;
            } else {
                p1 = ponto1;
                p2 = ponto2;
            }

            int ymax, ymin, x0;

            if (p1.getY() > p2.getY()) {
                ymax = p1.getY();
                ymin = p2.getY();
                x0 = p2.getX();
            } else {
                ymax = p2.getY();
                ymin = p1.getY();
                x0 = p1.getX();
            }

            LinkedList<Node> nodes = et.get(ymin);

            if (nodes != null) {
                nodes.add(new Node(ymax, x0, p2.getX() - p1.getX(), p2.getY() - p1.getY()));

                Collections.sort(nodes);
            } else {
                nodes = new LinkedList<Node>();

                nodes.add(new Node(ymax, x0, p2.getX() - p1.getX(), p2.getY() - p1.getY()));

                this.et.put(ymin, nodes);
            }

            System.out.println(nodes);
            System.out.println(et);
        }
    }

    public void preenche(int minLine, int maxLine) {

        aet = new LinkedList<Node>();

        LinkedList<Node> nodes;

        for (int linha = minLine; linha <= maxLine; linha++) {

            nodes = et.get(linha);

            if (nodes != null) {

                for (Node node : nodes) {
                    aet.add(new Node(node));
                }

                Collections.sort(aet);
            }

            System.out.println(aet);

//            try {
//
//                TimeUnit.SECONDS.sleep(1);
//
//            } catch (Exception e){
//                System.out.println("travou");
//            }


            draw(linha);
        }
    }

    void draw(int linha) {

        int i = 0;
        Node start = null, end;

        try {

            for (Node node : aet) {
                if (node.y_max == linha) {
                    aet.remove(node);
                } else {
                    if (i == 0) {
                        start = node;
                        i++;
                    } else {
                        i = 0;

                        end = node;

                        for (int j = start.x0; j <= end.x0; j++) {
                            bresenhamLineDrawer.drawPoint(j, linha);
                        }

                        start.incrementa();
                        end.incrementa();

                        Collections.sort(aet);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void leftEdgeScan(int xmin, int ymin, int xmax, int ymax, int valor) {
        int x, y;

        x = xmin;
        y = ymin;

        int numerador = xmax - xmin;
        int denominador = ymax - ymin;
        int incremento = denominador;

        for (y = ymin; y <= ymax; y++) {
            //pintaPixel(x, y, valor);

            incremento += numerador;

            if (incremento > denominador) {
                x++;
                incremento -= denominador;
            }
        }
    }


    class Node implements Comparable<Node> {
        int y_max, x0;
        int numInclinacao, denInclinacao, incremento, sinalInclinacao;

        public Node(Node node) {
            this.y_max = node.y_max;
            this.x0 = node.x0;
            this.numInclinacao = node.numInclinacao;
            this.denInclinacao = node.denInclinacao;
            this.incremento = node.incremento;
            this.sinalInclinacao = node.sinalInclinacao;
        }

        public Node(int y_max, int x0, int numInclinacao, int denInclinacao) {
            this.y_max = y_max;
            this.x0 = x0;
            this.numInclinacao = Math.abs(numInclinacao);
            this.denInclinacao = Math.abs(denInclinacao);

            if (numInclinacao*denInclinacao < 0){
                sinalInclinacao = -1;
            } else {
                sinalInclinacao = 1;
            }

            this.incremento = 0;
        }

        public void incrementa(){

            incremento += numInclinacao;

            if (incremento >= 0) {
                this.x0 += sinalInclinacao;
                incremento -= 0;
            }
        }

        @Override
        public String toString() {
            return "[" + String.valueOf(y_max) + ", " + String.valueOf(x0) + ", " + numInclinacao+ "/" + denInclinacao + "]";
        }

        @Override
        public int compareTo(Node node) {
            return Integer.compare(this.x0, node.x0);
        }
    }
}
