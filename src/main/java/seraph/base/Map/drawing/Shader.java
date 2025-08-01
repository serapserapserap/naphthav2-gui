package seraph.base.Map.drawing;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL20;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static seraph.base.Naphthav2.mc;

public class Shader {
    private final int program;

    public Shader(String vertexPath, String fragmentPath) {
        String vertexSrc = loadShaderSource(vertexPath);
        String fragmentSrc = loadShaderSource(fragmentPath);
        System.out.println(vertexSrc + "\n" + fragmentSrc);

        int vertexShader = createShader(vertexSrc, GL20.GL_VERTEX_SHADER);
        int fragmentShader = createShader(fragmentSrc, GL20.GL_FRAGMENT_SHADER);

        program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vertexShader);
        GL20.glAttachShader(program, fragmentShader);

        GL20.glLinkProgram(program);
        //checkGLError(program, "Program");

        GL20.glValidateProgram(program);

        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);
    }

    public void use() {
        GL20.glUseProgram(program);
    }

    public int getUniform(String name) {
        return glGetUniformLocation(program, name);
    }

    public void setFloat(String name, float value) {
        glUniform1f(getUniform(name), value);
    }

    public void setVec2(String name, float x, float y) {
        GL20.glUniform2f(getUniform(name), x, y);
    }

    public void setInt(String name, int value) {
        GL20.glUniform1i(getUniform(name), value);
    }

    public void setMat4(String name, FloatBuffer matrix) {
        GL20.glUniformMatrix4(getUniform(name),false, matrix);
    }

    private int createShader(String source, int type) {
        int shader = GL20.glCreateShader(type);
        GL20.glShaderSource(shader, source);
        GL20.glCompileShader(shader);

        checkGLError(shader, type == GL20.GL_VERTEX_SHADER ? "Vertex" : "Fragment");
        return shader;
    }

    private String loadShaderSource(String path) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mc.getResourceManager().getResource(new ResourceLocation("fish", path)).getInputStream(),
                    StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            reader.close();
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load shader: " + path, e);
        }
    }

    private void checkGLError(int shader, String type) {
        int success = GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS);
        if (success == 0) {
            String log = GL20.glGetShaderInfoLog(shader, 4096);
            System.out.println("Failed to compile " + type + " shader:\n" + log);
        }
    }

    public void setUniformf(String name, float... args) {
        int loc = glGetUniformLocation(program, name);
        switch (args.length) {
            case 1:
                glUniform1f(loc, args[0]);
                break;
            case 2:
                glUniform2f(loc, args[0], args[1]);
                break;
            case 3:
                glUniform3f(loc, args[0], args[1], args[2]);
                break;
            case 4:
                glUniform4f(loc, args[0], args[1], args[2], args[3]);
                break;
        }
    }

    public void setUniformi(String name, int... args) {
        int loc = glGetUniformLocation(program, name);
        if (args.length > 1) glUniform2i(loc, args[0], args[1]);
        else glUniform1i(loc, args[0]);
    }

    public void vertex1f(String name, float x) {
        int loc = glGetUniformLocation(program, name);
        glUniform1f(loc, x);
    }

    public void vertex2f(String name, float x, float y) {
        int loc = glGetUniformLocation(program, name);
        glUniform2f(loc, x, y);
    }

    public void vertex3f(String name, float x, float y, float z) {
        int loc = glGetUniformLocation(program, name);
        glUniform3f(loc, x, y, z);
    }

    public void vertex4f(String name, float x, float y, float z, float w) {
        int loc = glGetUniformLocation(program, name);
        glUniform4f(loc, x, y, z, w);
    }

    public void vertex1i(String name, int x) {
        int loc = glGetUniformLocation(program, name);
        glUniform1i(loc, x);
    }

    public void vertex2i(String name, int x, int y) {
        int loc = glGetUniformLocation(program, name);
        glUniform2i(loc, x, y);
    }

    public void vertex3i(String name, int x, int y, int z) {
        int loc = glGetUniformLocation(program, name);
        glUniform3i(loc, x, y, z);
    }

    public void vertex4i(String name, int x, int y, int z, int w) {
        int loc = glGetUniformLocation(program, name);
        glUniform4i(loc, x, y, z, w);
    }

    public void color4f(String name, int color) {
        Color c = new Color(color, true);
        vertex4f(name, c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
    }

    public void color3f(String name, int color) {
        Color c = new Color(color, true);
        vertex3f(name, c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f);
    }

    public void load() {
        glUseProgram(program);
    }

    public void unload() {
        glUseProgram(0);
    }

    public static void drawQuads(float x, float y, float width, float height) {
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(x, y);
        glTexCoord2f(0, 1);
        glVertex2f(x, y + height);
        glTexCoord2f(1, 1);
        glVertex2f(x + width, y + height);
        glTexCoord2f(1, 0);
        glVertex2f(x + width, y);
        glEnd();
    }
}
