package seraph.base.Map;

import java.io.*;

import static seraph.base.Map.StringHelper.replace;
import static seraph.base.Map.syshelpers.FileHelper.validateFile;

public class Logger {
    private final BufferedWriter writer;
    private final StringWriter stringWriter;
    private final PrintWriter printWriter;

    public Logger(String outputDir) throws IOException {
        File outputFile = validateFile(new File(outputDir + "/nv2log.balls"));
        this.writer = new BufferedWriter(new FileWriter(outputFile));
        this.stringWriter = new StringWriter();
        this.printWriter = new PrintWriter(this.stringWriter);
    }

    public <T extends Throwable> T  writeError(T e) {
        e.printStackTrace(this.printWriter);
        this.writeError(this.stringWriter.toString());
        this.stringWriter.flush();
        this.printWriter.flush();
        return e;
    }

    public Logger writeError(String s) {
        try {
            writer.write(
                    replace(
                            "{0}[{1}/ERROR] {2}",
                            Time.getCurrentTime(),
                            Thread.currentThread().getName(),
                            s
                    )
            );
            this.nl();
        } catch (IOException e) {
            throw new RuntimeException("Error Writing To Logger");
        }
        return this;
    }

    public Logger log(String s) {
        try {
            writer.write(
                    replace(
                            "{0}[{1}/INFO] {2}",
                            Time.getCurrentTime(),
                            Thread.currentThread().getName(),
                            s
                    )
            );
            this.nl();
        } catch (IOException e) {
            throw new RuntimeException("Error Writing To Logger");
        }
        return this;
    }

    public Logger nl() throws IOException{
        this.writer.newLine();
        return this;
    }
}
