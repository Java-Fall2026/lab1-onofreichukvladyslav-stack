package ua.BeautySalon.model;

import ua.common.BaseEntity;
import ua.BeautySalon.util.BeautySalonUtils;

import java.util.Objects;

public class BeautyService extends BaseEntity{
    private final String serviceCode;
    private final String name;
    private final String category;
    private final int durationMinutes;
    private double price;

    public BeautyService(String serviceCode, String name, String category, int durationMinutes, double price) {
        super();
        this.serviceCode = BeautySalonUtils.validateNotBlank(BeautySalonUtils.normalize(serviceCode), "Service code");
        this.name = BeautySalonUtils.validateNotBlank(BeautySalonUtils.normalize(name), "Service name");
        this.category = BeautySalonUtils.validateCategory(category);
        this.durationMinutes = BeautySalonUtils.validateDuration(durationMinutes);
        setPrice(price);
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public double getPrice() {
        return price;
    }

    public final void setPrice(double price){
        this.price = BeautySalonUtils.validatePositive(price, "Service price");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeautyService service = (BeautyService) o;
        return Objects.equals(serviceCode, service.serviceCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceCode);
    }

    @Override
    public String toString() {
        return "BeautyService{" +
                "Service Code: " + serviceCode + '\'' +
                ", Name: " + name + '\'' +
                ", Category: " + category + '\'' +
                ", Duration: " + durationMinutes + '\'' +
                ", Price: " + BeautySalonUtils.formatMoney(price) + '\'' +
                ", createdAt: " + createdAt +
                "}";
    }
}
