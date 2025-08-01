package seraph.base.Map.syshelpers;

import com.google.gson.GsonBuilder;
import seraph.base.Naphthav2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileHelper {
    public static File validateFile(final File f) {
        if(!f.exists()) {
            try {
                if(!f.createNewFile()) {
                    throw new RuntimeException("ur pc is shit");
                }
            } catch (IOException e) {
                throw new RuntimeException("baaa");
            }
        }
        return f;
    }

    public static File validateDir(final File f) {
        if(!f.exists()) {
            if(!f.mkdir()) {
                throw new RuntimeException("ur pc is shit");
            }
        }
        return f;
    }

    public static File reCreateFile(final File f) throws IOException {
        if(f.exists()) {
            if(!f.delete()) {
                throw new RuntimeException("ur pc is shit");
            }
        }
        System.out.println(f.getPath());
        if(!f.createNewFile()) {
            throw new RuntimeException("ur pc is shit");
        }
        return f;
    }

    public static void writeToJson(final File f, final Map<String, ?> map) {
        try (FileWriter writer = new FileWriter(f)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(map, writer);
        } catch (IOException e) {
            throw Naphthav2.logger.writeError(new RuntimeException("Error Writing File"));
        }
    }

}
