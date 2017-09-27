import java.io.IOException;
import java.util.logging.FileHandler;
import static otg.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * COMPUTACAO GRAFICA LWJGL E OPENGL TRADICIONAL PARA DESENHO DE ARCOS DE
 * CIRCUNFERENCIAS CENTRALIZADOS PELO MOUSE, CUJO ANGULO E RAIO PODE SER
 * INCREMENTADO OU DECREMENTADO POR EVENTOS DO TECLADO
 *
 * PROF. MARCELA
 *
 * PARA INSTALAR A BIBLIOTECA VER TUTORIAL EM
 * http://wiki.lwjgl.org/wiki/Setting_Up_LWJGL_with_NetBeans.html
 *
 */
public class CircunArc {
	public static final int DISPLAY_HEIGHT = 640;
       	public static final int DISPLAY_WIDTH = 1280;
       	public static final Logger LOGGER = Logger.getLogger(Main.class.getName());
        public static final int NRO_SEGMENTOS = 100;
       
	private int raio;
	private int posX;
	private int posY;
	private double ang;

	static {
		try {
			LOGGER.addHandler(new FileHandler("errors.log", true));
		} catch (IOException ex) {
			LOGGER.log(Level.WARNING, ex.toString(), ex);
		}
	}

	public static void main(String[] args) {
		CircunArc main = null;
		try {
			main = new CircunArc();
			main.create();
			main.run();
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, ex.toString(), ex);
		} finally {
			if (main != null) {
				main.destroy();
			}
		}
	}

	public CircunArc() {
		raio = 100;
		posX = 400;
		posY = 400;
		ang = 0.5 * Math.PI;
	}

	public void create() throws LWJGLException {
		//Display
		Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
		Display.setFullscreen(false);
		Display.setTitle("Arco de circunferencia");
		Display.create();

		//Keyboard
		Keyboard.create();

		//Mouse
		Mouse.setGrabbed(false);
		Mouse.create();

		//OpenGL
		initGL();
		resizeGL();
	}

	public void destroy() {
		Mouse.destroy();
		Keyboard.destroy();
		Display.destroy();
	}

	public void initGL() {
		//2D Initialization
		glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);
	}

	public void processKeyboard() {
	
	}

	public void processMouse() {
		posX = Mouse.getX();
		posY = Mouse.getY();
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT);
		glLoadIdentity();

		glTranslatef(posX, posY, 0.0f); // APLICO TARNSLACAO E DEPOIS DESENHO COMO SE ESTIVESSE NA ORIGEM
		glColor3f(1.0f, 0.0f, 0.0f);

		drawArc();
	}

	private void drawArc() {
		double x = raio;
		double y = 0;
		double deltaang = ang / (double) NRO_SEGMENTOS;
		and = 0;
		glBegin(GL_POINTS);
		for (int i = 0; i < NRO_SEGMENTOS; i++) {
			glVertex2d(x, y);
			ang += deltaang;
			x = raio * java.lang.Math.cos(ang);
			y = raio * java.lang.Math.sin(ang);
		}
		glEnd();

	}

	public void resizeGL() {
		glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluOrtho2D(0.0f, DISPLAY_WIDTH, 0.0f, DISPLAY_HEIGHT);
		glPushMatrix();
		
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glPushMatrix();
	}

	public void run() {
		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			if (Display.isVisible()) {
				processKeyboard();
				processMouse();
				update();
				render();
			} else {
				if (Display.isDirty()) {
					render();
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
				}
			}
			Display.update();
			Display.sync(60);
		}
	}

	public void update() {
	}
}

public void render() {
	glClear(GL_COLOR_BUFFER_BIT);
	glLoadIdentity();

	glTranslatef(posX, posY, 0.0f); // APLICO TRANSLACAO E DEPOIS DESENHO COMO SE ESTIVESSE NA ORIGEM
	
	glColor3f(1.0f, 0.0f, 0.0f);

	drawArc();
}

private void drawArc() {
	double x = raio;
	double y = 0;
	double deltaang = ang / (double) NRO_SEGMENTOS;
	ang = 0;
	glBegin(GL_POINTS);
	for (int i = 0; i < NRO_SEGMENTOS; i++) {
		glVertex2d(x, y);
		ang += deltaang;
		x = raio * java.lang.Math.cos(ang);
		y = raio * java.lang.Math.sin(ang);
	}
	glEnd();

}
