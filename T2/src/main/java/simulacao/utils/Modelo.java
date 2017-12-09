package simulacao.utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Modelo {
    float[] vertices = new float[]{
            // Face traseira
            -0.5f, 0.5f, -2.0f,
            -0.5f, -0.5f, -2.0f,
            0.5f, -0.5f, -2.0f,
            0.5f, 0.5f, -2.0f,

            // Face frontal
            -0.5f, 0.5f, -1.5f,
            -0.5f, -0.5f, -1.5f,
            0.5f, -0.5f, -1.5f,
            0.5f, 0.5f, -1.5f,
    };
    int[] indices = new int[]{
            // Face traseira
            0, 1, 3, 3, 1, 2,
            // Face frontal
            4, 5, 7, 7, 5, 6,
    };
    float[] colours = new float[]{
            // Face traseira
            0.5f, 0.0f, 0.0f,
            0.5f, 0.0f, 0.0f,
            0.5f, 0.0f, 0.0f,
            0.5f, 0.0f, 0.0f,

            // Face frontal
            0.0f, 0.5f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.5f, 0.0f,
    };

    private GerenciadorInterface INTERFACE;
    private ShaderSetup shaderSetup;

    public final Vector3f posicao;
    public float escala;
    public final Vector3f rotacao;

    Matrix4f projectionMatrix;
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;

    Matrix4f worldMatrix;

    public Modelo(GerenciadorInterface gerInterface) {
        shaderSetup = new ShaderSetup(vertices, indices, colours);
        INTERFACE = gerInterface;

        posicao = new Vector3f(0, 0, 0);
        escala = 1.0f;
        rotacao = new Vector3f(0, 0, 0);

        float aspectRatio = (float) INTERFACE.getWindowWidth() / INTERFACE.getWindowHeight();
        projectionMatrix = new Matrix4f().identity().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
        setWorldMatrix();
    }

    public void render(ShaderProgram shader) {
        shader.bind();

        shader.createUniform("projectionMatrix", projectionMatrix);
        shader.createUniform("worldMatrix", worldMatrix);

        glBindVertexArray(shaderSetup.getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, shaderSetup.getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shader.unbind();
    }

    public void translada(float x, float y, float z) {
        posicao.x = x;
        posicao.y = y;
        posicao.z = z;
        setWorldMatrix();
    }

    public void rotaciona(float x, float y, float z) {
        rotacao.x = x;
        rotacao.y = y;
        rotacao.z = z;
        setWorldMatrix();
    }

    public void redimensiona(float escala) {
        this.escala = escala;
        setWorldMatrix();
    }

    private void setWorldMatrix() {
        worldMatrix = new Matrix4f().identity().translate(posicao).
                rotateX((float) Math.toRadians(rotacao.x)).
                rotateY((float) Math.toRadians(rotacao.y)).
                rotateZ((float) Math.toRadians(rotacao.z)).
                scale(escala);
    }
}
