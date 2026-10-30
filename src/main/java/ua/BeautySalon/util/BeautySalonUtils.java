package ua.BeautySalon.util;

import ua.BeautySalon.model.BeautyService;
import ua.BeautySalon.model.ClientAppointment;
import ua.BeautySalon.model.ServiceCategory;
import ua.BeautySalon.model.AppointmentState;

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

    public static void validateCategoryMatch(ServiceCategory masterCategory, ServiceCategory serviceCategory){
        if (!masterCategory.equals(serviceCategory)) throw new IllegalArgumentException("Master category (" + masterCategory + ") must service (" + serviceCategory + ")");
    }

    public static String normalize(String text){
        return FormatHelper.normalize(text);
    }

    public static String formatMoney(double amount){
        return FormatHelper.formatMoney(amount);
    }

    public static String getCategoryDescription(ServiceCategory category) {
        validateNotNull(category, "Category");
        return switch (category) {
            case HAIRCUT -> "Hair styling, cutting, and care.";
            case MANICURE -> "Nail service and hand aesthetics.";
            case MASSAGE -> "Wellness and relaxation massage.";
            case FACIAL -> "Cosmetology facial skin treatment.";
        };
    }

    public static String getActionInstruction(AppointmentState state) {
        validateNotNull(state, "State");
        return switch (state) {
            case CONFIRMED -> "Confirmed: Prepare the master's workplace.";
            case COMPLETED -> "Completed: Process payment and issue receipt.";
            case CANCELLED -> "Cancelled: Free up the time slot in schedule.";
            case NO_SHOW -> "No-show: Mark client profile accordingly.";
        };
    }

    public static java.time.LocalTime calculateEndTime(ClientAppointment appointment) {
        validateNotNull(appointment, "Appointment");
        return appointment.getStartTime().plusMinutes(appointment.getService().durationMinutes());
    }

    public static double calculatePricePerMinute(BeautyService service){
        validateNotNull(service, "Beauty service");
        return service.price() / service.durationMinutes();
    }
}
