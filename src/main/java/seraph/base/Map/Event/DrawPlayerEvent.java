package seraph.base.Map.Event;

import net.minecraft.entity.player.EntityPlayer;

public class DrawPlayerEvent extends Event{
    public float scalex = 1.f;
    public float scaley = 1.f;
    public float scalez = 1.f;
    public final EntityPlayer player;
    public DrawPlayerEvent(EntityPlayer p) {
        this.player = p;
    }
}
