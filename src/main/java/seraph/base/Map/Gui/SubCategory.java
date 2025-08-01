package seraph.base.Map.Gui;


public enum SubCategory implements GuiElementWrapper{
    GUI("GUI"),
    MISC("Misc"),
    BANANA("BANANA"),
    AURA("Aura"),
    FLOOR7("Floor 7"),
    PUZZLE("Puzzles"),
    VISUAL("Visual"),
    BEANS("Beans"),
    SKILLS("Skills"),
    PRINGLES("Prigles"),
    ANANAS("Ananas"),
    MOVEMENT("Movement");

    public final String name;
    private final String description;
    SubCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }
    SubCategory(String name){
        this.name = name;
        this.description = "";
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
