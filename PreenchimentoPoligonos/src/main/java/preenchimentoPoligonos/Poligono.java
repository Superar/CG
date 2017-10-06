package preenchimentoPoligonos;

import java.util.ArrayList;

public class Poligono {
    private ArrayList<Ponto> pontos;
    private boolean closed; // Poligono ja esta fechado


    public Poligono() {
        pontos = new ArrayList<Ponto>();
        this.closed = false;
    }

    public void addPonto(int x, int y) {
        this.pontos.add(new Ponto(x, y));
    }

    public ArrayList<Ponto> getPontos() {
        return pontos;
    }

    public void close() {
        pontos.add(pontos.get(0));
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

        bresenhamLineDrawer lineDrawer = new bresenhamLineDrawer(1.0f, 1.0f, 1.0f);

        Ponto pontoInicial, pontoFinal;

        for (int i = 1; i < pontos.size(); i++) {
            pontoInicial = pontos.get(i - 1);
            pontoFinal = pontos.get(i);
            lineDrawer.drawLine(pontoInicial.getX(), pontoInicial.getY(), pontoFinal.getX(), pontoFinal.getY());
        }
    }
}
