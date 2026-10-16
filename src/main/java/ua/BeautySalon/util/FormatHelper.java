package ua.BeautySalon.util;

import java.util.Locale;

public class FormatHelper {
    private FormatHelper(){

    }

    static String normalize(String value){
        if (value == null) return null;
        return value.trim();
    }

    static String capitalize(String str) {
        if (str == null || str.isBlank()) return str;
        String trimmed = str.trim();
        return trimmed.substring(0, 1).toUpperCase(Locale.ROOT) + trimmed.substring(1).toLowerCase(Locale.ROOT);
    }

    static String formatMoney(double amount) {
        return String.format(Locale.US, "%.2f UAH", amount);
    }
}
