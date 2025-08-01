package seraph.base.Map.Gui.settings.impl;

import seraph.base.Map.Gui.Toggleable;
import seraph.base.Map.Gui.settings.Changeable;
import seraph.base.Map.Gui.settings.ExtraFeatureSetting;
import seraph.base.Map.Gui.settings.Setting;

public class Toggle extends ExtraFeatureSetting<Boolean,Boolean> implements Toggleable, Changeable<Boolean> {

    private Boolean defaultVal;

    public Toggle(String name, String description, Setting<?>[] settings, Boolean... values) {
        super(name, description, settings, values);
    }

    public Toggle(String name, String description){
        this(name, description, false);
    }
    public Toggle(String name, String description, Boolean defaultVal){
        super(name,description);
        this.setValueConstructor(defaultVal);
        this.defaultVal = defaultVal;
    }

    @Override
    public void toggle() {
        this.setValue(!this.getValue());
        this.onToggle();
    }

    public void onToggle(){
        if(this.getValue()){
            this.onEnable();
        }else{
            this.onDisable();
        }
    }

    public void onEnable(){}

    public void onDisable(){}

    @Override
    public void toggle(boolean bool) {
        this.setValue(bool);
        this.onToggle();
    }

    @Override
    public boolean isToggled() {
        if(!this.hasParent()) {
            return this.getValue();
        }

        if(this.getParent().isVisible(this)) {
            return this.getValue();
        }

        return false;
    }

    @Override
    public Boolean getDefaultVal() {
        return this.defaultVal;
    }

    @Override
    public void wipeConfig() {
        this.setValue(this.getDefaultVal());
    }
}
