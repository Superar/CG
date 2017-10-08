package preenchimentoPoligonos;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

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

        for (int linha = minLine; linha < maxLine; linha++) {

            nodes = et.get(linha);

            if (nodes != null) {

                for (Node node : nodes) {
                    aet.add(new Node(node));
                }

                Collections.sort(aet);
            }

            System.out.println("linha " + linha + ": "+ aet);

            draw(linha);
        }
    }

    void draw(int linha) {
        Node start = null, end;
        int par = 0;

        for (int i = 0; i < aet.size();){

            if (aet.get(i).y_max == linha){
                aet.remove(i);
            } else {
                if (par == 0){
                    start = aet.get(i);
                    par++;
                    i++;
                } else {
                    par = 0;

                    end = aet.get(i);

                    for (int j = start.x0; j < end.x0; j++) {
                        bresenhamLineDrawer.drawPoint(j, linha);
                    }

                    start.incrementa();
                    end.incrementa();
                }
            }

        }

//        Collections.sort(aet);

    }

    class Node implements Comparable<Node> {
        int y_max, x0;
        int dx, dy, inc, sinal;

        public Node(Node node) {
            this.y_max = node.y_max;
            this.x0 = node.x0;
            this.dx = node.dx;
            this.dy = node.dy;
            this.inc = node.inc;
            this.sinal = node.sinal;
        }

        public Node(int y_max, int x0, int dx, int dy) {
            this.y_max = y_max;
            this.x0 = x0;
            this.dx = Math.abs(dx);
            this.dy = Math.abs(dy);

            if (dx*dy < 0){
                sinal = -1;
            } else {
                sinal = 1;
            }

            this.inc = 0;
        }

        public void incrementa(){

            inc += dx;

            while (inc >= dy) {
                this.x0 += sinal;
                inc -= dy;
            }
        }

        @Override
        public String toString() {
            return "[" + y_max + ", " + x0 + ", " + sinal*dx+ "/" + dy + "]";
        }

        @Override
        public int compareTo(Node node) {
            return Integer.compare(this.x0, node.x0);
        }
    }
}
