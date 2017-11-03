package preenchimentoPoligonos;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import preenchimentoPoligonos.auxiliares.Ponto;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class preenchimentoPoligonos {
    private ArrayList<Poligono> objetos;

    private long window;

    private static final int LARGURA = 600;
    private static final int ALTURA = 420;
    private static final String TITULO = "Preenchimento de Polígonos";

    // Cor do fundo
    private static final float BG_COLOR_R = 0.0f;
    private static final float BG_COLOR_G = 0.0f;
    private static final float BG_COLOR_B = 0.0f;

    // Cor da linha
    private static final float LINE_COLOR_R = 1.0f;
    private static final float LINE_COLOR_G = 1.0f;
    private static final float LINE_COLOR_B = 1.0f;

    // Preenchimento de poligono
    private Poligono poligono;

    // Flags de estados
    private boolean focusedWindow = true;

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

    private void key_released(int key) {
        switch (key) {
            case GLFW_KEY_ESCAPE: // Esc fecha a tela
                glfwSetWindowShouldClose(window, true);
                break;
            case GLFW_KEY_SPACE: // Tecla espaço fecha o poligono e cria um novo
                poligono.close();
                objetos.add(poligono);
                poligono = new Poligono();
                break;
            case GLFW_KEY_R:
                poligono.cor.setCor(1, 0, 0);
                break;

            case GLFW_KEY_G:
                poligono.cor.setCor(0, 1, 0);
                break;

            case GLFW_KEY_B:
                poligono.cor.setCor(0, 0, 1);
                break;

            case GLFW_KEY_W:
                poligono.cor.setCor(1, 1, 1);
                break;

            case GLFW_KEY_P:
                poligono.cor.setCor(0, 0, 0);
                break;
        }
    }

    private void init() {

        objetos = new ArrayList<Poligono>();

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

        // Tratamento do redimensionamento da tela
        glfwSetFramebufferSizeCallback(window, (long window, int width, int height) ->
        {
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
        });

        // Criacao do registro de pressionamento de teclas
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) ->
        {
            if (action == GLFW_RELEASE) {
                key_released(key);
            }
        });

        // Registra pontos para desenhar o poligono
        glfwSetMouseButtonCallback(window, (window, button, action, mods) ->
        {
            if (button == GLFW_MOUSE_BUTTON_1) {
                DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
                glfwGetCursorPos(window, xpos, ypos);

                if (action == GLFW_RELEASE) {
                    if (poligono.isClosed()) {
                        poligono = new Poligono();
                    }

                    poligono.addPonto((int) xpos.get(0), (int) ypos.get(0));
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

        // Inicializacao dos parametros
        poligono = new Poligono();
    }

    private void loop() {
        GL.createCapabilities();

        glOrtho(0.0f, LARGURA, ALTURA, 0.0f, 0.0f, 1.0f); // Projecao para usar coordenadas igual em plano cartesiano
        glClearColor(BG_COLOR_R, BG_COLOR_G, BG_COLOR_B, 0.0f); // Qual a cor que ele usa para limpar o framebuffer

        BresenhamLineDrawer lineDrawer = new BresenhamLineDrawer(LINE_COLOR_R, LINE_COLOR_G, LINE_COLOR_B);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
        double x = 1, y = 1;

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Limpa o frame buffer

            // Salva posicao atual do mouse para desenhar linha enquanto seleciona proximo ponto
            glfwGetCursorPos(window, xpos, ypos);
            glfwGetWindowSize(window, width, height);

            // Verifica se mouse esta na tela
            if (xpos.get(0) >= 0 && xpos.get(0) < (double) width.get(0) && ypos.get(0) >= 0 && ypos.get(0) < (double) height.get(0)) {
                if (!focusedWindow)
                    focusedWindow = true;
            } else if (focusedWindow) {
                x = xpos.get(0);
                y = ypos.get(0);
                focusedWindow = false;
            }

            if (!focusedWindow) {
                xpos.put(0, x);
                ypos.put(0, y);
            }

            for (Poligono p : objetos) {
                p.desenha();
            }

            // Desenha poligono
            poligono.desenha();

            Ponto ultimoPonto = poligono.getUltimoPonto();
            if (ultimoPonto != null) {
                lineDrawer.drawLine(ultimoPonto.getX(), ultimoPonto.getY(), (int) xpos.get(0), (int) ypos.get(0));
            }

            glfwSwapBuffers(window); // Desenha o que ta no buffer na tela

            glfwPollEvents(); // Registra os eventos
        }
    }

    public static void main(String[] args) {
        new preenchimentoPoligonos().run();
    }
}
