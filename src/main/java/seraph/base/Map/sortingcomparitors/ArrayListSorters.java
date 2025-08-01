package seraph.base.Map.sortingcomparitors;

import net.minecraft.util.Vec3;
import seraph.base.Map.Gui.modules.NonToggleModule;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static seraph.base.Naphthav2.mc;

public class ArrayListSorters {
    public static void sortByClosestToPlayer(List<Vec3> vecs){
        vecs.sort(Comparator.comparingDouble(vec -> vec.distanceTo(new Vec3(mc.thePlayer.posX,mc.thePlayer.posY,mc.thePlayer.posZ))));
    }

    public static void sort(ArrayList<String> strings) {
        strings.sort((s1, s2) -> Integer.compare(s2.length(), s1.length()));
    }

    public static void sortList(List<String> strings) {
        strings.sort((s1, s2) -> Integer.compare(mc.fontRendererObj.getStringWidth(s2), mc.fontRendererObj.getStringWidth(s1)));
    }

    public static void sort(List<NonToggleModule> modules){
    }
}
