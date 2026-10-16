package ua.BeautySalon.model;

import ua.common.BaseEntity;
import ua.BeautySalon.util.BeautySalonUtils;

import java.time.LocalDate;
import java.util.Objects;

public class BeautyClient extends BaseEntity{
    private final String phone;
    private final String name;
    private final LocalDate birthDate;

    private BeautyClient(String phone, String name, LocalDate birthDate){
        super();
        this.phone = BeautySalonUtils.validateNotBlank(BeautySalonUtils.normalize(phone), "Client phone");
        this.name = BeautySalonUtils.validateNotBlank(BeautySalonUtils.normalize(name), "Client name");
        this.birthDate = BeautySalonUtils.validateBirthDate(birthDate);
    }

    public static BeautyClient of(String phone, String name, LocalDate birthDate){
        return new BeautyClient(phone, name, birthDate);
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeautyClient that = (BeautyClient) o;
        return Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode(){
        return Objects.hash(phone);
    }

    @Override
    public String toString(){
        return "BeautyClient{" +
                "phone: " + phone + '\'' +
                ", name: " + name + '\'' +
                ", birthDate: " + birthDate +
                ", createdAt: " + createdAt +
                '}';
    }
}
