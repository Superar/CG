package simulacao;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;
import simulacao.utils.GerenciadorInterface;
import simulacao.utils.ShaderProgram;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memFree;

public class simulacao3D {

    private long window;

    private static final int LARGURA = 600;
    private static final int ALTURA = 420;
    private static final String TITULO = "Simulação 3D";

    private static final float BG_COLOR_R = 0.0f;
    private static final float BG_COLOR_G = 0.0f;
    private static final float BG_COLOR_B = 0.0f;

    private static GerenciadorInterface INTERFACE;
    private ShaderProgram SHADER;
    private int vaoId;

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Nao foi possivel encontrar o GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(LARGURA, ALTURA, TITULO, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Falha na criação da janela");
        }

        INTERFACE = new GerenciadorInterface(window);
        INTERFACE.setupCallbacks();

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - LARGURA) / 2, (vidmode.height() - ALTURA) / 2);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void loop() {
        GL.createCapabilities();
        glClearColor(BG_COLOR_R, BG_COLOR_G, BG_COLOR_B, 0.0f);

        SHADER = new ShaderProgram();
        SHADER.createVertexShader("/vertex.vs");
        SHADER.createFragmentShader("/fragment.fs");
        SHADER.link();

        float[] vertices = new float[]{
                0.0f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f
        };
        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        memFree(verticesBuffer);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            SHADER.bind();

            glBindVertexArray(vaoId);
            glEnableVertexAttribArray(0);

            glDrawArrays(GL_TRIANGLES, 0, 3);

            glDisableVertexAttribArray(0);
            glBindVertexArray(0);

            SHADER.unbind();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void run() {
        init();
        loop();

        // Destruicao da janela
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Termina o GLFW
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static void main(String[] args) {
        new simulacao3D().run();
    }
}
