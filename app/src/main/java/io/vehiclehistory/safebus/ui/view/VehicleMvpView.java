package io.vehiclehistory.safebus.ui.view;

import io.vehiclehistory.safebus.data.model.vehicle.VehicleResponse;

public interface VehicleMvpView  extends MvpView {
    void onVehicleFinished(VehicleResponse vehicleResponse);
}
