package seraph.base.Map.Gui.modules;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;
import seraph.base.Map.Event.MotionUpdateEvent;
import seraph.base.Map.Gui.GuiscreenWrapper;
import seraph.base.Map.Gui.settings.impl.Keybind;
import seraph.base.Naphthav2;
import seraph.base.features.impl.ClickGuiModule;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static seraph.base.Map.StringHelper.replace;
import static seraph.base.Naphthav2.mc;

public class ModuleManager {
    private final List<NonToggleModule> allModules;
    private final List<Module> toggleModules;
    private final List<Hud> hudElements;
    public static List<Module> silenced = new ArrayList<>();
    private final ConcurrentHashMap<Class<? extends NonToggleModule>,NonToggleModule> moduleMap;
    private final ConcurrentHashMap<Class<? extends  Module>, Module> toggleModuleMap;
    private final List<Keybind<?>> keybinds;
    public static ClickGuiModule clickGuiModule;

    public ModuleManager(){
        this.allModules = new ArrayList<>();
        this.toggleModules = new ArrayList<>();
        this.moduleMap = new ConcurrentHashMap<>();
        this.toggleModuleMap = new ConcurrentHashMap<>();
        this.keybinds = new ArrayList<>();
        this.hudElements = new ArrayList<>();
        Naphthav2.register(this);
    }

    public List<NonToggleModule> getAllModules(){
        return this.allModules;
    }

    public List<Module> getToggleModules(){
        return this.toggleModules;
    }

    public boolean registerKeybind(Keybind<?> k){
        if(this.keybinds.contains(k)) return false;
        this.keybinds.add(k);
        System.out.println("Registered Keybind: " + this.keybinds.size());
        return true;
    }

    public boolean register(NonToggleModule m){
        if(moduleMap.containsKey(m.getClass())) return false;
        if(m instanceof Module){
            Module module = (Module) m;
            this.toggleModules.add(module);
            this.toggleModuleMap.put(module.getClass(),module);
        }
        this.allModules.add(m);
        this.moduleMap.put(m.getClass(),m);
        this.allModules.sort(Comparator.comparing(NonToggleModule::getSubCategory));
        return true;
    }

    public NonToggleModule getModuleFromAllModules(Class<? extends NonToggleModule> clazz){
        for(Class<?> var0 : this.moduleMap.keySet()){
            if(var0 == clazz) return this.moduleMap.get(var0);
        }
        return null;
    }
    public Module getModuleFromToggleModules(Class<? extends Module> clazz){
        return this.toggleModuleMap.get(clazz);
    }

    public NonToggleModule getModuleFromAll(String name){
        for(NonToggleModule ntm : this.allModules){
            if(ntm.getName().equalsIgnoreCase(name)) return ntm;
        }
        return null;
    }

    public Module getModuleFroMToggleable(String name){
        for(Module m : this.toggleModules){
            if(m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }

    @SubscribeEvent
    public void onTick(MotionUpdateEvent.PreMotionUpdateEvent e){
        GuiScreen screen = Naphthav2.mc.currentScreen;
        if(screen != null) {
            if(
                    !clickGuiModule.shouldCheckFromInv() || (
                            screen instanceof GuiChat ||
                            screen instanceof GuiEditSign ||
                            screen instanceof GuiRepair ||
                            screen instanceof GuiscreenWrapper
                    )
            ) return;
        }

        for(Keybind<?> keybind : this.keybinds){
            if(keybind == null || keybind.getValue() == 0x00)  continue;
            if(keybind.checkKeySinglePress()){
                keybind.onPress();
            }
        }
    }

    public void configHud() {
        for(Hud element : hudElements) {
            if(!element.draw()) continue;
            if(Mouse.isButtonDown(1)) element.toDefault();
            if(!Mouse.isButtonDown(0)) continue;
            double scale = new ScaledResolution(mc).getScaleFactor();
            double mouseX = Mouse.getX() / scale;
            double mouseY = (mc.displayHeight - Mouse.getY()) / scale;
            element.setPosition(mouseX / mc.displayWidth, mouseY / mc.displayHeight);
        }
    }

    public static void logError(NonToggleModule m, String s) {
        String modPrefix = clickGuiModule.modName.getValue();
        modPrefix = replace(modPrefix.replace("&", "§"), m.getName().toLowerCase(), "err");
        Naphthav2.print(modPrefix + " " + s);
    }

    public static void logInfo(NonToggleModule m, String s) {
        String modPrefix = clickGuiModule.modName.getValue();
        modPrefix = replace(modPrefix.replace("&", "§"), m.getName().toLowerCase(), "info");
        Naphthav2.print(modPrefix + " " + s);
    }
    public static void log(NonToggleModule m, String s) {
        String modPrefix = clickGuiModule.modName.getValue();
        modPrefix = replace(modPrefix.replace("&", "§"), m.getName().toLowerCase(), "log");
        Naphthav2.print(modPrefix + " " + s);
    }
}
