package seraph.base.Map.Gui.font;

import net.minecraft.util.ResourceLocation;
import seraph.base.Naphthav2;

import java.awt.*;

import static seraph.base.Map.StringHelper.replace;
import static seraph.base.Naphthav2.mc;

public class FontDefiner {
    public static FentRenderer getFontRenderer(ResourceLocation loc, float size) {
        FentRenderer tempRenderer;
        try {
            tempRenderer = new FentRenderer(Font.createFont(Font.TRUETYPE_FONT, mc.getResourceManager().getResource(loc).getInputStream()).deriveFont(Font.PLAIN, size), true, true);
        } catch (Exception e) {
            throw Naphthav2.logger.writeError(new RuntimeException(e));
        }
        return tempRenderer;
    }

    public static void initFonts(String... names) {
        for(int i = 0 ; i < names.length ; i ++) {
            String name = names[i];
            float size = 20;
            boolean sizeOveride = name.contains(":");

            if(sizeOveride) {
                String[] var0 = name.split(":");
                name = var0[0];
                System.out.println(name);
                System.out.println(size);
                size = Float.parseFloat(var0[1]);
            }
            Naphthav2.fentRenderers.put(
                    name + ":" + size,
                    getFontRenderer(
                            Naphthav2.getLoc(replace("font/{0}.ttf", name)),
                            size
                    )
            );
        }
    }
}
