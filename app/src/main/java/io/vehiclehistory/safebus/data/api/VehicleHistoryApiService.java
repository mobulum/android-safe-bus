package io.vehiclehistory.safebus.data.api;

import io.vehiclehistory.safebus.config.ApplicationConfiguration;
import io.vehiclehistory.safebus.data.model.vehicle.VehicleResponse;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

public interface VehicleHistoryApiService {
    public static final String VEHICLE_HISTORY_RESOURCE = "/api/vehicle-history";

    @GET(VEHICLE_HISTORY_RESOURCE)
    @Headers({
            "Accept: " + ApplicationConfiguration.API_VERSION_1
    })
    Observable<VehicleResponse> getVehicleHistory(@Query("plate") String plate);
}
