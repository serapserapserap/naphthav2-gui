package seraph.base.Map.Gui.graphicaluserinterfaces;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import scala.actors.threadpool.Arrays;
import seraph.base.Map.Gui.Category;
import seraph.base.Map.Gui.GraphicalUserInterface;
import seraph.base.Map.Gui.GuiElementWrapper;
import seraph.base.Map.Gui.font.FentRenderer;
import seraph.base.Map.Gui.modules.Module;
import seraph.base.Map.Gui.modules.NonToggleModule;
import seraph.base.Map.Gui.settings.ExtraFeatureSetting;
import seraph.base.Map.Gui.settings.Setting;
import seraph.base.Map.Gui.settings.impl.*;
import seraph.base.Map.drawing.Drawer;
import seraph.base.Map.mc.skyblock.dungeonMap.scan.Pair;
import seraph.base.Map.sortingcomparitors.ArrayListSorters;
import seraph.base.Map.syshelpers.MouseHelper$1;
import seraph.base.Map.timing.Timer;
import seraph.base.Naphthav2;

import java.util.ArrayList;
import java.util.List;

import static seraph.base.Naphthav2.mc;

public class DropdownClickGui implements GraphicalUserInterface {

    private String hoveredDropDownSetting;

    final double LG = 0.237254902D;

    public static final String VALID_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()_+[]{}\\|;:'\",.<>/?~1234567890`-=~ ";

    private GuiElementWrapper hovered;
    private GuiElementWrapper lastHovered;

    private double[] lastPos = MouseHelper$1.getMouse();

    final double[] enabledColour = {0.696,0.861,1.000, 0.381,0.679,1.000};
    final double[] disabledColour = {.5,.5,.5, 0,0,0};
    private final Timer hoverTimer = new Timer(50);

    private int hoverTime;

    private static final List<GuiElementWrapper> collapsed = new ArrayList<>();
    @Override
    public void onOpen() {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.lastHovered = this.hovered;
        this.hovered = null;
        this.hoveredDropDownSetting = null;

        int offset = 0;

        for(Category category : Category.values()){
            renderCategory(category, Naphthav2.jetBrainsMono.getKey(),15 + offset, 15,75, 20, partialTicks);
            offset+= 90;
        }
        if(this.hovered == this.lastHovered && lastPos[0] == MouseHelper$1.getMouse()[0] && lastPos[1] == MouseHelper$1.getMouse()[1]){
            if(this.hoverTimer.isReady()) {
                this.hoverTime ++;
            }
        } else {
            this.hoverTime = 0;
        }
        if(this.hoverTime > 40 && hovered != null) {
            this.drawDescription(this.hovered,Naphthav2.jetBrainsMono.getValue());
        }
        lastPos = MouseHelper$1.getMouse();
    }

    @Override
    public void onMouseDrag(float mouseX, float mouseY, int mouseButton, long timeSinceLastClick) {

    }

    @Override
    public void onMouseScroll(int state) {

    }

