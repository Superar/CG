package Projeto.auxiliares;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class Ponto {
    private float x, y, z;

    private INDArray matrizProjecao;
    private static final float FOCALD = -1000;

    public Ponto(float x, float y, float z) {
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
        return (int)this.x;
    }

    public int getY() {
        return (int)this.y;
    }

    public int getZ() {
        return (int)this.z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public Ponto projetaPonto() {
        INDArray ponto = Nd4j.create(new float[]{x, y, z, 1}, new int[]{4, 1});
        INDArray pontoProjetado = matrizProjecao.mmul(ponto);
        pontoProjetado = pontoProjetado.div(pontoProjetado.getFloat(3, 0));

        return new Ponto(pontoProjetado.getInt(0, 0),
                         pontoProjetado.getInt(1, 0),
                         pontoProjetado.getInt(2, 0));
    }

    public void rotaciona(float angulo_x, float angulo_y) {
        float cos_x = (float) Math.cos(angulo_x);
        float sen_x = (float) Math.sin(angulo_x);

        float cos_y = (float) Math.cos(angulo_y);
        float sen_y = (float) Math.sin(angulo_y);

        INDArray matrizRotacao = Nd4j.create(new float[]{ cos_y, 0, sen_y, 0,
                                                          sen_x*sen_y, cos_x, -sen_x*cos_y, 0,
                                                          -cos_x*sen_y, sen_x, cos_x*cos_y, 0,
                                                          0, 0, 0, 1},
                                             new int[]{4, 4});
        INDArray ponto = Nd4j.create(new float[]{this.x, this.y, this.z, 1}, new int[]{4, 1});
        INDArray pontoRotacionado = matrizRotacao.mmul(ponto);

        this.x = pontoRotacionado.getFloat(0,0);
        this.y = pontoRotacionado.getFloat(1,0);
        this.z = pontoRotacionado.getFloat(2,0);
    }

    public void transladaPonto(int dx, int dy, int dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public double distancia(Ponto p){
        double x = Math.pow(this.x - p.x, 2);
        double y = Math.pow(this.y - p.y, 2);
        double z = Math.pow(this.z - p.z, 2);
        return Math.sqrt(x+y+z);
    }
}
