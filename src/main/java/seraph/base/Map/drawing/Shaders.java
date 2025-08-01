package seraph.base.Map.drawing;

import net.minecraft.client.renderer.GlStateManager;
import seraph.base.Map.Gui.modules.ModuleManager;
import seraph.base.Map.Gui.theme.Theme;
import seraph.base.Map.Gui.theme.ThemeManager;

import static org.lwjgl.opengl.GL20.glUseProgram;
import static seraph.base.Naphthav2.mc;

public class Shaders {
    public static Shader celNoise;
    public static Shader roundedGlowRect;
    public static Shader weakerGlowRect;
    public static Shader fontShader;
    public static Shader gradientShader;

    public static void init() {
        //celNoise = new Shader("shader/cell/vertex.vsh","shader/cell/fragment.fsh");
        roundedGlowRect = new Shader("shader/vertex.vsh","shader/roundedglow.fsh");
        weakerGlowRect = new Shader("shader/vertex.vsh","shader/altroundedglow.fsh");
        fontShader = new Shader("shader/vertex.vsh", "shader/fontgradient.fsh");
        gradientShader = new Shader("shader/vertex.vsh", "shader/fuck.fsh");
        //Drawer.init();
    }

    public static void glowRect(float x, float y, float w, float h, int mainColour, int glowColour, float glowSz, float radius) {
        Shaders.roundedGlowRect.use();
        Shaders.roundedGlowRect.setUniformf("radius", radius);
        Shaders.roundedGlowRect.color4f("fillColor", mainColour);
        Shaders.roundedGlowRect.color4f("glowColor", glowColour);
        Shaders.roundedGlowRect.setUniformf("glowSize",glowSz);
        Shaders.roundedGlowRect.setUniformf("resolution", (float) mc.displayWidth, (float) mc.displayHeight);
        Shader.drawQuads(x, y, w, h);
        glUseProgram(0);
    }

    public static void setupChromaTexture() {
        Theme t = ThemeManager.theme();
        GlStateManager.color(1,1,1,1);
        Shaders.fontShader.use();
        float[] var0 = t.asFloatArr(t.colourMain);
        Shaders.fontShader.setUniformf("c1", var0[0], var0[1], var0[2]);
        var0 = t.asFloatArr(t.colourSecond);
        Shaders.fontShader.setUniformf("c2", var0[0], var0[1], var0[2]);
        float time = System.nanoTime() / 1_000_000_000f;
        Shaders.fontShader.setUniformf("time", time);
        Shaders.fontShader.setUniformf("resolution", mc.displayWidth, mc.displayHeight);
        Shaders.fontShader.setUniformf("speed",(float) (double) ModuleManager.clickGuiModule.colourWaveSpeed.getValue());
        Shaders.fontShader.setUniformf("frequency",(float) (double) ModuleManager.clickGuiModule.colourWaveFrequency.getValue());
    }

    public static void setupChroma() {
        Theme t = ThemeManager.theme();
        GlStateManager.color(1,1,1,1);
        Shaders.gradientShader.use();
        float[] var0 = t.asFloatArr(t.colourMain);
        Shaders.gradientShader.setUniformf("c1", var0[0], var0[1], var0[2]);
        var0 = t.asFloatArr(t.colourSecond);
        Shaders.gradientShader.setUniformf("c2", var0[0], var0[1], var0[2]);
        float time = System.nanoTime() / 1_000_000_000f;
        Shaders.gradientShader.setUniformf("time", time);
        Shaders.gradientShader.setUniformf("resolution", mc.displayWidth, mc.displayHeight);
        Shaders.gradientShader.setUniformf("speed",(float) (double) ModuleManager.clickGuiModule.colourWaveSpeed.getValue());
        Shaders.gradientShader.setUniformf("frequency",(float) (double) ModuleManager.clickGuiModule.colourWaveFrequency.getValue());
    }
}
