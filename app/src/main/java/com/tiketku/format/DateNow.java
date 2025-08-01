package com.tiketku.format;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateNow {

    public static String getDateNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm", new Locale("id", "ID"));
        return sdf.format(new Date());
    }

    public static String getDayNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", new Locale("id", "ID"));
        return sdf.format(new Date());
    }
}
