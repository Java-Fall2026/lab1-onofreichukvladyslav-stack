package ua.BeautySalon.model;

import ua.BeautySalon.util.BeautySalonUtils;

import java.time.LocalDate;

public record BeautyClient (
        String phone,
        String name,
        LocalDate birthDate
) {

    public BeautyClient {
        phone = BeautySalonUtils.validateNotBlank(BeautySalonUtils.normalize(phone), "Client phone");
        name = BeautySalonUtils.validateNotBlank(BeautySalonUtils.normalize(name), "Client name");
        birthDate = BeautySalonUtils.validateBirthDate(birthDate);
    }

    public static BeautyClient of(String phone, String name, LocalDate birthDate){
        return new BeautyClient(phone, name, birthDate);
    }
}
