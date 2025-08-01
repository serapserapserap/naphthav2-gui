package seraph.base.Map.Gui.settings.impl;

import seraph.base.Map.Gui.GuiElementWrapper;
import seraph.base.Map.Gui.settings.Setting;
import seraph.base.Map.Gui.settings.TextInput;
import seraph.base.Map.mc.skyblock.dungeonMap.scan.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * use SubModule Instead
 */
@Deprecated
public class MapOption extends Setting<List<Pair<String, String>>> implements TextInput {
    private final String defaultVal;
    private final String defaultKey;
    public MapOption(String name, String description, String defaultKey, String defaultVal) {
        super(name, description);
        this.setValueConstructor(new ArrayList<>());
        this.defaultKey = defaultKey;
        this.defaultVal = defaultVal;
    }

    public void addOption(final int index) {
        this.getValue().add(index, new Pair<>("key" + index, defaultVal));
    }

    public void addOption() {
        this.getValue().add(new Pair<>("key" + this.getValue().size(), defaultVal));
    }

    public void modifyOption(final int index, final String key, final String value) {
        this.getValue().get(index).setKey(key).setValue(value);
    }

    public Map<String, String> asMap() {
        Map<String, String> map = new HashMap<>();
        for(Pair<String, String> pair: this.getValue()) {
            map.put(pair.getKey(), pair.getValue());
        }
        return map;
    }

    @Override
    public Object getJsonValue() {
        return this.asMap();
    }

    public static class MapPair<K,V> extends Pair<K,V> implements GuiElementWrapper {
        @Override
        public String getDescription() {
            return "";
        }

        public final Setting<?> setting;

        public MapPair(Setting<?> setting, K key, V value) {
            super(key, value);
            this.setting = setting;
        }

        @Override
        public boolean equals(Object o) {
            if(!(o instanceof MapPair)) {
                return false;
            }

            return ((MapPair<?, ?>)o).getKey().equals(this.getKey()) && ((MapPair<?, ?>)o).getValue().equals(this.getValue());
        }
    }
}
