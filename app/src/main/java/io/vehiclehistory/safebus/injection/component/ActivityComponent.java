package io.vehiclehistory.safebus.injection.component;

import android.content.Context;

import dagger.Component;
import io.vehiclehistory.safebus.activity.BusActivity;
import io.vehiclehistory.safebus.activity.MainActivity;
import io.vehiclehistory.safebus.injection.ActivityContext;
import io.vehiclehistory.safebus.injection.ApplicationContext;
import io.vehiclehistory.safebus.injection.PerActivity;
import io.vehiclehistory.safebus.injection.module.ActivityModule;


/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);
    void inject(BusActivity busActivity);
}
