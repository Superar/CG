package Projeto.auxiliares;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class Ponto {
    private int x, y, z;

    private INDArray matrizProjecao;
    private static final float FOCALD = -1000;

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

    public Ponto rotacionaPonto(float angulo, float x, float y, float z) {
        float cos = (float) Math.cos(angulo);
        float sen = (float) Math.sin(angulo);
        INDArray matrizRotacao = Nd4j.create(new float[]{ x*x*(1-cos)+cos, x*y*(1-cos)-z*sen, x*z*(1-cos)+y*sen, 0,
                                                          y*x*(1-cos)+z*sen, y*y*(1-cos)+cos, y*z*(1-cos)-x*sen, 0,
                                                          x*z*(1-cos)-y*sen, y*z*(1-cos)+x*sen, z*z*(1-cos)+cos, 0,
                                                          0, 0, 0, 1},
                                             new int[]{4, 4});
        INDArray ponto = Nd4j.create(new float[]{this.x, this.y, this.z, 1}, new int[]{4, 1});
        INDArray pontoRotacionado = matrizRotacao.mmul(ponto);
        return new Ponto(pontoRotacionado.getInt(0, 0),
                         pontoRotacionado.getInt(1, 0),
                         pontoRotacionado.getInt(2, 0));
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }
}
