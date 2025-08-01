package seraph.base.Map.Gui.theme;

import seraph.base.Naphthav2;

import java.awt.*;

public class ThemeManager {
    private Theme currentTheme;
    private Theme[] allThemes = {
            new Theme(true, 0f, new Color(255,70,70).getRGB(), new Color(105, 175, 255,255).getRGB(), new Color(255,200,200).getRGB()),
            new Theme(false, .35f, new Color(255,70,70).getRGB(), new Color(255,70,70).getRGB(), new Color(255,200,200).getRGB())
    };

    public Theme getTheme() {
        return this.currentTheme;
    }

    public static Theme theme() {
        //return Naphthav2.themeManager.getTheme();
        return Naphthav2.themeManager.allThemes[0];
    }

    public void useTheme(final int i) {
        this.currentTheme = this.allThemes[i];
    }

    public void addTheme(Theme t) {
        int len = this.allThemes.length;
        Theme[] themes = this.allThemes;
        this.allThemes = new Theme[len + 1];
        System.arraycopy(themes, 0, this.allThemes, 0, len);
        this.allThemes[len] = t;
    }

    public ThemeManager() {
        this.currentTheme = allThemes[1];
    }
}
