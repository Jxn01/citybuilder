package util;

import java.util.Calendar;
import java.util.Date;

/**
 * This class represents the logger.
 */
public class Logger {
    /**
     * This method logs a message.
     * @param message is the message to log
     */
    public static void log(String message) {
        System.out.println("[" + convertTime(System.currentTimeMillis())+ "] " + message);
    }

    /**
     * This method converts a time to a string.
     * @param time is the time to convert
     * @return the converted time
     */
    private static String convertTime(long time) {
        String result = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        result += cal.get(Calendar.YEAR) + "-";
        result += (cal.get(Calendar.MONTH) + 1 < 10 ? "0" : "") + (cal.get(Calendar.MONTH) + 1) + "-";
        result += (cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + cal.get(Calendar.DAY_OF_MONTH) + " ";
        result += (cal.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + cal.get(Calendar.HOUR_OF_DAY) + ":";
        result += (cal.get(Calendar.MINUTE) < 10 ? "0" : "") + cal.get(Calendar.MINUTE) + ":";
        result += (cal.get(Calendar.SECOND) < 10 ? "0" : "") + cal.get(Calendar.SECOND);
        return result;
    }
}
