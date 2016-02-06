package io.vehiclehistory.safebus.data.model.vehicle;

import java.io.Serializable;

import io.vehiclehistory.safebus.R;

public enum PolicyStatus implements Serializable {
    UNKNOWN(R.string.inspection_status_unknown),
    UPTODATE(R.string.inspection_status_up_to_date),
    OUTDATED(R.string.inspection_status_outdated);

    private int valueResource;

    PolicyStatus(int valueResource) {
        this.valueResource = valueResource;
    }

    public int getValueResource() {
        return valueResource;
    }
}
