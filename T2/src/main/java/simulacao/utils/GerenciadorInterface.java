package simulacao.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GerenciadorInterface {
    private long window;

    private GLFWFramebufferSizeCallback redimensionamento;
    private GLFWKeyCallback teclasTeclado;
    private GLFWMouseButtonCallback botoesMouse;

    private boolean PointerInsideWindow = true;

    public enum Acao {MOUSE_CLICK, TECLADO_PRESS}

    public Acao acao = null;
    public int key_pressed = 0;
    public double xClique = 0;
    public double yClique = 0;
    public double xAtual = 0;
    public double yAtual = 0;

    public GerenciadorInterface(long window) {
        this.window = window;

        redimensionamento = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                glViewport(0, 0, width, height);
                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                glPushMatrix();
                glOrtho(0.0f, width, height, 0.0f, 0.0f, 0.0f);

                glMatrixMode(GL_MODELVIEW);
                glLoadIdentity();
                glPushMatrix();
                glRotatef(-180.0f, 1.0f, 0.0f, 0.0f);
                glScalef(2.0f / width, 2.0f / height, 1.0f);
                glTranslatef(-width / 2.0f, -height / 2.0f, 0.0f);
            }
        };

        teclasTeclado = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action == GLFW_RELEASE) {
                    acao = Acao.TECLADO_PRESS;
                    key_pressed = key;
                }
            }
        };

        botoesMouse = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (button == GLFW_MOUSE_BUTTON_1) {
                    DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
                    DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
                    glfwGetCursorPos(window, xpos, ypos);

                    if (action == GLFW_RELEASE) {
                        acao = Acao.MOUSE_CLICK;
                        xClique = xpos.get(0);
                        yClique = ypos.get(0);
                    }
                }
            }
        };
    }

    public void setupCallbacks() {
        glfwSetFramebufferSizeCallback(window, redimensionamento);
        glfwSetKeyCallback(window, teclasTeclado);
        glfwSetMouseButtonCallback(window, botoesMouse);
    }

    public void atualizaCoodenadasAtuais() {
        DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window, xpos, ypos);
        xAtual = xpos.get(0);
        yAtual = ypos.get(0);
    }

    public boolean isPointerInsideWindow() {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);

        glfwGetWindowSize(window, width, height);
        glfwGetCursorPos(window, xpos, ypos);

        if (xpos.get(0) >= 0 && xpos.get(0) < (double) width.get(0) && ypos.get(0) >= 0 && ypos.get(0) < (double) height.get(0)) {
            if (!PointerInsideWindow) {
                PointerInsideWindow = true;
            }
        } else if (PointerInsideWindow) {
            PointerInsideWindow = false;
        }

        return PointerInsideWindow;
    }

    public void limpaAcao() {
        this.acao = null;
    }

    public int getWindowWidth() {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(window, width, height);
        return width.get(0);
    }

    public int getWindowHeight() {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(window, width, height);
        return height.get(0);
    }
}
