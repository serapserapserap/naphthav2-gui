package seraph.base.Map.Gui.graphicaluserinterfaces;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import seraph.base.Map.Event.SetupGuiScaleEvent;
import seraph.base.Map.Gui.*;
import seraph.base.Map.Gui.font.FentRenderer;
import seraph.base.Map.Gui.modules.Module;
import seraph.base.Map.Gui.modules.ModuleManager;
import seraph.base.Map.Gui.modules.NonToggleModule;
import seraph.base.Map.Gui.settings.Changeable;
import seraph.base.Map.Gui.settings.ExtraFeatureSetting;
import seraph.base.Map.Gui.settings.Setting;
import seraph.base.Map.Gui.settings.impl.Button;
import seraph.base.Map.Gui.settings.impl.*;
import seraph.base.Map.Gui.theme.Theme;
import seraph.base.Map.Gui.theme.ThemeManager;
import seraph.base.Map.drawing.Drawer;
import seraph.base.Map.drawing.Shader;
import seraph.base.Map.drawing.Shaders;
import seraph.base.Map.math.EasingFunction;
import seraph.base.Map.math.easingfunctions.back.EaseOutBack;
import seraph.base.Map.math.easingfunctions.cubic.EaseInOutCubic;
import seraph.base.Map.math.easingfunctions.cubic.EaseOutCubic;
import seraph.base.Map.math.easingfunctions.quint.EaseOutQuint;
import seraph.base.Map.math.easingfunctions.sin.EaseInOutSine;
import seraph.base.Map.mc.skyblock.dungeonMap.scan.Pair;
import seraph.base.Naphthav2;
import seraph.base.features.impl.ArrayListModule;

import java.awt.*;
import java.util.List;
import java.util.*;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static seraph.base.Map.Gui.settings.ExtraFeatureSetting.flatten;
import static seraph.base.Map.Gui.settings.ExtraFeatureSetting.flattenAll;
import static seraph.base.Map.StringHelper.replace;
import static seraph.base.Naphthav2.*;

public class PanelClickGui implements GraphicalUserInterface {
    private final int PANEL_WIDTH = 800;
    private final int PANEL_HEIGHT = 500;
    private static Category selected = Category.values()[0];
    private static final float scale = 1;
    private static final ScrollManager categorySM = new ScrollManager(0);
    private static final ScrollManager moduleSM = new ScrollManager(0);
    private static final ScrollManager settingSM = new ScrollManager(0);
    public final Map<Object, Animation> animationMap = new HashMap<>();
    private final Map<Text, TextBoxManager<?>> textTextBoxManagerMap = new HashMap<>();
    private static final List<GuiElementWrapper>  droppedDown = new ArrayList<>(Arrays.asList(Category.values()));
    private static final EasingFunction func = new EaseOutBack();
    private static NonToggleModule opened = ModuleManager.clickGuiModule;
    private GuiElementWrapper hovered = null;
    private long animationStartAt = 0;
    private RightCLickMenu<?> currentMenu = null;


    public void pushAnimMap(NonToggleModule m) {
        animationMap.put(m, new Animation(500).flip(Animation.AnimationDirection.REVERSE));
        if(m instanceof Module)
            animationMap.put(m.getName(), new Animation(500).flip(((Module)m).isToggled() ? Animation.AnimationDirection.FORWARD : Animation.AnimationDirection.REVERSE).reset(((Module)m).isToggled() ? 1 : 0));
        m.getSettings().forEach(this::pushAnimMapSetting);
    }

    public void pushAnimMap(Setting<?> s) {
        if (s instanceof Button) {
            Animation a = new Animation(100).pause();
            a.onEnd(() -> {
                if (a.getProgress() == 0) {
                    a.reset(0.00001f).pause();
                }
            });
            animationMap.put(s, a.loop());
        }
        else if(s instanceof Toggle) {
            animationMap.put(s, new Animation(250).flip(((Toggle) s).isToggled() ? Animation.AnimationDirection.FORWARD : Animation.AnimationDirection.REVERSE).reset(((Toggle) s).isToggled() ? 1 : 0));
        }
        else if(s instanceof DropDown) {
            boolean b = droppedDown.contains(s);
            animationMap.put(s, new Animation(150).flip(b ? Animation.AnimationDirection.FORWARD : Animation.AnimationDirection.REVERSE).reset(b ? 1 : 0));
        }
        else if(s instanceof Slider) {
            animationMap.put(s, new Animation(150).flip(Animation.AnimationDirection.REVERSE));
        }
        else if(s instanceof Text) {
            this.textTextBoxManagerMap.put((Text) s, new TextBoxManager<>(((Text) s).getValue(), (Text) s));
            animationMap.put(s, new Animation(500));
        }
        else {
            animationMap.put(s, new Animation(500));
        }
    }

    public void pushAnimMapSetting(Setting<?> source) {
        if(source instanceof ExtraFeatureSetting<?, ?>) {
            for(Pair<Setting<?>, Integer> pair : flattenAll((ExtraFeatureSetting<?, ?>) source)) {
                this.pushAnimMap(pair.getKey());
            }
        }
        this.pushAnimMap(source);
    }

    /*
            INSTANCE.moduleManager.getAllModules().forEach(m -> {
            animationMap.put(m, new Animation(500).flip(Animation.AnimationDirection.REVERSE));
            if(m instanceof Module)
                animationMap.put(m.getName(), new Animation(500).flip(((Module)m).isToggled() ? Animation.AnimationDirection.FORWARD : Animation.AnimationDirection.REVERSE).reset(((Module)m).isToggled() ? 1 : 0));
            m.getSettings().forEach(s -> {
                if (s instanceof Button) {
                    Animation a = new Animation(100).pause();
                    a.onEnd(() -> {
                        if (a.getProgress() == 0) {
                            a.reset(0.00001f).pause();
                        }
                    });
                    animationMap.put(s, a.loop());
                }
                else if(s instanceof Toggle) {
                    animationMap.put(s, new Animation(250).flip(((Toggle) s).isToggled() ? Animation.AnimationDirection.FORWARD : Animation.AnimationDirection.REVERSE).reset(((Toggle) s).isToggled() ? 1 : 0));
                }
                else if(s instanceof DropDown) {
                    boolean b = droppedDown.contains(s);
                    animationMap.put(s, new Animation(150).flip(b ? Animation.AnimationDirection.FORWARD : Animation.AnimationDirection.REVERSE).reset(b ? 1 : 0));
                }
                else if(s instanceof Slider) {
                    animationMap.put(s, new Animation(150).flip(Animation.AnimationDirection.REVERSE));
                }
                else {
                    animationMap.put(s, new Animation(500));
                }
            });
        });
     */

    @Override
    public void onOpen() {
        animationStartAt = System.currentTimeMillis();
        animationMap.put(0, new Animation(2000).loop()); // less memory I think
        INSTANCE.moduleManager.getAllModules().forEach(this::pushAnimMap);
        for(Category c : Category.values()) {
            animationMap.put(c, new Animation(500).flip(Animation.AnimationDirection.REVERSE));
            for(SubCategory sc : c.subCategories)
                animationMap.put(new Category.CategoryPair(c, sc), new Animation(500).flip(Animation.AnimationDirection.REVERSE));
        }
    }

    @Override
    public void onClose() {
        //ModuleManager.clickGuiModule.toggle(false);
    }

