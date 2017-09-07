package bresenham;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;


public class BresenhamAlgorithm
{
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

    private void run()
    {
        init();
        loop();

        // Destruicao da janela
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Termina o GLFW
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    private void init()
    {
        GLFWErrorCallback.createPrint(System.err).set();

        if( !glfwInit() )
        {
            throw new IllegalStateException("Nao foi possivel encontrar o GLFW");
        }

        // Configuracao da janela
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(LARGURA, ALTURA, TITULO, NULL, NULL);
        if ( window == NULL )
        {
            throw new RuntimeException("Falha na criacao da janela GLFW");
        }

        // Criacao do registro de pressionamento de teclas
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) ->
        {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);
        });

        glfwSetMouseButtonCallback(window, (window, button, action, mods) ->
        {
            if ( button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS )
            {
                DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
                glfwGetCursorPos(window, xpos, ypos);
                System.out.println("Cursor em: (" + xpos.get(0) + ", " + ypos.get(0) + ")");
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

    private void loop()
    {
        GL.createCapabilities();

        glOrtho(0.0f, LARGURA, ALTURA, 0.0f, 0.0f, 1.0f); // Projecao para usar coordenadas igual em plano cartesiano
        glClearColor(BG_COLOR_R, BG_COLOR_G, BG_COLOR_B, 0.0f); // Qual a cor que ele usa para limpar o framebuffer

        while ( !glfwWindowShouldClose(window) )
        {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Limpa o frame buffer

            drawPoint(20, 20);
            drawPoint(21, 20);
            drawPoint(560, 320);
            glfwSwapBuffers(window); // Desenha o que ta no buffer na tela

            glfwPollEvents(); // Registra os eventos
        }
    }

    private void drawPoint(int x, int y)
    {
        glPointSize(1);
        glColor3f(LINE_COLOR_R, LINE_COLOR_G, LINE_COLOR_B);
        glBegin(GL_POINTS);
        glVertex2i(x, y);
        glEnd();
    }

    public static void main(String[] args)
    {
        new BresenhamAlgorithm().run();
    }
}