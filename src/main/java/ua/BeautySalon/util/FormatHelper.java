package ua.BeautySalon.util;

import java.util.Locale;

public class FormatHelper {
    private FormatHelper(){
        throw new UnsupportedOperationException("Utility class cannot be instantiated!");
    }

    static String normalize(String value){
        if (value == null) return null;
        return value.trim();
    }

    static String formatMoney(double amount) {
        return String.format(Locale.US, "%.2f UAH", amount);
    }
}
