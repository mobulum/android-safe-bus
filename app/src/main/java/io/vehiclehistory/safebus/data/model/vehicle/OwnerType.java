package io.vehiclehistory.safebus.data.model.vehicle;

import java.io.Serializable;

import io.vehiclehistory.safebus.R;

public enum OwnerType implements Serializable {
    UNKNOWN(0),
    PRIVATE(R.string.owner_type_private),
    COMPANY(R.string.owner_type_company);

    private int valueResource;

    OwnerType(int valueResource) {
        this.valueResource = valueResource;
    }

    public int getValueResource() {
        return valueResource;
    }
}
