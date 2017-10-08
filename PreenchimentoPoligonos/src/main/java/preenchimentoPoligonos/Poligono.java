package preenchimentoPoligonos;

import java.util.ArrayList;

public class Poligono {
    private ArrayList<Ponto> pontos;
    private Scanline scanline;
    private boolean closed; // Poligono ja esta fechado

    private int maxLine;
    private int minLine;


    public Poligono() {
        pontos = new ArrayList<Ponto>();
        this.closed = false;
        scanline = new Scanline();
    }

    public void addPonto(int x, int y) {

        System.out.println("Ponto: " + x + ", " + y);

        Ponto p1 = new Ponto(x,y);

        if (!pontos.isEmpty()) {
            Ponto p2 = pontos.get(pontos.size() - 1);
            scanline.addAresta(p1, p2);

            if(this.minLine > y){
                this.minLine = y;
            }

            if(this.maxLine < y){
                this.maxLine = y;
            }

            System.out.println("Min: " + minLine + ", Max: " + maxLine);
        } else {
            this.maxLine = y;
            this.minLine = y;
        }

        this.pontos.add(p1);
    }

    public void close() {
        Ponto pontoFinal = new Ponto(pontos.get(0).getX(), pontos.get(0).getY());

        scanline.addAresta(pontos.get(pontos.size() - 1), pontoFinal);

        pontos.add(pontoFinal);

        this.closed = true;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public Ponto getUltimoPonto() {
        if (!pontos.isEmpty()) {
            return pontos.get(pontos.size() - 1);
        } else {
            return null;
        }
    }

    public void desenha() {

        if (!isClosed()){
            bresenhamLineDrawer lineDrawer = new bresenhamLineDrawer(1.0f, 1.0f, 1.0f);

            Ponto pontoInicial, pontoFinal;

            for (int i = 1; i < pontos.size(); i++) {
                pontoInicial = pontos.get(i - 1);
                pontoFinal = pontos.get(i);
                lineDrawer.drawLine(pontoInicial.getX(), pontoInicial.getY(), pontoFinal.getX(), pontoFinal.getY());
            }

        } else {

            scanline.preenche(this.minLine, this.maxLine);
        }
    }
}
