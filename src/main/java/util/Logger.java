package util;

import static util.Date.getShortDate;

/**
 * This class represents the logger.
 */
public class Logger {
    /**
     * This method logs a message.
     *
     * @param message is the message to log
     */
    public static void log(String message) {
        System.out.println("[" + getShortDate(System.currentTimeMillis()) + "] " + message);
    }
}
