package seraph.base.Map.Gui.settings.impl;

import seraph.base.Map.Gui.settings.Setting;

public class Button extends Setting<Runnable> {
    public Button(String name, String description, Runnable onClick) {
        super(name, description);
        this.setValueConstructor(onClick);
    }
    public Button(String name, String description, Runnable onClick, Runnable lambdaOnConfigLoad) {
        super(name, description);
        this.setValueConstructor(onClick);
        this.lambdaOnLoadConfig = lambdaOnConfigLoad;
    }
    public Button(String name, String description, Runnable onClick, boolean runOnLoadConfig) {
        super(name, description);
        this.setValueConstructor(onClick);
        if(runOnLoadConfig) this.lambdaOnLoadConfig = onClick;
    }

    @Override
    public Object getJsonValue() {
        return null;
    }
}
