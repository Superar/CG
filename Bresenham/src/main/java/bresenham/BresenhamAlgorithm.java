package bresenham;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.nio.DoubleBuffer;

import static java.lang.Math.abs;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;


public class BresenhamAlgorithm {
    private long window;

    private static final int LARGURA = 600;
    private static final int ALTURA = 420;
    private static final String TITULO = "Algoritmo de Bresenham";

    // Cor do fundo
    private static final float BG_COLOR_R = 0.0f;
    private static final float BG_COLOR_G = 0.0f;
    private static final float BG_COLOR_B = 0.0f;

    // Cor da linha
    private static final float LINE_COLOR_R = 1.0f;
    private static final float LINE_COLOR_G = 1.0f;
    private static final float LINE_COLOR_B = 1.0f;

    // Estados para eventos
    private boolean mouseDown = false;
    private boolean mouseAlreadyPressed = false;
    private double initialMouseXPos;
    private double initialMouseYPos;
    private double finalMouseXpos;
    private double finalMouseYpos;

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

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Nao foi possivel encontrar o GLFW");
        }

        // Configuracao da janela
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(LARGURA, ALTURA, TITULO, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Falha na criacao da janela GLFW");
        }

        // Criacao do registro de pressionamento de teclas
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) ->
        {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        glfwSetMouseButtonCallback(window, (window, button, action, mods) ->
        {
            if (button == GLFW_MOUSE_BUTTON_1) {
                DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
                glfwGetCursorPos(window, xpos, ypos);

                if (action == GLFW_PRESS) // Se o mouse for apertado, salva como posicao inicial
                {
                    mouseDown = true;
                    mouseAlreadyPressed = true;

                    initialMouseXPos = xpos.get(0);
                    initialMouseYPos = ypos.get(0);
                } else if (action == GLFW_RELEASE) // Se o mouse for solto, salva como posicao final
                {
                    mouseDown = false;

                    finalMouseXpos = xpos.get(0);
                    finalMouseYpos = ypos.get(0);
                }
            }
        });

        // Resolucao do monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // Centraliza a janela
        glfwSetWindowPos(window,
                (vidmode.width() - LARGURA) / 2,
                (vidmode.height() - ALTURA) / 2);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void loop() {
        GL.createCapabilities();

        glOrtho(0.0f, LARGURA, ALTURA, 0.0f, 0.0f, 1.0f); // Projecao para usar coordenadas igual em plano cartesiano
        glClearColor(BG_COLOR_R, BG_COLOR_G, BG_COLOR_B, 0.0f); // Qual a cor que ele usa para limpar o framebuffer

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Limpa o frame buffer

            if (mouseDown) {
                /* Enquanto o mouse e apertado, desenha linha a partir da posicao inicial ate
                 * a posicao atual do ponteiro */

                DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
                glfwGetCursorPos(window, xpos, ypos);

                bresenham((int) initialMouseXPos, (int) initialMouseYPos, (int) xpos.get(0), (int) ypos.get(0));
            } else if (mouseAlreadyPressed) {
                /* Enquanto o mouse estiver solto, desenha linha a partir
                * da posicao inicial ate a posicao final salva */

                bresenham((int) initialMouseXPos, (int) initialMouseYPos, (int) finalMouseXpos, (int) finalMouseYpos);
            }

            glfwSwapBuffers(window); // Desenha o que ta no buffer na tela

            glfwPollEvents(); // Registra os eventos
        }
    }

    public void bresenham(int x0, int y0, int x1, int y1) {
        int dx = x1 - x0;
        int dy = y1 - y0;

        if(abs(dx)>abs(dy)){
            bresenhamX(x0,y0,x1,y1);
        } else {
            bresenhamY(x0,y0,x1,y1);
        }
    }

    public void bresenhamX(int x0, int y0, int x1, int y1) {

        int dx = x1 - x0;
        int dy = y1 - y0;

        int incY = 1;

        if (dx < 0) {
            int aux = x0;
            x0 = x0 + dx;
            x1 = aux;

            if (dy < 0) {
                y0 = y0 + dy;
                dy = -dy;
                dx = -dx;
            } else {
                y0 = y1;
                incY = -1;
                dx = -dx;
            }
        } else {

            if (dy < 0) {
                y1 = y0 + dy;
                dy = -dy;
                incY = -1;
            }
        }

        int d = 2 * dy - dx;

        int incE = 2 * dy;
        int incNE = 2 * (dy - dx);


        for (int x = x0, y = y0; x < x1; x = x + 1) {
            drawPoint(x, y);
            if (d <= 0) {
                d = d + incE;
            } else {
                d = d + incNE;
                y = y + incY;
            }
        }
    }

    public void bresenhamY(int x0, int y0, int x1, int y1) {

        int dx = x1 - x0;
        int dy = y1 - y0;

        int incX = 1;

        if (dy < 0) {
            int aux = y0;
            y0 = y0 + dy;
            y1 = aux;

            if (dx < 0) {
                aux = y0;
                x0 = x0 + dx;
                dx = -dx;
                dy = -dy;
            } else {
                x0 = x1;
                incX = -1;
                dy = -dy;
            }
        } else {

            if (dx < 0) {
                x1 = x0 + dx;
                dx = -dx;
                incX = -1;
            }
        }

        int d = 2 * dx - dy;

        int incE = 2 * dx;
        int incNE = 2 * (dx - dy);


        for (int x = x0, y = y0; y < y1; y = y + 1) {
            drawPoint(x, y);
            if (d <= 0) {
                d = d + incE;
            } else {
                d = d + incNE;
                x = x + incX;
            }
        }
    }

    private void drawPoint(int x, int y) {
        glPointSize(1);
        glColor3f(LINE_COLOR_R, LINE_COLOR_G, LINE_COLOR_B);
        glBegin(GL_POINTS);
        glVertex2i(x, y);
        glEnd();
    }

    public static void main(String[] args) {
        new BresenhamAlgorithm().run();
    }
}