package seraph.base.features.impl;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import seraph.base.Map.Gui.Category;
import seraph.base.Map.Gui.SubCategory;
import seraph.base.Map.Gui.modules.Hud;
import seraph.base.Map.Gui.modules.Interface;
import seraph.base.Map.Gui.modules.Module;
import seraph.base.Map.Gui.modules.ModuleClass;
import seraph.base.Map.Gui.settings.impl.DropDown;
import seraph.base.Map.Gui.settings.impl.Keybind;
import seraph.base.Map.Gui.settings.impl.PosSetting;
import seraph.base.Map.Gui.settings.impl.Toggle;
import seraph.base.Map.drawing.Shaders;
import seraph.base.Naphthav2;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static seraph.base.Naphthav2.mc;

@Interface
@ModuleClass
public class ArrayListModule extends Module implements Hud {
    public static List<Module> pinned;
    public static List<Module> hidden;
    private final DropDown style = new DropDown("Style", "ArrayList Style",
            new String[]{
                    "Basic",
                    "Line",
                    "Athenaware",
                    "CMH"
            }
    );
    private final DropDown splitting = new DropDown("Splitting", "",
            new String[] {
                    "Space",
                    "None",
                    "Comment",
                    "Dash",
                    "Arrow"
            }
    );

    private final DropDown sorting = new DropDown("Sorting", "",
            new String[]{
                    "Width",
                    "Off"
            }
    );
    private final Toggle lowercase = new Toggle("Lowercase", "", false);
    private final Toggle onlyName = new Toggle("only names", "only show names", false);
    private final Toggle split = new Toggle("split", "", false);
    private final PosSetting mainPos = new PosSetting("arraylistPos1", 1,0);
    private final PosSetting pos2 = new PosSetting("arraylistPos2", 0,0);

    static {
        pinned = new ArrayList<>();
        hidden = new ArrayList<>();
    }



    /*
     * style ideas:
     *  - Athenaware -> rounded rects (with spacing) & random colours (can be set to be on theme)
     *  - CMH -> similar to athenaware but with an icon on the right (and keybind in the name)
     */

    public ArrayListModule() {
        super(
                "Array List",
                "Displays a list of modules",
                Category.TWO,
                SubCategory.GUI
        );
        this.addSettings(
                style,
                splitting,
                sorting,
                lowercase,
                split,
                onlyName,
                mainPos,
                pos2
        );
    }

    protected void sort(List<Module> modules) {
        switch(this.sorting.getValue().toLowerCase()) {
            case "width":
                modules.sort((a, b) ->
                        Double.compare(
                                Naphthav2.jetBrainsMono.getValue().getStringWidth(this.getArrayListText(b, false)),
                                Naphthav2.jetBrainsMono.getValue().getStringWidth(this.getArrayListText(a, false))
                        )
                );
                break;
            case "off":
                break;
        }
    }

    private String getArrayListText(Module m, boolean mainList) {
        if(this.style.getValue().equals("CMH")) {
            int key = ((Keybind<?>)m.getSettings().get(0)).getValue();
            String formatted = key == 0x00 ? "" : "[" + Keyboard.getKeyName(key) + "]";
            return mainList ? m.getName() + this.getSeparator() + formatted : formatted + this.getSeparator() + m.getName();
        }
        if(onlyName.isToggled()) {
            return m.getName();
        }
        String extraInfo = m.getArrayListText();
        if(extraInfo.isEmpty()) {
            return m.getName();
        }
        return mainList ? m.getName() + this.getSeparator() + m.getArrayListText() : "§7" + m.getArrayListText() + this.getSeparator() + "§z" + m.getName();
    }

    private String getSeparator() {
        switch (this.splitting.getValue().toLowerCase()) {
            case "space":
                return " §7";
            case "none":
                return "§7";
            case "comment":
                return " §7// ";
            case "dash":
                return " §7- ";
            case "arrow":
                return "->";
        }
        return " (you have an error rofl) ";
    }

    public boolean drawBasic() {
        //list.sort((a, b) -> Integer.compare(getValue(b), getValue(a))); largest -> smallest
        List<Module> modules = new ArrayList<>(Naphthav2.INSTANCE.moduleManager.getToggleModules());
        sort(modules);
        double width = Naphthav2.jetBrainsMono.getValue().getStringWidth(modules.get(0).getName());
        boolean mainCol = true;
        float yOffset1 = 0;
        float yOffset2 = 0;
        ScaledResolution sr = new ScaledResolution(mc);
        for(Module m : modules) {
            if(!m.isToggled() && !pinned.contains(m)) continue;
            if(hidden.contains(m)) continue;
            String t = "§z" + (this.lowercase.isToggled() ? this.getArrayListText(m, mainCol).toLowerCase() : this.getArrayListText(m, mainCol));
            Naphthav2.jetBrainsMono.getValue().drawString(
                    t,
                    (mainCol ? mainPos.getValue().getKey() : pos2.getValue().getKey()) * ((double) sr.getScaledWidth() / (2 * ((double) 1 / sr.getScaleFactor()))) - (mainCol ? Naphthav2.jetBrainsMono.getValue().getStringWidth(t) + 2 : -2),
                    (float) ((mainCol ? mainPos.getValue().getValue() : pos2.getValue().getValue()) * ((double) sr.getScaledHeight() / (2 * ((double) 1 / sr.getScaleFactor()))) + (mainCol ? yOffset1 : yOffset2)) + 2,
                    0xffffffff
            );
            if (mainCol) {
                yOffset1 += Naphthav2.jetBrainsMono.getValue().getHeight() + 1;
            } else {
                yOffset2 += Naphthav2.jetBrainsMono.getValue().getHeight() + 1;
            }
            if(split.isToggled()) {
                mainCol = !mainCol;
            }
        }
        return false;
    }

