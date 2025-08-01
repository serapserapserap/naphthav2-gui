package seraph.base.Map;

public class StringHelper {
    public static String replace(String original, String... replace) {
        for(int i = 0; i < replace.length; i++) {
            original = original.replace("{" + i + "}", replace[i]);
        }
        return original;
    }

    public static String[] toStringArray(Object[] oa) {
        String[] var0 = new String[oa.length];
        for(int i = 0 ; i < oa.length ; i++) {
            var0[i] = oa[i].toString();
        }
        return var0;
    }
}
