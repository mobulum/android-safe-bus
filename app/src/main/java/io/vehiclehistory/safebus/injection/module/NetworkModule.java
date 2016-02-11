package io.vehiclehistory.safebus.injection.module;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import io.vehiclehistory.safebus.BuildConfig;
import io.vehiclehistory.safebus.config.ApplicationConfiguration;
import io.vehiclehistory.safebus.config.AuthorizationConfiguration;
import io.vehiclehistory.safebus.data.api.VehicleHistoryApiService;
import io.vehiclehistory.safebus.data.api.auth.AuthApiService;
import io.vehiclehistory.safebus.data.model.Auth;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import timber.log.Timber;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient httpClient) {
        Gson gson = new GsonBuilder()
                .create();

        return new Retrofit.Builder()
                .baseUrl(ApplicationConfiguration.HOST)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    AuthApiService provideAuthApiService(Retrofit retrofit) {
        return retrofit.create(AuthApiService.class);
    }

    @Provides
    @Singleton
    VehicleHistoryApiService provideReceiptsApiService(Retrofit retrofit) {
        return retrofit.create(VehicleHistoryApiService.class);
    }
}
