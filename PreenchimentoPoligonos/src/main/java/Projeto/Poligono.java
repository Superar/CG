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

    private Poligono(Poligono p) {
        this.pontos = new ArrayList<>();

        this.scanline = new Scanline();
        this.closed = p.closed;
        this.cor = p.cor;
    }

    void addPonto(int x, int y, int z) {

        Ponto p1 = new Ponto(x, y, z);

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
        Ponto pontoFinal = new Ponto(pontos.get(0).getX(), pontos.get(0).getY(), pontos.get(0).getZ());

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

    Poligono projetaPoligono() {
        Poligono poligonoProjetado = new Poligono(this);
        for (Ponto p : this.pontos) {
            poligonoProjetado.addPonto(p.projetaPonto().getX(),
                                       p.projetaPonto().getY(),
                                       p.projetaPonto().getZ());
        }
        return poligonoProjetado;
    }

    void rotaciona(float angulo_x, float angulo_y) {
//        Poligono polRotacionado = new Poligono(this);
        for (Ponto p : this.pontos) {
            p.rotaciona(angulo_x, angulo_y);
//            polRotacionado.addPonto(pontoProjetado.getX(), pontoProjetado.getY(), pontoProjetado.getZ());
        }
//        return polRotacionado;
    }

    void translada(int dx, int dy, int dz) {
        Poligono polTransladado = new Poligono(this);

        for (Ponto p : pontos) {
            p.transladaPonto(dx, dy, dz);

//            polTransladado.addPonto(pontoTranladado.getX(), pontoTranladado.getY(), pontoTranladado.getZ());
        }

//        return polTransladado;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("[ ");
        for (int i = 0; i < pontos.size() - 1; i++) {
            s.append(pontos.get(i));
            s.append(", ");
        }
        s.append(pontos.get(pontos.size() - 1));
        s.append(" ]");
        return s.toString();
    }
}
