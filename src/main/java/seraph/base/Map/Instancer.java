package seraph.base.Map;

import java.lang.reflect.Constructor;

public class Instancer {
    public static <T> T createInstance(Class<T> clazz, Object... constructorArgs) {
        try{
            int var0 = constructorArgs.length;
            Class<?>[] paramClasses = new Class<?>[var0];

            for( int i = 0 ; i < var0 ; i++){
                paramClasses[i] = constructorArgs[i].getClass();
            }

            Constructor<T> constructor = clazz.getConstructor(paramClasses);

            return constructor.newInstance(constructorArgs);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
