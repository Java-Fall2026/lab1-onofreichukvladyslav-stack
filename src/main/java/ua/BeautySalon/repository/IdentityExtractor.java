package ua.BeautySalon.repository;

@FunctionalInterface
public interface IdentityExtractor<T> {
    String identityOf(T item);
}