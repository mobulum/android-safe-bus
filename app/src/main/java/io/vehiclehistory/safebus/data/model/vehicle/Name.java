package io.vehiclehistory.safebus.data.model.vehicle;

import java.io.Serializable;

public class Name implements Serializable {
    private CarMake make;
    private String carName;
    private String model;

    public CarMake getMake() {
        return make;
    }

    public String getCarName() {
        return carName;
    }

    public String getModel() {
        return model;
    }
}
