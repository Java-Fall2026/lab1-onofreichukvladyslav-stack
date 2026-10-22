package ua.BeautySalon.model;

import ua.BeautySalon.util.BeautySalonUtils;

import java.time.LocalDateTime;

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
}
