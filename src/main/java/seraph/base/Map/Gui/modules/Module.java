package seraph.base.Map.Gui.modules;

import seraph.base.Map.Gui.settings.impl.DropDown;
import seraph.base.Naphthav2;
import seraph.base.Map.Gui.Category;
import seraph.base.Map.Gui.SubCategory;
import seraph.base.Map.Gui.Toggleable;
import seraph.base.Map.Gui.settings.Setting;
import seraph.base.Map.Gui.settings.impl.Keybind;

public abstract class Module extends NonToggleModule implements Toggleable {

    private boolean toggled = false;
    private boolean hidden = false;

    public void hide(final boolean state) {this.hidden = state;}

    @Override
    public boolean isToggled(){
        return this.toggled;
    }

    public Module(String name, String description, Category category, SubCategory subCategory, Setting<?>... settings) {
        super(name, description, category, subCategory);
        this.addSettings(new Keybind<Module>("Keybind","",this));
        this.addSettings(settings);
    }
    public Module(String name, String description, Category category, SubCategory subCategory) {
        super(name, description, category, subCategory);
        this.addSettings(new Keybind<Module>("Keybind","",this));
    }
    public Module(String name, String description, Category category, SubCategory subCategory, int keybind, Setting<?>... settings ) {
        super(name, description, category, subCategory);
        this.addSettings(new Keybind<Module>("Keybind","",this,keybind));
        this.addSettings(settings);
    }
    public Module(String name, String description, Category category, SubCategory subCategory, int keybind) {
        super(name, description, category, subCategory);
        this.addSettings(new Keybind<Module>("Keybind","",this,keybind));
    }

    public void onEnable(){}

    public void onDisable(){}

    @Override
    public void toggle(){
        this.toggled = !this.toggled;
        this.onToggle();
    }

    private void onToggle(){
        if(toggled){
            Naphthav2.register(this);
            this.onEnable();
        }else{
            Naphthav2.unregister(this);
            this.onDisable();
        }
    }

    @Override
    public void toggle(boolean bool) {
        this.toggled = bool;
        this.onToggle();
    }

    public void setToggleNoFlag(boolean state) {
        this.toggled = state;
        if(toggled){
            Naphthav2.register(this);
        }else{
            Naphthav2.unregister(this);
        }
    }

    public String getArrayListText(){
        DropDown option = this.getDropDownOption();
        if(option == null) return "";
        return option.getValue();
    }
}
