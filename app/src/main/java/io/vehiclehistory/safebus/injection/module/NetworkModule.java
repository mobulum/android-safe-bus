package io.vehiclehistory.safebus.injection.module;

import android.app.Application;
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
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
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

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Application application, final Auth auth) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Timber.d("intercept for request: %s", chain.request());

                String authorization = getAuthorization(chain.request());

                Timber.d("set request Authorization header to: \"%s\"", authorization);

                Request newRequest = chain.request()
                        .newBuilder()
                        .addHeader("Accept-language", "pl")
                        .addHeader("Authorization", authorization)
                        .addHeader("User-Agent", "SafeBus;Android;" + BuildConfig.VERSION_NAME)
                        .build();
                return chain.proceed(newRequest);
            }

            @NonNull
            private String getAuthorization(Request request) throws IOException {
                //default

                String accessToken = auth.getAccessToken();
                String authorization = AuthorizationConfiguration.BEARER_AUTHORIZATION + " " + accessToken;

                Timber.d("request intercept for path: %s", request.url().encodedPath());

                if (AuthApiService.OAUTH_TOKEN.equals(request.url().encodedPath())) {
                    String credentialsToken = AuthorizationConfiguration.CLIENT + ":" + AuthorizationConfiguration.CLIENT_PASSWORD;

                    byte[] bytes = new byte[0];

                    try {
                        bytes = credentialsToken.getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        Timber.e(e, "UnsupportedEncodingException while preparing authorization data");
                    }

                    String base64 = Base64.encodeToString(bytes, Base64.NO_WRAP);

                    authorization = AuthorizationConfiguration.BASIC_AUTHORIZATION + " " + base64;
                }
                return authorization;
            }
        };

        try {
            InputStream inputStream = application.getAssets().open(ApplicationConfiguration.TRUSTSTORE_NAME);

            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(inputStream, null);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keystore);

            X509TrustManager defaultTrustManager = (X509TrustManager) trustManagerFactory.getTrustManagers()[0];

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{defaultTrustManager}, null);

            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .readTimeout(ApplicationConfiguration.READ_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                    .connectTimeout(ApplicationConfiguration.CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return ApplicationConfiguration.HOSTNAME.equals(hostname);
                        }
                    });

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Timber.tag("OkHttp").d(message);
                    }
                });

                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClientBuilder.addInterceptor(loggingInterceptor);
            }

            return okHttpClientBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
