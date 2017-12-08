package simulacao.utils;

import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Modelo {
    float[] vertices = new float[]{
            -0.5f, 0.5f, -2.0f,
            -0.5f, -0.5f, -2.0f,
            0.5f, -0.5f, -1.5f,
            0.5f, 0.5f, -1.5f,
    };
    int[] indices = new int[]{
            0, 1, 3, 3, 1, 2,
    };
    float[] colours = new float[]{
            0.5f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f,
            0.0f, 0.5f, 0.5f,
    };

    private GerenciadorInterface INTERFACE;
    private ShaderSetup shaderSetup;

    Matrix4f projectionMatrix;
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;

    public Modelo(GerenciadorInterface gerInterface) {
        shaderSetup = new ShaderSetup(vertices, indices, colours);
        INTERFACE = gerInterface;

        float aspectRatio = (float) INTERFACE.getWindowWidth() / INTERFACE.getWindowHeight();
        projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public void render(ShaderProgram shader) {
        shader.bind();

        shader.createUniform("projectionMatrix", projectionMatrix);

        glBindVertexArray(shaderSetup.getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, shaderSetup.getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shader.unbind();
    }
}
