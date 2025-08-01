package seraph.base.Map.Gui.settings;

import org.lwjgl.input.Keyboard;
import seraph.base.Map.syshelpers.ClipboardHelper;

import static seraph.base.Map.Gui.graphicaluserinterfaces.DropdownClickGui.VALID_CHARACTERS;

public interface TextInput {
    default String handleTyped(String originalVal, char typedChar, int keycode) {
        switch (keycode) {
            case 14:
                if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && !originalVal.isEmpty()){
                    return "";
                }
                else if (!originalVal.isEmpty()) {
                    originalVal = originalVal.substring(0, originalVal.length() - 1);
                }
                break;
            case 47:
                if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                    String clipboardText = ClipboardHelper.getClipboardText();
                    if (clipboardText != null) {
                        originalVal = originalVal + filterInvalidCharacters(clipboardText);
                    }
                }
                break;
            default:
                if (isValidCharacter(typedChar) ) {
                    originalVal = originalVal + typedChar;
                }
                break;
        }
        return originalVal;
    }

    default String filterInvalidCharacters(String input) {
        StringBuilder filtered = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (isValidCharacter(c)) {
                filtered.append(c);
            }
        }
        return filtered.toString();
    }

    default String getCharset() {
        return VALID_CHARACTERS;
    }

    default boolean isValidCharacter(char c) {
        return getCharset().indexOf(c) >= 0;
    }
}
