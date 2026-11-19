package ua.BeautySalon.repository;

import ua.BeautySalon.model.BeautyClient;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SearchBenchmark {

    private static final int DATASET_SIZE = 100_000;
    private static final int WARMUP_ITERATIONS = 5;
    private static final int MEASURE_ITERATIONS = 15;

    public static void main(String[] args){
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        IdentityExtractor<BeautyClient> extractor = BeautyClient::phone;
        GenericRepository<BeautyClient> repository = new GenericRepository<>("BenchmarkClients", extractor);

        LocalDate baseDate = LocalDate.of(1995, 1, 1);
        for (int i = 0; i < DATASET_SIZE; i++){
            String phone = String.format("+38000%07d", i);
            repository.add(BeautyClient.of(phone, "Client " + i, baseDate));
        }

        List<BeautyClient> snapshotList = repository.findAll();

        String firstKey = String.format("+38000%07d", 0);
        String lastKey = String.format("+38000%07d", DATASET_SIZE - 1);
        String missingKey = "+380999999999";

        for (int i = 0; i < WARMUP_ITERATIONS; i++){
            runMapLookup(repository, firstKey);
            runMapLookup(repository, lastKey);
            runMapLookup(repository, missingKey);
            linearSearch(snapshotList, firstKey, extractor);
            linearSearch(snapshotList, lastKey, extractor);
            linearSearch(snapshotList, missingKey, extractor);
        }

        long mapFirstMinNs = measureMin(() -> runMapLookup(repository, firstKey));
        long listFirstMinNs = measureMin(() -> linearSearch(snapshotList, firstKey, extractor));

        long mapLastMinNs = measureMin(() -> runMapLookup(repository, lastKey));
        long listLastMinNs = measureMin(() -> linearSearch(snapshotList, lastKey, extractor));

        long mapMissingMinNs = measureMin(() -> runMapLookup(repository, missingKey));
        long listMissingMinNs = measureMin(() -> linearSearch(snapshotList, missingKey, extractor));

        System.out.println("=== Результати бенчмарку (100 000 елементів, мінімум із " + MEASURE_ITERATIONS + " прогонів) ===");
        System.out.printf("Перший елемент  | Map: %6d нс | List: %10d нс%n", mapFirstMinNs, listFirstMinNs);
        System.out.printf("Останній елемент | Map: %6d нс | List: %10d нс%n", mapLastMinNs, listLastMinNs);
        System.out.printf("Відсутній       | Map: %6d нс | List: %10d нс%n", mapMissingMinNs, listMissingMinNs);
    }

    private static Optional<BeautyClient> runMapLookup(GenericRepository<BeautyClient> repo, String key){
        return repo.findByIdentity(key);
    }

    private static long measureMin(Runnable task){
        long[] samples = new long[MEASURE_ITERATIONS];

        for (int i = 0; i < MEASURE_ITERATIONS; i++){
            long start = System.nanoTime();
            task.run();
            samples[i] = System.nanoTime() - start;
        }
        Arrays.sort(samples);

        return samples[0];
    }

    private static <E> Optional<E> linearSearch(List<E> items, String identity, IdentityExtractor<E> extractor){
        for (E item : items){
            if (identity.equals(extractor.identityOf(item))) return Optional.of(item);
        }
        return Optional.empty();
    }
}
