package Projeto.auxiliares;

public class Cor {
    public float red, green, blue;

    public Cor(){
        this.red = 1.0f;
        this.green = 1.0f;
        this.blue = 1.0f;
    }

    public void setCor(float r, float g, float b) {
        this.red = r;
        this.green = g;
        this.blue = b;
    }
}
