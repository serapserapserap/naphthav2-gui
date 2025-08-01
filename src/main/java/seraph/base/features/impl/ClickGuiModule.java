package seraph.base.features.impl;

import seraph.base.Map.Gui.Category;
import seraph.base.Map.Gui.GraphicalUserInterface;
import seraph.base.Map.Gui.GuiscreenWrapper;
import seraph.base.Map.Gui.SubCategory;
import seraph.base.Map.Gui.graphicaluserinterfaces.PanelClickGui;
import seraph.base.Map.Gui.modules.Module;
import seraph.base.Map.Gui.modules.ModuleClass;
import seraph.base.Map.Gui.settings.impl.*;
import seraph.base.Naphthav2;

import static seraph.base.Naphthav2.mc;
@ModuleClass
public class ClickGuiModule extends Module {
    //public final PosSetting panelPos = new PosSetting("panel",.5,.5 );
    public final Slider scrollAmnt = new Slider("Scroll Amount", "Control how fast the gui scrolls", 1D, 16D, 8D);
    private final DropDown style = new DropDown("Gui Style", "Change the gui style", new String[]{"DropDown","Panel"});
    private final Toggle readInInv = new Toggle("Check from inv","");
    public final Text modName = new Text("Client Name", "Name of the client (& for colours, {0} for module name, {1} for type)", "&d[&9naphthav2&d/&9{0}&d/&9{1}&d]");
    public final Slider colourWaveSpeed = new Slider("Colour Speed", "Speed of the colour wave", 0.25D, 4D, .5D, 2);
    public final Slider colourWaveFrequency = new Slider("Colour Wave Frequency", "Frequency of the colour wave", 1D, 10D, 5D, 2);

    public ClickGuiModule(){
        super(
                "Click Gui",
                "Configure the gui",
                Category.TWO,
                SubCategory.GUI,
                0x36
        );
        this.addSettings(
                style,
                readInInv,
                modName,
                new Button("Edit Hud Locations", "Modify Hud locations", () -> Naphthav2.INSTANCE.moduleManager.configHud()),
                scrollAmnt,
                colourWaveSpeed,
                colourWaveFrequency
        );
    }

    public boolean shouldCheckFromInv() {
        return this.readInInv.getValue();
    }

    private GraphicalUserInterface getStyle(){
        //Naphthav2.print(System.getProperty("os.name"));
        switch (style.getValue().toLowerCase()){
            case "dropdown"://throw illegal state exception if you try to enable dropdown on mac
                return new PanelClickGui();
            case "panel":
                return new PanelClickGui();
            default:
                throw Naphthav2.logger.writeError(new RuntimeException("invalid clickgui config"));
        }
    }

    @Override
    public void onEnable(){
        mc.displayGuiScreen(new GuiscreenWrapper(this.getStyle()));
        this.toggle(false);
    }
}
