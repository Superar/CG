package Projeto;

import Projeto.auxiliares.GerenciadorInterface;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

class ModelagemCubo {
    // Solido representando o cubo e angulo de rotacao
    private Solido cuboProjetado;
    private float ang;

    // Utilitarios
    private GerenciadorInterface INTERFACE;

    // Flag
    private boolean deveSair;

    ModelagemCubo(GerenciadorInterface i) {
        INTERFACE = i;

        Solido cubo = new Solido(); // TODO: Retirar a distancia focal da criacao do solido

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

        System.out.println(cubo.projetaSolido());

        cuboProjetado = cubo.projetaSolido();

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

//            Solido cuboRotacionado = cubo.rotacionaSolido(ang);

            // TODO: Corrigir desenho da rotacao. Nao esta desenhando correto, mas esta rotacionando.
//            System.out.println(cuboRotacionado);
            for (Poligono p : cuboProjetado.faces) {
                p.projetaPoligono().desenha();
            }
            ang += 0.5f;

            return true;
        }
    }
}
