package seraph.base.Map.dataHelpers;

import net.minecraft.command.ICommand;
import net.minecraftforge.client.ClientCommandHandler;
import seraph.base.Map.Gui.modules.Hud;
import seraph.base.Map.Gui.modules.Module;
import seraph.base.Naphthav2;
import seraph.base.Map.Gui.modules.NonToggleModule;

public class AnnotationHandlers {

    public static void handleCommandAnnotation(Object o) {
        if(o instanceof ICommand) {
            System.out.println("Handled Command");
            ClientCommandHandler.instance.registerCommand((ICommand) o);
        }
    }

    public static void handleModuleAnnotation(Object o) {
        if(o instanceof NonToggleModule) {
            System.out.println("Handled Module");
            Naphthav2.INSTANCE.getModuleManager().register((NonToggleModule) o);
        }
    }

    public static void handleInterfaceAnnotation(Object o) {
        if(o instanceof Hud && o instanceof Module) {
            //don't to put the new instance in
            Naphthav2.INSTANCE.moduleManager.getModuleFromToggleModules(((Module)o).getClass());
        }
    }
}
