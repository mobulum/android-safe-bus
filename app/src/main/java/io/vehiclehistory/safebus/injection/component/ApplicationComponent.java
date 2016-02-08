package io.vehiclehistory.safebus.injection.component;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;

import javax.inject.Singleton;

import dagger.Component;
import io.vehiclehistory.safebus.SafeBusApp;
import io.vehiclehistory.safebus.components.NetworkStateManager;
import io.vehiclehistory.safebus.data.AuthProvider;
import io.vehiclehistory.safebus.data.DataManager;
import io.vehiclehistory.safebus.data.api.auth.AuthApiService;
import io.vehiclehistory.safebus.data.api.caller.GetVehicleHistoryCaller;
import io.vehiclehistory.safebus.data.model.Auth;
import io.vehiclehistory.safebus.injection.ApplicationContext;
import io.vehiclehistory.safebus.injection.module.ApplicationModule;
import retrofit2.Retrofit;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Application application();

    SharedPreferences sharedPreferences();

    DataManager dataManager();

    LayoutInflater layoutInflater();

    ConnectivityManager connectivityManager();

    NetworkStateManager networkStateManager();

    AuthProvider sessionHandler();

    Auth auth();

    Retrofit retrofit();

    AuthApiService authApiService();

    void inject(GetVehicleHistoryCaller vehicleHistoryCaller);

    void inject(AuthProvider authProvider);

    void inject(SafeBusApp ticketsLotteryApp);
}
