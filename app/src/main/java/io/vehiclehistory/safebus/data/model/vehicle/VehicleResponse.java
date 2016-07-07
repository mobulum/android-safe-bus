package io.vehiclehistory.safebus.data.model.vehicle;

import java.io.Serializable;
import java.util.List;

public class VehicleResponse implements Serializable {
    private Vehicle vehicle;
    private List<Event> events;

    public Vehicle getVehicle() {
        return vehicle;
    }

    public List<Event> getEvents() {
        return events;
    }
}
