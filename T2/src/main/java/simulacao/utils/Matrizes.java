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

    public Matrix4f getViewMatrix(Camera camera) {
        Vector3f cameraPos = camera.posicao;
        Vector3f rotation = camera.rotacao;

        viewMatrix.identity();
        // First do the rotation so camera rotates over its position
        viewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        // Then do the translation
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
    }

    public Matrix4f getModelViewMatrix(Camera camera, Modelo modelo) {
        return viewMatrix.identity().
                rotateX((float)Math.toRadians(modelo.rotacao.x)).
                rotateY((float)Math.toRadians(modelo.rotacao.y)).
                translate(-camera.posicao.x, -camera.posicao.y, -camera.posicao.z).
                rotateX((float)Math.toRadians(camera.rotacao.x)).
                rotateY((float)Math.toRadians(camera.rotacao.y));
    }
}
