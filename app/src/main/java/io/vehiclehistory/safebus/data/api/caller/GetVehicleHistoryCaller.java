package io.vehiclehistory.safebus.data.api.caller;

import javax.inject.Inject;

import io.vehiclehistory.safebus.components.NetworkStateManager;
import io.vehiclehistory.safebus.data.DataManager;
import io.vehiclehistory.safebus.data.model.vehicle.VehicleResponse;
import io.vehiclehistory.safebus.ui.view.VehicleMvpView;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class GetVehicleHistoryCaller extends BaseCaller<VehicleMvpView> {

    private String plate;

    @Inject
    public GetVehicleHistoryCaller(
            NetworkStateManager networkStateManager,
            DataManager dataManager,
            Retrofit retrofit
    ) {
        super(networkStateManager, dataManager, retrofit);
    }

    public void getVehicleHistory(String plate) {
        this.plate = plate;
        resetRetry();
        checkViewAttached();
        preCall();
    }

    @Override
    public Subscription call() {
        return getDataManager().getVehicleHistory(plate)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //nop
                    }
                })
                .subscribe(new Subscriber<VehicleResponse>() {
                    @Override
                    public void onCompleted() {
                        //nop
                    }

                    @Override
                    public void onError(Throwable e) {
                        handleApiError(e);
                    }

                    @Override
                    public void onNext(VehicleResponse vehicleResponse) {
                        getMvpView().onVehicleFinished(vehicleResponse);
                        getMvpView().finishedLoadingData();
                    }
                });
    }
}
