package ua.BeautySalon.model;

import ua.common.BaseEntity;
import ua.BeautySalon.util.BeautySalonUtils;

import java.util.Objects;

public class Master extends BaseEntity{
    private final String employeeId;
    private final String fullName;
    private final String category;

    private Master(String employeeId, String fullName, String category) {
        super();
        this.employeeId = BeautySalonUtils.validateNotBlank(BeautySalonUtils.normalize(employeeId), "Employee id");
        this.fullName = BeautySalonUtils.validateNotBlank(BeautySalonUtils.normalize(fullName), "Full Name");
        this.category = BeautySalonUtils.validateCategory(category);
    }

    public static Master of(String employeeId, String fullName, String category){
        return new Master(employeeId, fullName, category);
    }

    public String getEmployeeId(){
        return employeeId;
    }

    public String getFullName(){
        return fullName;
    }

    public String getCategory(){
        return category;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Master master = (Master) o;
        return Objects.equals(employeeId, master.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }

    @Override
    public String toString() {
        return "Master{" +
                "EmployeeId: " + employeeId + '\'' +
                ", FullName: " + fullName + '\'' +
                ", Category: " + category + '\'' +
                ", createdAt: " + createdAt +
                "}";
    }
}
