package ua.BeautySalon.util;

import ua.BeautySalon.model.BeautyService;
import ua.BeautySalon.model.ClientAppointment;

import java.time.LocalDate;

public final class BeautySalonUtils {

    private BeautySalonUtils(){

    }

    public static String validateNotBlank(String value, String fieldName){
        return ValidationHelper.requireNotBlank(value, fieldName);
    }

    public static <T> T validateNotNull(T value, String fieldName) {
        return ValidationHelper.requireNotNull(value, fieldName);
    }

    public static double validatePositive(double value, String fieldName) {
        return ValidationHelper.requirePositive(value, fieldName);
    }

    public static LocalDate validateBirthDate(LocalDate value) {
        return ValidationHelper.requireAge(value, "BirthDate");
    }

    public static int validateDuration(int value){
        return ValidationHelper.requireInRange(value, ValidationHelper.MIN_SERVICE_DURATION, ValidationHelper.MAX_SERVICE_DURATION, "Duration");
    }

    public static String validateCategory(String category){
        return ValidationHelper.requireInList(category, ValidationHelper.ALLOWED_CATEGORIES, "Category");
    }

    public static String validateState(String state){
        return ValidationHelper.requireInList(state, ValidationHelper.ALLOWED_STATES, "State");
    }

    public static void validateCategoryMatch(String masterCategory, String serviceCategory){
        if (!masterCategory.equals(serviceCategory)) throw new IllegalArgumentException("Master category (" + masterCategory + ") must service (" + serviceCategory + ")");
    }

    public static String normalize(String text){
        return FormatHelper.normalize(text);
    }

    public static String formatMoney(double amount){
        return FormatHelper.formatMoney(amount);
    }

    public static java.time.LocalTime calculateEndTime(ClientAppointment appointment) {
        validateNotNull(appointment, "Appointment");
        return appointment.getStartTime().plusMinutes(appointment.getService().getDurationMinutes());
    }

    public static double calculatePricePerMinute(BeautyService service){
        validateNotNull(service, "Beauty service");
        return service.getPrice() / service.getDurationMinutes();
    }
}
