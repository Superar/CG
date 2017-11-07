package Projeto;

import Projeto.auxiliares.Cor;

import static java.lang.Math.abs;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2i;

class BresenhamLineDrawer
{
    private Cor cor;

    BresenhamLineDrawer()
    {
        cor = new Cor();
        cor.setCor(1.0f, 1.0f, 1.0f);
    }

    BresenhamLineDrawer(float line_color_R, float line_color_G, float line_color_B)
    {
        cor = new Cor();

        cor.setCor(line_color_R, line_color_G, line_color_B);
    }

    void drawLine(int x0, int y0, int x1, int y1)
    {
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
                drawPoint(secundario, principal, this.cor);
            else
                drawPoint(principal, secundario, this.cor);
            if (d <= 0) {
                d = d + incE;
            } else {
                d = d + incNE;
                secundario = secundario + inc;
            }
        }
    }

    static void drawPoint(int x, int y, Cor cor)
    {
        glPointSize(1);
        glColor3f(cor.red, cor.green, cor.blue);
        glBegin(GL_POINTS);
        glVertex2i(x, y);
        glEnd();
    }
}
