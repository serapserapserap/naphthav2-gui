package seraph.base.Map.Gui.settings.impl;

import seraph.base.Map.Gui.modules.Module;
import seraph.base.Map.Gui.modules.NonToggleModule;
import seraph.base.Map.Gui.settings.Changeable;
import seraph.base.Map.Gui.settings.Setting;
import seraph.base.Naphthav2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubModule<T extends NonToggleModule> extends Setting<List<T>> implements Changeable<List<T>> {
    private final Class<T> clazz;
    public SubModule(String name, String description, Class<T> clazz) {
        super(
                name,
                description
        );
        this.setValueConstructor(new ArrayList<>());
        this.clazz = clazz;
        try {
            T var0 = clazz.newInstance();
            for(Setting<?> var1 : var0.getSettings()) {
                if(var1 instanceof SubModule) {
                    throw Naphthav2.logger.writeError(
                            new RuntimeException(
                                    new IllegalStateException(
                                            "Sub-Module Setting's T Module Contained a Sub-Module Setting"
                                    )
                            )
                    );
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw Naphthav2.logger.writeError(new RuntimeException(e));
        }
    }

    public T addNew() {
        T newItem = null;
        try {
            newItem = clazz.newInstance();
            this.getValue().add(newItem);
        } catch (InstantiationException | IllegalAccessException e) {
            throw Naphthav2.logger.writeError(new RuntimeException(e));
        }
        return newItem;
    }

    public T remove(final int index) {
        return this.getValue().remove(index);
    }   

    @Override
    public Object getJsonValue() {
        List<Map<String, Object>> moduleData = new ArrayList<>();
        for (NonToggleModule module: this.getValue()) {
            if (module == null) continue;
            Map<String, Object> settingData = new HashMap<>();
            settingData.put("name", module.getName());
            Map<String, Object> settings = new HashMap<>();
            for (Setting<?> setting : module.getSettings()) {
                settings.put(setting.getName(), setting.getJsonValue());
            }
            settingData.put("settings", settings);

            if (module instanceof Module) {
                settingData.put("Enabled", ((Module)module).isToggled());
            }

            moduleData.add(settingData);
        }
        return moduleData;
    }

    @Override
    public void wipeConfig() {
        this.getValue().clear();
    }
}
