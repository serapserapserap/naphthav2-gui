package seraph.base.Map.Gui;

public interface Toggleable {
    void toggle();
    void toggle(boolean bool);

    boolean isToggled();
    String getName();
}
