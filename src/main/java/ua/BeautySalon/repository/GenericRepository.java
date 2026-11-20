package ua.BeautySalon.repository;

import org.slf4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.BeautySalon.exception.DuplicateIdentityException;

import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class GenericRepository<T> {
    private static final Logger log = LoggerFactory.getLogger(GenericRepository.class);

    private final String name;
    private final IdentityExtractor<T> extractor;
    private final Map<String, T> storage = new LinkedHashMap<>();

    public GenericRepository(String name, IdentityExtractor<T> extractor){
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Repository name must be not blank");
        this.name = name.trim();
        this.extractor = Objects.requireNonNull(extractor, "Identity extractor must be not null");
    }

    public String getName(){
        return name;
    }

    public void add(T item){
        String identity = requireValidIdentity(item);

        if (storage.containsKey(identity)){
            String message = "Duplicate identity '" + identity + "' in repository '" + name + "'";
            log.error("Duplicate identity '{}' detected in repository '{}', throwing DuplicateIdentityException", identity, name);
            throw new DuplicateIdentityException(message);
        }

        storage.put(identity, item);
        log.info("Added item with identity '{}' to repository '{}'", identity, name);
    }

    public boolean addIfAbsent(T item){
        String identity = requireValidIdentity(item);

        if (storage.containsKey(identity)){
            log.warn("Skipped duplicate via addIfAbsent in repository '{}': identity='{}'", name, identity);
            return false;
        }

        storage.put(identity, item);
        log.info("Added item via addIfAbsent with identity '{}' to repository '{}'", identity, name);
        return true;
    }

    public void addAll(Collection<? extends T> items){
        Objects.requireNonNull(items, "Items collection must be not null");
        for (T item : items) add(item);
    }

    public boolean removeByIdentity(String identity){
        if (identity == null || identity.isBlank()){
            log.warn("Attempted to remove item with blank identity from repository '{}'", name);
            return false;
        }

        T removed = storage.remove(identity);
        if (removed == null){
            log.warn("Item with identity '{}' not found for removal in repository '{}'", identity, name);
            return false;
        }

        log.info("Removed item with identity '{}' from repository '{}'", identity, name);
        return true;
    }

    public Optional<T> findByIdentity(String identity){
        if (identity == null || identity.isBlank()) {
            log.warn("Lookup with blank identity in repository '{}'", name);
            return Optional.empty();
        }
        T found = storage.get(identity);
        if (found == null){
            log.warn("Item with identity '{}' not found in repository '{}'", identity, name);
            return Optional.empty();
        }

        return Optional.of(found);
    }

    public List<T> findAll(){
        return Collections.unmodifiableList(new ArrayList<>(storage.values()));
    }

    public int size(){
        return storage.size();
    }

    private String requireValidIdentity(T item){
        if (item == null) throw new IllegalArgumentException("Entity must not be null");

        String identity = extractor.identityOf(item);
        if (identity == null || identity.isBlank()) throw new IllegalArgumentException("Extracted identity must not be blank");

        return identity;
    }
}
