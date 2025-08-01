package seraph.base.features.impl;

import seraph.base.Map.Gui.Category;
import seraph.base.Map.Gui.SubCategory;
import seraph.base.Map.Gui.modules.Module;
import seraph.base.Map.Gui.modules.ModuleClass;
import seraph.base.Map.Gui.settings.ExtraFeatureSetting;
import seraph.base.Map.Gui.settings.impl.*;
import seraph.base.features.impl.settingModules.SubModuleModule;

@ModuleClass
public class AllSettingsModule extends Module {
    public AllSettingsModule() {
        super(
                "All Settings",
                "all settings",
                Category.FOUR,
                SubCategory.GUI
        );

        DropDown dropDown = new DropDown("drop down", "drop down", new String[]{"setting 1", "setting 2", "setting 3", "setting 4"});
        SelectAll selectAll = new SelectAll("select all", "select all", "setting 1", "setting 2", "setting 3", "setting 4");
        Slider slider = new Slider("slider", "slider", 0D, 5D, 3D);
        Text text = new Text("text", "text", "exampleText");
        Toggle toggle = new Toggle("toggle", "toggle", false);
        MapOption map = new MapOption("map option", "map option", "key", "value");
        DropDown longNameDropDown = new DropDown("very long name drop down option for example", "", new String[]{
                "option",
                "longer option",
                "short",
                "a bit longer",
                "very long option"
        });
        DropDown anotherLongDropDown = new DropDown("ddddsa", "", new String[]{
                "option",
                "longer option",
                "short",
                "a bit longer",
                "very long option"
        });
        DropDown anotherLongDropDown2 = new DropDown("ddddsadddddd", "", new String[]{
                "option",
                "longer option",
                "short",
                "a bit longer",
                "very long option"
        });
        Slider longNameSlider = new Slider("very long name slider like holy this name is long", "", 0D, 5D, 3D);
        PosSetting pos = new PosSetting("blah", 0,0);
        SubModule<SubModuleModule> subModule = new SubModule<>(
                "sub module",
                "",
                SubModuleModule.class
        );


        toggle.addSettings(new ExtraFeatureSetting.settingSet<>(true, new Toggle("Toggle 2", "toggler",false)));

        this.addSettings(
                dropDown,
                selectAll,
                slider,
                text,
                toggle,
                map,
                longNameDropDown,
                pos,
                subModule,
                longNameSlider,
                anotherLongDropDown,
                anotherLongDropDown2
        );

    }


}
