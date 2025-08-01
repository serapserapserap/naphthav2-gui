package seraph.base.Map.Gui.settings;

public interface Changeable<T> {

    default T getDefaultVal() {
        return null;
    }
    void wipeConfig();
}
