package ua.BeautySalon.model;

import ua.BeautySalon.util.BeautySalonUtils;

import java.time.LocalDateTime;
import java.util.Objects;


public class BeautyService {
    private final LocalDateTime createdAt;
    private final String serviceCode;
    private final String name;
    private final ServiceCategory category;
    private final int durationMinutes;
    private double price;

    public BeautyService (String serviceCode, String name, ServiceCategory category, int durationMinutes, double price){
        this.createdAt = LocalDateTime.now();
        this.serviceCode = BeautySalonUtils.validateNotBlank(BeautySalonUtils.normalize(serviceCode), "Service code");
        this.name = BeautySalonUtils.validateNotBlank(BeautySalonUtils.normalize(name), "Service name");
        this.category = BeautySalonUtils.validateNotNull(category, "Service category");
        this.durationMinutes = BeautySalonUtils.validateDuration(durationMinutes);
        this.price = BeautySalonUtils.validatePositive(price, "Service price");
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getName() {
        return name;
    }

    public ServiceCategory getCategory() {
        return category;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = BeautySalonUtils.validatePositive(price, "Service price");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeautyService that = (BeautyService) o;
        return Objects.equals(serviceCode, that.serviceCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceCode);
    }

    @Override
    public String toString() {
        return "Beauty Service{" +
                "serviceCode='" + serviceCode + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", durationMinutes=" + durationMinutes +
                ", price=" + BeautySalonUtils.formatMoney(price) +
                ", createdAt=" + createdAt +
                '}';
    }
}
