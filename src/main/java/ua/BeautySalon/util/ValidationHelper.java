package ua.BeautySalon.util;

import java.time.LocalDate;
import java.util.Set;

final class ValidationHelper {

    static final Set<String> ALLOWED_STATES = Set.of("COMPLETED", "CONFIRMED", "CANCELED", "NO_SHOW");
    static final Set<String> ALLOWED_CATEGORIES = Set.of("HAIRCUT", "MANICURE", "MASSAGE", "FACIAL");
    static final int MIN_SERVICE_DURATION = 15;
    static final int MAX_SERVICE_DURATION = 240;

    private ValidationHelper(){

    }

    static <T> T requireNotNull(T obj, String fieldName){
        if (obj == null){
            throw new IllegalArgumentException(fieldName + "must not be null!");
        }
        return obj;
    }

    static String requireNotBlank(String value, String fieldName){
        if (value == null || value.trim().isEmpty()){
            throw new IllegalArgumentException(fieldName + "must not be blank!");
        }
        return value;
    }

    static double requirePositive(double value, String fieldName){
        if (value <= 0){
            throw new IllegalArgumentException(fieldName + "must be positive!");
        }
        return value;
    }
    static LocalDate requireAge(LocalDate BirthDate, String fieldName){
        requireNotNull(BirthDate, fieldName);
        LocalDate minAllowedBirthDate = LocalDate.now().minusYears(14);
        if (BirthDate.isAfter(minAllowedBirthDate)){
            int actualAge = LocalDate.now().getYear() - BirthDate.getYear();
            throw new IllegalArgumentException("must be 14+" + "got age " + actualAge);
        }
        return BirthDate;
    }

    static String requireInList(String value, Set<String> allowedValues, String fieldName){
        requireNotBlank(value, fieldName);
        String upper = value.trim().toUpperCase();
        for (String allowed : allowedValues){
            if (allowed.equals(upper)){
                return upper;
            }
        }
        throw new IllegalArgumentException(fieldName + " must be one of " + allowedValues);
    }

    static int requireInRange(int value, int min, int max, String fieldName){
        if (value < min || value > max) throw new IllegalArgumentException(fieldName + "must be more than" + min + "and less than" + max);
        return value;
    }
}
