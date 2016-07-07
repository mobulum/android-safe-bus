package io.vehiclehistory.safebus.data.model.vehicle;

import java.io.Serializable;

public class Engine implements Serializable {
    private String cubicCapacity;
    private FuelType fuel;

    public String getCubicCapacity() {
        return cubicCapacity;
    }

    public FuelType getFuel() {
        return fuel;
    }
}
