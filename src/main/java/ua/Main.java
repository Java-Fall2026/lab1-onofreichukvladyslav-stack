package ua;

import ua.BeautySalon.model.BeautyClient;
import ua.BeautySalon.model.BeautyService;
import ua.BeautySalon.model.ClientAppointment;
import ua.BeautySalon.model.Master;
import ua.BeautySalon.util.BeautySalonUtils;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args){
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.out.println("=== 1. Створення об'єктів конструктором та фабричними методами ===");
        BeautyClient client1 = BeautyClient.of("+380971112233", "Олена Коваль", LocalDate.of(1998, 5, 14));
        Master master1 = Master.of("M-101", "Анна Ткаченко", "HAIRCUT");

        BeautyService service1 = new BeautyService("SRV-01", "Жіноча стрижка", "HAIRCUT", 60, 850);
        ClientAppointment appointment1 = new ClientAppointment(client1, master1, service1, LocalDate.now().plusDays(1), LocalTime.of(14, 0), "CONFIRMED");

        System.out.println("Клієнт: " + client1);
        System.out.println("Майстер: " + master1);
        System.out.println("Послуга: " + service1);
        System.out.println("Запис: " + appointment1);

        System.out.println("\n=== 2. Нормалізація в дії ===");
        String rawPhone = "   +380979998877   ";
        String rawName = "  Ольга Сидоренко  ";
        BeautyClient clientNormalized = BeautyClient.of(rawPhone, rawName, LocalDate.of(2000, 1, 1));
        System.out.println("Очищені поля клієнта: phone='" + clientNormalized.getPhone() + "', name='" + clientNormalized.getName() + "'");

        System.out.println("\n=== 3. Три різні порушення правил у try/catch ===");

        try {
            BeautyClient.of("+380501112233", "Марічка", LocalDate.now().minusYears(10));
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка 1: " + e.getMessage());
        }

        try {
            new BeautyService("SRV-99", "Масаж спини", "MASSAGE", 45, -200);
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка 2: " + e.getMessage());
        }

        try {
            BeautyService massageService = new BeautyService("SRV-02", "Масаж спини", "MASSAGE", 60, 1200);
            new ClientAppointment(client1, master1, massageService, LocalDate.now(), LocalTime.of(10, 0), "CONFIRMED");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка 3 : " + e.getMessage());
        }

        System.out.println("\n=== 4. Спроба зіпсувати некоректним значенням через сеттер ===");
        try {
            System.out.println("Початкова ціна: " + service1.getPrice());
        } catch (IllegalArgumentException e){
            System.out.println("Перехоплено виняток при спробі встановити ціну 0: " + e.getMessage());
            System.out.println("Ціна після невдалої зміни лишилася: " + service1.getPrice());
        }

        System.out.println("\n=== 5. Порівняння об'єктів (==, equals, hashCode) ===");
        BeautyClient client1SameKey = BeautyClient.of("+380971112233", "Олена Мельничук", LocalDate.of(1995, 3, 10));
        BeautyClient clientDifferentKey = BeautyClient.of("+380509990011", "Ірина", LocalDate.of(1998, 5, 14));

        System.out.println("client1 == client1SameKey: " + (client1 == client1SameKey));
        System.out.println("client1.equals(client1SameKey): " + client1.equals(client1SameKey));
        System.out.println("client1.hashCode() == client1SameKey.hashCode(): " + (client1.hashCode() == client1SameKey.hashCode()));
        System.out.println("client1.equals(clientDifferentKey): " + client1.equals(clientDifferentKey));

        System.out.println("\n=== 6. Виклик обчислювальних методів у BeautySalonUtils ===");
        LocalTime endTime = BeautySalonUtils.calculateEndTime(appointment1);
        double pricePerMin = BeautySalonUtils.calculatePricePerMinute(service1);

        System.out.println("Час завершення візиту: " + endTime);
        System.out.println("Вартість послуги за хвилину: " + BeautySalonUtils.formatMoney(pricePerMin));

        System.out.println("\n=== 7. Вивід усіх сутностей через toString() ===");
        System.out.println(client1);
        System.out.println(master1);
        System.out.println(service1);
        System.out.println(appointment1);

        System.out.println("\n=== 8. Закоментовані рядки, що не компілюються ===");
        // BeautyClient badClient = new BeautyClient("+380", "Test", LocalDate.now());
        // КОМЕНТАР: Не компілюється, оскільки конструктор BeautyClient є private (використовується фабричний метод of).

        // ValidationHelper.requireNotNull(client1, "test");
        // КОМЕНТАР: Не компілюється, оскільки клас ValidationHelper має package-private доступ і невидимий з пакета ua.beauty.
    }
}
