package simulacao.utils;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    private final Map<String, Integer> uniforms;

    public ShaderProgram(String vertexShader, String fragmentShader) throws RuntimeException {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new RuntimeException("Falha na criação do shader");
        }
        vertexShaderId = createShader(loadResource(vertexShader), GL_VERTEX_SHADER);
        fragmentShaderId = createShader(loadResource(fragmentShader), GL_FRAGMENT_SHADER);
        link();

        uniforms = new HashMap<>();
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

    private int createShader(String shader, int shaderType) throws RuntimeException {
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

    public void createUniform(String uniformName) throws RuntimeException {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            throw new RuntimeException("uniform não encontrado:" + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
        }
    }

    private void link() throws RuntimeException {
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
}
