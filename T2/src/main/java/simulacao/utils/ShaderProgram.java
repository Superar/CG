package simulacao.utils;

import com.sun.prism.ps.Shader;

import java.io.InputStream;
import java.util.Scanner;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    public ShaderProgram() throws RuntimeException {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new RuntimeException("Falha na criação do shader");
        }
    }

    private String loadResource(String filename) {
        String result = "";
        try {
            InputStream in = Class.forName(ShaderProgram.class.getName()).getResourceAsStream(filename);
            Scanner scanner = new Scanner(in, "UTF-8");
            result = scanner.useDelimiter("\\A").next();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void createVertexShader(String shader) throws RuntimeException {
        vertexShaderId = createShader(loadResource(shader), GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shader) throws RuntimeException {
        fragmentShaderId = createShader(loadResource(shader), GL_FRAGMENT_SHADER);
    }

    protected int createShader(String shader, int shaderType) throws RuntimeException {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new RuntimeException("Falha na criação do shader: " + shaderType);
        }

        glShaderSource(shaderId, shader);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Erro de compilação do shader: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link() throws RuntimeException {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Falha no link do codigo do shader: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
}
