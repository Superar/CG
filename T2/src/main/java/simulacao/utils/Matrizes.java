package simulacao.utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import simulacao.Camera;
import simulacao.Modelo;

public class Matrizes {
    private final Matrix4f projectionMatrix;
    private final Matrix4f worldMatrix;
    private final Matrix4f viewMatrix;

    private GerenciadorInterface INTERFACE;

    public Matrizes(GerenciadorInterface gerenciadorInterface) {
        projectionMatrix = new Matrix4f();
        worldMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();

        INTERFACE = gerenciadorInterface;
    }

    public Matrix4f getProjectionMatrix(float FOV, float zNear, float zFar) {
        return projectionMatrix.identity().perspective(FOV, INTERFACE.getAspectRatio(), zNear, zFar);
    }

    public Matrix4f getWorldMatrix(Modelo modelo) {
        return worldMatrix.identity().
                rotateX((float) Math.toRadians(modelo.rotacao.x)).
                rotateY((float) Math.toRadians(modelo.rotacao.y)).
                rotateZ((float) Math.toRadians(modelo.rotacao.z)).
                scale(modelo.escala);
    }

    public Matrix4f getViewMatrix(Camera camera, Modelo modelo) {
        return viewMatrix.identity().
                rotate((float)Math.toRadians(modelo.rotacao.x), new Vector3f(1, 0, 0)).
                rotate((float)Math.toRadians(modelo.rotacao.y), new Vector3f(0, 1, 0)).
                translate(-camera.posicao.x, -camera.posicao.y, -camera.posicao.z);
    }
}
