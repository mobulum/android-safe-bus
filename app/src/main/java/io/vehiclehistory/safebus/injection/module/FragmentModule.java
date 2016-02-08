package io.vehiclehistory.safebus.injection.module;

import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;
import io.vehiclehistory.safebus.injection.PerFragment;

@Module
public class FragmentModule {
    private final Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @PerFragment
    Fragment provideFragment() {
        return fragment;
    }
}
