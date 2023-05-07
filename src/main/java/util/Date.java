package util;

import java.util.Calendar;

/**
 * This class represents the date.
 */
public class Date {

    /**
     * This method returns the short date as a string.
     *
     * @param time is the time
     * @return the date as a string
     */
    public static String getShortDate(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date(time));
        return getShortString(cal);
    }

    /**
     * This method returns the long date as a string.
     *
     * @param time is the time
     * @return the date as a string
     */
    public static String getLongDate(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date(time));
        return getLongString(cal);
    }

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date(time));
        return getLongString(cal).split(" ")[0];
    }

    public static String nextDay(String date) { // the format is "YYYY-MM-DD"
        String[] dateParts = date.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return getLongString(cal).split(" ")[0];
    }

    public static String nextSecond(String shortDate) { // the format is hh:mm:ss
        String[] timeParts = shortDate.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        int second = Integer.parseInt(timeParts[2]);
        Calendar cal = Calendar.getInstance();
        cal.set(0, 0, 0, hour, minute, second);
        cal.add(Calendar.SECOND, 1);
        return getShortString(cal);
    }

    /**
     * This method returns the long date as a string.
     *
     * @param cal is the calendar
     * @return the date as a string
     */
    private static String getLongString(Calendar cal) {
        String result = cal.get(Calendar.YEAR) + "-";
        result += (cal.get(Calendar.MONTH) + 1 < 10 ? "0" : "") + (cal.get(Calendar.MONTH) + 1) + "-";
        result += (cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + cal.get(Calendar.DAY_OF_MONTH) + " ";
        result += getShortString(cal);
        return result;
    }

    /**
     * This method returns the short date as a string.
     *
     * @param cal is the calendar
     * @return the date as a string
     */
    private static String getShortString(Calendar cal) {
        String result = (cal.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "") + cal.get(Calendar.HOUR_OF_DAY) + ":";
        result += (cal.get(Calendar.MINUTE) < 10 ? "0" : "") + cal.get(Calendar.MINUTE) + ":";
        result += (cal.get(Calendar.SECOND) < 10 ? "0" : "") + cal.get(Calendar.SECOND);
        return result;
    }

    /**
     * This method returns the date as a calendar.
     *
     * @param longDate is the date as a string
     * @return the date as a calendar
     */
    public static Calendar toCalendar(String longDate) {
        String[] dateParts = longDate.split(" ")[0].split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        return cal;
    }

}
