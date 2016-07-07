package io.vehiclehistory.safebus.data.model.vehicle;

import java.io.Serializable;

public class Registration implements Serializable {
    private RegistrationStatus status;
    private String firstAt;

    public RegistrationStatus getStatus() {
        return status;
    }

    public String getFirstAt() {
        return firstAt;
    }
}
