package Projeto;

import Projeto.auxiliares.GerenciadorInterface;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

class ModelagemCubo {
    // Solido representando o cubo e angulo de rotacao
    private Solido cubo;
    private float ang;

    // Utilitarios
    private GerenciadorInterface INTERFACE;

    // Flag
    private boolean deveSair;

    ModelagemCubo(GerenciadorInterface i) {
        INTERFACE = i;

        cubo = new Solido(-900); // TODO: Retirar a distancia focal da criacao do solido

        // Face traseira. Z fixo em 100. Verde.
        cubo.setCorFaceAtual(0, 1.0f, 0);
        cubo.adicionaPonto(100, 100, 100);
        cubo.adicionaPonto(100, 250, 100);
        cubo.adicionaPonto(250, 250, 100);
        cubo.adicionaPonto(250, 100, 100);
        cubo.closeFaceAtual();

        // Face inferior. Y fixo em 250. Laranja
        cubo.setCorFaceAtual(1.0f, 0.5f, 0);
        cubo.adicionaPonto(100, 250, 100);
        cubo.adicionaPonto(100, 250, 250);
        cubo.adicionaPonto(250, 250, 250);
        cubo.adicionaPonto(250, 250, 100);
        cubo.closeFaceAtual();

        // Face esquerda. X fixo em 100. Vermelho.
        cubo.setCorFaceAtual(1.0f, 0, 0);
        cubo.adicionaPonto(100, 100, 100);
        cubo.adicionaPonto(100, 250, 100);
        cubo.adicionaPonto(100, 250, 250);
        cubo.adicionaPonto(100, 100, 250);
        cubo.closeFaceAtual();

        // Face direita. X fixo em 250. Magenta.
        cubo.setCorFaceAtual(1.0f, 0, 1.0f);
        cubo.adicionaPonto(250, 100, 100);
        cubo.adicionaPonto(250, 250, 100);
        cubo.adicionaPonto(250, 250, 250);
        cubo.adicionaPonto(250, 100, 250);
        cubo.closeFaceAtual();

        // Face superior. Y fixo em 100. Amarelo
        cubo.setCorFaceAtual(1.0f, 1.0f, 0);
        cubo.adicionaPonto(100, 100, 100);
        cubo.adicionaPonto(100, 100, 250);
        cubo.adicionaPonto(250, 100, 250);
        cubo.adicionaPonto(250, 100, 100);
        cubo.closeFaceAtual();

        // Face frontal. Z fixo em 250. Azul.
        cubo.setCorFaceAtual(0, 0, 1.0f);
        cubo.adicionaPonto(100, 100, 250);
        cubo.adicionaPonto(100, 250, 250);
        cubo.adicionaPonto(250, 250, 250);
        cubo.adicionaPonto(250, 100, 250);
        cubo.closeFaceAtual();

        ang = 1.0f;
        deveSair = false;
    }

    boolean render() {

        if (deveSair) {
            deveSair = false;
            return false;
        } else {
            if (INTERFACE.acao == GerenciadorInterface.Acao.TECLADO_PRESS && INTERFACE.key_pressed == GLFW_KEY_ESCAPE) {
                deveSair = true;
                INTERFACE.limpaAcao();
            }

            cubo.rotacionaSolido(ang); // TODO: Corrigir rotacao do solido
            for (Poligono p : cubo.faces) {
                p.desenha();
            }
            ang += 0.5f;

            return true;
        }
    }
}
