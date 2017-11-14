package Projeto;

import Projeto.auxiliares.Ponto;

import java.util.ArrayList;

import Projeto.auxiliares.Ponto;

class Solido {

    private ArrayList<Poligono> faces;
    Ponto ancora;
    private Poligono faceAtual;

    Solido() {
        this.ancora = new Ponto(0,0,0);
        faces = new ArrayList<Poligono>();
        faceAtual = new Poligono();
    }

    private Solido(Solido s) {
        this.faces = new ArrayList<Poligono>();
        this.faceAtual = s.faceAtual;
        this.ancora = s.ancora;
    }

    void adicionaPonto(int x, int y, int z) {
        faceAtual.addPonto(x, y, z);
    }

    void closeFaceAtual() {
        faceAtual.close();
        faces.add(faceAtual);
        faceAtual = new Poligono();
    }

    void setAncora(int x, int y, int z){
        this.ancora.setX(x);
        this.ancora.setY(y);
        this.ancora.setZ(z);
    }

    void setCorFaceAtual(float red, float green, float blue) {
        faceAtual.cor.setCor(red, green, blue);
    }

    void rotaciona(float angulo_x, float angulo_y) {
        this.translada(-ancora.getX(), -ancora.getY(), -ancora.getZ());
//        Solido solidoRotacionado = new Solido(this);
        for (Poligono p : this.faces) {
            p.rotaciona(angulo_x, angulo_y);
        }
        this.translada(ancora.getX(), ancora.getY(), ancora.getZ());

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

    void ordenaFaces(){
        ArrayList<Poligono> facesOrdenadas = new ArrayList<Poligono>(this.faces.size());

        int size = 0;
        double d,e;

        for(Poligono p : this.faces){
            Ponto ponto = new Ponto(0, 0, 1000);
            d = p.getMaiorDistancia(ponto);
            int i;
            for(i =0; i < size; i ++){
                e = facesOrdenadas.get(i).getMaiorDistancia(ponto);
                if(!(e>=d)) {
                    break;
                }
            }
            facesOrdenadas.add(i,p);
            size++;
        }
        this.faces = facesOrdenadas;
    }

}