    private static void drawResize(float x, float y, float w, float h) {
        glPushMatrix();
        GL11.glColor3d(.25f,.25f,.25f);
        glBegin(GL_LINES);
        glVertex2f(x, y + h);
        glVertex2f(x + w, y);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x + w * 0.4, y + h);
        glVertex2d(x + w, y + h * 0.4);
        glEnd();
        glBegin(GL_LINES);
        glVertex2d(x + w * 0.8, y + h);
        glVertex2d(x + w, y + h * 0.8);
        glEnd();
        glPopMatrix();
    }

    private static boolean isMouseOver2(float x, float y, float w, float h) {
        ScaledResolution sr = new ScaledResolution(mc);
        float scaleFactor = 2 * ((float) 1 / sr.getScaleFactor());
        // Get raw mouse position (already scaled by Minecraft to GUI space)
        int mouseX = Mouse.getX() * sr.getScaledWidth() / mc.displayWidth;
        int mouseY = sr.getScaledHeight() - (Mouse.getY() * sr.getScaledHeight() / mc.displayHeight) - 1;

        // Convert to scaled GUI space
        double scaledMouseX = (mouseX - sr.getScaledWidth() / 2.0) / scaleFactor + sr.getScaledWidth() / 2.0;
        double scaledMouseY = (mouseY - sr.getScaledHeight() / 2.0) / scaleFactor + sr.getScaledHeight() / 2.0;
        return (scaledMouseX >= x && scaledMouseX <= x + w && scaledMouseY >= y && scaledMouseY <= y + h);
    }

    public static void scale(double number) {
        ScaledResolution sr = new ScaledResolution(mc);
        glTranslated(((double) sr.getScaledWidth() / 2) * (1 - number), ((double) sr.getScaledHeight() / 2) * (1 - number), 0);
        glScaled(number,number,number);
    }

    public static void drawPanel(float x, float y, float w, float h, int mainColour, int glowColour, float glowSz, float radius) {
        Shaders.roundedGlowRect.use();
        Shaders.roundedGlowRect.setUniformf("radius", radius);
        Shaders.roundedGlowRect.color4f("fillColor", mainColour);
        Shaders.roundedGlowRect.color4f("glowColor", glowColour);
        Shaders.roundedGlowRect.setUniformf("glowSize",glowSz);
        Shaders.roundedGlowRect.setUniformf("resolution", w * 2, h * 2);
        Shader.drawQuads(x, y, w, h);

        glUseProgram(0);
    }

    public static void drawPanelNoInset(float x, float y, float w, float h, int mainColour, int glowColour, float glowSz, float radius) {
        float minDim = Math.min(w * 2, h * 2);

        float insetX = glowSz * ((w * 2) / minDim);
        float insetY = glowSz * ((h * 2) / minDim);

        float drawW = w + insetX * 2;
        float drawH = h + insetY * 2;

        Shaders.roundedGlowRect.use();
        Shaders.roundedGlowRect.setUniformf("radius", radius);
        Shaders.roundedGlowRect.color4f("fillColor", mainColour);
        Shaders.roundedGlowRect.color4f("glowColor", glowColour);
        Shaders.roundedGlowRect.setUniformf("glowSize", glowSz);
        Shaders.roundedGlowRect.setUniformf("resolution", drawW, drawH);
        Shader.drawQuads(x - insetX, y - insetY, drawW, drawH);

        glUseProgram(0);
    }


    public static void drawMainPanel(float x, float y, float w, float h, int mainColour, int glowColour, float glowSz, float radius) {
        Shaders.weakerGlowRect.use();
        Shaders.weakerGlowRect.setUniformf("radius", radius);
        Shaders.weakerGlowRect.color4f("fillColor", mainColour);
        Shaders.weakerGlowRect.color4f("glowColor", glowColour);
        Shaders.weakerGlowRect.setUniformf("glowSize",glowSz);
        Shaders.weakerGlowRect.setUniformf("resolution", w * 2, h * 2);
        Shader.drawQuads(x, y, w, h);

        glUseProgram(0);
    }
    //

    public static ScissorBox setupScissorBox(float x, float y, float width, float height) {
        return new ScissorBox(
                (int) (x * 2),
                mc.displayHeight - (int) ((y + height) * 2),
                (int) (width * 2),
                (int) (height * 2)
        ).glScissor();
    }

    private static Vector3f drawDropDownPanel(String[] allOptions, int selected, float rightX, float y, float lerp, ScissorBox sb, float s0, ScissorBox mainScissor) {
        float minTextInset = (float) jetBrainsMonoExtraSmall.getStringWidth(allOptions[selected]) + 2;
        glDisable(GL_TEXTURE_2D);
        if(lerp == 0) {
            drawPanelNoInset(
                    rightX - .5f,
                    y - 2.5f,
                    (float) jetBrainsMonoExtraSmall.getStringWidth(allOptions[selected]) + 1,
                    jetBrainsMonoExtraSmall.getHeight() + 4,
                    new Color(84, 84, 84,255).getRGB(),
                    new Color(84, 84, 84,255).getRGB(),
                    1,
                    1f
            );
            glEnable(GL_TEXTURE_2D);
            jetBrainsMonoExtraSmall.drawString(
                    allOptions[selected],
                    rightX,
                    y,
                    0xffffffff
            );
            return new Vector3f(
                    minTextInset,
                    0F,
                    -1F
            );
        } else {
            float maxInset = minTextInset;
            for(String s : allOptions)
                maxInset = Math.max(maxInset, (float) jetBrainsMonoExtraSmall.getStringWidth(s) + 2);
            float height = (2.5f + jetBrainsMonoExtraSmall.getHeight()) * (allOptions.length) + 2f;
            drawPanelNoInset(
                    (float) (rightX - .5f - (((maxInset - 1) - (jetBrainsMonoExtraSmall.getStringWidth(allOptions[selected]) + 1)) * lerp)),
                    y - 2.5f,
                    (float) ((jetBrainsMonoExtraSmall.getStringWidth(allOptions[selected]) + 1) + ((maxInset - 1) - (jetBrainsMonoExtraSmall.getStringWidth(allOptions[selected]) + 1)) * lerp),
                    jetBrainsMonoExtraSmall.getHeight() + 4 + (height - (jetBrainsMonoExtraSmall.getHeight() + 4)) * lerp,
                    new Color(84, 84, 84,255).getRGB(),
                    new Color(84, 84, 84,255).getRGB(),
                    1,
                    1f
            );
            setupScissor(
                    (float) (sb.x + (1 - (((maxInset - 1) - (jetBrainsMonoExtraSmall.getStringWidth(allOptions[selected]) + 1)) * lerp))),
                    sb.y + settingSM.getScroll(),
                    (float) ((jetBrainsMonoExtraSmall.getStringWidth(allOptions[selected]) + 1) + ((maxInset - 1) - (jetBrainsMonoExtraSmall.getStringWidth(allOptions[selected]) + 1)) * lerp),
                    jetBrainsMonoExtraSmall.getHeight() + 4 + (height - (jetBrainsMonoExtraSmall.getHeight() + 4)) * lerp,
                    s0
            ).intersection(mainScissor).glScissor();

            glEnable(GL_TEXTURE_2D);
            int hoveredIndex = -1;
            for(int i = 0 ; i < allOptions.length ; i++) {
                if(i == selected) {
                    if(
                            isMouseOver(
                                    (float) (sb.x + (1 - (((maxInset - 1) - (jetBrainsMonoExtraSmall.getStringWidth(allOptions[selected]) + 1)) * lerp))),
                                    sb.y + settingSM.getScroll(),
                                    (float) ((jetBrainsMonoExtraSmall.getStringWidth(allOptions[selected]) + 1) + ((maxInset - 1) - (jetBrainsMonoExtraSmall.getStringWidth(allOptions[selected]) + 1)) * lerp),
                                    (jetBrainsMonoExtraSmall.getHeight() << 1)
                            )
                    ) {
                        hoveredIndex = i;
                    }
                    jetBrainsMonoExtraSmall.drawString(
                            allOptions[i],
                            rightX,
                            y,
                            hoveredIndex != i ? 0xffffffff : new Color(255,200,200).getRGB()
                    );
                    continue;
                }
                int offsetIndex = i > selected ? i : i + 1;
                if(
                        isMouseOver(
                                (float) (sb.x + (1 - (((maxInset - 1) - (jetBrainsMonoExtraSmall.getStringWidth(allOptions[selected]) + 1)) * lerp))),
                                sb.y + (offsetIndex) * (jetBrainsMonoExtraSmall.getHeight() + 2.5f) + settingSM.getScroll(),
                                (float) ((jetBrainsMonoExtraSmall.getStringWidth(allOptions[selected]) + 1) + ((maxInset - 1) - (jetBrainsMonoExtraSmall.getStringWidth(allOptions[selected]) + 1)) * lerp),
                                (jetBrainsMonoExtraSmall.getHeight() << 1)
                        )
                ) {
                    hoveredIndex = i;
                }
                jetBrainsMonoExtraSmall.drawString(
                        allOptions[i],
                        rightX + ((jetBrainsMonoExtraSmall.getStringWidth(allOptions[selected]) - jetBrainsMonoExtraSmall.getStringWidth(allOptions[i]))),
                        y + (offsetIndex) * (jetBrainsMonoExtraSmall.getHeight() + 2.5f),
                        hoveredIndex != i ? 0xffffffff : new Color(255,200,200).getRGB()
                );
            }
            return new Vector3f(
                    minTextInset + (maxInset - minTextInset) * lerp,
                    Math.max(0,(((height + 6) * lerp) - jetBrainsMonoExtraSmall.getHeight() - 10)),
                    hoveredIndex
            );
        }
    }

    @SubscribeEvent
    public void onInitGui(SetupGuiScaleEvent e) {
        //e.width = MathHelper.ceiling_double_int((double)mc.displayWidth / (double)2);
        //e.height = MathHelper.ceiling_double_int((double)mc.displayHeight / (double)2);
        //e.setCanceled(true);
    }

    public static void drawToggleSlider(float x, float y, float animProgress, Color backgroundColour, float[] offColour, float[] onColour, EasingFunction func) {
        double d0 = func.apply(animProgress);
        double[] mixed = {
                offColour[0] + (onColour[0] - offColour[0]) * d0,
                offColour[1] + (onColour[1] - offColour[1]) * d0,
                offColour[2] + (onColour[2] - offColour[2]) * d0
        };
        int c = new Color((float) mixed[0], (float) mixed[1], (float) mixed[2]).getRGB();
        drawPanel(
                x,
                y,
                20,
                10,
                backgroundColour.getRGB(),
                c,
                2,
                8
        );
        //float xMaxOffset = 10.75f;
        //float xMinOffset = 2.75f;
        float position = (float) (2.75f + (8f) * d0);
        drawPanel(
                x + position,
                y + 1.75f,
                6.5f,6.5f,
                c,
                c,
                1.f,
                5.5f
        );
    }

    public static int setAlpha(int color, int alpha) {
        alpha = Math.max(0, Math.min(255, alpha));
        return (color & 0x00FFFFFF) | (alpha << 24);
    }

    public static Color mix(final Color colourOff, final Color colourOn, final double d0) {
        int r = (int) (colourOff.getRed() + (colourOn.getRed() - colourOff.getRed()) * d0);
        int g = (int) (colourOff.getGreen() + (colourOn.getGreen() - colourOff.getGreen()) * d0);
        int b = (int) (colourOff.getBlue() + (colourOn.getBlue() - colourOff.getBlue()) * d0);
        int a = (int) (colourOff.getAlpha() + (colourOn.getAlpha() - colourOff.getAlpha()) * d0);
        return new Color(r, g, b, a);
    }

    public static int mixColors(int colorOff, int colorOn, double t) {
        int a1 = (colorOff >> 24) & 0xFF;
        int r1 = (colorOff >> 16) & 0xFF;
        int g1 = (colorOff >> 8)  & 0xFF;
        int b1 =  colorOff        & 0xFF;

        int a2 = (colorOn >> 24) & 0xFF;
        int r2 = (colorOn >> 16) & 0xFF;
        int g2 = (colorOn >> 8)  & 0xFF;
        int b2 =  colorOn        & 0xFF;

        int a = (int)(a1 + (a2 - a1) * t);
        int r = (int)(r1 + (r2 - r1) * t);
        int g = (int)(g1 + (g2 - g1) * t);
        int b = (int)(b1 + (b2 - b1) * t);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static Color invert(final Color og) {
        return new Color(
                255 - og.getRed(),
                255 - og.getGreen(),
                255 - og.getBlue(),
                og.getAlpha()
        );
    }

    public static int mixi(final Color colourOff, final Color colorOn, final double d0) {
        return mix(colourOff,colorOn,d0).getRGB();
    }

    public static void drawButtonAnimated(float x, float y, float animProgress, Color background, Color border, EasingFunction func) {
        double d0 = func.apply(Math.max(0,Math.min(1,animProgress)));
        Color var0 = border.darker();
        int r = (int) (background.getRed() + (var0.getRed() - background.getRed()) * d0);
        int g = (int) (background.getGreen() + (var0.getGreen() - background.getGreen()) * d0);
        int b = (int) (background.getBlue() + (var0.getBlue() - background.getBlue()) * d0);
        int a = (int) (background.getAlpha() + (var0.getAlpha() - background.getAlpha()) * d0);

        Color bg = new Color(r, g, b, a);
        drawPanel(
                x,
                y,
                10.5f,
                10.5f,
                bg.getRGB(),
                border.getRGB(),
                2,
                2

        );
    }

    private static ScissorBox setupScissor(float x, float y, float w, float h, double interpolation) {
        return setupScissorBox(
                (float) (x * interpolation + ((mc.displayWidth / 2f) / 2f) * (1 - interpolation)),
                (float) (y * interpolation + ((mc.displayHeight / 2f) / 2f) * (1 - interpolation)),
                (float) (w * interpolation),
                (float) (h * interpolation)
        );
    }

    private void drawButton(float x, float y, float animProgress) {
        drawButtonAnimated(
                x,
                y,
                animProgress,
                new Color(38,38,38,255),
                new Color(255,70,70,255),
                new EaseOutCubic()
        );
    }

    private void drawToggle(float x, float y, float animProgress) {
        drawToggleSlider(
                x,
                y,
                animProgress,
                new Color(38,38,38,255),
                new float[] {0.3294117647F, 0.3294117647F, 0.3294117647F},
                new float[] {1.F, 0.2745098039F, 0.2745098039F},
                new EaseInOutCubic()
        );
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        GuiElementWrapper lastHovered = this.hovered;
        GuiElementWrapper forceHovered = null;
        Theme theme = ThemeManager.theme();
        this.hovered = null;
        ScaledResolution sr = new ScaledResolution(mc);
        float panelWidth = (float) PANEL_WIDTH / 2;
        float panelHeight = (float) PANEL_HEIGHT / 2;
        float anchorX = (((float) sr.getScaledWidth() / 2) - panelWidth / 2);
        float anchorY = (((float) sr.getScaledHeight() / 2) - panelHeight / 2);
        double minX = anchorX + 48.5;
        double width = 303;
        double height = 165;
        double categoryListWidth = 70;
        glPushMatrix();
        double s0 = Math.max(Math.min((double) (System.currentTimeMillis() - animationStartAt) / 500, 1), 0);
        double s1 = func.apply(s0);
        scale(s1);
        scale(2);
        scale((double) 1 /sr.getScaleFactor());
        GL11.glDisable(GL_TEXTURE_2D);
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        drawMainPanel(
                anchorX,
                anchorY,
                panelWidth,
                panelHeight,
                theme.colourPanelBackground,
                theme.colourMain,
                60,
                16
        );
        //GL11.glColor3d(.25f,.25f,.25f);
        theme.colour(theme.colourPanelBackgroundSecondary);
        Drawer.drawRectangle2D(minX,anchorY + 55, panelWidth - 97, 1.5);
        SubCategory last = null;
        float offset = 2.5f;
        float moduleHeight = 30;
        float w = 140;
        float trueWidth = 122.5F;
        glColor3f(1,1,1);
        glEnable(GL_SCISSOR_TEST);

        ScissorBox mainScissor = setupScissor(
                mc.displayWidth / 2f / 2f - panelWidth / 2f + 48.5f,
                mc.displayHeight / 2f / 2f - panelHeight / 2f + 58f,
                (float) width,
                (float) (height - 4),
                s1
        );
        glPushMatrix();
        glTranslated(0,moduleSM.getScroll(),0);
        for(NonToggleModule m : Naphthav2.INSTANCE.moduleManager.getAllModules()) {
            if(!m.getCategory().equals(selected)) continue;
            if(m.getSubCategory() != null && !m.getSubCategory().equals(last)) {
                theme.colour(theme.colourElementBackground);
                Drawer.drawRectangle2D(
                        minX + categoryListWidth + 2.5f,
                        (float) ((anchorY + 56.5) + offset + (double) jetBrainsMonoExtraSmall.getHeight() / 2 - 1.5f),
                        (trueWidth / 2 - Naphthav2.jetBrainsMonoExtraSmall.getStringWidth(m.getSubCategory().name) / 2) - 5,
                        1
                );

                Drawer.drawRectangle2D(
                        minX + categoryListWidth + 2.5f + (trueWidth / 2 + Naphthav2.jetBrainsMonoExtraSmall.getStringWidth(m.getSubCategory().name) / 2),
                        (float) ((anchorY + 56.5) + offset + (double) jetBrainsMonoExtraSmall.getHeight() / 2 - 2),
                        (trueWidth / 2 - Naphthav2.jetBrainsMonoExtraSmall.getStringWidth(m.getSubCategory().name) / 2) - 5,
                        1
                );
                glEnable(GL_TEXTURE_2D);
                Naphthav2.jetBrainsMonoExtraSmall.drawString(
                        m.getSubCategory().name,
                        (minX + categoryListWidth + (trueWidth / 2 - Naphthav2.jetBrainsMonoExtraSmall.getStringWidth(m.getSubCategory().name) / 2)),
                        (float) ((anchorY + 56.5) + offset),
                        theme.colourText
                );
                offset+= Naphthav2.jetBrainsMonoExtraSmall.getHeight();
                glDisable(GL_TEXTURE_2D);
            }
            boolean toggled = (m instanceof Module && ((Module) m).isToggled());
            int colour = mixColors(
                    theme.colourElementBackground,
                    theme.colourElementBackgroundHovered,
                    animationMap.get(m).getProgress()
            );
            drawPanel(
                    (float) (minX + categoryListWidth) - 9.5f,
                    (float) (anchorY + 56.5) + offset,
                    w,
                    moduleHeight,
                    colour,
                    toggled ? theme.colourMain : colour,
                    4,
                    8
            );
            if(
                    isMouseOver(
                            (((float) (mc.displayWidth / 2) / 2) - panelWidth / 2) + categoryListWidth - .5f + 48.5f,
                            (((float) (mc.displayHeight / 2) / 2) - panelHeight / 2) + 56.6f +offset,
                            trueWidth,
                            moduleHeight
                    )
            ) {
                this.hovered = m;
                animationMap.get(m).flip(Animation.AnimationDirection.FORWARD);
            } else
                animationMap.get(m).flip(Animation.AnimationDirection.REVERSE);
            GL11.glEnable(GL_TEXTURE_2D);
            Drawer.drawString(Naphthav2.jetBrainsMono.getValue(),
                    m.getName(),
                     (minX + categoryListWidth) + 2.5f,
                    (anchorY + 56.5) + offset + 16,
                    1,
                    toggled ? theme.colourMain : theme.colourText
            );
            GL11.glDisable(GL_TEXTURE_2D);
            offset += moduleHeight + 1;
            last = m.getSubCategory();
        }
        glPopMatrix();
        moduleSM.maxScroll = offset - (float) (height) + 2.5f;
        GL11.glEnable(GL_TEXTURE_2D);
        offset = 2.5f ;
        glPushMatrix();
        glTranslated(0,categorySM.getScroll(), 0);
        for(Category c : Category.values()) {
            GlStateManager.color(1,1,1,1);
            boolean isSelected = selected.equals(c);
            boolean reset = true;
            Naphthav2.jetBrainsMono.getValue().drawString(
                    c.name,
                    minX + 5.5,
                    (float) (anchorY + 56.5) + offset,
                    isSelected ? theme.colourMain : theme.colourText
            );
            float var1 = (((float) (mc.displayWidth / 2) / 2) - panelWidth / 2) + 48.5f + 5.5f;
            float var2 = (float) ((((float) (mc.displayHeight / 2) / 2) - panelHeight / 2) + 56.5);
            if(
                    isMouseOver(
                            var1,
                            var2 + offset + categorySM.getScroll(),
                            Naphthav2.jetBrainsMono.getValue().getStringWidth(c.name),
                            Naphthav2.jetBrainsMono.getValue().getHeight()
                    )
            ) {
                this.hovered = c;
                reset = false;
            }
            Naphthav2.jetBrainsMono.getValue().drawString(
                c.name,
                minX + 5.5,
                (float) (anchorY + 56.5) + offset,
                setAlpha(theme.colourMain, (int)((200 * new EaseOutQuint().apply(animationMap.get(c).getProgress()))))
            );
            animationMap.get(c).flip(reset ? Animation.AnimationDirection.REVERSE : Animation.AnimationDirection.FORWARD);
            offset += Naphthav2.jetBrainsMono.getValue().getHeight() + 1.75f;
            for(SubCategory sc : c.subCategories) {
                boolean reverse = true;
                Category.CategoryPair var3 = new Category.CategoryPair(c,sc);
                String var0 = replace("§c- §7{0}", sc.name);
                Naphthav2.jetBrainsMono.getValue().drawString(
                        var0,
                        minX + 5.5,
                        (float) (anchorY + 56.5) + offset,
                        theme.colourText
                );
                if(
                        isMouseOver(
                                var1,
                                var2 + offset + categorySM.getScroll(),
                                jetBrainsMono.getValue().getStringWidth(var0),
                                jetBrainsMono.getValue().getHeight()
                        )
                ) {
                    hovered = var3;
                    reverse = false;
                }

                Naphthav2.jetBrainsMono.getValue().drawString(
                        var0.replace("§c- §7", "- "),
                        minX + 5.5,
                        (float) (anchorY + 56.5) + offset,
                        setAlpha(theme.colourMain, (int)((200 * new EaseOutQuint().apply(animationMap.get(var3).getProgress()))))
                );
                animationMap.get(var3).flip(reverse ? Animation.AnimationDirection.REVERSE : Animation.AnimationDirection.FORWARD);
                offset += Naphthav2.jetBrainsMono.getValue().getHeight() + 2.5f;
            }
        }
        categorySM.setMaxScroll(offset - (float) (height) + 1);
        glPopMatrix();
        offset = 0;
        glDisable(GL_SCISSOR_TEST);
        glDisable(GL_TEXTURE_2D);
        //Shader.drawQuads(Mouse.getX(), (mc.displayHeight - Mouse.getY()) , 10, 10);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_SCISSOR_TEST);
        glPushMatrix();
        glTranslated(0,settingSM.getScroll(), 0);

        if(opened != null) {
            boolean resetAnimation = true;
            float moduleEnd = (float) (minX + categoryListWidth) - 9.5f + w;
            for(Setting<?> s : opened.getSettings()) {
                float additionalOffset = 0.f;
                boolean oddCaseHovered = false;
                float y = (float) (anchorY + 56.5) + offset + 7.5f;
                float maxTextWidth = 97.5f;
                final double nameWidth = jetBrainsMonoExtraSmall.getStringWidth(s.getName());
                glEnable(GL_TEXTURE_2D);
                if(s instanceof Button) {
                    glDisable(GL_TEXTURE_2D);
                    drawButton(
                            (float) (minX + 304 - 5 - 15),
                            y - 3.75f,
                            (float) animationMap.get(s).getProgress()
                    );
                    if(
                            isMouseOver(
                                    (((double) mc.displayWidth / 2 / 2) - panelWidth / 2) + 48.5f + 304 - 5 - 15 + 1,
                                    (((float) mc.displayHeight / 2 / 2) - panelHeight / 2) + 56.5f + offset + 5.5f,
                                    10,
                                    10
                            )
                    ) {
                        hovered = s;
                    }
                    maxTextWidth -= 16;
                } //done
                else if(s instanceof PosSetting) {
                    continue;
                } //done
                else if(s instanceof DropDown) {
                    ScissorBox sb = new ScissorBox(
                            (int) ((((double) mc.displayWidth / 2 / 2) - panelWidth / 2) + 48.5f + 304 - 5 - jetBrainsMonoExtraSmall.getStringWidth(((DropDown)s).getValue()) - .5f),
                            (int) ((((float) mc.displayHeight / 2 / 2) - panelHeight / 2) + 56.5f + offset + 7.5f - 2f),
                            (int) ((float) jetBrainsMonoExtraSmall.getStringWidth(((DropDown)s).getValue()) + 2),
                            (int) (jetBrainsMonoExtraSmall.getHeight() + 3.5f)
                    );

                    mainScissor.glScissor();
                    Vector3f v = drawDropDownPanel(
                            (String[]) ((DropDown)s).getDropdownOptions().toArray(),
                            ((DropDown)s).getDropdownOptions().indexOf(((DropDown)s).getValue()),
                            (float) (minX + 304 - 5 - jetBrainsMonoExtraSmall.getStringWidth(((DropDown)s).getValue())),
                            y,
                            (float) animationMap.get(s).getProgress(),
                            sb,
                            (float) s1,
                            mainScissor
                    );
                    if(
                            isMouseOver(
                                    (((double) mc.displayWidth / 2 / 2) - panelWidth / 2) + 48.5f + 304 - 5 - jetBrainsMonoExtraSmall.getStringWidth(((DropDown)s).getValue()) - .5f ,
                                    (((float) mc.displayHeight / 2 / 2) - panelHeight / 2) + 56.5f + offset + 7.5f - 1.5f + settingSM.getScroll(),
                                    (float) jetBrainsMonoExtraSmall.getStringWidth(((DropDown)s).getValue()) + 1,
                                    jetBrainsMonoExtraSmall.getHeight() + 4.5f
                            )
                    ) {
                        hovered = s;
                    }
                    if(v.z != -1) {
                        oddCaseHovered = true;
                        hovered = new DropDownPair((DropDown) s, (int) v.z);
                    }
                    mainScissor.glScissor();
                    maxTextWidth -= v.x;
                    additionalOffset += v.y;
                }//done
                else if(s instanceof Keybind<?>) {
                    String str = droppedDown.contains(s) ? "§f[" + "§ ..." + "§f]" : "§f[§ " + ((Keybind<?>)s).getNiceKeyName() + "§f]";
                    drawKeybind(
                            (float) (minX + 304 - 5 - jetBrainsMonoExtraSmall.getStringWidth(str)),
                            y,
                            str,
                            theme.colourText
                    );
                    /*
                        x - .5f,
                        y - 2.5f,
                        (float) jetBrainsMonoExtraSmall.getStringWidth(text) + 1,
                        jetBrainsMonoExtraSmall.getHeight() + 4,
                     */
                    if (
                        isMouseOver(
                                (((double) mc.displayWidth / 2 / 2) - panelWidth / 2) + 48.5f + 304 - 5 - jetBrainsMonoExtraSmall.getStringWidth("§7[" + ((Keybind<?>)s).getNiceKeyName() + "§7]") - .5f,
                                (((float) mc.displayHeight / 2 / 2) - panelHeight / 2) + 56.5f + offset + 7.5 - 1.25f + settingSM.getScroll(),
                                (float) jetBrainsMonoExtraSmall.getStringWidth(str) + 1,
                                jetBrainsMonoExtraSmall.getHeight() + 4
                        )
                    ) {
                        hovered = s;
                    }

                    maxTextWidth-= (float) jetBrainsMonoExtraSmall.getStringWidth(str) + 1;
                } //done
                else if(s instanceof MapOption) {} //todo: unstarted (maybe scrap since subModule is just so much better)
                else if(s instanceof SelectAll) {

                } //todo: unstarted (cba)
                else if(s instanceof Slider) {
                    double endPoint = moduleEnd + nameWidth;
                    double panelEnd = minX + 303.5f;
                    double space = panelEnd - endPoint;
                    glDisable(GL_TEXTURE_2D);
                    float barWidth = (float) Math.max(20 + 27.5f * animationMap.get(s).getProgress(),Math.min(27.5f + 20 * animationMap.get(s).getProgress(), space));
                    float start = (float) (minX + 303.5f - barWidth - 4);
                    float end = start + barWidth;
                    drawPanelNoInset(
                            start,
                            y + 1,
                            barWidth,
                            1,
                            theme.colourElementBackground,
                            theme.colourElementBackground,
                            .5f,
                            .5f
                    );
                    Slider slider = (Slider) s;
                    double fract = slider.getFract();
                    float point = (float) (barWidth * fract);
                    drawPanelNoInset(//test
                            start + point - 2,
                            y - .5f,
                            4,
                            4,
                            theme.colourMain,
                            theme.colourMain,
                            1,
                            2
                    );

                    if(
                            isMouseOver(
                                    (((double) mc.displayWidth / 2 / 2) - panelWidth / 2) + 48.5f + 303.5f - barWidth - 4,
                                    (((float) mc.displayHeight / 2 / 2) - panelHeight / 2) + 56.5f + offset + 7.5f + 2.5f - 3f + settingSM.getScroll(),
                                    barWidth,
                                    6f
                            ) || (Mouse.isButtonDown(0) && s.equals(lastHovered))
                    ) {
                        animationMap.get(s).flip(Animation.AnimationDirection.FORWARD);
                    } else {
                        animationMap.get(s).flip(Animation.AnimationDirection.REVERSE);
                    }

                    if(
                            isMouseOver(
                                    (((double) mc.displayWidth / 2 / 2) - panelWidth / 2) + 48.5f + 303.5f - barWidth - 4,
                                    (((float) mc.displayHeight / 2 / 2) - panelHeight / 2) + 56.5f + offset + 7.5f + 2.5f - 1.5f + settingSM.getScroll(),
                                    barWidth,
                                    3f
                            ) || (Mouse.isButtonDown(0) && s.equals(lastHovered))
                    ) {
                        forceHovered = s;
                        hovered = s;

                        if(Mouse.isButtonDown(0)) {
                            float mx = ((float) Mouse.getX() / 2);
                            float progress = (float) ((mx - ((((double) mc.displayWidth / 2 / 2) - panelWidth / 2) + 48.5f + 303.5f - barWidth - 4)) / barWidth);
                            progress = Math.min(1, Math.max(0, progress));
                            glEnable(GL_TEXTURE_2D);
                            glDisable(GL_SCISSOR_TEST);
                            jetBrainsMonoExtraSmall.drawCenteredString(
                                    ((Slider)s).getValue() + "",
                                    start + barWidth * progress,
                                    y - 10,
                                    theme.colourText
                            );
                            glEnable(GL_SCISSOR_TEST);
                            glDisable(GL_TEXTURE_2D);
                            ((Slider)s).setValue(progress * ((Slider)s).getMax());
                        }
                    }
                    maxTextWidth -= (barWidth + 2);

                } //todo: make knob clickable
                else if(s instanceof SubModule<?>) {
                    String str = "add new";
                    drawKeybind(
                            (float) (minX + 304 - 5 - jetBrainsMonoExtraSmall.getStringWidth(str)),
                            y,
                            str,
                            theme.colourText
                    );
                    if (
                            isMouseOver(
                                    (((double) mc.displayWidth / 2 / 2) - panelWidth / 2) + 48.5f + 304 - 5 - jetBrainsMonoExtraSmall.getStringWidth(str) - .5f,
                                    (((float) mc.displayHeight / 2 / 2) - panelHeight / 2) + 56.5f + offset + 7.5 - 1.25f + settingSM.getScroll(),
                                    (float) jetBrainsMonoExtraSmall.getStringWidth(str) + 1,
                                    jetBrainsMonoExtraSmall.getHeight() + 4
                            )
                    ) {
                        hovered = s;
                    }

                    maxTextWidth-= (float) jetBrainsMonoExtraSmall.getStringWidth(str) + 1;
                    for(NonToggleModule n : ((SubModule<?>)s).getValue()) {
                        for(Setting<?> settig : n.getSettings()) {
                            offset += jetBrainsMonoExtraSmall.getHeight() + 10 + additionalOffset;
                            Object[] results = this.drawSetting(settig, 1, anchorY, offset, moduleEnd, mainScissor, categoryListWidth, width, panelHeight, panelWidth, minX, s1, lastHovered, w, true);
                            offset += (float) results[0];
                            resetAnimation = (boolean) results[1];
                            forceHovered = (GuiElementWrapper) results[2];
                            if(settig instanceof ExtraFeatureSetting<?, ?>) {
                                List<Setting<?>> visible = ((ExtraFeatureSetting<?, ?>) settig).getVisible();
                                for(Pair<Setting<?>, Integer> setttt: flatten((ExtraFeatureSetting<?, ?>) settig)) {
                                    boolean v = visible.contains(setttt.getKey());
                                    results = this.drawSetting(setttt.getKey(), setttt.getValue() + 1, anchorY, offset, moduleEnd, mainScissor, categoryListWidth, width, panelHeight, panelWidth, minX, s1, lastHovered, w, v);
                                    offset += jetBrainsMonoExtraSmall.getHeight() + 10 + additionalOffset;
                                    offset += (float) results[0];
                                    resetAnimation = (boolean) results[1];
                                    forceHovered = (GuiElementWrapper) results[2];
                                }
                            }
                        }
                    }
                } //todo: unstarted
                else if(s instanceof Text) {
                    //come back
                    String fuckfuckfuck = this.displayText((Text)s, 85, 1, jetBrainsMonoExtraSmall);
                    fuckfuckfuck = Objects.equals(fuckfuckfuck, "") ? " " : fuckfuckfuck;
                    drawBoxAndText(
                            (float) (minX + 304 - 5 - jetBrainsMonoExtraSmall.getStringWidth(fuckfuckfuck)),
                            y,
                            fuckfuckfuck
                    );

                    if (
                            isMouseOver(
                                    (((double) mc.displayWidth / 2 / 2) - panelWidth / 2) + 48.5f + 304 - 5 - jetBrainsMonoExtraSmall.getStringWidth(fuckfuckfuck) - .5f,
                                    (((float) mc.displayHeight / 2 / 2) - panelHeight / 2) + 56.5f + offset + 7.5 - 1.25f + settingSM.getScroll(),
                                    (float) jetBrainsMonoExtraSmall.getStringWidth(fuckfuckfuck) + 1,
                                    jetBrainsMonoExtraSmall.getHeight() + 4
                            )
                    ) {
                        hovered = s;
                    }

                    maxTextWidth -= (float) jetBrainsMonoExtraSmall.getStringWidth(fuckfuckfuck) + 2;
                } //todo: handling
                else if(s instanceof Toggle) {
                    drawToggle(
                            (float) (minX + 304 - 5 - 20),
                            y - 3.5f,
                            (float) animationMap.get(s).getProgress()
                    );

                    if(
                            isMouseOver(
                                    (((double) mc.displayWidth / 2 / 2) - panelWidth / 2) + 48.5f + 304 - 5 - 20 + 1,
                                    (((float) mc.displayHeight / 2 / 2) - panelHeight / 2) + 56.5f + offset + 5.5f + settingSM.getScroll(),
                                    19,
                                    10
                            )
                    ) {
                        hovered = s;
                    }

                    maxTextWidth -= 21;
                } // done
                glEnable(GL_TEXTURE_2D);
                if(nameWidth < maxTextWidth)
                    jetBrainsMonoExtraSmall.drawString(
                        s.getName(),
                        moduleEnd, y,
                        theme.colourText
                    );
                else {

                    setupScissor(
                            (float) ((float) mc.displayWidth / 2 / 2 - width / 2 + categoryListWidth - 9.5 + w),
                            (float) ((((float) mc.displayHeight / 2 / 2) - panelHeight / 2) + 56.5 + offset + 5.5f) + settingSM.getScroll(),
                            maxTextWidth,
                            jetBrainsMonoExtraSmall.getHeight() << 1,
                            s1
                    ).intersection(mainScissor).glScissor();
                    glPushMatrix();
                    String text = s.getName();
                    if(s.equals(this.hovered) || oddCaseHovered) {
                        resetAnimation = false;
                        double difference = nameWidth - maxTextWidth;
                        glTranslated(-new EaseInOutSine().apply(animationMap.get(0).getProgress()) * (difference + 0.5f), 0, 0);
                    }
                    jetBrainsMonoExtraSmall.drawString(
                            text,
                            moduleEnd, y,
                            theme.colourText
                    );
                    mainScissor.glScissor();
                    glPopMatrix();
                }
                offset += jetBrainsMonoExtraSmall.getHeight() + 10 + additionalOffset;
                if(s instanceof ExtraFeatureSetting<?, ?>) {
                    List<Setting<?>> visible = ((ExtraFeatureSetting<?, ?>) s).getVisible();
                    for(Pair<Setting<?>, Integer> pair : flattenAll((ExtraFeatureSetting<?, ?>) s)) {
                        boolean v = visible.contains(pair.getKey());
                        Setting<?> setting = pair.getKey();
                        float startOffset = offset;
                        int indentation = pair.getValue();
                        //0 = offset
                        //1 = resetAnimation
                        //2 = forceHovered
                        Object[] results = this.drawSetting(setting, indentation, anchorY, offset, moduleEnd, mainScissor, categoryListWidth, width, panelHeight, panelWidth, minX, s1, lastHovered, w, v);
                        offset += jetBrainsMonoExtraSmall.getHeight() + 10 + additionalOffset;
                        offset += (float) results[0];
                        resetAnimation = (boolean) results[1];
                        forceHovered = (GuiElementWrapper) results[2];
                    }
                }



            }
            if(resetAnimation) {
                animationMap.get(0).reset(0,1);
            }
        }
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
        settingSM.setMaxScroll(offset - (float) (height) + 2.5f);
        glDisable(GL_SCISSOR_TEST);
        //todo: chroma shader for text
        Naphthav2.jetBrainsMono.getKey().drawString(
                "§zn",
                anchorX + 60,
                anchorY + 37,
                theme.colourMain
        );
        Naphthav2.jetBrainsMono.getKey().drawString(
                "aphtha",
                anchorX + 60 + Naphthav2.jetBrainsMono.getKey().getScaledStringWidth("n", 1.f),
                anchorY + 37,
                theme.colourElementBackground
        );
        //drawResize((float) (anchorX + 48.5 + 303 - 9 - 1), anchorY + 210 - 1, 8,8);
        GL11.glEnable(GL_TEXTURE_2D);
        glPopMatrix();

        if(forceHovered != null)
            this.hovered = forceHovered;


        if(this.currentMenu != null) {
            if(this.currentMenu.shouldCLosePanel) {
                this.currentMenu = null;
                glDisable(GL_BLEND);
                return;
            }
            glPushMatrix();
            scale((double) 1 /sr.getScaleFactor());
            scale(2);
            if(this.currentMenu.draw()) {
                this.hovered = this.currentMenu;
            }
            glPopMatrix();
        }
        glDisable(GL_BLEND);
    }

    public static boolean isMouseOver(double x, double y, double xSize, double ySize){
        double scale = 2;
        double mouseX = Mouse.getX() / scale;
        double mouseY = (mc.displayHeight - Mouse.getY()) / scale;
        return mouseX >= x && mouseY >= y && mouseX < x + xSize && mouseY < y + ySize;
    }

    @Override
    public void onMouseDrag(float mouseX, float mouseY, int mouseButton, long timeSinceLastClick) {

    }

    //this REEKS
    //0 = offset
    //1 = resetAnimation
    //2 = forceHovered
    public Object[] drawSetting(Setting<?> s, int indentation, float anchorY, float offset, float moduleEnd, ScissorBox mainScissor, double categoryListWidth, double width, float panelHeight, float panelWidth, double minX, double s1, GuiElementWrapper lastHovered, float w, boolean visible) {
        Object[] r = new Object[3];
        r[1] = true;
        Theme theme = ThemeManager.theme();
        float additionalOffset = 0.f;
        boolean oddCaseHovered = false;
        float y = (float) (anchorY + 56.5) + offset + 7.5f;
        float maxTextWidth = 97.5f;
        final double nameWidth = jetBrainsMonoExtraSmall.getStringWidth(s.getName());
        glEnable(GL_TEXTURE_2D);
        if(s instanceof Button) {
            glDisable(GL_TEXTURE_2D);
            drawButton(
                    (float) (minX + 304 - 5 - 15),
                    y - 3.75f,
                    (float) animationMap.get(s).getProgress()
            );
            if(
                    isMouseOver(
                            (((double) mc.displayWidth / 2 / 2) - panelWidth / 2) + 48.5f + 304 - 5 - 15 + 1,
                            (((float) mc.displayHeight / 2 / 2) - panelHeight / 2) + 56.5f + offset + 5.5f,
                            10,
                            10
                    )
            ) {
                hovered = s;
            }
            maxTextWidth -= 16;
        } //done
        else if(s instanceof PosSetting) {
        } //done
        else if(s instanceof DropDown) {
            ScissorBox sb = new ScissorBox(
                    (int) ((((double) mc.displayWidth / 2 / 2) - panelWidth / 2) + 48.5f + 304 - 5 - jetBrainsMonoExtraSmall.getStringWidth(((DropDown)s).getValue()) - .5f),
                    (int) ((((float) mc.displayHeight / 2 / 2) - panelHeight / 2) + 56.5f + offset + 7.5f - 2f),
                    (int) ((float) jetBrainsMonoExtraSmall.getStringWidth(((DropDown)s).getValue()) + 2),
                    (int) (jetBrainsMonoExtraSmall.getHeight() + 3.5f)
            );

            mainScissor.glScissor();
            Vector3f v = drawDropDownPanel(
                    (String[]) ((DropDown)s).getDropdownOptions().toArray(),
                    ((DropDown)s).getDropdownOptions().indexOf(((DropDown)s).getValue()),
                    (float) (minX + 304 - 5 - jetBrainsMonoExtraSmall.getStringWidth(((DropDown)s).getValue())),
                    y,
                    (float) animationMap.get(s).getProgress(),
                    sb,
                    (float) s1,
                    mainScissor
            );
            if(
                    isMouseOver(
                            (((double) mc.displayWidth / 2 / 2) - panelWidth / 2) + 48.5f + 304 - 5 - jetBrainsMonoExtraSmall.getStringWidth(((DropDown)s).getValue()) - .5f ,
                            (((float) mc.displayHeight / 2 / 2) - panelHeight / 2) + 56.5f + offset + 7.5f - 1.5f + settingSM.getScroll(),
                            (float) jetBrainsMonoExtraSmall.getStringWidth(((DropDown)s).getValue()) + 1,
                            jetBrainsMonoExtraSmall.getHeight() + 4.5f
                    )
            ) {
                hovered = s;
            }
            if(v.z != -1) {
                oddCaseHovered = true;
                hovered = new DropDownPair((DropDown) s, (int) v.z);
            }
            mainScissor.glScissor();
            maxTextWidth -= v.x;
            additionalOffset += v.y;
        }//done
        else if(s instanceof Keybind<?>) {
            String str = droppedDown.contains(s) ? "§f[" + "§ ..." + "§f]" : "§f[§ " + ((Keybind<?>)s).getNiceKeyName() + "§f]";
            drawKeybind(
                    (float) (minX + 304 - 5 - jetBrainsMonoExtraSmall.getStringWidth(str)),
                    y,
                    str,
                    theme.colourText
            );
                    /*
                        x - .5f,
                        y - 2.5f,
                        (float) jetBrainsMonoExtraSmall.getStringWidth(text) + 1,
                        jetBrainsMonoExtraSmall.getHeight() + 4,
                     */
            if (
                    isMouseOver(
                            (((double) mc.displayWidth / 2 / 2) - panelWidth / 2) + 48.5f + 304 - 5 - jetBrainsMonoExtraSmall.getStringWidth("§7[" + ((Keybind<?>)s).getNiceKeyName() + "§7]") - .5f,
                            (((float) mc.displayHeight / 2 / 2) - panelHeight / 2) + 56.5f + offset + 7.5 - 1.25f + settingSM.getScroll(),
                            (float) jetBrainsMonoExtraSmall.getStringWidth(str) + 1,
                            jetBrainsMonoExtraSmall.getHeight() + 4
                    )
            ) {
                hovered = s;
            }

            maxTextWidth-= (float) jetBrainsMonoExtraSmall.getStringWidth(str) + 1;
        } //done
        else if(s instanceof MapOption) {} //todo: unstarted (maybe scrap since subModule is just so much better)
        else if(s instanceof SelectAll) {
        } //todo: unstarted (cba)
        else if(s instanceof Slider) {
            double endPoint = moduleEnd + nameWidth;
            double panelEnd = minX + 303.5f;
            double space = panelEnd - endPoint;
            glDisable(GL_TEXTURE_2D);
            float barWidth = (float) Math.max(20 + 27.5f * animationMap.get(s).getProgress(),Math.min(27.5f + 20 * animationMap.get(s).getProgress(), space));
            float start = (float) (minX + 303.5f - barWidth - 4);
            float end = start + barWidth;
            drawPanelNoInset(
                    start,
                    y + 1,
                    barWidth,
                    1,
                    theme.colourElementBackground,
                    theme.colourElementBackground,
                    .5f,
                    .5f
            );
            Slider slider = (Slider) s;
            double fract = slider.getFract();
            float point = (float) (barWidth * fract);
            drawPanelNoInset(//test
                    start + point - 2,
                    y - .5f,
                    4,
                    4,
                    theme.colourMain,
                    theme.colourMain,
                    1,
                    2
            );

            if(
                    isMouseOver(
                            (((double) mc.displayWidth / 2 / 2) - panelWidth / 2) + 48.5f + 303.5f - barWidth - 4,
                            (((float) mc.displayHeight / 2 / 2) - panelHeight / 2) + 56.5f + offset + 7.5f + 2.5f - 3f + settingSM.getScroll(),
                            barWidth,
                            6f
                    ) || (Mouse.isButtonDown(0) && s.equals(lastHovered))
            ) {
                animationMap.get(s).flip(Animation.AnimationDirection.FORWARD);
            } else {
                animationMap.get(s).flip(Animation.AnimationDirection.REVERSE);
            }

            if(
                    isMouseOver(
                            (((double) mc.displayWidth / 2 / 2) - panelWidth / 2) + 48.5f + 303.5f - barWidth - 4,
                            (((float) mc.displayHeight / 2 / 2) - panelHeight / 2) + 56.5f + offset + 7.5f + 2.5f - 1.5f + settingSM.getScroll(),
                            barWidth,
                            3f
                    ) || (Mouse.isButtonDown(0) && s.equals(lastHovered))
            ) {
                r[2] = s;
                hovered = s;

                if(Mouse.isButtonDown(0)) {
                    float mx = ((float) Mouse.getX() / 2);
                    float progress = (float) ((mx - ((((double) mc.displayWidth / 2 / 2) - panelWidth / 2) + 48.5f + 303.5f - barWidth - 4)) / barWidth);
                    progress = Math.min(1, Math.max(0, progress));
                    glEnable(GL_TEXTURE_2D);
                    glDisable(GL_SCISSOR_TEST);
                    jetBrainsMonoExtraSmall.drawCenteredString(
                            ((Slider)s).getValue() + "",
                            start + barWidth * progress,
                            y - 10,
                            theme.colourText
                    );
                    glEnable(GL_SCISSOR_TEST);
                    glDisable(GL_TEXTURE_2D);
                    ((Slider)s).setValue(progress * ((Slider)s).getMax());
                }
            }
            maxTextWidth -= (barWidth + 2);

        } //todo: make knob clickable
        else if(s instanceof SubModule<?>) {

        } //todo: unstarted
        else if(s instanceof Text) {
            drawBoxAndText(
                    (float) (minX + 304 - 5 - jetBrainsMonoExtraSmall.getStringWidth(((Text)s).getValue())),
                    y,
                    ((Text)s).getValue()
            );
            maxTextWidth -= (float) jetBrainsMonoExtraSmall.getStringWidth(((Text)s).getValue()) + 2;
        } //todo: handling
        else if(s instanceof Toggle) {
            drawToggle(
                    (float) (minX + 304 - 5 - 20),
                    y - 3.5f,
                    (float) animationMap.get(s).getProgress()
            );

            if(
                    isMouseOver(
                            (((double) mc.displayWidth / 2 / 2) - panelWidth / 2) + 48.5f + 304 - 5 - 20 + 1,
                            (((float) mc.displayHeight / 2 / 2) - panelHeight / 2) + 56.5f + offset + 5.5f + settingSM.getScroll(),
                            19,
                            10
                    )
            ) {
                hovered = s;
            }

            maxTextWidth -= 21;
        } // done
        glEnable(GL_TEXTURE_2D);
        if(!visible) {
            glColor4f(1.f,1.f,1.f,.5f);
        }
        float indentationthinggy = 5 * Math.max(0, indentation - 1);
        maxTextWidth -= indentationthinggy;
        glPushMatrix();
        glTranslated(indentationthinggy, 0, 0);
        if(nameWidth < maxTextWidth)
            jetBrainsMonoExtraSmall.drawString(
                    indentation != 0 ? replace("- {0}", s.getName()) : s.getName(),
                    moduleEnd, y,
                    theme.colourText
            );
        else {

            setupScissor(
                    (float) ((float) mc.displayWidth / 2 / 2 - width / 2 + categoryListWidth - 9.5 + w + indentationthinggy),
                    (float) ((((float) mc.displayHeight / 2 / 2) - panelHeight / 2) + 56.5 + offset + 5.5f) + settingSM.getScroll(),
                    maxTextWidth,
                    jetBrainsMonoExtraSmall.getHeight() << 1,
                    s1
            ).intersection(mainScissor).glScissor();
            glPushMatrix();
            String text = indentation != 0 ? replace("- {0}", s.getName()) : s.getName();
            if(s.equals(this.hovered) || oddCaseHovered) {
                r[1] = false;
                double difference = nameWidth - maxTextWidth;
                glTranslated(-new EaseInOutSine().apply(animationMap.get(0).getProgress()) * (difference + 0.5f), 0, 0);
            }
            jetBrainsMonoExtraSmall.drawString(
                    text,
                    moduleEnd, y,
                    theme.colourText
            );
            mainScissor.glScissor();
            glPopMatrix();
        }
        glPopMatrix();
        r[0] = additionalOffset;
        return r;
    }

    @Override
    public void onMouseScroll(int state) {
        double multiplier = ModuleManager.clickGuiModule.scrollAmnt.getValue();
        float panelWidth = (float) PANEL_WIDTH / 2;
        float panelHeight = (float) PANEL_HEIGHT / 2;
        float anchorX = (((float) (mc.displayWidth / 2) / 2) - panelWidth / 2);
        float anchorY = (((float) (mc.displayHeight / 2) / 2) - panelHeight / 2);
        double minX = anchorX + 48.5;
        double minY = anchorY + 55;
        double height = 165;
        double categoryListWidth = 70;
        if(isMouseOver(minX + 5.5, minY, categoryListWidth - 6, height)) {
            categorySM.update((float) (((double) state / 6) * multiplier));
        } else {
            categorySM.update(0);
        } if(isMouseOver(minX + 5.5 + categoryListWidth - 6, minY,122.5f, height)){//122.5F
            moduleSM.update((float) (((double) state / 6) * multiplier));
        } else {
            moduleSM.update(0);
        } if(isMouseOver(minX + 5.5 + categoryListWidth - 6 + 122.5f, minY, 111f, height)) {
            settingSM.update((float) (((double) state / 6) * multiplier));
        } else {
            settingSM.update(0);
        }
    }

    @Override
    public void onClick(float mouseX, float mouseY, int button) {
        droppedDown.removeIf(s -> (s instanceof Text && !s.equals(this.hovered)) || (s instanceof Keybind<?> && !(hovered instanceof Keybind<?>)));
        if(!(hovered instanceof RightCLickMenu<?>)) {
            this.currentMenu = null;
        }
        if(hovered == null) {
            return;
        }
        else if(hovered instanceof Category) {
            if(button == 0) {
                selected = (Category) this.hovered;
            }
        }
        else if(hovered instanceof NonToggleModule) {
            if(button == 0) {
                opened = (NonToggleModule) this.hovered;
            } else if(hovered instanceof Module && button == 1) {
                this.currentMenu = new RightCLickMenu<>((Module) this.hovered, this).addOption(
                        new ModuleToggleElement()
                ).addOption(
                        new OpenSettingsElement<>()
                ).addOption(
                        new PinToArrayListElement()
                ).addOption(
                        new HideFromArraylistElement()
                ).addOption(
                        new SilenceNotificationElement()
                ).addOption(
                        new WipeModuleConfigElement<>()
                );
                ScaledResolution sr = new ScaledResolution(mc);
                double finalScale = 2 * (1.0 / sr.getScaleFactor());

                double centerX = sr.getScaledWidth() / 2.0;
                double centerY = sr.getScaledHeight() / 2.0;

                double drawX = centerX + (mouseX - centerX) / finalScale;
                double drawY = centerY + (mouseY - centerY) / finalScale;
                this.currentMenu.x = (float) drawX;
                this.currentMenu.y = (float) drawY;
            } else if(button == 1) {
                this.currentMenu = new RightCLickMenu<>((NonToggleModule) this.hovered, this).addOption(
                        new OpenSettingsElement<>()
                ).addOption(
                        new WipeModuleConfigElement<>()
                );
                ScaledResolution sr = new ScaledResolution(mc);
                double finalScale = 2 * (1.0 / sr.getScaleFactor());

                double centerX = sr.getScaledWidth() / 2.0;
                double centerY = sr.getScaledHeight() / 2.0;

                double drawX = centerX + (mouseX - centerX) / finalScale;
                double drawY = centerY + (mouseY - centerY) / finalScale;
                this.currentMenu.x = (float) drawX;
                this.currentMenu.y = (float) drawY;
            }
        }
        else if(hovered instanceof Button) {
            if(button == 0) {
                animationMap.get(hovered).reset(0.00001f).play().flip(Animation.AnimationDirection.FORWARD);
                ((Button)hovered).getValue().run();
            }
        }
        else if(hovered instanceof Toggle) {
            if(button == 0) {
                ((Toggle)hovered).toggle();
                animationMap.get(hovered).flip(((Toggle)hovered).isToggled() ? Animation.AnimationDirection.FORWARD : Animation.AnimationDirection.REVERSE);
            }
        }
        else if(hovered instanceof DropDown) {
            if(button == 0) {
                if(droppedDown.contains(hovered)) {
                    //new selection handling
                    print("uh oh spaghetti o!");
                    droppedDown.remove(hovered);
                    animationMap.get(hovered).flip(Animation.AnimationDirection.REVERSE);
                } else {
                    droppedDown.add(hovered);
                    animationMap.get(hovered).flip(Animation.AnimationDirection.FORWARD);
                }
            }
        }
        else if(hovered instanceof DropDownPair) {
            if(button == 0) {
                if(droppedDown.contains(((DropDownPair) hovered).getKey())) {
                    ((DropDownPair) hovered).getKey().setValue(((DropDownPair) hovered).getKey().getDropdownOptions().get(((DropDownPair) hovered).getValue()));
                    droppedDown.remove(((DropDownPair) hovered).getKey());
                    animationMap.get(((DropDownPair) hovered).getKey()).flip(Animation.AnimationDirection.REVERSE);
                }
            }
        }
        else if(hovered instanceof Keybind<?>) {
            if(button == 0) {
                if(droppedDown.contains(hovered)) {
                    droppedDown.remove(hovered);
                } else {
                    droppedDown.add(hovered);
                }
            }
        }
        else if(hovered instanceof Text) {
            if(button == 0) {
                if(!droppedDown.contains(hovered))
                    droppedDown.add(hovered);
                else
                    droppedDown.remove(hovered);
            }
        }
        else if(hovered instanceof RightCLickMenu<?>) {
            ((RightCLickMenu<?>)hovered).onClick();
        }
        else if(hovered instanceof SubModule<?> && button == 0)  {
            this.pushAnimMap(((SubModule<?>)hovered).addNew());
        }
        if(hovered instanceof Changeable<?> && button == 1 && hovered instanceof Setting<?>) {
            this.currentMenu = new RightCLickMenu<Setting<?>>((Setting<?>) this.hovered, this).addOption(
                    new WipeConfigElement<>()
            );
            ScaledResolution sr = new ScaledResolution(mc);
            double finalScale = 2 * (1.0 / sr.getScaleFactor());

            double centerX = sr.getScaledWidth() / 2.0;
            double centerY = sr.getScaledHeight() / 2.0;

            double drawX = centerX + (mouseX - centerX) / finalScale;
            double drawY = centerY + (mouseY - centerY) / finalScale;
            this.currentMenu.x = (float) drawX;
            this.currentMenu.y = (float) drawY;
        }
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int state) {

    }

    public static void drawBoxAndText(float x, float y, String text) {
        glDisable(GL_TEXTURE_2D);
        drawPanelNoInset(
                x - .5f,
                y - 2.5f,
                (float) jetBrainsMonoExtraSmall.getStringWidth(text) + 1,
                jetBrainsMonoExtraSmall.getHeight() + 4,
                new Color(84, 84, 84,255).getRGB(),
                new Color(84, 84, 84,255).getRGB(),
                1,
                1f
        );
        glEnable(GL_TEXTURE_2D);
        jetBrainsMonoExtraSmall.drawString(
                text,
                x,
                y,
                0xffffffff
        );
    }

    public static void drawKeybind(float x, float y, String text, int colour) {
        glDisable(GL_TEXTURE_2D);
        drawPanelNoInset(
                x - .5f,
                y - 2.5f,
                (float) jetBrainsMonoExtraSmall.getStringWidth(text) + 1,
                jetBrainsMonoExtraSmall.getHeight() + 4,
                new Color(84, 84, 84,255).getRGB(),
                new Color(84, 84, 84,255).getRGB(),
                1,
                1f
        );
        glEnable(GL_TEXTURE_2D);
        jetBrainsMonoExtraSmall.drawString(
                text,
                x,
                y,
                colour
        );
    }

    @Override
    public boolean onKeyTyped(char character, int keyCode) {
        boolean var0 = true;
        for(GuiElementWrapper element : new ArrayList<>(droppedDown)) {
            if(element instanceof Keybind<?>) {
                if(keyCode == KEY_ESCAPE) {
                    var0 = false;
                    ((Keybind<?>)element).setValue(0x00);
                }
                else
                    ((Keybind<?>)element).setValue(keyCode);
                droppedDown.remove(element);
            }
            else if(element instanceof Text) {
                if(keyCode == KEY_ESCAPE) {
                    var0 = false;
                    droppedDown.remove(element);
                }
                else {
                    Text e = ((Text) element);
                    e.setValue(e.handleTyped(e.getValue(), character, keyCode));
                }
            }
        }
        if(hovered instanceof Slider) {
            if(keyCode == KEY_LEFT) {
                ((Slider)hovered).setValue(((Slider)hovered).getValue() - ((Keyboard.isKeyDown(KEY_LSHIFT) ? 5 : 1) * Math.pow(10, -((Slider)hovered).decimalPlaces)));
            } else if(keyCode == KEY_RIGHT) {
                ((Slider)hovered).setValue(((Slider)hovered).getValue() + ((Keyboard.isKeyDown(KEY_LSHIFT) ? 5 : 1) * Math.pow(10, -((Slider)hovered).decimalPlaces)));
            }
        }
        return var0;
    }

    public void drawDropDownIcon(float x, float y, float w, float h, float anim) {
        glPushMatrix();
        glScalef(w, h, 1);
        glTranslatef(x, y, 0);
        glRotated(180 * anim, 0,0,1);
        glBegin(GL_TRIANGLES);
        glVertex2f(-1.0f, 1.0f);
        glVertex2f(1.0f, 1.0f);
        glVertex2f(0.0f, -1.0f);
        glEnd();
        glPopMatrix();
    }

    private static class DropDownPair extends Pair<DropDown, Integer> implements GuiElementWrapper {
        public DropDownPair(DropDown setting, int index) {
            super(setting, index);
        }

        public DropDownPair(Setting s, int index) {
            super((DropDown) s, index);
        }

        @Override
        public String getDescription() {
            return "";
        }
    }

    private static class RightCLickMenu<T extends GuiElementWrapper> implements GuiElementWrapper {
        public static int PANEL_WIDTH = 150;
        public float x,y;
        private MenuElement<T>[] elements;
        private MenuElement<T> hovered = null;
        private PanelClickGui gui;
        private boolean shouldCLosePanel = false;
        private T context;

        public boolean shouldClosePanel() {
            return this.shouldCLosePanel;
        }

        public void setContext(T context) {
            this.context = context;
        }

        public MenuElement<T> getHovered() {
            return this.hovered;
        }

        @SuppressWarnings("unchecked")
        public RightCLickMenu(T context, PanelClickGui gui) {
            this(context, (MenuElement<T>[]) new MenuElement<?>[0], gui);
        }

        public RightCLickMenu(T context, MenuElement<T>[] elements, PanelClickGui gui) {
            this.context = context;
            this.elements = elements;
            this.gui = gui;
        }

        @SuppressWarnings("unchecked")
        public RightCLickMenu<T> addOption(MenuElement<T> element) {
            MenuElement<T>[] old = this.elements;
            this.elements = (MenuElement<T>[]) new MenuElement<?>[old.length + 1];
            System.arraycopy(old, 0, this.elements, 0, old.length);
            this.elements[old.length] = element;
            return this;
        }

        public void onClick() {
            if(this.hovered != null) {
                if(this.hovered.onClick(this.context, this.gui)) {
                    this.shouldCLosePanel = true;
                }
            }
        }

        public boolean draw() {
            MenuElement<T> lastHovered = this.hovered;
            PANEL_WIDTH = 75;
            MenuElementType last = null;
            this.hovered = null;

            //todo -> sort by type
            int offset = 0;
            for(MenuElement<T> element : this.elements) {
                if(last == null)
                    last = element.getType();

                if(!last.equals(element.getType())) {
                    Theme t = ThemeManager.theme();
                    t.colour(t.colourPanelBackgroundSecondary);
                    glDisable(GL_TEXTURE_2D);
                    Shader.drawQuads(x, y + offset, RightCLickMenu.PANEL_WIDTH, 2);
                    t.colour(t.colourElementBackground);
                    Shader.drawQuads(x + 2, y + offset, RightCLickMenu.PANEL_WIDTH - 4, 1 );
                    offset+=2;
                }

                MenuElement<T> result = element.draw(this.x, this.y + offset, lastHovered, this.context);
                if(result != null)
                    this.hovered = result;
                last = element.getType();
                offset += jetBrainsMonoExtraSmall.getHeight() + 4;
            }
            return this.hovered != null; //todo -> return mouse over
        }

        @Override
        public String getDescription() {
            return "";
        }
    }

    /*
     * types:
     *      - Open settings in new tab
     *      - open settings
     *      - hide/show from arraylist
     *      - pin/unpin to arraylist
     *      - silence notifications
     *      - enable/disable
     *      - wipe module config (possibly)
     *      - export module config to clipboard
     *      - import module config from clipboard
     *
     * categories:
     *      - arraylist
     *      - config
     *      - general
     */

    private interface MenuElement<T extends GuiElementWrapper> {

        MenuElementType getType();

        String getText(T context);

        default MenuElement<T> draw(float x, float y, MenuElement<T> lastHovered, T context) {
            MenuElement<T> hovered = null;
            Theme t = ThemeManager.theme();
            t.colour(t.colourPanelBackgroundSecondary);
            glDisable(GL_TEXTURE_2D);
            Shader.drawQuads(x, y, RightCLickMenu.PANEL_WIDTH, jetBrainsMonoExtraSmall.getHeight() + 4);
            glEnable(GL_TEXTURE_2D);
            t.colour(t.colourText);
            jetBrainsMonoExtraSmall.drawString(
                    this.getText(context),
                    x + 2,
                    y + 2,
                    t.colourText
            );
            if(
                    isMouseOver2(
                            x, y,
                            RightCLickMenu.PANEL_WIDTH,
                            jetBrainsMonoExtraSmall.getHeight() + 4
                    )
            ) {
                hovered = this;
                t.colour(t.colourText, .25f);
                glDisable(GL_TEXTURE_2D);
                glEnable(GL_BLEND);
                Shader.drawQuads(x, y, RightCLickMenu.PANEL_WIDTH, jetBrainsMonoExtraSmall.getHeight() + 4);
                glEnable(GL_TEXTURE_2D);
            }

            return hovered;
        }

        boolean onClick(T context, PanelClickGui gui);
    }

    private static class ModuleToggleElement implements MenuElement<Module> {
        @Override
        public MenuElementType getType() {
            return MenuElementType.GENERAL;
        }

        @Override
        public String getText(Module context) {
            return (context.isToggled() ? "Disable" : "Enable") + " module";
        }

        @Override
        public boolean onClick(Module context, PanelClickGui gui) {
            context.toggle();
            return true;
        }
    }

    private static class HideFromArraylistElement implements MenuElement<Module> {

        @Override
        public MenuElementType getType() {
            return MenuElementType.VISIBILITY;
        }

        @Override
        public String getText(Module context) {
            return ArrayListModule.hidden.contains(context) ? "Show in arraylist" : "Hide from arraylist";
        }

        @Override
        public boolean onClick(Module context, PanelClickGui gui) {
            if(ArrayListModule.hidden.contains(context)) {
                ArrayListModule.hidden.remove(context);
            } else {
                ArrayListModule.hidden.add(context);
            }
            return false;
        }
    }

    private static class PinToArrayListElement implements MenuElement<Module> {

        @Override
        public MenuElementType getType() {
            return MenuElementType.VISIBILITY;
        }

        @Override
        public String getText(Module context) {
            return ArrayListModule.pinned.contains(context) ? "Unpin from arraylist" : "Pin to arraylist";
        }

        @Override
        public boolean onClick(Module context, PanelClickGui gui) {
            if(ArrayListModule.pinned.contains(context)) {
                ArrayListModule.pinned.remove(context);
            } else {
                ArrayListModule.pinned.add(context);
            }
            return false;
        }
    }

    private static class SilenceNotificationElement implements MenuElement<Module> {

        @Override
        public MenuElementType getType() {
            return MenuElementType.VISIBILITY;
        }

        @Override
        public String getText(Module context) {
            return ModuleManager.silenced.contains(context) ? "Show notifications" : "Hide notifications";
        }

        @Override
        public boolean onClick(Module context, PanelClickGui gui) {
            if(ModuleManager.silenced.contains(context)) {
                ModuleManager.silenced.remove(context);
            } else {
                ModuleManager.silenced.add(context);
            }
            return false;
        }

    }

    private static class OpenSettingsElement<T extends NonToggleModule> implements MenuElement<T> {

        @Override
        public MenuElementType getType() {
            return MenuElementType.GENERAL;
        }

        @Override
        public String getText(NonToggleModule context) {
            return "§lOpen";
        }

        @Override
        public boolean onClick(NonToggleModule context, PanelClickGui gui) {
            PanelClickGui.opened = context;
            return true;
        }
    }

    private static class WipeModuleConfigElement<T extends NonToggleModule> implements MenuElement<T> {

        @Override
        public MenuElementType getType() {
            return MenuElementType.CONFIG;
        }

        @Override
        public String getText(T context) {
            return "wipe config";
        }

        @Override
        public boolean onClick(T context, PanelClickGui gui) {
            context.getSettings().forEach(s -> {
                if(s instanceof Changeable) {
                    ((Changeable<?>) s).wipeConfig();
                    if(s instanceof Toggle) {
                        gui.animationMap.get(s).flip(((Toggle)s).isToggled() ? Animation.AnimationDirection.FORWARD : Animation.AnimationDirection.REVERSE);
                    }
                }

                if(s instanceof ExtraFeatureSetting<?, ?>) {
                    for(Pair<Setting<?>, Integer> pair : flattenAll((ExtraFeatureSetting<?, ?>) s)) {
                        if(pair.getKey() instanceof Changeable) {
                            ((Changeable<?>) pair.getKey()).wipeConfig();
                            if(pair.getKey() instanceof Toggle) {
                                gui.animationMap.get(pair.getKey()).flip(((Toggle)pair.getKey()).isToggled() ? Animation.AnimationDirection.FORWARD : Animation.AnimationDirection.REVERSE);
                            }
                        }
                    }
                }
            });
            return true;
        }
    }

    private static class WipeConfigElement<T extends Setting<?>> implements MenuElement<T> {

        @Override
        public MenuElementType getType() {
            return MenuElementType.CONFIG;
        }

        @Override
        public String getText(T context) {
            return "wipe config";
        }

        @Override
        public boolean onClick(T context, PanelClickGui gui) {
            if(context instanceof Changeable) {
                ((Changeable<?>) context).wipeConfig();
                if(context instanceof Toggle) {
                    gui.animationMap.get(context).flip(((Toggle)context).isToggled() ? Animation.AnimationDirection.FORWARD : Animation.AnimationDirection.REVERSE);
                }
            }
            return true;
        }
    }

    enum MenuElementType {
        VISIBILITY,
        CONFIG,
        GENERAL
    }

    private String displayText(Text setting, double xSize, double fontScale, FentRenderer fr){
        String displayText;
        String text = setting.getValue();
        double spaceLeft = xSize - (5 + Drawer.getScaledTextWidth(fr,setting.getName(),.5f) + 5);
        if(fitsInTextBox(setting.getValue(),fr,spaceLeft,fontScale)) return text;
        displayText = text;
        while(!fitsInTextBox(displayText,fr,spaceLeft,fontScale)){
            displayText = displayText.substring(1);
        }
        return displayText;
    }

    private boolean fitsInTextBox(String string, FentRenderer fr, double xSize, double scale){
        return Drawer.getScaledTextWidth(fr,string,scale) < xSize;
    }
}
