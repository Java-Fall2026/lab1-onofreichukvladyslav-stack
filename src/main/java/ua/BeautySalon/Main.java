package ua.BeautySalon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.BeautySalon.loader.BeautyClientLoader;
import ua.BeautySalon.model.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        System.out.println("=== ДЕМОНСТРАЦІЯ ЛАБОРАТОРНОЇ РОБОТИ №3 ===");

        System.out.println("\n--- 1. Читання файлу з коректними й некоректними рядками ---");
        Path validPath = Path.of("data", "BeautyClients.csv");
        List<BeautyClient> loadedClients = BeautyClientLoader.loadClients(validPath);
        System.out.println("Успішно завантажено клієнтів з файлу: " + loadedClients.size());
        for (BeautyClient client : loadedClients) {
            System.out.println(" -> " + client);
        }

        System.out.println("\n--- 2. Спроба прочитати відсутній файл ---");
        Path missingPath = Path.of("data", "non_existent_file.csv");
        List<BeautyClient> emptyList = BeautyClientLoader.loadClients(missingPath);
        System.out.println("Результат читання відсутнього файлу (розмір списку): " + emptyList.size());

        System.out.println("\n--- 3. Некоректне значення в сеттері змінюваного класу і його обробка ---");
        BeautyService service = new BeautyService("SRV-01", "Стрижка", ServiceCategory.HAIRCUT, 45, 600.0);
        System.out.println("Початкова ціна послуги: " + service.getPrice());

        try {
            service.setPrice(-150.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Перехоплено очікуваний виняток сеттера: " + e.getMessage());
            System.out.println("Ціна залишилася незмінною: " + service.getPrice());
        }

        System.out.println("\n--- 4. Демонстрація логування трьох рівнів (INFO, WARN, ERROR) ---");
        log.info("Application Main started successfully.");
        log.warn("This is a demonstration warning message.");
        log.error("This is a demonstration error message for testing logs.");


        System.out.println("\n=== РОБОТУ ПРОГРАМИ ЗАВЕРШЕНО УСПІШНО ===");
    }
}
