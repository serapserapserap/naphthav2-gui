package seraph.base.mixins.Accessors;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface AccessorMinecraft {
    @Accessor("timer")
    Timer getTimer();
    @Accessor("leftClickCounter")
    int getLeftClickCounter();
    @Accessor("leftClickCounter")
    void setLeftClickCounter(int leftClickCounter);
}
