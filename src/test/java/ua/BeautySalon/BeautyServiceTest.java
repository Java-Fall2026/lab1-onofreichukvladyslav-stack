package ua.BeautySalon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.BeautySalon.exception.InvalidDataException;import ua.BeautySalon.loader.BeautyClientLoader;import ua.BeautySalon.model.*;
import ua.BeautySalon.util.BeautySalonUtils;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class BeautyServiceTest {
    @Test
    @DisplayName("Should successfully create a valid beauty service object")
    void testServiceCreationSuccess(){
        BeautyService service = new BeautyService("SRV-100", "Стрижка", ServiceCategory.HAIRCUT, 45, 500.0);
        assertEquals("SRV-100", service.getServiceCode());
        assertEquals(500.0, service.getPrice());
        assertEquals(ServiceCategory.HAIRCUT, service.getCategory());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when service price is negative or zero")
    void testServiceInvalidPriceThrowsException(){
        assertThrows(IllegalArgumentException.class, () -> {
            new BeautyService("SRV-101", "Массаж", ServiceCategory.MASSAGE, 60, -100.0);
        });
    }

    @Test
    @DisplayName("Should successfully create a client using the factory method")
    void testClientCreationSuccess(){
        BeautyClient client = BeautyClient.of("+380971112233", "Олена", LocalDate.of(1995, 5, 14));
        assertNotNull(client);
        assertEquals("+380971112233", client.phone());
    }

    @Test
    @DisplayName("Should throw IllegalArgumendException when client is under the minimum age")
    void testClientUnderageThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            BeautyClient.of("+380501112233", "Дитина", LocalDate.now().minusYears(10));
        });
    }

    @Test
    @DisplayName("Should correctly calculate price per minute for a service")
    void testCalculatePricePerMinute(){
        BeautyService service = new BeautyService("SRV-102", "Догляд", ServiceCategory.FACIAL, 60, 600.0);
        double pricePerMin = BeautySalonUtils.calculatePricePerMinute(service);
        assertEquals(10.0, pricePerMin, 0.001);
    }

    @Test
    @DisplayName("Should correctly calculate appointment end time")
    void testCalculateEndTime(){
        BeautyClient client = BeautyClient.of("+380971112233", "Олена", LocalDate.of(1995, 5, 14));
        Master master = Master.of("M-1", "Анна", ServiceCategory.HAIRCUT);
        BeautyService service = new BeautyService("SRV-103", "Стрижка", ServiceCategory.HAIRCUT, 30, 400.0);

        ClientAppointment appointment = new ClientAppointment(client, master, service, LocalDate.now(),LocalTime.of(12, 0), AppointmentState.CONFIRMED);

        LocalTime endTime = BeautySalonUtils.calculateEndTime(appointment);
        assertEquals(LocalTime.of(12, 30), endTime);
    }

    @Test
    @DisplayName("Should throw InvalidDataException with line number and cause on invalid client line")
    void testParseClientLineInvalidDataThrowsException(){
        String invalidLine = "+380971112233,Олена,invalid-date";
        int lineNumber = 3;

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            BeautyClientLoader.parseClientLine(invalidLine, lineNumber);
        });

        assertTrue(exception.getMessage().contains("Line 3"), "Exception message should contain line number");

        assertNotNull(exception.getCause(), "Exception cause should not be null");
    }

    @Test
    @DisplayName("Should throw InvalidDataException when validation fails inside parseClientLine")
    void testParseClientLineValidationFails(){
        String invalidLine = "+380971112233,Марічка,2022-01-01";
        int lineNumber = 5;

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            BeautyClientLoader.parseClientLine(invalidLine, lineNumber);
        });

        assertTrue(exception.getMessage().contains("Line 5"));
        assertNotNull(exception.getCause());
        assertInstanceOf(IllegalArgumentException.class, exception.getCause());
    }
}
