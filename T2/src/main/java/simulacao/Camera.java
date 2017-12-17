package simulacao;

import org.joml.Vector3f;

public class Camera {
    public Vector3f posicao;
    public Vector3f rotacao;

    Camera() {
        posicao = new Vector3f(0, 0, 200);
        rotacao = new Vector3f(0, 0, 0);
    }

    Camera(Vector3f pos, Vector3f rot) {
        posicao = pos;
        rotacao = rot;
    }

    public void mover(float x, float y, float z) {
        if ( z != 0 ) {
            posicao.x += (float)Math.sin(Math.toRadians(rotacao.y)) * -1.0f * z;
            posicao.z += (float)Math.cos(Math.toRadians(rotacao.y)) * z;
        }
        if ( x != 0) {
            posicao.x += (float)Math.sin(Math.toRadians(rotacao.y - 90)) * -1.0f * x;
            posicao.z += (float)Math.cos(Math.toRadians(rotacao.y - 90)) * x;
        }
        posicao.y += y;
    }

    public void rotacionar(float x, float y, float z) {
        rotacao.x += x;
        rotacao.y += y;
        rotacao.z += z;
    }
}
