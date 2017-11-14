package Projeto;

import Projeto.auxiliares.GerenciadorInterface;

import static org.lwjgl.glfw.GLFW.*;

class ModelagemCubo {
    // Solido representando o cubo e angulo de rotacao
    private Solido cubo;
    private float angX;
    private float angY;

    // Utilitarios
    private GerenciadorInterface INTERFACE;

    // Flag
    private boolean deveSair;

    ModelagemCubo(GerenciadorInterface i) {
        INTERFACE = i;

        cubo = new Solido();

        int min = INTERFACE.getWindowHeight() / 2 - 50;
        int max = INTERFACE.getWindowHeight() / 2 + 50;

        // Face traseira. Z fixo em min. Verde.
        cubo.setCorFaceAtual(0, 1.0f, 0);
        cubo.adicionaPonto(min, min, min);
        cubo.adicionaPonto(min, max, min);
        cubo.adicionaPonto(max, max, min);
        cubo.adicionaPonto(max, min, min);
        cubo.closeFaceAtual();

        // Face inferior. Y fixo em max. Laranja
        cubo.setCorFaceAtual(1.0f, 0.5f, 0);
        cubo.adicionaPonto(min, max, min);
        cubo.adicionaPonto(min, max, max);
        cubo.adicionaPonto(max, max, max);
        cubo.adicionaPonto(max, max, min);
        cubo.closeFaceAtual();

        // Face esquerda. X fixo em min. Vermelho.
        cubo.setCorFaceAtual(1.0f, 0, 0);
        cubo.adicionaPonto(min, min, min);
        cubo.adicionaPonto(min, max, min);
        cubo.adicionaPonto(min, max, max);
        cubo.adicionaPonto(min, min, max);
        cubo.closeFaceAtual();

        // Face direita. X fixo em max. Magenta.
        cubo.setCorFaceAtual(1.0f, 0, 1.0f);
        cubo.adicionaPonto(max, min, min);
        cubo.adicionaPonto(max, max, min);
        cubo.adicionaPonto(max, max, max);
        cubo.adicionaPonto(max, min, max);
        cubo.closeFaceAtual();

        // Face superior. Y fixo em min. Amarelo
        cubo.setCorFaceAtual(1.0f, 1.0f, 0);
        cubo.adicionaPonto(min, min, min);
        cubo.adicionaPonto(min, min, max);
        cubo.adicionaPonto(max, min, max);
        cubo.adicionaPonto(max, min, min);
        cubo.closeFaceAtual();

        // Face frontal. Z fixo em max. Azul.
        cubo.setCorFaceAtual(0, 0, 1.0f);
        cubo.adicionaPonto(min, min, max);
        cubo.adicionaPonto(min, max, max);
        cubo.adicionaPonto(max, max, max);
        cubo.adicionaPonto(max, min, max);
        cubo.closeFaceAtual();

        angX = 0;
        angY = 0;
        deveSair = false;
    }

    private void verificaTecla() {
        switch (INTERFACE.key_pressed)
        {
            case GLFW_KEY_ESCAPE:
                deveSair = true;
                break;
            case GLFW_KEY_RIGHT:
                angY = angY + 0.25f;
                break;
            case  GLFW_KEY_LEFT:
                angY = angY - 0.25f;
                break;
            case  GLFW_KEY_UP:
                angX = angX + 0.25f;
                break;
            case  GLFW_KEY_DOWN:
                angX = angX - 0.25f;
        }
    }

    boolean render() {
        if (deveSair) {
            deveSair = false;
            return false;
        } else {
            if (INTERFACE.acao == GerenciadorInterface.Acao.TECLADO_PRESS) {
                verificaTecla();
                INTERFACE.limpaAcao();
            }

            // TODO: Corrigir desenho da rotacao. Nao esta desenhando correto, mas esta rotacionando.
            Solido cuboRotacionado = cubo.rotacionaSolido(angX, 1.0f, 0, 0).rotacionaSolido(angY, 0, 1.0f, 0);
            cuboRotacionado.ordenaFaces();
            cuboRotacionado.desenha();
            return true;
        }
    }
}
