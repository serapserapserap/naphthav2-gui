package seraph.base;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import seraph.base.Map.AnnotationScanner;
import seraph.base.Map.Gui.config.Config;
import seraph.base.Map.Gui.font.FentRenderer;
import seraph.base.Map.Gui.font.FontDefiner;
import seraph.base.Map.Gui.modules.Interface;
import seraph.base.Map.Gui.modules.ModuleClass;
import seraph.base.Map.Gui.modules.ModuleManager;
import seraph.base.Map.Gui.notification.NotificationManager;
import seraph.base.Map.Gui.theme.ThemeManager;
import seraph.base.Map.Logger;
import seraph.base.Map.dataHelpers.AnnotationHandlers;
import seraph.base.Map.drawing.Shaders;
import seraph.base.Map.mc.skyblock.dungeonMap.scan.Pair;
import seraph.base.commands.CommandClass;
import seraph.base.features.impl.ClickGuiModule;
import seraph.base.features.impl.Modules;

import java.io.IOException;
import java.util.HashMap;

@Mod(modid = Naphthav2.MODID,version = Naphthav2.VERSION,name = Naphthav2.NAME)
public class Naphthav2 {
    public static final String MODID = "fish";
    public static final String NAME = "naphthav2";
    public static final String VERSION = "Version";
    public AnnotationScanner annotationScanner;
    public static Naphthav2 INSTANCE;
    public ModuleManager moduleManager;
    public static Logger logger;
    public NotificationManager notificationManager = new NotificationManager();
    public static HashMap<String, FentRenderer> fentRenderers = new HashMap<>();
    public static ThemeManager themeManager = new ThemeManager();
    public static Pair<FentRenderer, FentRenderer> jetBrainsMono;
    public static FentRenderer jetBrainsMonoExtraSmall;
    public static Pair<FentRenderer, FentRenderer> openSans;

    public ModuleManager getModuleManager(){
        return this.moduleManager;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) throws InterruptedException {
        try {
            logger = new Logger(mc.mcDataDir + "/config/naphthav2");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try{
            INSTANCE = this;
            this.annotationScanner = new AnnotationScanner();
            MinecraftForge.EVENT_BUS.register(new Naphthav2());
            moduleManager = new ModuleManager();

            try{
                Naphthav2.INSTANCE.annotationScanner.processAnnotation(CommandClass.class, AnnotationHandlers::handleCommandAnnotation);
                Naphthav2.INSTANCE.annotationScanner.processAnnotation(ModuleClass.class, AnnotationHandlers::handleModuleAnnotation);
                Naphthav2.INSTANCE.annotationScanner.processAnnotation(Interface.class, AnnotationHandlers::handleInterfaceAnnotation);
            } catch (Exception e){
                logger.writeError(e);
                throw new RuntimeException("ERROR PROCESSING ANNOTATIONS");
            }
        } catch (Exception e){
            logger.writeError(e);
        }
        ModuleManager.clickGuiModule = (ClickGuiModule) moduleManager.getModuleFromAllModules(ClickGuiModule.class);
        Modules.loadSingleton();
        Shaders.init();
        FontDefiner.initFonts(
                "OpenSansEB",
                "JetBrainsMonoSB:25",
                "JetBrainsMonoSB:18.75",
                "JetBrainsMonoSB:12"
        );
        fentRenderers.keySet().forEach(System.out::println);
        jetBrainsMono = new Pair<>(fentRenderers.get("JetBrainsMonoSB:25.0"), fentRenderers.get("JetBrainsMonoSB:18.75"));
        jetBrainsMonoExtraSmall = fentRenderers.get("JetBrainsMonoSB:12.0");
        Config.loadConfig();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    public static void print(String s) {
        mc.thePlayer.addChatComponentMessage(new ChatComponentText(s));
        logger.log(s);
    }

    public void startGame() {}

    public static void sendModMsg(String message){
        if(mc.thePlayer != null) mc.thePlayer.addChatComponentMessage(new ChatComponentText("§aBase:§f "+ message));
    }

    public static void register(Class<?> clazz){
        MinecraftForge.EVENT_BUS.register(clazz);
    }

    public static void unregister(Class<?> clazz){
        MinecraftForge.EVENT_BUS.unregister(clazz);
    }
    public static void register(Object clazz){
        MinecraftForge.EVENT_BUS.register(clazz);
    }

    public static void unregister(Object clazz){
        MinecraftForge.EVENT_BUS.unregister(clazz);
    }

    public static final Minecraft mc = Minecraft.getMinecraft();
    public static ResourceLocation getLoc(String loc) {
        return new ResourceLocation("fish", loc);
    }
}

