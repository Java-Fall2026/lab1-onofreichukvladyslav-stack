package ua.BeautySalon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.BeautySalon.exception.DuplicateIdentityException;
import ua.BeautySalon.loader.BeautyClientLoader;
import ua.BeautySalon.model.*;
import ua.BeautySalon.repository.GenericRepository;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        System.out.println("=== ДЕМОНСТРАЦІЯ ЛАБОРАТОРНОЇ РОБОТИ №4 ===");

        System.out.println("\n--- 1. Три репозиторії різних типів і пошук за identity ---");

        GenericRepository<BeautyClient> clientRepo = new GenericRepository<>("Clients", BeautyClient::phone);
        GenericRepository<Master> masterRepo = new GenericRepository<>("Masters", Master::employeeId);
        GenericRepository<BeautyService> serviceRepo = new GenericRepository<>("Services", BeautyService::getServiceCode);

        BeautyClient client1 = BeautyClient.of("+380971112233", "Олена Коваль", LocalDate.of(1998, 5, 14));
        Master master1 = Master.of("M-101", "Анна Ткаченко", ServiceCategory.HAIRCUT);
        BeautyService service1 = new BeautyService("SRV-01", "Жіноча стрижка", ServiceCategory.HAIRCUT, 60, 850.0);

        clientRepo.add(client1);
        masterRepo.add(master1);
        serviceRepo.add(service1);

        System.out.println("Пошук клієнта (наявний): " + clientRepo.findByIdentity("+380971112233"));
        System.out.println("Пошук клієнта (відсутній): " + clientRepo.findByIdentity("+380000000000"));
        System.out.println("Пошук майстра (наявний): " + masterRepo.findByIdentity("M-101"));
        System.out.println("Пошук послуги (відсутній): " + serviceRepo.findByIdentity("SRV-999"));


        System.out.println("\n--- 2. Наповнення репозиторію з CSV-файла ---");
        GenericRepository<BeautyClient> csvClientRepo = new GenericRepository<>("CsvClients", BeautyClient::phone);
        int importedCount = BeautyClientLoader.loadClientsIntoRepository(Path.of("data", "BeautyClients.csv"), csvClientRepo);
        System.out.println("Додано нових клієнтів із CSV: " + importedCount + ", всього в репозиторії: " + csvClientRepo.size());

        System.out.println("\n--- 3. Дублікат через add vs addIfAbsent ---");
        BeautyClient duplicateClient = BeautyClient.of("+380971112233", "Олена Інша", LocalDate.of(1995, 2,2));


        try {
            clientRepo.add(duplicateClient);
        } catch (DuplicateIdentityException e) {
            System.out.println("add кинув вийняток: " + e.getMessage());
        }

        boolean inserted = clientRepo.addIfAbsent(duplicateClient);
        System.out.println("addIfAbsent() повернув: " + inserted + ", розмір репозиторія: " + clientRepo.size());

        System.out.println("\n--- 4. Видалення наявного та відсутнього елемента ---");
        boolean removedExisting = clientRepo.removeByIdentity("+380971112233");
        boolean removedMissing = clientRepo.removeByIdentity("+380999999999");
        System.out.println("Видалення наявного клієнта: " + removedExisting + ", розмір репозиторія: " + clientRepo.size());
        System.out.println("Видалення відсутнього клієнта: " + removedMissing + ", розмір репозиторія: " + clientRepo.size());

        System.out.println("\n--- 5. Демонстрація логування трьох рівнів (INFO, WARN, ERROR) ---");
        log.info("Demo INFO log: repository sizes checked.");
        log.warn("Demo WARN log: duplicate or missing record condition.");
        try {
            ClientAppointment invalidAppointment = new ClientAppointment(
                    BeautyClient.of("+380971112233", "Олена", LocalDate.of(1998, 5, 14)),
                    master1,
                    new BeautyService("SRV-02", "Масаж", ServiceCategory.MASSAGE, 60, 1000),
                    LocalDate.now(),
                    LocalTime.of(11, 0),
                    AppointmentState.CONFIRMED
            );
            System.out.println(invalidAppointment);
        } catch (IllegalArgumentException e){
            log.error("Demo ERROR log: propagating validation failure -> {}", e.getMessage(), e);
        }
        System.out.println("\n=== РОБОТУ ПРОГРАМИ ЗАВЕРШЕНО УСПІШНО ===");
    }
}
