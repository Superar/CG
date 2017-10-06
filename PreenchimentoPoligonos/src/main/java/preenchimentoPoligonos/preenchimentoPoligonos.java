package preenchimentoPoligonos;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class preenchimentoPoligonos {
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
            glScalef(2.0f/width, 2.0f/height, 1.0f);
            glTranslatef(-width/2.0f, -height/2.0f, 0.0f);
        });

        // Criacao do registro de pressionamento de teclas
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) ->
        {
            // Esc fecha a tela
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
            // Tecla P fecha o poligono
            if (key == GLFW_KEY_SPACE && !poligono.isClosed()) {
                poligono.close();
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

        bresenhamLineDrawer lineDrawer = new bresenhamLineDrawer(LINE_COLOR_R, LINE_COLOR_G, LINE_COLOR_B);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Limpa o frame buffer

            // Salva posicao atual do mouse para desenhar linha enquanto seleciona proximo ponto
            DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
            glfwGetCursorPos(window, xpos, ypos);


            // Desenha poligono
            poligono.desenha();
            if (!poligono.isClosed()) {
                Ponto ultimoPonto = poligono.getUltimoPonto();
                if (ultimoPonto != null){
                    lineDrawer.drawLine(ultimoPonto.getX(), ultimoPonto.getY(), (int) xpos.get(0), (int) ypos.get(0));
                }
            }

            glfwSwapBuffers(window); // Desenha o que ta no buffer na tela

            glfwPollEvents(); // Registra os eventos
        }
    }

    public static void main(String[] args) {
        new preenchimentoPoligonos().run();
    }
}
