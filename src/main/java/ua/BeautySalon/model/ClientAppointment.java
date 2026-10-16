package ua.BeautySalon.model;

import ua.BeautySalon.util.BeautySalonUtils;
import ua.common.BaseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class ClientAppointment extends BaseEntity{
    private final BeautyClient client;
    private final Master master;
    private final BeautyService service;
    private final LocalDate appointmentDate;
    private final LocalTime startTime;
    private String state;

    public ClientAppointment(BeautyClient client, Master master, BeautyService service, LocalDate appointmentDate, LocalTime startTime, String state){
        super();
        this.client = BeautySalonUtils.validateNotNull(client, "Appointment client");
        this.master = BeautySalonUtils.validateNotNull(master, "Appointment master");
        this.service = BeautySalonUtils.validateNotNull(service, "Appointment service");

        BeautySalonUtils.validateCategoryMatch(this.master.getCategory(), this.service.getCategory());

        this.appointmentDate = BeautySalonUtils.validateNotNull(appointmentDate, "Appointment Date");
        this.startTime = BeautySalonUtils.validateNotNull(startTime, "Start Time");
        setState(state);
    }

    public BeautyClient getClient() {
        return client;
    }

    public Master getMaster() {
        return master;
    }

    public BeautyService getService() {
        return service;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public String getState() {
        return state;
    }

    public final void setState(String state){
        this.state = BeautySalonUtils.validateState(state);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientAppointment that = (ClientAppointment) o;
        return Objects.equals(client, that.client) && Objects.equals(master, that.master) && Objects.equals(appointmentDate, that.appointmentDate) && Objects.equals(startTime, that.startTime);
    }

    @Override
    public int hashCode(){
        return Objects.hash(client, master, appointmentDate, startTime);
    }

    @Override
    public String toString() {
        return "ClientAppointment{" +
                "client=" + client.getName() +
                ", master=" + master.getFullName() +
                ", service=" + service.getName() +
                ", appointmentDate=" + appointmentDate +
                ", startTime=" + startTime +
                ", state='" + state + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
