package Projeto;

import java.util.ArrayList;

import Projeto.auxiliares.Cor;
import Projeto.auxiliares.Ponto;

class Poligono {
    private ArrayList<Ponto> pontos;
    private Scanline scanline;
    private boolean closed; // Poligono ja esta fechado

    Cor cor;

    private int maxLine;
    private int minLine;


    Poligono() {
        pontos = new ArrayList<>();
        this.closed = false;
        scanline = new Scanline();
        cor = new Cor();
    }

    void addPonto(int x, int y) {

        Ponto p1 = new Ponto(x, y);

        if (!pontos.isEmpty()) {
            Ponto p2 = pontos.get(pontos.size() - 1);
            scanline.addAresta(p1, p2);

            if (this.minLine > y) {
                this.minLine = y;
            }

            if (this.maxLine < y) {
                this.maxLine = y;
            }

        } else {
            this.maxLine = y;
            this.minLine = y;
        }

        this.pontos.add(p1);
    }

    void close() {
        Ponto pontoFinal = new Ponto(pontos.get(0).getX(), pontos.get(0).getY());

        scanline.addAresta(pontos.get(pontos.size() - 1), pontoFinal);

        pontos.add(pontoFinal);

        this.closed = true;
    }

    boolean isClosed() {
        return this.closed;
    }

    Ponto getUltimoPonto() {
        if (!pontos.isEmpty()) {
            return pontos.get(pontos.size() - 1);
        } else {
            return null;
        }
    }

    void desenha() {

        if (!isClosed()) {
            BresenhamLineDrawer lineDrawer = new BresenhamLineDrawer(cor.red, cor.green, cor.blue);

            Ponto pontoInicial, pontoFinal;

            for (int i = 1; i < pontos.size(); i++) {
                pontoInicial = pontos.get(i - 1);
                pontoFinal = pontos.get(i);
                lineDrawer.drawLine(pontoInicial.getX(), pontoInicial.getY(), pontoFinal.getX(), pontoFinal.getY());
            }

        } else {
            scanline.preenche(this.minLine, this.maxLine, cor);
        }
    }
}
