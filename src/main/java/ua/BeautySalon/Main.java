package ua.BeautySalon;

import ua.BeautySalon.model.*;
import ua.BeautySalon.util.BeautySalonUtils;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args){
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        System.out.println("=== 1. Створення об'єктів конструктором та фабричними методами ===");
        BeautyClient client1 = BeautyClient.of("+380971112233", "Олена Коваль", LocalDate.of(1998, 5, 14));

        System.out.println("Телефон (компонент record): " + client1.phone());
        System.out.println("Ім'я (компонент record): " + client1.name());
        System.out.println("Дата народження: " + client1.birthDate());

        System.out.println("\n=== 2. Enum у роботі ===");
        System.out.println("Перебір усіх категорій послуг (values()):");
        for (ServiceCategory cat : ServiceCategory.values()) {
            System.out.println(" - " + cat + " | Ціновий коефіцієнт (власне поле): " + cat.getPriceFactor());
        }

        try {
            System.out.println("\nСпроба викликати ServiceCategory.valueOf(\"WRONG_CATEGORY\"):");
            ServiceCategory.valueOf("WRONG_CATEGORY");
        } catch (IllegalArgumentException e) {
            System.out.println("Перехоплено виняток для некоректного enum-рядка: " + e.getMessage());
        }

        System.out.println("\n=== 3. Обидва switch-вирази ===");
        Master master1 = Master.of("M-101", "Анна Ткаченко", ServiceCategory.HAIRCUT);
        BeautyService service1 = new BeautyService("SRV-01", "Жіноча стрижка", ServiceCategory.HAIRCUT, 60, 850);

        String catDesc = BeautySalonUtils.getCategoryDescription(service1.category());
        System.out.println("Опис категорії (" + service1.category() + "): " + catDesc);

        AppointmentState currentState = AppointmentState.CONFIRMED;
        String actionInstruction = BeautySalonUtils.getActionInstruction(currentState);
        System.out.println("Інструкція за статусом (" + currentState + "): " + actionInstruction);
        
        System.out.println("\n=== 4. Record із невалідними даними IllegalArgumentException ===");
        try {
            new BeautyService("SRV-99", "Експрес-догляд", ServiceCategory.FACIAL, 5, 300);
        } catch (IllegalArgumentException e){
            System.out.println("Виняток з компактного конструктора: " + e.getMessage());
        }

        System.out.println("\n=== 5. Два однакові record-об'єкти: equals та hashCode ===");
        LocalDateTime fixedTime = LocalDateTime.of(2026, 1, 1, 12, 0);

        BeautyClient c1 = new BeautyClient("+380971112233", "Олена Мельничук", LocalDate.of(1995, 3, 10), fixedTime);
        BeautyClient c2 = new BeautyClient("+380971112233", "Олена Мельничук", LocalDate.of(1995, 3, 10), fixedTime);

        System.out.println("c1 == c2: " + (c1 == c2));
        System.out.println("client1.equals(c2): " + c1.equals(c2));
        System.out.println("client1.hashCode() == c2.hashCode(): " + (c1.hashCode() == c2.hashCode()));

        System.out.println("\n=== 6. Виклик обчислювальних методів у BeautySalonUtils ===");
        System.out.println("Клієнт: (record)" + client1);
        System.out.println("Майстер: (record)" + master1);
        System.out.println("Послуга: (record)" + service1);
        
        ClientAppointment appointment = new ClientAppointment(client1, master1, service1, LocalDate.now().plusDays(1), LocalTime.of(14, 0), AppointmentState.CONFIRMED);
        System.out.println("Запис (class): " + appointment);
    }
}
