package io.vehiclehistory.safebus.data.api.auth;

import javax.inject.Inject;

import io.vehiclehistory.safebus.components.NetworkStateManager;
import io.vehiclehistory.safebus.config.ApplicationConfiguration;
import io.vehiclehistory.safebus.config.AuthorizationConfiguration;
import io.vehiclehistory.safebus.data.model.Auth;
import io.vehiclehistory.safebus.ui.view.AuthMvpView;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class AuthFromRefreshTokenCaller {

    private final AuthApiService authApiService;

    private final NetworkStateManager networkStateManager;
    private final Retrofit retrofit;

    private AuthMvpView authMvpView;
    private OnAuthCallback finishedListener;
    private String refreshToken;

    private Subscription subscription;

    private int retry = 0;

    @Inject
    public AuthFromRefreshTokenCaller(AuthApiService authApiService, NetworkStateManager networkStateManager, Retrofit retrofit) {
        this.authApiService = authApiService;
        this.networkStateManager = networkStateManager;
        this.retrofit = retrofit;
    }

    public void auth(String refreshToken, AuthMvpView authMvpView, OnAuthCallback finishedListener) {
        this.refreshToken = refreshToken;
        this.authMvpView = authMvpView;
        this.finishedListener = finishedListener;
        preCall();
    }

    private void preCall() {
        ++retry;

        if (!networkStateManager.isConnectedOrConnecting()) {
            Timber.e("no connection");
            authMvpView.onNoConnectionError();
            finishedListener.onError();
            return;
        }

        if (retry > ApplicationConfiguration.RETRY_COUNT) {
            Timber.e("Reached max retries: %d", retry);
            authMvpView.onRefreshFailed(finishedListener);
            return;
        }

        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        subscription = auth();
    }

    private Subscription auth() {
        return authApiService.getSessionFromRefreshToken(
                refreshToken,
                AuthorizationConfiguration.CLIENT,
                AuthorizationConfiguration.REFRESH_TOKEN_GRANT_TYPE
        )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //nop
                    }
                })
                .subscribe(new Subscriber<Auth>() {
                    @Override
                    public void onCompleted() {
                        //nop
                    }

                    @Override
                    public void onError(Throwable e) {
//                        ApiError exception = ApiError.internalApiError();
//
//                        if (e instanceof HttpException) {
//                            HttpException ex = (HttpException) e;
//
//                            if (ex.response() != null) {
//                                exception = ErrorUtils.parseError(ex.response(), retrofit);
//                            }
//                        }

                        preCall();
                    }

                    @Override
                    public void onNext(Auth auth) {
                        authMvpView.onAuth(auth);
                        finishedListener.onSuccess(auth);
                    }
                });
    }
}