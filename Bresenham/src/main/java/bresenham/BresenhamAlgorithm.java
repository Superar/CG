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

    private void bresenham(int x0, int y0, int x1, int y1) {
        int dx = x1 - x0;
        int dy = y1 - y0;

        if (abs(dx) > abs(dy)) {
            bresenhamAlgorithm(x0, y0, x1, y1, false);
        } else {
            bresenhamAlgorithm(y0, x0, y1, x1, true);
        }
    }

    private void bresenhamAlgorithm(int inicioPrincipal, int inicioSecundario, int fimPrincipal, int fimSecundario, boolean swap)
    {
        int dPrincipal = inicioPrincipal - fimPrincipal;
        int dSecundario = inicioSecundario - fimSecundario;

        int inc = 1;

        if (dPrincipal < 0)
        {
            int aux = fimPrincipal;
            fimPrincipal = fimPrincipal + dPrincipal;
            inicioPrincipal = aux;

            if (dSecundario < 0)
            {
                fimSecundario = fimSecundario + dSecundario;
                dSecundario = -dSecundario;
                dPrincipal = -dPrincipal;
            }
            else
            {
                fimSecundario = inicioSecundario;
                inc = -1;
                dPrincipal = -dPrincipal;
            }
        }
        else
        {
            if (dSecundario < 0)
            {
                dSecundario = -dSecundario;
                inc = -1;
            }
        }

        int d = 2 * dSecundario - dPrincipal;

        int incE = 2 * dSecundario;
        int incNE = 2 * (dSecundario - dPrincipal);


        for (int principal = fimPrincipal, secundario = fimSecundario; principal < inicioPrincipal; principal = principal + 1)
        {
            if (swap)
                drawPoint(secundario, principal);
            else
                drawPoint(principal, secundario);
            if (d <= 0) {
                d = d + incE;
            } else {
                d = d + incNE;
                secundario = secundario + inc;
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