package seraph.base.Map.Gui.settings.impl;

import seraph.base.Map.Gui.settings.Changeable;
import seraph.base.Map.Gui.settings.Setting;
import seraph.base.Map.Gui.settings.TextInput;

public class Text extends Setting<String> implements TextInput, Changeable<String> {

    private String defaultVal;

    public Text(String name, String description){
        this(name, description, "");
    }

    public Text(String name, String description, String defaultVal){
        super(name,description);
        this.setValueConstructor(defaultVal);
        this.defaultVal = defaultVal;
    }


    @Override
    public void wipeConfig() {
        this.setValue(this.getDefaultVal());
    }

    @Override
    public String getDefaultVal() {
        return this.defaultVal;
    }
}
