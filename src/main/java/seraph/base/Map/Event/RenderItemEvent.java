package seraph.base.Map.Event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import seraph.base.mixins.Accessors.AccessorMinecraft;

import static seraph.base.Naphthav2.mc;

public abstract class RenderItemEvent extends Event {

    @Cancelable
    public static class TransformFirstPersonEvent extends RenderItemEvent {
        public ItemStack itemStackIn;
        public float equipProgress, swingProgress;
        public TransformFirstPersonEvent(final ItemStack stackIn, final float equipProgressIn, final float swingProgressIn) {
            this.itemStackIn = stackIn;
            this.equipProgress = equipProgressIn;
            this.swingProgress = swingProgressIn;
        }
    }
    @Cancelable
    public static class ItemUsedTransformationEvent extends RenderItemEvent {
        public ItemStack itemStackIn;
        public float swingProgress;
        public ItemUsedTransformationEvent(ItemStack stackIn, float swingProgress) {
            this.itemStackIn = stackIn;
            this.swingProgress = swingProgress;
        }
    }

    @Cancelable
    public static class ItemBlockTransformationEvent extends RenderItemEvent {
        public float swingProgress;
        public ItemBlockTransformationEvent() {
            this.swingProgress = mc.thePlayer.getSwingProgress(((AccessorMinecraft)mc).getTimer().renderPartialTicks);
        }
    }

    @Cancelable
    public static class RenderHandEvent extends RenderItemEvent {
        public float equipProgress, swingProgress;
        public RenderHandEvent(float equipProgress, float swingProgress) {
            this.equipProgress = equipProgress;
            this.swingProgress = swingProgress;
        }
    }

    @Cancelable
    public static class PreformDrinkingEvent extends ItemBlockTransformationEvent {
        public float partialTicks;
        public PreformDrinkingEvent(float partialTicks) {
            super();
            this.partialTicks = partialTicks;
        }
    }

    @Cancelable
    public static class BowTransformationEvent extends RenderItemEvent {
        public float partialTicks;
        public BowTransformationEvent(float partialTicks) {
            this.partialTicks = partialTicks;
        }
    }
}
