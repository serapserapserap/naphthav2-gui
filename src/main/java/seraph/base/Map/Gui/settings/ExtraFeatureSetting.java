package seraph.base.Map.Gui.settings;

import seraph.base.Map.mc.skyblock.dungeonMap.scan.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ExtraFeatureSetting<T,T1> extends Setting<T1> {
    public final HashMap<Setting<?>, T> settings;

    @SafeVarargs
    public ExtraFeatureSetting(String name, String description, Setting<?>[] settings, T... Value){
        super(name,description);
        this.settings = new HashMap<>();
        this.addSettings(settings,Value);
    }

    public ExtraFeatureSetting(String name,String description){
        super(name,description);
        this.settings = new HashMap<>();
    }

    @SafeVarargs
    public final void addSettings(Setting<?>[] settings, T... value){
        for(int i = 0; i < settings.length; i++){
            this.settings.put(settings[i].parent(this), value[i]);
        }
    }

    @SafeVarargs
    public final void removeSettings(Setting<?>[] settings, T... value){
        for(int i = 0; i < settings.length; i++){
            this.settings.remove(settings[i].parent(null), value[i]);
        }
    }

    public void addSettings(Setting<?>... settings){
        for(Setting<?> setting : settings){
            this.settings.put(setting,null);
        }
    }

    @SafeVarargs
    public final ExtraFeatureSetting<T,T1> addSettings(settingSet<T>... settingSets){
        for(settingSet<T> settingSet : settingSets){
            this.settings.put(settingSet.setting,settingSet.showValue);
        }
        return this;
    }

    public List<Setting<?>> getVisible() {
        List<Setting<?>> visible = new ArrayList<>();
        for (Setting<?> setting : this.settings.keySet()) {
            if (this.getValue() == this.settings.get(setting) || this.settings.get(setting) == null) {
                visible.add(setting);
            }
        }
        return visible;
    }

    public boolean isVisible(Setting<?> s) {
        return this.getValue() == this.settings.get(s) || this.settings.get(s) == null;
    }

    public static class settingSet<var0>{
        public var0 showValue;
        public Setting<?> setting;

        public settingSet(var0 showValue,Setting<?> setting){
            this.showValue = showValue;
            this.setting = setting;
        }
    }

    @Override
    public Object getJsonValue() {
        Map<String, Object> result = new HashMap<>();
        result.put("value", getValue());

        if (!this.settings.keySet().isEmpty()) {
            Map<String, Object> nested = new HashMap<>();
            for (Setting<?> setting : this.settings.keySet()) {
                nested.put(setting.getName(), setting.getJsonValue());
            }
            result.put("settings", nested);
        }

        return result;
    }

    public static List<Pair<Setting<?>, Integer>> flatten(ExtraFeatureSetting<?, ?> source) {
        List<Pair<Setting<?>, Integer>> result = new ArrayList<>();
        flatten(result, source, 1);
        return result;
    }

    public static List<Pair<Setting<?>, Integer>> flattenAll(ExtraFeatureSetting<?, ?> source) {
        List<Pair<Setting<?>, Integer>> result = new ArrayList<>();
        flattenAll(result, source, 1);
        return result;
    }

    private static void flattenAll(List<Pair<Setting<?>, Integer>> result, ExtraFeatureSetting<?, ?> source, int levelOfIndentation) {
        for(Setting<?> s : source.settings.keySet()) {
            result.add(new Pair<>(s,levelOfIndentation));
            if(s instanceof ExtraFeatureSetting<?, ?>)
                flattenAll(result, (ExtraFeatureSetting<?, ?>) s, levelOfIndentation + 1);

        }
    }

    private static void flatten(List<Pair<Setting<?>, Integer>> result, ExtraFeatureSetting<?, ?> source, int levelOfIndentation) {
        for(Setting<?> s : source.getVisible()) {
            result.add(new Pair<>(s,levelOfIndentation));
            if(s instanceof ExtraFeatureSetting<?, ?>)
                flatten(result, (ExtraFeatureSetting<?, ?>) s, levelOfIndentation + 1);

        }
    }
}
