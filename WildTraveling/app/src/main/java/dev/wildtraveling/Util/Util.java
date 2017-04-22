package dev.wildtraveling.Util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by pere on 4/12/17.
 */
public final class Util {

    private static Integer loaded = 0;

    public static String obtainDateString(Date date) {
        String result = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) day = "0" + day;
        if (calendar.get(Calendar.MONTH) < 10) month = "0" + month;
        result += " " + day + "/" + month + "/" + year + "\n";

        return result;
    }

    public static Integer getLoaded() { return loaded; }

    public static void increaseLoaded() { ++loaded; }
}
