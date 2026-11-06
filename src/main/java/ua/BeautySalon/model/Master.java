package ua.BeautySalon.model;

import ua.BeautySalon.util.BeautySalonUtils;


public record Master (
        String employeeId,
        String fullName,
        ServiceCategory category
) {

    public Master {
        employeeId = BeautySalonUtils.validateNotBlank(BeautySalonUtils.normalize(employeeId), "Employee id");
        fullName = BeautySalonUtils.validateNotBlank(BeautySalonUtils.normalize(fullName), "Full Name");
        BeautySalonUtils.validateNotNull(category, "Master category");
    }

    public static Master of(String employeeId, String fullName, ServiceCategory category){
        return new Master(employeeId, fullName, category);
    }
}
