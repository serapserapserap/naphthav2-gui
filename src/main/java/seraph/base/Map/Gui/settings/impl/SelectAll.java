package seraph.base.Map.Gui.settings.impl;

import seraph.base.Map.Gui.settings.Changeable;
import seraph.base.Map.Gui.settings.ExtraFeatureSetting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectAll extends ExtraFeatureSetting<String, List<String>> implements Changeable<List<String>> {
    private final List<String> dropDownOptions;
    private final List<String> defaultVal;

    public List<String> getDropDownOptions(){
        return this.dropDownOptions;
    }

    public SelectAll(String name, String description,String... options){
        this(name, description, options, new String[]{});
    }

    public SelectAll(String name, String description,String[] options, String[] selected) {
        super(name, description);
        this.dropDownOptions = new ArrayList<>(Arrays.asList(options));
        this.setValueConstructor(Arrays.asList(selected));
        this.defaultVal = Arrays.asList(selected);
    }

    public void selectOption(String option){
        this.dropDownOptions.remove(option);
        List<String> var0 = this.getValue();
        var0.add(option);
        this.setValue(var0);
    }

    public void clickedOption(String option){
        if(option == null) return;
        if(this.dropDownOptions.contains(option)){
            selectOption(option);
        } else{
            deSelectOption(option);
        }
    }

    public void deSelectOption(String option){
        List<String> var0 = this.getValue();
        var0.remove(option);
        this.setValue(var0);
        this.dropDownOptions.add(option);
    }

    @Override
    public void wipeConfig() {
        this.dropDownOptions.addAll(this.getValue());
        this.getValue().clear();
        for(String s : this.getDefaultVal()) {
            this.clickedOption(s);
        }
    }

    @Override
    public List<String> getDefaultVal() {
        return this.defaultVal;
    }
}
