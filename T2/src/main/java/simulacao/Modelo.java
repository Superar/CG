package simulacao;

import com.mokiat.data.front.parser.MTLMaterial;
import org.joml.Vector3f;
import simulacao.utils.ShaderSetup;

import java.util.HashMap;

public class Modelo {

    public static final HashMap<String, MTLMaterial> materiais = new HashMap<>();

    ShaderSetup shaderSetup;

    private final Vector3f posicao;
    public float escala;
    public final Vector3f rotacao;

    private float[] vertices;
    private int[] indices;
    private float[] colours;
    private float[] normals;

    Modelo() {
        normals = new float[]{
                -0.5f, 0.5f, -2.0f,
                -0.5f, -0.5f, -2.0f,
                0.5f, -0.5f, -2.0f,
                0.5f, 0.5f, -2.0f,

                // Face frontal
                -0.5f, 0.5f, -1.5f,
                -0.5f, -0.5f, -1.5f,
                0.5f, -0.5f, -1.5f,
                0.5f, 0.5f, -1.5f,
        };
        vertices = new float[]{
                // Face traseira
                -0.5f, 0.5f, -2.0f,
                -0.5f, -0.5f, -2.0f,
                0.5f, -0.5f, -2.0f,
                0.5f, 0.5f, -2.0f,

                // Face frontal
                -0.5f, 0.5f, -1.5f,
                -0.5f, -0.5f, -1.5f,
                0.5f, -0.5f, -1.5f,
                0.5f, 0.5f, -1.5f,
        };
        indices = new int[]{
                // Face traseira
                0, 1, 3, 3, 1, 2,
                // Face frontal
                4, 5, 7, 7, 5, 6,
        };
        colours = new float[]{
                // Face traseira
                0.5f, 0.0f, 0.0f,
                0.5f, 0.0f, 0.0f,
                0.5f, 0.0f, 0.0f,
                0.5f, 0.0f, 0.0f,

                // Face frontal
                0.0f, 0.5f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.5f, 0.0f,
        };


        shaderSetup = new ShaderSetup(vertices,normals, indices, colours);

        posicao = new Vector3f(0, 0, 0);
        escala = 1.0f;
        rotacao = new Vector3f(0, 0, 0);
    }

    public Modelo(float[] vertices, float[] normals, int[] indices, float[] colours) {
        this.vertices = vertices;
        this.indices = indices;
        this.colours = colours;
        this.normals = normals;

        shaderSetup = new ShaderSetup(this.vertices, this.normals, this.indices, this.colours);

        posicao = new Vector3f(0, 0, 0);
        escala = 1.0f;
        rotacao = new Vector3f(0, 0, 0);
    }

    public void translada(float x, float y, float z) {
        posicao.x = x;
        posicao.y = y;
        posicao.z = z;
    }

    public void rotaciona(float x, float y, float z) {
        rotacao.x = x;
        rotacao.y = y;
        rotacao.z = z;
    }

    public void redimensiona(float escala) {
        this.escala = escala;
    }
}
