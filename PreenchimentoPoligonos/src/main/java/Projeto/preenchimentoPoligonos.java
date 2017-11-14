package Projeto;

import Projeto.auxiliares.GerenciadorInterface;
import Projeto.auxiliares.Ponto;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

class preenchimentoPoligonos {
    // Variaveis para desenho e controle dos poligonos
    private ArrayList<Poligono> objetos;
    private Poligono poligono;

    // Utilitarios
    private GerenciadorInterface INTERFACE;
    private static BresenhamLineDrawer lineDrawer;

    // Flag
    private boolean deveSair;

    preenchimentoPoligonos(GerenciadorInterface i) {
        INTERFACE = i;
        lineDrawer = new BresenhamLineDrawer();

        objetos = new ArrayList<Poligono>();
        poligono = new Poligono();

        deveSair = false;
    }

    private void verificaTecla() {
        switch (INTERFACE.key_pressed) {
            case GLFW_KEY_ESCAPE: // Esc volta para o menu
                deveSair = true;
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

    boolean render() {
        double x = INTERFACE.xAtual;
        double y = INTERFACE.yAtual;

        if (deveSair) {
            deveSair = false;
            return false;
        } else {
            if (INTERFACE.acao == GerenciadorInterface.Acao.TECLADO_PRESS) {
                verificaTecla();
                INTERFACE.limpaAcao();
            } else if (INTERFACE.acao == GerenciadorInterface.Acao.MOUSE_CLICK) {
                if (poligono.isClosed()) {
                    poligono = new Poligono();
                }
                poligono.addPonto((int) INTERFACE.xClique, (int) INTERFACE.yClique, 0);

                INTERFACE.limpaAcao();
            }

            for (Poligono p : objetos) {
                p.desenha();
            }

            // Desenha poligono
            poligono.desenha();

            Ponto ultimoPonto = poligono.getUltimoPonto();
            if (ultimoPonto != null) {
                lineDrawer.drawLine(ultimoPonto.getX(), ultimoPonto.getY(), (int) x, (int) y);
            }

            return true;
        }
    }
}
