package seraph.base.mixins.Accessors;

import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPlayerSP.class)
public interface AccessorEntityPlayerSP {
    @Accessor("lastReportedYaw")
    float getLastReportedYaw();

    @Accessor("lastReportedPitch")
    float getLastReportedPitch();

}
