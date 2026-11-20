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
    private static final int MAP_BATCH_SIZE = 10_000;
    private static final int LIST_BATCH_SIZE = 100;
    private static final int WARMUP_BATCHES = 5;
    private static final int MEASURE_BATCHES = 15;

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

        for (int i = 0; i < WARMUP_BATCHES; i++){
            runMapBatch(repository, firstKey, MAP_BATCH_SIZE);
            runMapBatch(repository, lastKey, MAP_BATCH_SIZE);
            runMapBatch(repository, missingKey, MAP_BATCH_SIZE);

            runListBatch(snapshotList, firstKey, extractor, LIST_BATCH_SIZE);
            runListBatch(snapshotList, lastKey, extractor, LIST_BATCH_SIZE);
            runListBatch(snapshotList, missingKey, extractor, LIST_BATCH_SIZE);
        }

        double mapFirstNs = measureMinPerOpNs(() -> runMapBatch(repository, firstKey, MAP_BATCH_SIZE), MAP_BATCH_SIZE);
        double listFirstNs = measureMinPerOpNs(() -> runListBatch(snapshotList, firstKey, extractor, LIST_BATCH_SIZE), LIST_BATCH_SIZE);

        double mapLastNs = measureMinPerOpNs(() -> runMapBatch(repository, lastKey, MAP_BATCH_SIZE), MAP_BATCH_SIZE);
        double listLastNs = measureMinPerOpNs(() -> runListBatch(snapshotList, lastKey, extractor, LIST_BATCH_SIZE), LIST_BATCH_SIZE);

        double mapMissingNs = measureMinPerOpNs(() -> runMapBatch(repository, missingKey, MAP_BATCH_SIZE), MAP_BATCH_SIZE);
        double listMissingNs = measureMinPerOpNs(() -> runListBatch(snapshotList, missingKey, extractor, LIST_BATCH_SIZE), LIST_BATCH_SIZE);

        System.out.println("=== Результати бенчмарку (100 000 елементів, мінімум із " + MEASURE_BATCHES + " прогонів) ===");
        System.out.printf("Перший елемент  | Map: %8.2f нс | List: %12.2f нс%n", mapFirstNs, listFirstNs);
        System.out.printf("Останній елемент | Map: %8.2f нс | List: %12.2f нс%n", mapLastNs, listLastNs);
        System.out.printf("Відсутній       | Map: %8.2f нс | List: %12.2f нс%n", mapMissingNs, listMissingNs);
    }

    private static void runMapBatch(GenericRepository<BeautyClient> repo, String key, int batchSize) {
        Optional<BeautyClient> lastResult = Optional.empty();
        for (int i = 0; i < batchSize; i++) {
            lastResult = repo.findByIdentity(key);
        }
        if (lastResult == null) {
            throw new AssertionError("Unreachable");
        }
    }

    private static void runListBatch(
            List<BeautyClient> items,
            String key,
            IdentityExtractor<BeautyClient> extractor,
            int batchSize
    ) {
        Optional<BeautyClient> lastResult = Optional.empty();
        for (int i = 0; i < batchSize; i++) {
            lastResult = linearSearch(items, key, extractor);
        }
        if (lastResult == null) {
            throw new AssertionError("Unreachable");
        }
    }

    private static double measureMinPerOpNs(Runnable batchTask, int batchSize) {
        long[] perOpSamples = new long[MEASURE_BATCHES];
        for (int i = 0; i < MEASURE_BATCHES; i++) {
            long start = System.nanoTime();
            batchTask.run();
            long elapsedNs = System.nanoTime() - start;
            perOpSamples[i] = elapsedNs / batchSize;
        }
        Arrays.sort(perOpSamples);
        return perOpSamples[0];
    }
    private static <E> Optional<E> linearSearch(List<E> items, String identity, IdentityExtractor<E> extractor) {
        for (E item : items) {
            if (identity.equals(extractor.identityOf(item))) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }
}
