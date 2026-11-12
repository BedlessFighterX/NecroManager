package com.bedlessfighter.necroManager.managers;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/** Shared currency formatting. */
public final class EconomyFormat {
    private EconomyFormat() { throw new UnsupportedOperationException("Utility class"); }
    public static String format(double amount) {
        String pattern = ConfigManager.getString("economy.format", "$#,##0.00");
        try { return new DecimalFormat(pattern, DecimalFormatSymbols.getInstance(Locale.US)).format(amount); }
        catch (IllegalArgumentException exception) { return String.format(Locale.US, "%.2f", amount); }
    }
}
