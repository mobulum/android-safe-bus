package io.vehiclehistory.safebus.data.api;

import io.vehiclehistory.safebus.config.ApplicationConfiguration;
import io.vehiclehistory.safebus.data.model.vehicle.VehicleResponse;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;

public interface VehicleHistoryApiService {
    String SAFE_BUS_RESOURCE = "/api/bus-reports";

    @GET(SAFE_BUS_RESOURCE + "/{plate}")
    @Headers({
            "Accept: " + ApplicationConfiguration.API_VERSION_1
    })
    Observable<VehicleResponse> getVehicleHistory(@Path("plate") String plate);
}
