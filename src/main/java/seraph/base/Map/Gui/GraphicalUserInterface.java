package seraph.base.Map.Gui;

public interface GraphicalUserInterface {
    void onOpen();
    void onClose();

    void render(int mouseX, int mouseY, float partialTicks);

    void onMouseDrag(float mouseX, float mouseY, int mouseButton, long timeSinceLastClick);
    void onMouseScroll(int state);
    void onClick(float mouseX, float mouseY, int button);
    void onRelease(int mouseX, int mouseY, int state);

    boolean onKeyTyped(char character, int keyCode);
}
