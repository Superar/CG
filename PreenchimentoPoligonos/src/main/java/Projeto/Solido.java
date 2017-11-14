package Projeto;

import Projeto.auxiliares.Ponto;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;

class Solido {

    private INDArray matrizProjecao;
    ArrayList<Poligono> faces;
    private Poligono faceAtual;

    Solido (float d) {
        matrizProjecao = Nd4j.create(new float[]{1, 0, 0, 0,
                                                 0, 1, 0, 0,
                                                 0, 0, 0, 0,
                                                 0, 0, 1/d, 1},
                                     new int[]{4, 4});
        faces = new ArrayList<Poligono>();
        faceAtual = new Poligono();
    }

    void adicionaPonto(int x, int y, int z) {
        INDArray ponto = Nd4j.create(new float[]{x, y, z, 1}, new int[]{4, 1});
        INDArray pontoProjetado = matrizProjecao.mmul(ponto);
        pontoProjetado = pontoProjetado.div(pontoProjetado.getFloat(3, 0));

        faceAtual.addPonto(pontoProjetado.getInt(0, 0), pontoProjetado.getInt(1, 0));
    }

    void closeFaceAtual() {
        faceAtual.close();
        faces.add(faceAtual);
        faceAtual = new Poligono();
    }

    void setCorFaceAtual(float red, float green, float blue)
    {
        faceAtual.cor.setCor(red, green, blue);
    }

    // TODO Melhorar rotacao de poligono. Nao esta alterando nada
    void rotacionaSolido(float angulo) {
        for (Poligono p : faces) {
            p.rotacionaPoligono(angulo);
        }
    }
}
