package seraph.base.Map.Gui.settings.impl;

import org.lwjgl.input.Keyboard;
import seraph.base.Map.Gui.Toggleable;
import seraph.base.Map.Gui.modules.Module;
import seraph.base.Map.Gui.modules.ModuleManager;
import seraph.base.Map.Gui.notification.notifications.impl.NotificationType;
import seraph.base.Map.Gui.settings.Changeable;
import seraph.base.Map.Gui.settings.Setting;
import seraph.base.Naphthav2;

public class Keybind<T extends Toggleable> extends Setting<Integer> implements Changeable<Integer> {
    public final T linkedToggleable;
    private final Integer defaultVal;

    private boolean lastPressed;

    public void setLastPressed(boolean value){
        this.lastPressed = value;
    }

    public Keybind(String name, String description, T toggleable){
        super(name,description);
        this.linkedToggleable = toggleable;
        this.setValueConstructor(0x00);
        this.defaultVal = 0x00;
        Naphthav2.INSTANCE.moduleManager.registerKeybind(this);
    }
    public Keybind(String name, String description, T toggleable, int keycode){
        super(name,description);
        this.linkedToggleable = toggleable;
        this.setValueConstructor(keycode);
        this.defaultVal = keycode;
        Naphthav2.INSTANCE.moduleManager.registerKeybind(this);
    }

    public boolean checkKeySinglePress(){
        boolean var0 = Keyboard.isKeyDown(this.getValue());
        boolean var1 = var0 && !this.lastPressed;
        this.lastPressed = var0;
        return var1;
    }

    public void onPress(){
        this.linkedToggleable.toggle();
        if(linkedToggleable instanceof Module && !ModuleManager.silenced.contains((Module) this.linkedToggleable)) {
            Naphthav2.INSTANCE.notificationManager.pushNotification(3000, "Module " + (this.linkedToggleable.isToggled() ? "Enabled" : "Disabled"), this.linkedToggleable.getName(), NotificationType.SUCCESS);
        }
    }

    @Override
    public void onValueChange(Integer oldVal) {
    }

    public boolean checkKeyPressHold(){ return Keyboard.isKeyDown(this.getValue()) && this.lastPressed; }

    public void onHold(){}

    public String getNiceKeyName() {
        String var0 = Keyboard.getKeyName(this.getValue());
        return var0 == null ? " " : var0.replace("NONE", " ");
    }

    @Override
    public Integer getDefaultVal() {
        return this.defaultVal;
    }

    @Override
    public void wipeConfig() {
        this.setValue(this.getDefaultVal());
    }
}
