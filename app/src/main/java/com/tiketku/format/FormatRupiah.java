package com.tiketku.format;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatRupiah {
    public static String formatRupiah(int price) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(localeID);
        format.setMaximumFractionDigits(0);
        return format.format(price);
    }
}
