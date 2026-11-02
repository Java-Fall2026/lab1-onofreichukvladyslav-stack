package ua.BeautySalon.model;

import ua.BeautySalon.util.BeautySalonUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public record BeautyClient (
        String phone,
        String name,
        LocalDate birthDate,
        LocalDateTime createdAt
) {

    public BeautyClient {
        phone = BeautySalonUtils.validateNotBlank(BeautySalonUtils.normalize(phone), "Client phone");
        name = BeautySalonUtils.validateNotBlank(BeautySalonUtils.normalize(name), "Client name");
        birthDate = BeautySalonUtils.validateBirthDate(birthDate);
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public static BeautyClient of(String phone, String name, LocalDate birthDate){
        return new BeautyClient(phone, name, birthDate, LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeautyClient that = (BeautyClient) o;
        return Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone);
    }
}
