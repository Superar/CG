package Projeto;

import java.util.ArrayList;

class Solido {

    private ArrayList<Poligono> faces;
    private Poligono faceAtual;

    Solido () {
        faces = new ArrayList<Poligono>();
        faceAtual = new Poligono();
    }

    private Solido (Solido s) {
        this.faces = new ArrayList<Poligono>();
        this.faceAtual = s.faceAtual;
    }

    void adicionaPonto(int x, int y, int z) {
        faceAtual.addPonto(x, y, z);
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

    void rotaciona(float angulo_x, float angulo_y) {
//        Solido solidoRotacionado = new Solido(this);
        for (Poligono p : faces) {
            p.rotaciona(angulo_x, angulo_y);
        }
    }

    void translada(int dx, int dy, int dz) {
//        Solido solidoTransladado = new Solido(this);
        for (Poligono p : faces) {
            p.translada(dx, dy, dz);
        }
    }

    private Solido projetaSolido() {
        Solido solidoProjetado = new Solido(this);
        for (Poligono p : faces) {
            solidoProjetado.faces.add(p.projetaPoligono());
        }
        return solidoProjetado;
    }

    void desenha() {
        for (Poligono p : this.projetaSolido().faces)
            p.desenha();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < faces.size(); i++) {
            s.append("Face: ");
            s.append(i);
            s.append(" - ");
            s.append(faces.get(i));
            s.append("\n");
        }
        return s.toString();
    }
}
