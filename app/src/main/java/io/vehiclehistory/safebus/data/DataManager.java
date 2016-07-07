package io.vehiclehistory.safebus.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.vehiclehistory.safebus.data.api.VehicleHistoryApiService;
import io.vehiclehistory.safebus.data.api.auth.OnAuthCallback;
import io.vehiclehistory.safebus.data.model.vehicle.VehicleResponse;
import rx.Observable;

@Singleton
public class DataManager {

    private final VehicleHistoryApiService apiService;
    private final AuthProvider authProvider;

    @Inject
    public DataManager(
            VehicleHistoryApiService apiService,
            AuthProvider authProvider
    ) {
        this.apiService = apiService;
        this.authProvider = authProvider;
    }

    public Observable<VehicleResponse> getVehicleHistory(String plate) {
        return apiService.getVehicleHistory(plate);
    }

    public void getNewSession(OnAuthCallback onFinishedListener) {
        authProvider.getNewSession(onFinishedListener);
    }
}