    @Override
    public void onClick(float mouseX, float mouseY, int button) {//bro this is the only thing I hate abt java

        boolean leftClick = button == 0;

        if(this.hovered instanceof DropDown){

            if(!collapsed.contains(this.hovered)){

                if(!leftClick) collapsed.add(((Setting<?>) this.hovered));

            }else{
                if(this.hoveredDropDownSetting == null) return;
                DropDown dropDown = (DropDown) this.hovered;
                if(dropDown.getDropdownOptions().contains(this.hoveredDropDownSetting)) dropDown.setValue(this.hoveredDropDownSetting);
                collapsed.remove(dropDown);

            }
        }else if(this.hovered instanceof Toggle){

            ((Toggle)hovered).toggle();

        }else if(this.hovered instanceof Text){

            if(!collapsed.contains(this.hovered)){

                if(leftClick) collapsed.add(((Setting<?>) this.hovered));

            }else{

                if(leftClick) collapsed.remove(this.hovered);

            }
        }else if(this.hovered instanceof Keybind){

            if(!collapsed.contains(this.hovered)){

                if(leftClick) {
                    collapsed.add(((Setting<?>) this.hovered));
                    ((Keybind<?>) this.hovered).setValue(Keyboard.KEY_NONE);
                }


            }else{
                collapsed.remove(this.hovered);
                //TODO: mouse binds

            }
        }else if(this.hovered instanceof SelectAll){

            if(!collapsed.contains(this.hovered)){

                if(!leftClick) collapsed.add(((Setting<?>) this.hovered));

            }else{

                if(leftClick && this.hoveredDropDownSetting != null)((SelectAll)this.hovered).clickedOption(this.hoveredDropDownSetting);
                if(!leftClick && this.hoveredDropDownSetting == null) collapsed.remove(this.hovered);

            }
        } else if (this.hovered instanceof Button) {

            if(leftClick) {

                ((Button)this.hovered).getValue().run();

            }

        } else if (this.hovered instanceof MapOption) {

            if(leftClick) {

                ((MapOption)this.hovered).addOption();

            }

        } else if (this.hovered instanceof MapOption.MapPair && ((MapOption.MapPair<?, ?>)this.hovered).setting instanceof MapOption) {

            if(leftClick) {
                if(!collapsed.contains(this.hovered)) {
                    collapsed.removeIf(element -> element instanceof MapOption.MapPair);
                    collapsed.add(this.hovered);
                } else {
                    collapsed.remove(this.hovered);
                }
            } else {
                MapOption.MapPair<Integer, Boolean> pair = (MapOption.MapPair<Integer, Boolean>)this.hovered;
                if(pair.getValue()) {
                    collapsed.remove(this.hovered);
                    collapsed.remove(new MapOption.MapPair<Integer, Boolean>(pair.setting, pair.getKey(), false));
                    ((MapOption)pair.setting).getValue().remove((int) pair.getKey());
                }
            }

        } else if(this.hovered instanceof NonToggleModule){

            if(leftClick){

                if(this.hovered instanceof Module){

                    ((Module)hovered).toggle();

                }

            } else {

                ((NonToggleModule)this.hovered).dropDown();

            }
        } else if(this.hovered instanceof Category){

            if(!leftClick) {

                ((Category)this.hovered).dropDown();

            }
        }
    }

    @Override
    public void onRelease(int mouseX, int mouseY, int state) {

    }

    @Override
    public boolean onKeyTyped(char character, int keyCode) {
        List<GuiElementWrapper> var0 = new ArrayList<>(collapsed);
        for(GuiElementWrapper setting : var0) {
            if(setting instanceof Text) {
                ((Text)setting).setValue(
                        ((Text)setting).handleTyped(
                                ((Text)setting).getValue(), character, keyCode
                        )
                );
            } else if (setting instanceof Keybind<?>) {
                ((Keybind<?>)setting).setValue(keyCode);
                collapsed.remove(setting);
            } else if (setting instanceof MapOption.MapPair && ((MapOption.MapPair<?,?>)setting).setting instanceof MapOption) {
                MapOption option = (MapOption) ((MapOption.MapPair) setting).setting;
                if(((MapOption.MapPair<Integer, Boolean>)setting).getValue()) {
                    option.getValue().get(
                            ((MapOption.MapPair<Integer, Boolean>) setting).getKey()
                    ).setKey(
                            option.handleTyped(
                                    option.getValue().get(
                                            ((MapOption.MapPair<Integer, Boolean>) setting).getKey()
                                    ).getKey(),
                                    character,
                                    keyCode
                            )
                    );
                } else {
                    option.getValue().get(
                            ((MapOption.MapPair<Integer, Boolean>) setting).getKey()
                    ).setValue(
                            option.handleTyped(
                                    option.getValue().get(
                                            ((MapOption.MapPair<Integer, Boolean>) setting).getKey()
                                    ).getValue(),
                                    character,
                                    keyCode
                            )
                    );
                }
            }
        }
        return true;
    }

    public static boolean isMouseOver(double x, double y, double xSize, double ySize){
        double scale = new ScaledResolution(mc).getScaleFactor();
        double mouseX = Mouse.getX() / scale;
        double mouseY = (mc.displayHeight - Mouse.getY()) / scale;
        return mouseX >= x && mouseY >= y && mouseX < x + xSize && mouseY < y + ySize;
    }

