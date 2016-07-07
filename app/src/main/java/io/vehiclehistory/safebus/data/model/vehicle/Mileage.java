package io.vehiclehistory.safebus.data.model.vehicle;

import java.io.Serializable;

public class Mileage implements Serializable {
    private String value;
    private MileageType type;

    public String getValue() {
        return value;
    }

    public MileageType getType() {
        return type;
    }
}
