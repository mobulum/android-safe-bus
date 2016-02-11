package io.vehiclehistory.safebus.injection.module;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.vehiclehistory.safebus.components.NetworkStateManager;
import io.vehiclehistory.safebus.data.AuthProvider;
import io.vehiclehistory.safebus.data.api.auth.AuthApiService;
import io.vehiclehistory.safebus.data.api.auth.AuthFromPasswordCaller;
import io.vehiclehistory.safebus.data.api.auth.AuthFromRefreshTokenCaller;
import io.vehiclehistory.safebus.data.model.Auth;
import retrofit2.Retrofit;

@Module
public class SessionModule {

    @Provides
    @Singleton
    AuthProvider provideSessionHandler(AuthFromPasswordCaller authFromPasswordCaller, AuthFromRefreshTokenCaller authFromRefreshTokenCaller, SharedPreferences sharedPreferences, Retrofit retrofit, Auth auth) {
        return new AuthProvider(authFromPasswordCaller, authFromRefreshTokenCaller, sharedPreferences, retrofit, auth);
    }

    @Provides
    @Singleton
    AuthFromPasswordCaller provideAuthFromPasswordProvider(AuthApiService authApiService, NetworkStateManager networkStateManager, Retrofit retrofit) {
        return new AuthFromPasswordCaller(authApiService, networkStateManager, retrofit);
    }

    @Provides
    @Singleton
    AuthFromRefreshTokenCaller provideAuthFromRefreshTokenProvider(AuthApiService authApiService, NetworkStateManager networkStateManager, Retrofit retrofit) {
        return new AuthFromRefreshTokenCaller(authApiService, networkStateManager, retrofit);
    }

    @Provides
    @Singleton
    Auth provideAuth(SharedPreferences sharedPreferences) {
        String accessToken = sharedPreferences.getString(AuthProvider.ACCESS_TOKEN, null);
        String refreshToken = sharedPreferences.getString(AuthProvider.REFRESH_TOKEN, null);

        Auth auth = new Auth();
        auth.setAccessToken(accessToken);
        auth.setRefreshToken(refreshToken);

        return auth;
    }
}
