package seraph.base.Map.Gui;

import seraph.base.Map.mc.skyblock.dungeonMap.scan.Pair;

import java.util.Objects;

public enum Category implements GuiElementWrapper {
    ONE(
            "Category 1",
            new SubCategory[] {
                    SubCategory.MISC
            }
    ),
    TWO(
            "Category 2",
            new SubCategory[] {
                    SubCategory.GUI,
                    SubCategory.BANANA,
                    SubCategory.MISC
            }
    ),
    THREE(
            "Categiry 3",
            new SubCategory[]{
                    SubCategory.FLOOR7,
                    SubCategory.AURA,
                    SubCategory.PUZZLE,
                    SubCategory.MOVEMENT,
                    SubCategory.MISC
            }
    ),
    FOUR(
            "Category 4",
            new SubCategory[] {
                    SubCategory.BEANS,
                    SubCategory.SKILLS,
                    SubCategory.VISUAL,
                    SubCategory.PRINGLES,
                    SubCategory.MISC
            }
    ),
    FIVE(
            "Category 5",
            new SubCategory[]{
                    SubCategory.VISUAL,
                    SubCategory.ANANAS,
                    SubCategory.MOVEMENT,
                    SubCategory.MISC
            }
    );
    public final String name;
    public final SubCategory[] subCategories;
    public boolean droppedDown = true;

    Category(String name, SubCategory[] subCategories){
        this.name = name;
        this.subCategories = subCategories;
    }

    public void dropDown(){

        this.droppedDown = !this.droppedDown;

    }

    @Override
    public String getDescription() {
        return null;
    }

    public static class CategoryPair extends Pair<Category, SubCategory> implements GuiElementWrapper {

        public CategoryPair(Category c, SubCategory sc) {
            super(c,sc);
        }

        @Override
        public String getDescription() {
            return this.getValue().getDescription();
        }

        @Override
        public boolean equals(Object other) {
            if(other == null) return false;
            if(!(other instanceof CategoryPair)) return false;
            return ((CategoryPair) other).getKey().equals(this.getKey()) && ((CategoryPair) other).getValue().equals(this.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.getKey(), this.getValue());
        }
    }
}
