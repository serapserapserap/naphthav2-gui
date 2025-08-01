package seraph.base.Map.Gui.settings.impl;

import seraph.base.Map.Gui.settings.Changeable;
import seraph.base.Map.Gui.settings.ExtraFeatureSetting;
import seraph.base.Map.Gui.settings.Setting;
import seraph.base.Map.sortingcomparitors.ArrayListSorters;

import java.util.Arrays;
import java.util.List;

/*
*This is the dropdown setting class
 */
public class DropDown extends ExtraFeatureSetting<String,String> implements Changeable<String> {
    private final List<String> dropdownOptions;
    private final String defaultVal;

    public List<String> getDropdownOptions(){
        return this.dropdownOptions;
    }

    public DropDown(String name, String description,String... settings) {
        super(name, description);
        this.dropdownOptions = Arrays.asList(settings);
        this.sortDropdownOptions();
        this.setValueConstructor(this.dropdownOptions.get(0));
        this.defaultVal = this.dropdownOptions.get(0);
    }

    public DropDown(String name, String description,String defaultVal,String... settings) {
        super(name, description);
        this.dropdownOptions = Arrays.asList(settings);
        this.sortDropdownOptions();
        this.setValueConstructor(defaultVal);
        this.defaultVal = defaultVal;
    }

    /*
    params:
    - name: module name
    - description: module description
    - settings: module settings
    - valueToShowSettings: the corrosponding mode that has to be active for the setting to show
    assign this to null if you want the setting to always show
    - options: all the options in the dropdown setting
     */
    public DropDown(String name, String description, Setting<?>[] settings, String[] valueToShowSetting,String... options){
        super(name,description,settings,valueToShowSetting);
        this.dropdownOptions = Arrays.asList(options);
        this.setValueConstructor(options[0]);
        this.defaultVal = options[0];
    }

    public DropDown(String name, String description,String defaultVal, Setting<?>[] settings, String[] valueToShowSetting,String... options){
        super(name,description,settings,valueToShowSetting);
        this.dropdownOptions = Arrays.asList(options);
        this.setValueConstructor(defaultVal);
        this.defaultVal = defaultVal;
    }

    public void sortDropdownOptions(){
        if(this.getValue() == null) return;
        this.dropdownOptions.remove(this.getValue());
        ArrayListSorters.sortList(this.dropdownOptions);
        this.dropdownOptions.add(0,this.getValue());
    }

    @Override
    public void wipeConfig() {
        this.setValue(this.defaultVal);
    }

    @Override
    public String getDefaultVal() {
        return this.defaultVal;
    }
}
