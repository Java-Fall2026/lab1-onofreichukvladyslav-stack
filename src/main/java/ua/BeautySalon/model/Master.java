package ua.BeautySalon.model;

import ua.BeautySalon.util.BeautySalonUtils;

import java.time.LocalDateTime;
import java.util.Objects;

public record Master (
        String employeeId,
        String fullName,
        ServiceCategory category,
        LocalDateTime createdAt
) {

    public Master {
        employeeId = BeautySalonUtils.validateNotBlank(BeautySalonUtils.normalize(employeeId), "Employee id");
        fullName = BeautySalonUtils.validateNotBlank(BeautySalonUtils.normalize(fullName), "Full Name");
        BeautySalonUtils.validateNotNull(category, "Master category");
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public static Master of(String employeeId, String fullName, ServiceCategory category){
        return new Master(employeeId, fullName, category, LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Master master = (Master) o;
        return Objects.equals(employeeId, master.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }
}
