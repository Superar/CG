package Projeto;

import Projeto.auxiliares.GerenciadorInterface;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import Projeto.auxiliares.SimpleText;

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
    private static PreenchimentoPoligonos Poligonos;
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

        Poligonos = new PreenchimentoPoligonos(INTERFACE);
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
                    desenhaMenu();
                    if (INTERFACE.acao == GerenciadorInterface.Acao.TECLADO_PRESS) {
                        verificaTeclaMenu();
                        INTERFACE.limpaAcao();
                    }
                    break;
                case POLIGONOS:
                    if (!Poligonos.render()) {
                        estado = Estados.MENU;
                    }

                    GL11.glColor3f(1,1,1);
                    SimpleText.drawString("O botao esquerdo do Mouse seleciona os vertices do Poligono", 10,28);
                    SimpleText.drawString("A barra de espaço completa o Poligono", 10,INTERFACE.getWindowHeight()-28);
                    SimpleText.drawString("ESC volta para o menu", 10,INTERFACE.getWindowHeight()-8);

                    break;
                case MODELAGEM:
                    if (!Modelagem.render()) {
                        estado = Estados.MENU;
                    }
                    GL11.glColor3f(1,1,1);
                    SimpleText.drawString("Translacao   teclas a, d, w, s, z, x", 10,INTERFACE.getWindowHeight()-48);
                    SimpleText.drawString("Rotacao      setas do teclado", 10,INTERFACE.getWindowHeight()-28);
                    SimpleText.drawString("ESC          volta para o menu", 10,INTERFACE.getWindowHeight()-8);

                    break;
            }

            glfwSwapBuffers(window); // Desenha o que ta no buffer na tela
            glfwPollEvents(); // Registra os eventos
        }
    }

    private void desenhaMenu(){
        GL11.glColor3f(1,1,1);
        SimpleText.drawString("Menu", INTERFACE.getWindowWidth()/2,INTERFACE.getWindowHeight()/2-25);
        SimpleText.drawString("p. Desenha poligono", INTERFACE.getWindowWidth()/2-72,INTERFACE.getWindowHeight()/2 - 5);
        SimpleText.drawString("m. desenha um solido", INTERFACE.getWindowWidth()/2-72,INTERFACE.getWindowHeight()/2+15);
        SimpleText.drawString("esc. sair", INTERFACE.getWindowWidth()/2-72,INTERFACE.getWindowHeight()/2 +35);
    }

    public static void main(String[] args) {
        new Projeto().run();
    }
}