    public void renderCategory(Category category, FentRenderer fr, double x, double y, double xSize,double ySize,double partialTicks){
        List<NonToggleModule> toRender = new ArrayList<>();
        for(NonToggleModule module : Naphthav2.INSTANCE.moduleManager.getAllModules()){
            if(module.getCategory() != category) continue;
            toRender.add(module);
        }

        double offset = 0;

        ArrayListSorters.sort(toRender);

        offset += this.drawCategory(category,x,y,xSize,ySize,fr);
        if(!category.droppedDown) return;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        for(NonToggleModule module : toRender) {
            this.drawModuleBox(module, x, y, xSize, ySize * .66f, offset,fr);
            offset += ySize * .66f;
            offset = this.drawModuleSettings(module,Naphthav2.jetBrainsMono.getValue(), x,y,xSize,ySize,offset);
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
    //c1 - 0.0,0.0,1.000
    //c2 - 0.381,0.679,1.000

    private double drawCategory(Category category, double x, double y, double xSize, double ySize, FentRenderer fr) {
        GL11.glColor4f(0,0,1,1);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        if(Drawer.drawShadedRect2D(
                x,
                y,
                xSize,
                ySize,
                new float[]{0.0f,0.0f,1.000f},
                new float[]{0.381f,0.679f,1.000f}
        )) {
            this.hovered = category;
        }

        GL11.glColor4f(1,1,1,1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        Drawer.drawCentredString(fr, category.name().toLowerCase(), x + xSize / 2, y + ySize / 2,1,0xffffff);
        return ySize;
    }

    private double drawDropDown(double offset, Setting<?> setting, FentRenderer fr, double x, double y, double xSize, double ySize){

        DropDown dropDown = ((DropDown)setting);

        List<String> johnFortnite = dropDown.getDropdownOptions();
        ArrayListSorters.sortList(johnFortnite);

        boolean flag = collapsed.contains(setting) && dropDown.getDropdownOptions().size() >= 2;

        GL11.glColor3d(LG,LG,LG);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        double height = 0;
        double buffer2 = 0;
        final double originalYsize = ySize;

        if(flag) {
            double buffer = 1;
            height = (Drawer.getScaledTextHight(fr,.5f) + buffer) * dropDown.getDropdownOptions().size() + buffer;
            buffer2 = ( (y + ySize / 2) - Drawer.getScaledTextHight(fr, .5f) * .5f + 1) - y;
            buffer2/=2;
            ySize = buffer2 * 2 + height;
        }

        Drawer.drawShadedRect2D(
                x,
                y + offset,
                xSize,
                ySize,
                new float[]{.7f,.7f,.7f},
                new float[]{(float) LG, (float) LG, (float) LG}
        );

        this.enableTex();

        Drawer.drawString(fr,setting.getName(),x + 5, y + originalYsize / 2 + offset, .5f, 0xffffff);

        this.disableTex();

        if(flag) {
            GL11.glColor3d(LG -.1f, LG-.1f, LG-.1f);
            double width = Drawer.getLargestFontWidth(dropDown.getDropdownOptions(), fr, .5f) + 2;
            if(Drawer.drawShadedRect2D(
                    x + xSize - 2 - width,
                    y + offset + buffer2,
                    width,
                    height,
                    new float[]{(float) LG - .1f, (float) LG -.1f, (float) LG - .1f},
                    new float[]{.5f, .5f, .5f}
            )) {
               this.hovered = dropDown;
            }
            final double incriment = Drawer.getScaledTextHight(fr,.5f) + 1;
            double offset2 = 0;
            this.enableTex();
            for(String s : dropDown.getDropdownOptions()) {
                double ex = x + xSize - 3 - Drawer.getScaledTextWidth(fr,s,.5f);
                double why = y + offset + originalYsize / 2 + offset2 + 1;

                Drawer.drawString(
                        fr,
                        s,
                        ex,
                        why,
                        .5f,
                        0xffffff
                );

                if(isMouseOver(ex, why - 2, Drawer.getScaledTextWidth(fr, s, .5f),Drawer.getScaledTextHight(fr, .5f))) {
                    this.hoveredDropDownSetting = s;
                }

                offset2 += incriment;
            }
            this.disableTex();

        } else {
            if(this.drawBoxWithText(
                    fr,
                    x + xSize - 2,
                    y + offset + ySize /2,
                    dropDown.getValue(),
                    .5f,
                    1,
                    new float[] {(float) (LG - 0.1F), (float) (LG - 0.1F), (float) (LG - 0.1F)}
            )) {
                this.hovered = dropDown;
            }
        }

        offset += ySize;

        return offset;
    }

    private List<String> getOrBlank(List<String> s) {
        return s.isEmpty() ? Arrays.asList(new String[]{"           "}) : s;
    }

    private double drawSelectAll(double offset, Setting<?> setting, FentRenderer fr, double x, double y, double xSize, double ySize){

        GL11.glColor3d(LG,LG,LG);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        SelectAll selectAll = (SelectAll) setting;

        if(Drawer.drawShadedRect2D(
                x,
                y + offset,
                xSize,
                ySize,
                new float[]{.7f,.7f,.7f},
                new float[]{(float) LG, (float) LG, (float) LG}
        )) {
            this.hovered = setting;
        }

        this.enableTex();
        Drawer.drawString(fr,setting.getName(),x + 5, y + ySize / 2 + offset, .5f, 0xffffff);
        offset += ySize;
        this.disableTex();

        if(collapsed.contains(setting)) {
            offset = drawList("Selected", selectAll.getValue(), fr, offset, x, y, xSize, ySize,setting);
            offset = drawList("Deselected", selectAll.getDropDownOptions(), fr, offset, x,y,xSize,ySize,setting);
        }

        return offset;
    }

    private double drawList(String name, List<String> s, FentRenderer fr, double offset, double x, double y, double xSize, double ySize, Setting<?> setting) {
        s = getOrBlank(s);
        GL11.glColor3d(LG,LG,LG);
        this.disableTex();
        boolean multipleOptions = s.size() > 1;
        double originalSize = ySize;
        double buffer2 = 0;
        double height = 0;

        if(multipleOptions){
            double buffer = 1;
            height = (Drawer.getScaledTextHight(fr,.5f) + buffer) * s.size() + buffer;
            buffer2 = ( (y + ySize / 2) - Drawer.getScaledTextHight(fr, .5f) * .5f + 1) - y;
            buffer2/=2;
            ySize = buffer2 * 2 + height;
        }

        if(Drawer.drawShadedRect2D(
                x,
                y + offset,
                xSize,
                ySize,
                new float[]{.7f,.7f,.7f},
                new float[]{(float) LG, (float) LG, (float) LG}
        )) {
            this.hovered = setting;
        }

        this.enableTex();
        Drawer.drawString(fr,name,x + 5, y + ySize / 2 + offset, .5f, 0xffffff);
        this.disableTex();
        if(multipleOptions) {
            final double incriment = Drawer.getScaledTextHight(fr,.5f) + 1;
            double offset2 = 0;
            double width = Drawer.getLargestFontWidth(s, fr, .5f) + 2;
            GL11.glColor3d(LG - 0.1F,LG - 0.1F,LG - 0.1F);
            Drawer.drawShadedRect2D(
                    x + xSize - 2 - width,
                    y + offset + buffer2,
                    width,
                    height,
                    new float[]{(float) LG - .1f, (float) LG -.1f, (float) LG - .1f},
                    new float[]{.5f, .5f, .5f}
            );
            this.enableTex();
            for(String string : s) {
                double ex = x + xSize - 3 - Drawer.getScaledTextWidth(fr,string,.5f);
                double why = y + offset + originalSize / 2 + offset2 + 1;

                Drawer.drawString(
                        fr,
                        string,
                        ex,
                        why,
                        .5f,
                        0xffffff
                );

                if(isMouseOver(ex, why - 2, Drawer.getScaledTextWidth(fr, string, .5f),Drawer.getScaledTextHight(fr, .5f))) {
                    this.hoveredDropDownSetting = string;
                }

                offset2 += incriment;
            }
            this.disableTex();

        } else {
            this.enableTex();
            if(this.drawBoxWithText(
                    fr,
                    x + xSize - 2,
                    y + offset + ySize / 2,
                    s.get(0),
                    .5f,
                    1,
                    new float[]{(float) (LG - 0.1F), (float) (LG - 0.1F), (float) (LG - 0.1F)}
            )) {
                if(!s.get(0).equals("           ")) this.hoveredDropDownSetting = s.get(0);
            }
            this.disableTex();
        }
        offset += ySize;



        return offset;

    }

    private double drawToggle(double offset, Setting<?> setting,FentRenderer fr, double x, double y, double xSize, double ySize){

        Toggle toggle = (Toggle) setting;

        GL11.glColor3d(LG,LG,LG);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        Drawer.drawShadedRect2D(
                x,
                y + offset,
                xSize,
                ySize,
                new float[]{.7f,.7f,.7f},
                new float[]{(float) LG, (float) LG, (float) LG}
        );

        this.enableTex();

        Drawer.drawString(fr,setting.getName(),x + 5, y + ySize / 2 + offset, .5f, 0xffffff);

        this.disableTex();

        GL11.glColor4d(0.137254902D,0.137254902D,0.137254902D,1);


        double BIG_WIDTH = 7;
        double SMALL_WIDTH = 5;

        if(Drawer.drawShadedRect2D(
                x + xSize - 2 - BIG_WIDTH,
                y + offset + ySize / 2 - BIG_WIDTH / 2,
                BIG_WIDTH,
                BIG_WIDTH,
                new float[]{(float) LG - .1f, (float) LG -.1f, (float) LG - .1f},
                new float[]{.5f, .5f, .5f}
        )) {
            this.hovered = setting;
        }

        if(toggle.getValue()) {
            GL11.glColor3d(1,1,1);
            Drawer.drawShadedRect2D(
                    x + xSize - 2 - BIG_WIDTH + 1,
                    y + offset + ySize / 2 - BIG_WIDTH / 2 + 1,
                    SMALL_WIDTH,
                    SMALL_WIDTH,
                    new float[]{0.0f,0.0f,1.000f},
                    new float[]{0.381f,0.679f,1.000f}
            );
        }

        return ySize + offset;
    }


    private boolean fitsInTextBox(String string, FentRenderer fr, double xSize, double scale){
        return Drawer.getScaledTextWidth(fr,string,scale) < xSize;
    }

    private double drawText(FentRenderer fr, double offset, Setting<?> setting, double x, double y, double xSize, double ySize){

        String string = this.displayText((Text) setting,xSize,.5f,fr);

        boolean flag = collapsed.contains(setting);

        GL11.glColor3d(LG,LG,LG);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        Drawer.drawShadedRect2D(
                x,
                y + offset,
                xSize,
                ySize,
                new float[]{.7f,.7f,.7f},
                new float[]{(float) LG, (float) LG, (float) LG}
        );

        this.enableTex();

        Drawer.drawString(fr,setting.getName(),x + 5, y + ySize / 2 + offset, .5f, 0xffffff);

        this.disableTex();

        if(flag) {
            GL11.glColor3d(1,1,1);
            this.drawBoxWithText(
                    fr,
                    x + xSize - 1,
                    y + offset + ySize /2,
                    string,
                    .5f,
                    2,
                    new float[] {1,1,1}
            );
        }

        if(this.drawBoxWithText(
                fr,
                x + xSize - 2,
                y + offset + ySize /2,
                string,
                .5f,
                1,
                new float[] {(float) (LG - 0.1F), (float) (LG - 0.1F), (float) (LG - 0.1F)}
        )) {
            this.hovered = setting;
        }

        return offset + ySize;
    }

    private double drawSlider(Slider keybind, FentRenderer fr,double x, double y, double xSize, double ySize, double offset){
        GL11.glColor3d(LG,LG,LG);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        //Drawer.drawShadedRect2D(Shaders.celNoise, 0, 0, mc.displayWidth, mc.displayHeight);
        Drawer.drawShadedRect2D(
                x,
                y + offset,
                xSize,
                ySize,
                new float[]{.7f,.7f,.7f},
                new float[]{(float) LG, (float) LG, (float) LG}
        );

        this.enableTex();

        Drawer.drawString(fr,keybind.getName() + ": " + keybind.getValue(),x + 5, y + ySize / 2 + offset, .5f, 0xffffff);
        final double w = Drawer.getScaledTextWidth(fr,keybind.getName() + ": " + keybind.getMax(),.5f) + Drawer.getScaledTextWidth(fr, "0", .5f) * Math.max(keybind.decimalPlaces - 1, 0);
        this.disableTex();
        this.colour(new float[] {(float) (LG - 0.1F), (float) (LG - 0.1F), (float) (LG - 0.1F)});
        double width = xSize - w - 10;
        if(Drawer.drawShadedRect2D(x + 7.5 + w, y + ySize / 2 + offset - 1, width ,2,
                new float[]{(float) LG - .1f, (float) LG -.1f, (float) LG - .1f},
                new float[]{.5f, .5f, .5f}
        ) || (Mouse.isButtonDown(0) && this.lastHovered != null && this.lastHovered.equals(keybind) && isMouseOver(0, 0 ,5000, 5000))) {
            this.hovered = keybind;
            if(Mouse.isButtonDown(0)) {
                double scale = new ScaledResolution(mc).getScaleFactor();
                double mouseX = Mouse.getX() / scale;
                double ratio = Math.min(Math.max (0, (mouseX - x - w - 7.5f) / (width)),1);
                double val = keybind.getMax() * ratio;
                keybind.setValue(val);

            }
        }
        this.colour(new float[]{1,1,1});
        Drawer.drawShadedRect2D(x + 7.5 + w, y + ySize / 2 + offset - 1, width * keybind.getFract(), 2,
                new float[]{0.0f,0.0f,1.000f},
                new float[]{0.381f,0.679f,1.000f});
        return offset + ySize;
    }

    private void enableTex(){
        GL11.glColor3d(1,1,1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private void disableTex() {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    private void colour(float[] foat) {
        GL11.glColor3d(foat[0],foat[1],foat[2]);
    }

    private boolean drawBoxWithText(FentRenderer fr, double farRightX, double centreY, String text, double textScale, double buffer, float[] colour) {
        this.disableTex();
        this.colour(colour);
        double xSize = Drawer.getScaledTextWidth(fr, text, textScale) + buffer * 2;
        double ySize = Drawer.getScaledTextHight(fr, textScale) + buffer * 2;
        //(farRightX - (Drawer.getScaledTextWidth(fr, text, textScale) + buffer * 2)) + Drawer.getScaledTextWidth(fr, text, textScale) + buffer * 2;
        double y = centreY - ySize / 2;
        double x = farRightX - xSize;
        boolean var0 = Drawer.drawShadedRect2D(
                x,
                y,
                xSize,
                ySize,
                new float[] {colour[0], colour[1], colour[2]},
                new float[] {.5f, .5f, .5f}
        );

        this.enableTex();

        Drawer.drawCentredString(fr,text, x + xSize / 2, centreY + buffer / 2,textScale, 0xffffff);

        this.disableTex();

        return var0;
    }

    private void drawKeybind(Keybind<?> keybind, FentRenderer fr,double x, double y, double xSize, double ySize, double offset){
        GL11.glColor3d(LG,LG,LG);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        Drawer.drawShadedRect2D(
                x,
                y + offset,
                xSize,
                ySize,
                new float[]{.7f,.7f,.7f},
                new float[]{(float) LG, (float) LG, (float) LG}
        );

        this.enableTex();

        Drawer.drawString(fr,keybind.getName(),x + 5, y + ySize / 2 + offset, .5f, 0xffffff);


        if(this.drawBoxWithText(
                fr,
                x + xSize - 2,
                y + offset + ySize /2,
                "[" + keybind.getNiceKeyName() + "]",
                .5f,
                1,
                new float[] {(float) (LG - 0.1F), (float) (LG - 0.1F), (float) (LG - 0.1F)}
        )) {
            this.hovered = keybind;
        }

        this.disableTex();
    }

    private void drawModuleBox(NonToggleModule module, double x, double y, double xSize, double ySize, double offset,FentRenderer fentRnederer){
        double[] colour = module instanceof Module && ((Module)module).isToggled() ? enabledColour : disabledColour;

        GL11.glColor4d(colour[0],colour[1],colour[2], 1.f);

        if(Drawer.drawShadedRect2D(
                x,
                y + offset,
                xSize,
                ySize,
                new float[]{(float) colour[0], (float) colour[1], (float) colour[2]},
                new float[]{(float) colour[3], (float) colour[4], (float) colour[5]}

        )) {
            this.hovered = module;
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glColor4d(1,1,1,1);

        Drawer.drawCentredString(fentRnederer, module.getName(), x + xSize / 2, y + offset + ySize / 2,.65f,0xffffff);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    private void drawDescription(GuiElementWrapper hovered, FentRenderer fr) {
        if(hovered.getDescription() == null) return;
        double width = Drawer.getScaledTextWidth(fr, hovered.getDescription(),.5f);
        double height = Drawer.getScaledTextHight(fr, .5f);
        this.disableTex();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GL11.glColor4d(0,0,0, .75f);
        double[] m = MouseHelper$1.getMouse();
        Drawer.drawRectangle2D(
                m[0] + 4,
                m[1] - 1,
                width + 2,
                height + 2
        );

        this.enableTex();
        Drawer.drawString(fr,hovered.getDescription(),m[0] + 6,m[1] + Drawer.getScaledTextHight(fr,.5f) / 2 + 1,.5f,0xffffff);
    }

    private double drawModuleSettings(NonToggleModule module,FentRenderer fr, double x, double y, double xSize, double ySize, double offset){
        if(!module.droppedDown) return offset;

        for(Setting<?> setting : module.getSettings()){

            double settingHeight = ySize/2;

            if(setting instanceof Keybind) {
                this.drawKeybind((Keybind<seraph.base.Map.Gui.Toggleable>) setting,fr,x,y,xSize,settingHeight,offset);
                offset+= settingHeight;
            } else if (setting instanceof DropDown) {
                offset = this.drawDropDown(offset, setting, fr, x, y, xSize,settingHeight);
            } else if (setting instanceof SelectAll) {
                offset = this.drawSelectAll(offset, setting, fr, x ,y ,xSize,settingHeight);
            } else if (setting instanceof Text) {
                offset = this.drawText(fr, offset,setting,x,y,xSize,settingHeight);
            } else if (setting instanceof Toggle) {
                offset = this.drawToggle(offset,setting,fr,x,y,xSize,settingHeight);
            } else if (setting instanceof Slider) {
                offset = this.drawSlider((Slider) setting,fr,x,y,xSize,settingHeight,offset);
            } else if (setting instanceof Button) {
                offset = this.drawButton(offset,setting,fr,x,y,xSize,settingHeight);
            } else if(setting instanceof MapOption) {
                offset = this.drawMapOption(offset,setting,fr,x,y,xSize,settingHeight);
            }

            if(setting instanceof ExtraFeatureSetting) {

                for(Setting<?> subSetting : ((ExtraFeatureSetting<?, ?>)setting).getVisible()) {

                    if(subSetting instanceof Keybind) {
                        this.drawKeybind((Keybind<seraph.base.Map.Gui.Toggleable>) subSetting,fr,x,y,xSize,settingHeight,offset);
                        offset+= settingHeight;
                    } else if (subSetting instanceof DropDown) {
                        offset = this.drawDropDown(offset, subSetting, fr, x, y, xSize,settingHeight);
                    } else if (subSetting instanceof SelectAll) {
                        offset = this.drawSelectAll(offset, subSetting, fr, x ,y ,xSize,settingHeight);
                    } else if (subSetting instanceof Text) {
                        offset = this.drawText(fr, offset,subSetting,x,y,xSize,settingHeight);
                    } else if (subSetting instanceof Toggle) {
                        offset = this.drawToggle(offset,subSetting,fr,x,y,xSize,settingHeight);
                    } else if (subSetting instanceof Slider) {
                        offset = this.drawSlider((Slider) subSetting,fr,x,y,xSize,settingHeight,offset);
                    } else if (subSetting instanceof Button) {
                        offset = this.drawButton(offset,subSetting,fr,x,y,xSize,settingHeight);
                    } else if(subSetting instanceof MapOption) {
                        offset = this.drawMapOption(offset,subSetting,fr,x,y,xSize,settingHeight);
                    }

                }

            }

        }
        return offset;
    }

    private double drawMapOption(double offset, Setting<?> setting,FentRenderer fr, double x, double y, double xSize, double ySize) {
        GL11.glColor3d(LG,LG,LG);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        double sizeOffset = ((MapOption)setting).getValue().size() * ySize;

        Drawer.drawShadedRect2D(
                x,
                y + offset,
                xSize,
                ySize + sizeOffset,
                new float[]{.7f,.7f,.7f},
                new float[]{(float) LG, (float) LG, (float) LG}
        );


        this.enableTex();

        Drawer.drawString(fr,setting.getName(),x + 5, y + ySize / 2 + offset, .5f, 0xffffff);

        if(this.drawBoxWithText(
                fr,
                x + xSize - 2,
                y + offset + ySize /2,
                "add new",
                .5f,
                1,
                new float[] {(float) (LG - 0.1F), (float) (LG - 0.1F), (float) (LG - 0.1F)}
        )) {
            this.hovered = setting;
        }
        //((MapOption)setting).getValue()
        for(int i = 0; i < ((MapOption)setting).getValue().size(); i++) {
            Pair<String, String> pair = ((MapOption)setting).getValue().get(i);
            double centreY = y + offset + ySize / 2 + (i + 1) * ySize;
            if(this.drawBoxWithText(
                    fr,
                    x + xSize - 2,
                    centreY,
                    pair.getValue(),
                    .5f,
                    1,
                    new float[] {(float) (LG - 0.1F), (float) (LG - 0.1F), (float) (LG - 0.1F)}
            )) {
                this.hovered = new MapOption.MapPair<>(setting, i, false);
            }
            if(this.drawBoxWithText(
                    fr,
                    x + 2 + Drawer.getScaledTextWidth(fr, pair.getKey(), .5f) + 2,
                    centreY,
                    pair.getKey(),
                    .5f,
                    1,
                    new float[] {(float) (LG - 0.1F), (float) (LG - 0.1F), (float) (LG - 0.1F)}
            )) {
                this.hovered = new MapOption.MapPair<>(setting, i, true);
            }
        }

        this.disableTex();
        return ySize + offset + sizeOffset;
    }

    private double drawButton(double offset, Setting<?> setting,FentRenderer fr, double x, double y, double xSize, double ySize){

        GL11.glColor3d(LG,LG,LG);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        Drawer.drawShadedRect2D(
                x,
                y + offset,
                xSize,
                ySize,
                new float[]{.7f,.7f,.7f},
                new float[]{(float) LG, (float) LG, (float) LG}
        );

        this.enableTex();

        Drawer.drawString(fr,setting.getName(),x + 5, y + ySize / 2 + offset, .5f, 0xffffff);

        this.disableTex();

        GL11.glColor4d(0.137254902D,0.137254902D,0.137254902D,1);


        double BIG_WIDTH = 7;

        if(Drawer.drawShadedRect2D(
                x + xSize - 2 - BIG_WIDTH,
                y + offset + ySize / 2 - BIG_WIDTH / 2,
                BIG_WIDTH,
                BIG_WIDTH,
                new float[]{0.0f,0.0f,1.000f},
                new float[]{0.381f,0.679f,1.000f}
        )) {
            this.hovered = setting;
        }

        return ySize + offset;
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
}
