package seraph.base.Map.Gui.modules;

import seraph.base.Map.Gui.Category;
import seraph.base.Map.Gui.GuiElementWrapper;
import seraph.base.Map.Gui.SubCategory;
import seraph.base.Map.Gui.settings.Setting;
import seraph.base.Map.Gui.settings.impl.DropDown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public abstract class NonToggleModule implements GuiElementWrapper {
    private String name;
    private final String description;
    private final Category category;
    private final SubCategory subCategory;
    private final List<Setting<?>> settings;

    public void dropDown(){
        this.droppedDown = !this.droppedDown;
    }

    public boolean droppedDown = false;

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public void setName(String var0) { this.name = var0; }

    public Category getCategory(){
        return this.category;
    }

    public SubCategory getSubCategory(){
        return this.subCategory;
    }

    public List<Setting<?>> getSettings(){
        return this.settings;
    }

    public NonToggleModule(String name, String description, Category category, SubCategory subCategory){
        this.name = name;
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
        this.settings = new ArrayList<>();
    }

    public NonToggleModule(String name, String description, Category category, SubCategory subCategory, Setting<?>... settings){
        this.name = name;
        this.description = description;
        this.category = category;
        this.subCategory = subCategory;
        this.settings = Arrays.asList(settings);
    }
    public void addSettings(Setting<?>... settings){
        this.settings.addAll(Arrays.asList(settings));
    }

    public void removeSettings(Setting<?>... settings){
        this.settings.removeAll(Arrays.asList(settings));
    }

    public DropDown getDropDownOption(){
        for(Setting<?> setting : this.settings){
            if(setting instanceof DropDown) return (DropDown) setting;
        }
        return null;
    }
}
