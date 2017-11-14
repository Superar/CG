package Projeto;

import java.util.ArrayList;

import Projeto.auxiliares.Cor;
import Projeto.auxiliares.Ponto;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

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

    // TODO: Melhorar a rotacao de poligono. Nao esta alterando os pontos do poligono depois do calculo
    void rotacionaPoligono(float angulo) {
        ArrayList<Ponto> pontosRotacionados = new ArrayList<>();
        INDArray matrizRotacao = Nd4j.create(new float[]{(float) Math.cos(angulo), (float) -Math.sin((double)angulo), 0,
                                                         (float) Math.sin(angulo), (float) Math.cos(angulo), 0,
                                                         0, 0, 1},
                                             new int[]{3, 3});
        for (Ponto p : pontos) {
            INDArray ponto = Nd4j.create(new float[]{(float)p.getX(), p.getY(), 1}, new int[]{3, 1});
            INDArray pontoRotacionado = matrizRotacao.mmul(ponto);
            pontosRotacionados.add(new Ponto(pontoRotacionado.getInt(0, 0), pontoRotacionado.getInt(1, 0)));
        }
        pontos = pontosRotacionados;
    }
}
