package io.vehiclehistory.safebus.injection.module;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.vehiclehistory.safebus.components.NetworkStateManager;

@Module
public class ServiceModule {

    @Provides
    @Singleton
    LayoutInflater provideLayoutInflater(Application application) {
        return (LayoutInflater) application.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Provides
    @Singleton
    ConnectivityManager provideConnectivityManager(Application application) {
        return (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Provides
    @Singleton
    NetworkStateManager provideNetworkStateManager(ConnectivityManager connectivityManagerCompat) {
        return new NetworkStateManager(connectivityManagerCompat);
    }
}
