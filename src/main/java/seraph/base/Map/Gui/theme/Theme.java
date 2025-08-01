package seraph.base.Map.Gui.theme;

import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Theme {
    public Theme(Theme source) {
        this(
                source.colourMain,
                source.colourSecond,
                source.colourPanelBackground,
                source.colourPanelBackgroundSecondary,
                source.colourElementBackground,
                source.colourElementBackgroundHovered,
                source.colourText,
                source.colourHoveredText
        );
        this.themeType = source.themeType;
    }

    public Theme(int cMain, int cSecond, int cMainPanelBackground, int cMainPanelBackgroundSecondary, int cElementBackground, int cElementBackgroundHovered, int cText, int cHoveredText) {
        this.colourMain = cMain;
        this.colourSecond = cSecond;
        this.colourPanelBackground = cMainPanelBackground;
        this.colourPanelBackgroundSecondary = cMainPanelBackgroundSecondary;
        this.colourElementBackground = cElementBackground;
        this.colourElementBackgroundHovered = cElementBackgroundHovered;
        this.colourText = cText;
        this.colourHoveredText = cHoveredText;
        this.themeType = EnumThemeType.CUSTOM;
    }

    public Theme(boolean darkMode, float factorIfLight, int cMain, int cSecond, int cHoveredText) {//todo -> improve light mode
        this(
                cMain,
                cSecond,
                lightenColor(new Color(38,38,38), darkMode ? 0 : factorIfLight).getRGB(),
                lightenColor(new Color(64,64,64), darkMode ? 0 : factorIfLight).getRGB(),
                lightenColor(new Color(84,84,84,255), darkMode ? 0 : factorIfLight).getRGB(),
                lightenColor(new Color(106, 106, 106), darkMode ? 0 : factorIfLight).getRGB(),
                (darkMode ? 0xffffffff : 0xFF000000),
                cHoveredText
        );
        this.themeType = darkMode ? EnumThemeType.DARK : EnumThemeType.LIGHT;
    }

    public float[] asFloatArr(final int c) {
        float a = ((c >> 24) & 0xFF) / 255.0f;
        float r = ((c >> 16) & 0xFF) / 255.0f;
        float g = ((c >> 8) & 0xFF) / 255.0f;
        float b = (c & 0xFF) / 255.0f;
        return new float[] { r, g, b, a };
    }

    public int mixTheme(int yPos) {
        float ratio2 = (1 - Math.abs((((System.currentTimeMillis() + (yPos << 2)) % 3000) / 1500F) - 1)) / 255F;
        float ratio = 0.003921568627F - ratio2;
        int r1 = (this.colourMain >> 16) & 0xFF, r2 = (this.colourSecond >> 16) & 0xFF;
        int g1 = (this.colourMain >> 8)  & 0xFF, g2 = (this.colourSecond >> 8)  & 0xFF;
        int b1 = this.colourMain & 0xFF, b2 = this.colourSecond & 0xFF;
        return (0xFF << 24) | ((int)((r1 * ratio) + (r2 * ratio2)) << 16) | ((int)((g1 * ratio) + (g2 * ratio2)) << 8) | (int)((b1 * ratio) + (b2 * ratio2));
    }

    public int mixTheme(int xPos, int yPos) {
        int stripeWidth = 16;
        float speed = 0.05f;
        float position = (xPos + yPos) + System.currentTimeMillis() * speed;
        float t = (position % (stripeWidth * 2)) / (stripeWidth * 2);
        float blend = t < 0.5f ? t * 2 : (1 - t) * 2;
        int r1 = (this.colourMain >> 16) & 0xFF, r2 = (this.colourSecond >> 16) & 0xFF;
        int g1 = (this.colourMain >> 8) & 0xFF, g2 = (this.colourSecond >> 8)  & 0xFF;
        int b1 =  this.colourMain & 0xFF, b2 = this.colourSecond & 0xFF;
        int r = (int)(r1 * blend + r2 * (1 - blend));
        int g = (int)(g1 * blend + g2 * (1 - blend));
        int b = (int)(b1 * blend + b2 * (1 - blend));
        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }

    public void colour(int colour) {
        GL11.glColor4f(((colour >> 16) & 0xFF) / 255.0f, ((colour >> 8)  & 0xFF) / 255.0f, (colour & 0xFF) / 255.0f, ((colour >> 24) & 0xFF) / 255.0f);
    }
    public void colour(int colour, float a) {
        GL11.glColor4f(((colour >> 16) & 0xFF) / 255.0f, ((colour >> 8)  & 0xFF) / 255.0f, (colour & 0xFF) / 255.0f, a);
    }

    private EnumThemeType themeType;
    public int colourMain; //new Color(255, 70, 70)
    public int colourSecond; //for colour shifting
    public int colourPanelBackground; //new Color(38,38,38)
    public int colourPanelBackgroundSecondary; //new Color(64,64,64)
    public int colourElementBackground; //new Color(84,84,84,255)
    public int colourElementBackgroundHovered; //new Color(106, 106, 106)
    public int colourText; //new Colour(255,255,255)
    public int colourHoveredText;//new Color(255,200,200)

    private enum EnumThemeType {
        DARK,
        LIGHT,
        CUSTOM
    }

    public static Color lightenColor(Color color, float factor) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsb[2] = Math.min(1.0f, hsb[2] + factor);
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        //return new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
    }
}
