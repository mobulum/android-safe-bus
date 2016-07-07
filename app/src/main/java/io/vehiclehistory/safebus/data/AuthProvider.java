package io.vehiclehistory.safebus.data;

import android.content.SharedPreferences;
import android.text.TextUtils;

import javax.inject.Inject;

import io.vehiclehistory.safebus.data.api.auth.AuthFromPasswordCaller;
import io.vehiclehistory.safebus.data.api.auth.AuthFromRefreshTokenCaller;
import io.vehiclehistory.safebus.data.api.auth.OnAuthCallback;
import io.vehiclehistory.safebus.data.model.Auth;
import io.vehiclehistory.safebus.ui.view.AuthMvpView;
import retrofit2.Retrofit;
import timber.log.Timber;

public class AuthProvider implements AuthMvpView {

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";

    private final AuthFromPasswordCaller authFromPasswordCaller;

    private final AuthFromRefreshTokenCaller authFromRefreshTokenCaller;

    private final SharedPreferences sharedPreferences;

    private final Retrofit retrofit;

    private final Auth session;

    @Inject
    public AuthProvider(AuthFromPasswordCaller authFromPasswordCaller, AuthFromRefreshTokenCaller authFromRefreshTokenCaller, SharedPreferences sharedPreferences, Retrofit retrofit, Auth session) {
        this.authFromPasswordCaller = authFromPasswordCaller;
        this.authFromRefreshTokenCaller = authFromRefreshTokenCaller;
        this.sharedPreferences = sharedPreferences;
        this.retrofit = retrofit;
        this.session = session;
    }

    //TODO REMOVE OnAuthCallback
    public void getNewSession(OnAuthCallback onFinishedListener) {
        String refreshToken = session.getRefreshToken();
        Timber.d("getNewSession: refreshToken: %s", refreshToken);

        if (TextUtils.isEmpty(refreshToken)) {
            getNewSessionFromPassword(this, onFinishedListener);
        } else {
            getNewSessionFromRefreshToken(refreshToken, this, onFinishedListener);
        }
    }

    public void getNewSessionFromRefreshToken(final String refreshToken, final AuthMvpView authMvpView, OnAuthCallback onFinishedListener) {
        Timber.d("getNewSessionFromRefreshToken: refreshToken: %s", refreshToken);
        authFromRefreshTokenCaller.auth(refreshToken, authMvpView, onFinishedListener);
    }

    public void getNewSessionFromPassword(final AuthMvpView authMvpView, OnAuthCallback onFinishedListener) {
        Timber.d("getNewSessionFromPassword");
        authFromPasswordCaller.auth(authMvpView, onFinishedListener);
    }

    private void updateSession(Auth response) {
        Timber.d("updateSession: %s", response);

        session.setAccessToken(response.getAccessToken());
        session.setRefreshToken(response.getRefreshToken());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ACCESS_TOKEN, response.getAccessToken());
        editor.putString(REFRESH_TOKEN, response.getRefreshToken());
        editor.apply();
    }

    private void resetRefreshToken() {
        Timber.d("resetRefreshToken");
        session.setRefreshToken(null);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(REFRESH_TOKEN, null);
        editor.apply();
    }

    @Override
    public void onAuth(Auth auth) {
        Timber.d("onAuth");
        updateSession(auth);
    }

    @Override
    public void onRefreshFailed(OnAuthCallback onFinishedListener) {
        Timber.d("onRefreshFailed");
        resetRefreshToken();
        getNewSessionFromPassword(this, onFinishedListener);
    }

    @Override
    public void onPasswordFailed() {
        //toast TODO
    }

    @Override
    public void onNoConnectionError() {

    }
}
