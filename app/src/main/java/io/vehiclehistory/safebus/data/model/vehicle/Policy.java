package io.vehiclehistory.safebus.data.model.vehicle;

import java.io.Serializable;

public class Policy implements Serializable {
    private PolicyStatus status;

    public PolicyStatus getStatus() {
        return status;
    }
}
