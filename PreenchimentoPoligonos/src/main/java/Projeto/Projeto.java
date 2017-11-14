package Projeto;

import Projeto.auxiliares.GerenciadorInterface;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Projeto {
    private long window;

    private static final int LARGURA = 600;
    private static final int ALTURA = 420;
    private static final String TITULO = "Preenchimento de Polígonos";

    // Cor do fundo
    private static final float BG_COLOR_R = 0.0f;
    private static final float BG_COLOR_G = 0.0f;
    private static final float BG_COLOR_B = 0.0f;

    // Tratamento de INTERFACE
    private static GerenciadorInterface INTERFACE;

    // Funcionalidades
    private static preenchimentoPoligonos Poligonos;
    private static ModelagemCubo Modelagem;

    // Estados
    private enum Estados {
        MENU, POLIGONOS, MODELAGEM
    }

    private Estados estado = Estados.MENU;

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

        INTERFACE = new GerenciadorInterface(window);
        INTERFACE.setupCallbacks();

        Poligonos = new preenchimentoPoligonos(INTERFACE);
        Modelagem = new ModelagemCubo(INTERFACE);

        // Resolucao do monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // Centraliza a janela
        glfwSetWindowPos(window, (vidmode.width() - LARGURA) / 2, (vidmode.height() - ALTURA) / 2);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
    }

    private void verificaTeclaMenu() {
        switch (INTERFACE.key_pressed) {
            case GLFW_KEY_ESCAPE:
                glfwSetWindowShouldClose(window, true);
                break;
            case GLFW_KEY_P:
                estado = Estados.POLIGONOS;
                break;
            case GLFW_KEY_M:
                estado = Estados.MODELAGEM;
        }
    }

    private void loop() {
        GL.createCapabilities();

        glOrtho(0.0f, LARGURA, ALTURA, 0.0f, 0.0f, 1.0f);
        glClearColor(BG_COLOR_R, BG_COLOR_G, BG_COLOR_B, 0.0f);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if (INTERFACE.isPointerInsideWindow()) {
                INTERFACE.atualizaCoodenadasAtuais();
            }

            switch (estado) {
                case MENU:
                    if (INTERFACE.acao == GerenciadorInterface.Acao.TECLADO_PRESS) {
                        verificaTeclaMenu();
                        INTERFACE.limpaAcao();
                    }
                    break;
                case POLIGONOS:
                    if (!Poligonos.render()) {
                        estado = Estados.MENU;
                    }
                    break;
                case MODELAGEM:
                    if (!Modelagem.render()) {
                        estado = Estados.MENU;
                    }
                    break;
            }

            glfwSwapBuffers(window); // Desenha o que ta no buffer na tela
            glfwPollEvents(); // Registra os eventos
        }
    }

    public static void main(String[] args) {
        new Projeto().run();
    }
}
