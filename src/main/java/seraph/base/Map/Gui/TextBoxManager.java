package seraph.base.Map.Gui;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import seraph.base.Map.Gui.settings.TextInput;
import seraph.base.Map.syshelpers.ClipboardHelper;

import static seraph.base.Map.StringHelper.replace;

public class TextBoxManager<var0 extends GuiElementWrapper> implements GuiElementWrapper, TextInput {
    private String val;
    public final var0 owner;

    public var0 getOwner() {
        return this.owner;
    }
    public int index = 0;
    private final int maxChars;
    private int highlightedStartIndex, highlightedEndIndex = 0;
    public String charSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()_+[]{}\\|;:'\",.<>/?~1234567890`-=~ ";
    public TextBoxManager(String val, var0 owner, int maxChars) {
        this.val = val;
        this.owner = owner;
        this.maxChars = maxChars;
    }
    public TextBoxManager(String val, var0 owner) {
        this(val,owner,-1);
    }

    public TextBoxManager<var0> charSet(String newCharSet) {
        this.charSet = newCharSet;
        return this;
    }

    public void highlight(final int start, final int end) {
        this.highlightedStartIndex = start;
        this.highlightedEndIndex = end;
    }

    public String getHighlightedSubString() {
        return this.val.substring(this.highlightedStartIndex, this.highlightedEndIndex);
    }

    @Override
    public String getDescription() {
        return owner.getDescription();
    }

    private void onCopy() {
        GuiScreen.setClipboardString(this.getHighlightedSubString());
    }

    public void deleteHighlighted() {
        this.val = new StringBuilder(this.val).delete(this.highlightedStartIndex, this.highlightedEndIndex).toString();
        this.index = this.highlightedStartIndex;
        this.highlightedStartIndex = this.highlightedEndIndex = 0;
    }

    public String onType(char typedChar, int keycode) {
        String s0 = this.handleTyped(this.val, typedChar, keycode);
        this.charTyped(s0);
        return this.val;
    }

    private void charTyped(String c) {
        val = new StringBuffer(this.val).insert(index, c).toString();
        this.index += c.length();
        if(maxChars != -1) {
            this.val = this.val.substring(0, maxChars);
        }
    }

    public String getColouredString() {
        if(maxChars != -1) {
        String r0, r1;
        r0 = r1 = "f";
        if(val.length() > maxChars * .5f) r0 = "e";
        if(val.length() > maxChars * .75) r0 = r1 = "c";
        return replace(
                "§{0}{1}§{2}/{3}",
                r0,
                this.val.length() + "",
                r1,
                this.maxChars + ""
        );} else return "";
    }

    @Override
    public String getCharset() {
        return this.charSet;
    }

    @Override
    public String handleTyped(String originalVal, char typedChar, int keycode) {
        String change = String.valueOf(typedChar);
        //paste/copy etc
        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
            change = "";
            switch(keycode) {
                //copying
                case Keyboard.KEY_C:
                    this.onCopy();
                    break;
                //navigating and highlighting the string
                case Keyboard.KEY_LEFT:
                    if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                        this.highlight(index, originalVal.length());
                    }
                    index = 0;
                    break;
                case Keyboard.KEY_RIGHT:
                    if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                        this.highlight(0, index);
                    }
                    index = this.val.length();
                    break;
                //pasting
                case Keyboard.KEY_V:
                    this.deleteHighlighted();
                    change = this.filterInvalidCharacters(ClipboardHelper.getClipboardText());
                    break;
                //cut
                case Keyboard.KEY_X:
                    this.onCopy();
                    this.deleteHighlighted();
                    break;
                //todo undo & redo
                case Keyboard.KEY_Y:
                case Keyboard.KEY_Z:
                    if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                        //redo
                    } else {
                        //undo
                    }
                    break;
                //select all
                case Keyboard.KEY_A:
                    this.highlight(0, this.val.length());
                    break;

            }
        }
        //handle normally
        else {
            switch(keycode) {
                //todo
                case Keyboard.KEY_LEFT:
                    if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {

                    } else {

                    }
                    break;
                case Keyboard.KEY_RIGHT:
                    break;
                case Keyboard.KEY_UP:
                    if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                        highlight(0, index);
                    } else {
                        index = 0;
                    }
                    break;
                case Keyboard.KEY_DOWN:
                    if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                        highlight(index, this.val.length());
                    } else {
                        index = this.val.length();
                    }
                    break;
                case Keyboard.KEY_DELETE:
                case Keyboard.KEY_BACK:
                    deleteHighlighted();
                    if (index > 0) {
                        this.val = new StringBuilder(this.val).deleteCharAt(index - 1).toString();
                        index--;
                    }
                    change = "";
                    break;
            }
            if (!change.isEmpty() && !Character.isISOControl(typedChar)) deleteHighlighted();
        }
            return filterInvalidCharacters(change);
    }
}

