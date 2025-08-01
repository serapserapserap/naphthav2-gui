package seraph.base.Map;

import com.google.gson.Gson;
import net.minecraft.launchwrapper.Launch;
import seraph.base.Map.syshelpers.FileHelper;
import seraph.base.Naphthav2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static seraph.base.Map.syshelpers.FileHelper.validateDir;

public class AnnotationScanner {

    private final List<Class<?>> classes;

    public List<Class<?>> getClasses(){
        return this.classes;
    }

    public AnnotationScanner() {
        this.classes = new ArrayList<>();
        File file;
        validateDir(new File(Naphthav2.mc.mcDataDir + "/config/naphthav2"));
        File f = new File(Naphthav2.mc.mcDataDir + "/config/naphthav2/loadedClasses.json");
        boolean hasConfig = true;
        Map<String, Boolean> classesToLoad = new HashMap<>();
        if(!f.exists()) {
            hasConfig = false;
            try {
                if(!f.createNewFile()) {
                    throw Naphthav2.logger.writeError(new RuntimeException("ur os is shit"));
                }
            } catch (IOException e) {
                Naphthav2.logger.writeError(e);
                throw new RuntimeException(e);
            }
        } else {
            Gson gson = new Gson();
            FileReader reader;
            try {
                reader = new FileReader(f);
            } catch (FileNotFoundException e) {
                throw Naphthav2.logger.writeError(new RuntimeException(e));
            }
            classesToLoad = gson.fromJson(reader, Map.class);
        }
        try {
            URL location = Naphthav2.class.getProtectionDomain().getCodeSource().getLocation();
            URI uri = location.toURI();
            if ("jar".equals(uri.getScheme())) {
                String jarPath = uri.getSchemeSpecificPart().split("!")[0].substring(5);
                file = new File(jarPath);
            } else if ("file".equals(uri.getScheme())) {
                file = new File(uri);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (URISyntaxException | IllegalArgumentException e) {
            Naphthav2.logger.writeError(e);
            throw new RuntimeException(e);
        }
        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();
                String s = jarEntry.getName();
                if (s.startsWith("seraph") && s.endsWith(".class")) {
                    if (s.contains("mixin")) continue;
                    String className = s.replace("/", ".").replace(".class","");
                    Class<?> clazz = Class.forName(className, false, Launch.classLoader);
                    if(true){//if(!hasConfig || classesToLoad.get(clazz.getName())) {
                        //Naphthav2.logger.log(replace("scanned {0}", clazz.getName()));
                        this.classes.add(clazz);
                        System.out.println(clazz.getName());
                        classesToLoad.put(clazz.getName(), true);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(!hasConfig) {
            FileHelper.writeToJson(f,classesToLoad);
        }
        System.out.println(this.classes.size());
    }

    public static List<Class<? extends Annotation>> convertToClass(List<Annotation> var0){
        List<Class<? extends Annotation>> var1 = new ArrayList<>();
        var0.forEach(var2 -> var1.add(var2.getClass()));
        return var1;
    }

    public boolean annotatedWith(Class<?> clazz, Class<? extends Annotation> target) {
        return clazz.isAnnotationPresent(target);
    }

    public void processAnnotation(Class<? extends Annotation> var1, Consumer<? super Object> var2){
        List<Object> var0 =  new ArrayList<>();
        this.classes.forEach(clazz -> {
            if(this.annotatedWith(clazz,var1)){
                try {
                    var0.add(clazz.newInstance());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        var0.forEach(var2);
    }
}
