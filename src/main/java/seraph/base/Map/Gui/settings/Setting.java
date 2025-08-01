package seraph.base.Map.Gui.settings;

import seraph.base.Map.Gui.GuiElementWrapper;

public abstract class Setting<T> implements GuiElementWrapper {
    private ExtraFeatureSetting<?, ?> parent = null;
    public Runnable lambdaOnLoadConfig = () -> {};
    private final String name;
    private final String description;
    private T value;


    private Setting<T> lambdaOnLoad(Runnable r) {
        this.lambdaOnLoadConfig = r;
        return this;
    };

    public T getValue(){
        return this.value;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }
    public ExtraFeatureSetting<?, ?> getParent() {
        return this.parent;
    }

    public Setting<T> setValue(T newVal){
        T oldVal = this.value;
        this.value = newVal;
        this.onValueChange(oldVal);
        return this;
    }

    public Setting<T> parent(ExtraFeatureSetting<?, ?> parent) {
        this.parent = parent;
        return this;
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public void onValueChange(T oldVal){}

    @Deprecated
    public Setting<T> setValueConstructor(T newVal){
        this.value = newVal;
        return this;
    }

    public boolean hasDescription(){
        return this.description.equalsIgnoreCase("");
    }

    public Setting(String name, String description){
        this.name = name;
        this.description = description;
    }

    public Object getJsonValue() {
        return this.getValue();
    }
}