    public boolean drawLine() {
        //list.sort((a, b) -> Integer.compare(getValue(b), getValue(a))); largest -> smallest
        List<Module> modules = new ArrayList<>(Naphthav2.INSTANCE.moduleManager.getToggleModules());
        sort(modules);
        double width = Naphthav2.jetBrainsMono.getValue().getStringWidth(modules.get(0).getName());
        boolean mainCol = true;
        float yOffset1 = 0;
        float yOffset2 = 0;
        ScaledResolution sr = new ScaledResolution(mc);
        for(Module m : modules) {
            if(!m.isToggled() && !pinned.contains(m)) continue;
            if(hidden.contains(m)) continue;
            String t = "§z" + (this.lowercase.isToggled() ? this.getArrayListText(m, mainCol).toLowerCase() : this.getArrayListText(m, mainCol));
            Naphthav2.jetBrainsMono.getValue().drawString(
                    t,
                    (mainCol ? mainPos.getValue().getKey() : pos2.getValue().getKey()) * ((double) sr.getScaledWidth() / (2 * ((double) 1 / sr.getScaleFactor()))) - (mainCol ? Naphthav2.jetBrainsMono.getValue().getStringWidth(t) + 5 : -5),
                    (float) ((mainCol ? mainPos.getValue().getValue() : pos2.getValue().getValue()) * ((double) sr.getScaledHeight() / (2 * ((double) 1 / sr.getScaleFactor()))) + (mainCol ? yOffset1 : yOffset2)) + 2,
                    0xffffffff
            );
            if (mainCol) {
                yOffset1 += Naphthav2.jetBrainsMono.getValue().getHeight() + 1;
            } else {
                yOffset2 += Naphthav2.jetBrainsMono.getValue().getHeight() + 1;
            }
            if(split.isToggled()) {
                mainCol = !mainCol;
            }
        }
        //Shaders.setupChroma();
        Shaders.setupChroma();
        glDisable(GL_TEXTURE_2D);
        glLineWidth(4);
        double x1, y1;
        GL11.glBegin(GL11.GL_LINES);
        x1 = mainPos.getValue().getKey();
        y1 = mainPos.getValue().getValue();
        x1 = x1 * ((double) sr.getScaledWidth() / (2 * ((double) 1 / sr.getScaleFactor())))-1;
        y1 = y1 * ((double) sr.getScaledHeight() / (2 * ((double) 1 / sr.getScaleFactor())));
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x1, y1 + yOffset1);
        GL11.glEnd();
        if(split.isToggled()) {
            GL11.glBegin(GL11.GL_LINES);
            double x2, y2;
            x2 = pos2.getValue().getKey();
            y2 = pos2.getValue().getValue();
            x2 = x2 * ((double) sr.getScaledWidth() / (2 * ((double) 1 / sr.getScaleFactor())))+1;
            y2 = y2 * ((double) sr.getScaledHeight() / (2 * ((double) 1 / sr.getScaleFactor())));
            GL11.glVertex2d(x2, y2);
            GL11.glVertex2d(x2, y2 + yOffset2);
            GL11.glEnd();
        }
        glEnable(GL_TEXTURE_2D);
        glLineWidth(1);
        GL20.glUseProgram(0);
        return false;
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void renderPlayerInfo(RenderGameOverlayEvent.Post event) {
        this.draw();
    }

    @Override
    public boolean draw() {
        GL11.glPushMatrix();
        ScaledResolution sr = new ScaledResolution(mc);
        GL11.glScaled(2 * ((double) 1 /sr.getScaleFactor()),2 * ((double) 1 / sr.getScaleFactor()),0);
        switch (this.style.getValue()) {
            case "Basic":
                drawBasic();
                break;
            case "Line":
                drawLine();
                break;
            case "Athenaware":
                break;
            case "CMH":
                break;
        }
        GL11.glPopMatrix();
        return false;
    }

    @Override
    public void setPosition(double x, double y) {

    }

    @Override
    public void toDefault() {

    }

}
