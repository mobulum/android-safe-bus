package io.vehiclehistory.safebus.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.vehiclehistory.safebus.SafeBusApp;
import io.vehiclehistory.safebus.injection.component.ActivityComponent;
import io.vehiclehistory.safebus.injection.component.DaggerActivityComponent;
import io.vehiclehistory.safebus.injection.module.ActivityModule;


public class BaseActivity extends AppCompatActivity {

    private ActivityComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ActivityComponent component() {

        if (component == null) {
            component = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(SafeBusApp.get(this).component())
                    .build();
        }

        return component;
    }
}
