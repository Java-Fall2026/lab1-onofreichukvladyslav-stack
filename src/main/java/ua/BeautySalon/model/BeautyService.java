package ua.BeautySalon.model;

import ua.BeautySalon.util.BeautySalonUtils;

import java.time.LocalDateTime;


public record BeautyService (
        String serviceCode,
        String name,
        ServiceCategory category,
        int durationMinutes,
        double price,
        LocalDateTime createdAt
) {


    public BeautyService {
        serviceCode = BeautySalonUtils.validateNotBlank(BeautySalonUtils.normalize(serviceCode), "Service code");
        name = BeautySalonUtils.validateNotBlank(BeautySalonUtils.normalize(name), "Service name");
        BeautySalonUtils.validateNotNull(category, "Service category");
        durationMinutes = BeautySalonUtils.validateDuration(durationMinutes);
        price = BeautySalonUtils.validatePositive(price, "Service price");
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public BeautyService(String serviceCode, String name, ServiceCategory category, int durationMinutes, double price) {
        this(serviceCode, name, category, durationMinutes, price, LocalDateTime.now());
    }
}
