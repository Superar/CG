package Projeto;

import Projeto.auxiliares.Ponto;

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

    Solido rotacionaSolido(float angulo, float x, float y, float z) {
        Solido solidoRotacionado = new Solido(this);
        for (Poligono p : faces) {
            solidoRotacionado.faces.add(p.rotacionaPoligono(angulo, x, y, z));
        }
        return solidoRotacionado;
    }

    private Solido projetaSolido() {
        Solido solidoProjetado = new Solido(this);
        for (Poligono p : faces) {
            solidoProjetado.faces.add(p.projetaPoligono());
        }
        return solidoProjetado;
    }

    void desenha() {
        for (Poligono p : this.projetaSolido().faces) p.desenha();
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
