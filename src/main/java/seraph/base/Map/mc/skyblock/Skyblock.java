package seraph.base.Map.mc.skyblock;

import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import seraph.base.Map.mc.Gui;
import seraph.base.Naphthav2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static seraph.base.Naphthav2.mc;

public class Skyblock {
    public static Skyblock s = new Skyblock();
    public static String location = "none";
    public static boolean inSkyblock = false;
    private boolean locrawCanceled;
    private boolean locrawSent;
    private static final String LOCRAW_KEY = "\"map\":\"([^\"]+)\"";

    public Skyblock() {
        Naphthav2.register(this);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        locrawSent = false;
        locrawCanceled = false;
        location = "none";
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (!locrawSent) {
            mc.thePlayer.sendChatMessage("/locraw");
            locrawSent = true;
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onChat(ClientChatReceivedEvent e){
        if(isLocraw(e.message.getUnformattedText()) && !this.locrawCanceled) {
            locrawCanceled = true;
            e.setCanceled(true);
        }
    }

    private boolean isLocraw(String text){//{"server":"dynamiclobby12A","gametype":"PROTOTYPE","lobbyname":"prototypelobby1"}
        if(text.startsWith("{\"server\":\"") && text.contains("\"gametype\":\"") && text.contains("}")){
            if(text.contains(",\"gametype\":\"FOUR\"")){
                inSkyblock = true;
                Pattern pattern = Pattern.compile(LOCRAW_KEY);
                Matcher matcher = pattern.matcher(text);

                if (matcher.find()) {
                    location = matcher.group(1).trim();
                    return true;
                }
            }
            location = "none";
            return true;
        }
        return false;
    }

    public static boolean inSkyblock() {
        ScoreObjective scoreboardObj = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
        if (scoreboardObj != null) {
            if (Gui.removeSHIT(scoreboardObj.getDisplayName()).startsWith("FOUR")) {
                inSkyblock = true;
                return true;
            } else {
                inSkyblock = false;
                return false;
            }
        }
        inSkyblock = false;
        return false;
    }
}
