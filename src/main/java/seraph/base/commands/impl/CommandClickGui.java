package seraph.base.commands.impl;

import seraph.base.Naphthav2;
import seraph.base.commands.CommandClass;
import seraph.base.features.impl.ClickGuiModule;

@CommandClass
public class CommandClickGui extends Command {
    public CommandClickGui() {
        super("naphthav2", "opens the gui (v2)", new String[]{"np", "naphtha"});
    }

    @Override
    public void executeCommand(String[] args) {
        new Thread(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
            ((ClickGuiModule) Naphthav2.INSTANCE.moduleManager.getModuleFromAllModules(ClickGuiModule.class)).toggle();
        }).start();
    }
}
