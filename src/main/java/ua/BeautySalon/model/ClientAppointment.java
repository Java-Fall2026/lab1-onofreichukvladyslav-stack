package ua.BeautySalon.model;

import ua.BeautySalon.util.BeautySalonUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class ClientAppointment {
    private final LocalDateTime createdAt;
    private final BeautyClient client;
    private final Master master;
    private final BeautyService service;
    private final LocalDate appointmentDate;
    private final LocalTime startTime;
    private AppointmentState state;

    public ClientAppointment(BeautyClient client, Master master, BeautyService service, LocalDate appointmentDate, LocalTime startTime, AppointmentState state){
        this.createdAt = LocalDateTime.now();
        this.client = BeautySalonUtils.validateNotNull(client, "Appointment client");
        this.master = BeautySalonUtils.validateNotNull(master, "Appointment master");
        this.service = BeautySalonUtils.validateNotNull(service, "Appointment service");

        BeautySalonUtils.validateCategoryMatch(this.master.category(), this.service.category());

        this.appointmentDate = BeautySalonUtils.validateNotNull(appointmentDate, "Appointment Date");
        this.startTime = BeautySalonUtils.validateNotNull(startTime, "Start Time");
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

    public AppointmentState getState() {
        return state;
    }

    public final void setState(AppointmentState state){
        this.state = BeautySalonUtils.validateNotNull(state, "Appointment State");
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
                "client=" + client.name() +
                ", master=" + master.fullName() +
                ", service=" + service.name() +
                ", appointmentDate=" + appointmentDate +
                ", startTime=" + startTime +
                ", state='" + state + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
