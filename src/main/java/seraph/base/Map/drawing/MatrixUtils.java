package seraph.base.Map.drawing;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static seraph.base.Naphthav2.mc;

public class MatrixUtils {
    public static FloatBuffer createModelMatrix(float x, float y, float width, float height) {
        int fixedWidth = mc.displayWidth >> 1;
        int fixedHeight = mc.displayHeight >> 1;
        FloatBuffer buf = BufferUtils.createFloatBuffer(16);
        buf.put(width / fixedWidth);
        buf.put(0);
        buf.put(0);
        buf.put(0);
        buf.put(0);
        buf.put(height / fixedHeight);
        buf.put(0);
        buf.put(0);
        buf.put(0);
        buf.put(0);
        buf.put(1);
        buf.put(0);
        buf.put((x / fixedWidth) - 1);
        buf.put(-((y / fixedHeight) - 1));
        buf.put(0);
        buf.put(1);
        buf.flip();

        return buf;
    }

}
