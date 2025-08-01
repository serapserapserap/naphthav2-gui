package seraph.base.Map;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Time {
    public static String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[HH:mm:ss]");
        return LocalTime.now().format(formatter);
    }


}
