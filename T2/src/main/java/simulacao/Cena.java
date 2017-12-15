package simulacao;

import java.util.ArrayList;

import org.joml.Matrix4f;
import simulacao.utils.GerenciadorInterface;
import simulacao.utils.Matrizes;
import simulacao.utils.ShaderProgram;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

class Cena {
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float ZNEAR = 0.01f;
    private static final float ZFAR = 1000.f;

    private static GerenciadorInterface INTERFACE;
    private static ShaderProgram SHADER;
    private static Matrizes MATRIZES;

    private Camera camera;

    Cena(GerenciadorInterface gerenciadorInterface) {
        INTERFACE = gerenciadorInterface;
        SHADER = new ShaderProgram("/vertex.vs", "/fragment.fs");
        MATRIZES = new Matrizes(INTERFACE);

        camera = new Camera();

        SHADER.createUniform("projectionMatrix");
        SHADER.createUniform("viewMatrix");
        SHADER.createUniform("worldMatrix");
    }

    void render(ArrayList<Modelo> modelos) {
        checkInterface();
        SHADER.bind();
        Matrix4f projectionMatrix = MATRIZES.getProjectionMatrix(FOV, ZNEAR, ZFAR);
        SHADER.setUniform("projectionMatrix", projectionMatrix);

        for (Modelo modelo : modelos) {
            Matrix4f viewMatrix = MATRIZES.getViewMatrix(camera, modelo);
            SHADER.setUniform("viewMatrix", viewMatrix);
            Matrix4f worldMatrix = MATRIZES.getWorldMatrix(modelo);
            SHADER.setUniform("worldMatrix", worldMatrix);

            glBindVertexArray(modelo.shaderSetup.getVaoId());
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glDrawElements(GL_TRIANGLES, modelo.shaderSetup.getVertexCount(), GL_UNSIGNED_INT, 0);
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glBindVertexArray(0);
        }
        SHADER.unbind();
    }

    private void checkInterface() {
        if (INTERFACE.acao == GerenciadorInterface.Acao.TECLADO_PRESS) {
            switch (INTERFACE.key_pressed) {
                case GLFW_KEY_LEFT:
                    camera.rotacionar(0, 5, 0);
                    break;
                case GLFW_KEY_RIGHT:
                    camera.rotacionar(0, -5, 0);
                    break;
                case GLFW_KEY_UP:
                    camera.rotacionar(5, 0, 0);
                    break;
                case GLFW_KEY_DOWN:
                    camera.rotacionar(-5, 0, 0);
                    break;
                case GLFW_KEY_W:
                    camera.mover(0, -5f, 0);
                    break;
                case GLFW_KEY_A:
                    camera.mover(5f, 0, 0);
                    break;
                case GLFW_KEY_S:
                    camera.mover(0, 5f, 0);
                    break;
                case GLFW_KEY_D:
                    camera.mover(-5, 0, 0);
            }
        }
        INTERFACE.limpaAcao();
    }
}
