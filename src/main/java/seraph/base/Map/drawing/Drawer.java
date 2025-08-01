package seraph.base.Map.drawing;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import seraph.base.Map.Gui.font.FentRenderer;
import seraph.base.Map.Gui.graphicaluserinterfaces.DropdownClickGui;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL20.glUniform3f;
import static seraph.base.Naphthav2.mc;

@Deprecated
public class Drawer {
    static int vao;
    static int vbo;
    static int ebo;

    public static void drawCentredString(FontRenderer fr,String text, double centreX, double centreY,double size, int colour){
        GL11.glPushMatrix();
        GL11.glScaled(size,size,size);
        int offset = (int) (fr.getStringWidth(text) * size /2);
        int var0 = (int) (fr.FONT_HEIGHT * size);
        fr.drawString(
                text,
                (int) (((int) centreX - offset) / size),
                (int) (((int) centreY - var0/2 ) / size),
                colour
        );
        GL11.glPopMatrix();
    }

    public static void drawCentredString(FentRenderer fr, String text, double centreX, double centreY, double size, int colour){
        GL11.glPushMatrix();
        GL11.glScaled(size,size,size);
        int offset = (int) (fr.getStringWidth(text) * size /2);
        int var0 = (int) (fr.getHeight() * size);
        fr.drawCenteredStringWithShadow(
                text,
                (float) (((int) centreX) / size),
                (float) (((int) centreY - var0/2 ) / size),
                colour
        );
        GL11.glPopMatrix();
    }

    public static double getScaledTextWidth(FontRenderer fr, String text, double scale) {
        return fr.getStringWidth(text) * scale;
    }

    public static double getScaledTextWidth(FentRenderer fr, String text, double scale) {
        return fr.getScaledStringWidth(text, (float) scale);
    }

    public static double getScaledTextHight(FontRenderer fr, double scale) {
        return fr.FONT_HEIGHT * scale;
    }

    public static double getScaledTextHight(FentRenderer fr, double scale) {
        return fr.getHeight() * scale;
    }

    public static double getLargestFontWidth(List<String> strings, FontRenderer fr, double scale) {
        double maxWidth = 0;
        for (String str : strings) {
            if (str != null) {
                double width = fr.getStringWidth(str) * scale;
                if (width > maxWidth) {
                    maxWidth = width;
                }
            }
        }
        return maxWidth;
    }

    public static double getLargestFontWidth(List<String> strings, FentRenderer fr, double scale) {
        double maxWidth = 0;
        for (String str : strings) {
            if (str != null) {
                double width = fr.getStringWidth(str) * scale;
                if (width > maxWidth) {
                    maxWidth = width;
                }
            }
        }
        return maxWidth;
    }

    public static void drawString(FontRenderer fr,String text, double x, double centreY,double size, int colour) {
        GL11.glPushMatrix();
        GL11.glScaled(size,size,size);
        int var0 = (int) (fr.FONT_HEIGHT * size);
        fr.drawString(
                text,
                (int) (((int) x) / size),
                (int) (((int) centreY  - var0/2 )/ size),
                colour
        );
        GL11.glPopMatrix();
    }

    public static void drawString(FentRenderer fr,String text, double x, double y,double size, int colour) {
        GL11.glPushMatrix();
        GL11.glScaled(size,size,size);
        int var0 = (int) (fr.getHeight() * size);
        fr.drawString(
                text,
                (((int) x) / size),
                (float) (((int) y  - var0/2 )/ size),
                colour
        );
        GL11.glPopMatrix();
    }

    public static void drawCentredStringShadow(FontRenderer fr,String text, double centreX, double centreY,double size, int colour){
        GL11.glPushMatrix();
        GL11.glScaled(size,size,size);
        fr.drawStringWithShadow(
                text,
                (int) centreX,
                (int) centreY,
                colour
        );
        GL11.glPopMatrix();
    }

    public static boolean drawRectangle2D(double x, double y, double width, double height){
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y + height);
        GL11.glVertex2d(x + width, y + height);
        GL11.glVertex2d(x + width, y);
        GL11.glEnd();
        return DropdownClickGui.isMouseOver(x,y,width,height);
    }

    public static boolean drawShadedRect2D(double x, double y, double width, double height, float[] c1, float[] c2) {
        float scale = (float) new ScaledResolution(mc).getScaleFactor();
        double nx = x * scale;
        double xy = y * scale;
        double nw = width * scale;
        double nh = height * scale;
        Shader shader = Shaders.celNoise;
        shader.use();
        shader.setFloat("iTime", (float)System.currentTimeMillis() / 1000);
        shader.setVec2("res", mc.displayWidth, mc.displayHeight);
        glUniform3f(shader.getUniform("c1"), c1[0], c1[1], c1[2]);
        glUniform3f(shader.getUniform("c2"), c2[0], c2[1], c2[2]);
        nx = (nx / mc.displayWidth) * 2 - 1;
        xy = 1 - (xy / mc.displayHeight) * 2;
        nw = (nw / mc.displayWidth) * 2;
        nh = (nh / mc.displayHeight) * 2;
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glVertex2d(nx, xy - nh);
        GL11.glVertex2d(nx + nw, xy - nh);
        GL11.glVertex2d(nx, xy);
        GL11.glVertex2d(nx + nw, xy);
        GL11.glEnd();

        GL20.glUseProgram(0);
        return DropdownClickGui.isMouseOver(x,y,width,height);
    }

    public static void init() {
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);
        int vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        float[] f = new float[] {
                -1, 1,
                -1, -1,
                1, 1,
                1, -1
        };
        FloatBuffer buffer = BufferUtils.createFloatBuffer(f.length);
        buffer.put(f);
        buffer.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, Float.BYTES * 2, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

    }
}
