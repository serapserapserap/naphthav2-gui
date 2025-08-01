package seraph.base.Map.Gui.config;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import seraph.base.Map.Gui.Toggleable;
import seraph.base.Map.Gui.modules.Module;
import seraph.base.Map.Gui.modules.NonToggleModule;
import seraph.base.Map.Gui.settings.Setting;
import seraph.base.Map.Gui.settings.impl.*;
import seraph.base.Map.mc.skyblock.dungeonMap.scan.Pair;
import seraph.base.Naphthav2;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static seraph.base.Naphthav2.mc;


public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File cfgFile = new File(Minecraft.getMinecraft().mcDataDir + "/config/naphthav2/config.json");
    public static final String configDir = mc.mcDataDir + "/config/naphthav2/";

    public static void saveConfig() {
        try {
            if (!cfgFile.getParentFile().exists()) {
                cfgFile.getParentFile().mkdirs();
            }

            List<NonToggleModule> modules = Naphthav2.INSTANCE.moduleManager.getAllModules();
            List<Map<String, Object>> moduleData = new ArrayList<>();
            for (NonToggleModule module: modules) {
                if (module == null) continue;
                Map<String, Object> data = new HashMap<>();
                data.put("name", module.getName());
                Map<String, Object> settings = new HashMap<>();
                for (Setting<?> setting : module.getSettings()) {
                    Object var0 = setting.getJsonValue();
                    if(var0 != null) settings.put(setting.getName(),setting.getJsonValue());
                }
                data.put("settings", settings);

                if (module instanceof Module) {
                    data.put("Enabled", ((Module)module).isToggled());
                }

                moduleData.add(data);
            }
            try (FileWriter writer = new FileWriter(cfgFile)) {
                GSON.toJson(moduleData, writer);
            }
        } catch (IOException e) {
            ;
        }
    }

    public static void loadConfig() {
        try (FileReader reader = new FileReader(cfgFile)) {
            JsonElement element = GSON.fromJson(reader, JsonElement.class);
            if (element.isJsonArray()) {
                JsonArray arr = element.getAsJsonArray();
                for (JsonElement e : arr) {
                    final JsonObject o = e.getAsJsonObject();
                    final String mname = o.get("name").getAsString();
                    for(final NonToggleModule m : Naphthav2.INSTANCE.moduleManager.getAllModules()) {
                        if(mname.equalsIgnoreCase(m.getName())) {
                            if(o.has("Enabled")) {
                                ((Module)m).toggle(o.get("Enabled").getAsBoolean());
                            }
                            for (Map.Entry<String, JsonElement> entry : o.get("settings").getAsJsonObject().entrySet()) {
                                for (Setting<?> s : m.getSettings()) {
                                    if(entry.getKey().equalsIgnoreCase(s.getName())) {
                                        JsonElement obj = entry.getValue();
                                        if(s instanceof Keybind) {
                                            ((Keybind<? extends Toggleable>)s).setValueConstructor(obj.getAsInt());
                                        } else if(s instanceof Slider) {
                                            ((Slider)s).setValueConstructor(obj.getAsDouble());
                                        } else if(s instanceof Text) {
                                            ((Text)s).setValueConstructor(obj.getAsString());
                                        } else if(s instanceof Toggle) {
                                            ((Toggle)s).setValueConstructor(obj.getAsJsonObject().get("value").getAsBoolean());
                                            JsonElement je = obj.getAsJsonObject().get("settings");
                                            if(je == null) continue;
                                            JsonObject j = je.getAsJsonObject();
                                            for(Map.Entry<String, JsonElement> entry2 : j.entrySet()) {
                                                handleSettings(((Toggle)s).settings.keySet(), entry2);
                                            }
                                        } else if(s instanceof DropDown) {
                                            ((DropDown)s).setValueConstructor(obj.getAsJsonObject().get("value").getAsString());
                                            JsonElement je = obj.getAsJsonObject().get("settings");
                                            if(je == null) continue;
                                            JsonObject j = je.getAsJsonObject();
                                            for(Map.Entry<String, JsonElement> entry2 : j.entrySet()) {
                                                handleSettings(((DropDown)s).settings.keySet(), entry2);
                                            }
                                        } else if(s instanceof SelectAll) {
                                            for(JsonElement item : obj.getAsJsonObject().get("value").getAsJsonArray()) {
                                                ((SelectAll)s).clickedOption(item.getAsString());
                                            }
                                            JsonElement je = obj.getAsJsonObject().get("settings");
                                            if(je == null) continue;
                                            JsonObject j = je.getAsJsonObject();
                                            for(Map.Entry<String, JsonElement> entry2 : j.entrySet()) {
                                                handleSettings(((SelectAll)s).settings.keySet(), entry2);
                                            }
                                        } else if(s instanceof MapOption) {
                                            List<Pair<String, String>> var0 = new ArrayList<>();
                                            for (Map.Entry<String, JsonElement> entry2 : obj.getAsJsonObject().entrySet()) {
                                                var0.add(
                                                        new Pair<>(
                                                                entry2.getKey(),
                                                                entry2.getValue().getAsString()
                                                        )
                                                );
                                            }
                                            ((MapOption)s).setValueConstructor(var0);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            saveConfig();
        }
    }

    private static void handleSettings(Set<Setting<?>> settings, Map.Entry<String, JsonElement> entry) {
        for (Setting<?> s : settings) {
            if(entry.getKey().equalsIgnoreCase(s.getName())) {
                JsonElement obj = entry.getValue();
                if(s instanceof Keybind) {
                    ((Keybind<? extends Toggleable>)s).setValueConstructor(obj.getAsInt());
                } else if(s instanceof Slider) {
                    ((Slider)s).setValueConstructor(obj.getAsDouble());
                } else if(s instanceof Text) {
                    ((Text)s).setValueConstructor(obj.getAsString());
                } else if(s instanceof Toggle) {
                    ((Toggle)s).setValueConstructor(obj.getAsJsonObject().get("value").getAsBoolean());
                } else if(s instanceof DropDown) {
                    ((DropDown)s).setValueConstructor(obj.getAsJsonObject().get("value").getAsString());
                } else if(s instanceof SelectAll) {
                    for(JsonElement item : obj.getAsJsonObject().get("value").getAsJsonArray()) {
                        ((SelectAll)s).clickedOption(item.getAsString());
                    }
                } else if(s instanceof MapOption) {
                    List<Pair<String, String>> var0 = new ArrayList<>();
                    for (Map.Entry<String, JsonElement> entry2 : obj.getAsJsonObject().entrySet()) {
                        var0.add(
                                new Pair<>(
                                        entry2.getKey(),
                                        entry2.getValue().getAsString()
                                )
                        );
                    }
                    ((MapOption)s).setValueConstructor(var0);
                }
            }
        }
    }
}