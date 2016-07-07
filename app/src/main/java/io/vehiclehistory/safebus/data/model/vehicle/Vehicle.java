package io.vehiclehistory.safebus.data.model.vehicle;

import java.io.Serializable;

public class Vehicle implements Serializable {
    private Name name;
    private VehicleType type;
    private Engine engine;
    private Plate plate;
    private String vin;
    private Production production;
    private Policy policy;
    private Boolean stolen;
    private Registration registration;
    private Inspection inspection;
    private Mileage mileage;

    public Name getName() {
        return name;
    }

    public VehicleType getType() {
        return type;
    }

    public Engine getEngine() {
        return engine;
    }

    public Plate getPlate() {
        return plate;
    }

    public String getVin() {
        return vin;
    }

    public Production getProduction() {
        return production;
    }

    public Policy getPolicy() {
        return policy;
    }

    public Boolean getStolen() {
        return stolen;
    }

    public Registration getRegistration() {
        return registration;
    }

    public Inspection getInspection() {
        return inspection;
    }

    public Mileage getMileage() {
        return mileage;
    }
}
