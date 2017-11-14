package Projeto.auxiliares;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class Ponto {
    private int x, y, z;

    private INDArray matrizProjecao;
    private static final float FOCALD = -900;

    public Ponto(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;

        matrizProjecao = Nd4j.create(new float[]{1, 0, 0, 0,
                        0, 1, 0, 0,
                        0, 0, 0, 0,
                        0, 0, 1/FOCALD, 1},
                new int[]{4, 4});
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public Ponto projetaPonto() {
        INDArray ponto = Nd4j.create(new float[]{x, y, z, 1}, new int[]{4, 1});
        INDArray pontoProjetado = matrizProjecao.mmul(ponto);
        pontoProjetado = pontoProjetado.div(pontoProjetado.getFloat(3, 0));

        return new Ponto(pontoProjetado.getInt(0, 0),
                         pontoProjetado.getInt(1, 0),
                         pontoProjetado.getInt(2, 0));
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
}
