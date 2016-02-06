package io.vehiclehistory.safebus.data.model.vehicle;

import java.io.Serializable;

public class Inspection implements Serializable {
    private InspectionStatus status;

    public InspectionStatus getStatus() {
        return status;
    }
}
