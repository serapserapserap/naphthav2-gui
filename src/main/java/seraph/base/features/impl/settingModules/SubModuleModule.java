package seraph.base.features.impl.settingModules;

import seraph.base.Map.Gui.modules.NonToggleModule;
import seraph.base.Map.Gui.settings.impl.Slider;
import seraph.base.Map.Gui.settings.impl.Toggle;

public class SubModuleModule extends NonToggleModule {
    public SubModuleModule() {
        super("example", "example", null, null, new Toggle("toggle - submodule", ""), new Slider("slider - submodule", "", 0D, 5D, 3D));
    }
}