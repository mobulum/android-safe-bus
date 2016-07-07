package io.vehiclehistory.safebus.injection.module;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.vehiclehistory.safebus.injection.ApplicationContext;

/**
 * Provide application-level dependencies.
 */
@Module(includes = {
        StorageModule.class,
        SessionModule.class,
        ServiceModule.class,
        NetworkModule.class
})
public class ApplicationModule {
    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }
}
