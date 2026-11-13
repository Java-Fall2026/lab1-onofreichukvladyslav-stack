package ua.BeautySalon.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.BeautySalon.exception.InvalidDataException;
import ua.BeautySalon.model.BeautyClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


public class BeautyClientLoader {
    private static final Logger log = LoggerFactory.getLogger(BeautyClientLoader.class);

    public static List<BeautyClient> loadClients(Path filePath){
        List<BeautyClient> clients = new ArrayList<>();

        log.info("Starting to load clients from file: {}", filePath);

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)){
            String line;
            int lineNumber = 0;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null){
                lineNumber++;
                if (isHeader){
                    isHeader = false;
                    continue;
                }

                if (line.isBlank()){
                    continue;
                }

                try{
                    BeautyClient client = parseClientLine(line, lineNumber);
                    clients.add(client);
                } catch (InvalidDataException e){
                    log.error("Error at line {}: {}. Skipping row.", lineNumber, e.getMessage());
                }
            }
        } catch (NoSuchFileException e){
            log.warn("File not found at path: {}. Skipping loading", filePath);
        } catch (IOException e){
            log.error("I/O error occurred while reading file {}: {}", filePath, e.getMessage(), e);
        } finally{
            log.info("Finished processing file: {}. Successfully loaded {} clients.", filePath, clients.size());
        }

        return clients;
    }

    public static BeautyClient parseClientLine(String line, int lineNumber) throws InvalidDataException{
        String[] parts = line.split(",", -1);
        if(parts.length < 3){
            throw new InvalidDataException("Line " + lineNumber + ": Insufficient colums, expected 3, got " + parts.length);
        }

        String phone = parts[0].trim();
        String name = parts[1].trim();
        LocalDate birthDate;

        try {
            birthDate = LocalDate.parse(parts[2].trim());
        } catch (DateTimeParseException e){
            throw new InvalidDataException("Line " + lineNumber + ": Invalid date format for birthDate, got: '" + parts[2] + "'", e);
        }

        try {
            return BeautyClient.of(phone, name, birthDate);
        } catch (IllegalArgumentException e){
            throw new InvalidDataException("Line " + lineNumber + ": Validation failed -> " + e.getMessage(), e);
        }
    }
}
