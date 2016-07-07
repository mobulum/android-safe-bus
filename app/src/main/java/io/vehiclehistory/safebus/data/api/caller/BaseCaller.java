package io.vehiclehistory.safebus.data.api.caller;

import io.vehiclehistory.safebus.components.NetworkStateManager;
import io.vehiclehistory.safebus.config.ApplicationConfiguration;
import io.vehiclehistory.safebus.data.DataManager;
import io.vehiclehistory.safebus.data.api.ErrorUtils;
import io.vehiclehistory.safebus.data.api.auth.OnAuthCallback;
import io.vehiclehistory.safebus.data.model.ApiError;
import io.vehiclehistory.safebus.data.model.Auth;
import io.vehiclehistory.safebus.ui.view.MvpView;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscription;
import timber.log.Timber;

/**
 * Base class that implements the Caller interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
public abstract class BaseCaller<T extends MvpView> implements Caller<T> {

    protected static final int UNAUTHORIZED = 401;
    protected static final int FORBIDDEN = 403;

    private final NetworkStateManager networkStateManager;

    private final DataManager dataManager;

    private final Retrofit retrofit;

    private int retry = 0;

    private Subscription subscription;

    public BaseCaller(
            NetworkStateManager networkStateManager,
            DataManager dataManager,
            Retrofit retrofit
    ) {
        this.networkStateManager = networkStateManager;
        this.dataManager = dataManager;
        this.retrofit = retrofit;
    }

    private T mMvpView;


    protected void resetRetry() {
        retry = 0;
    }

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;

        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public T getMvpView() {
        return mMvpView;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please preCall Caller.attachView(MvpView) before" +
                    " requesting data to the Caller");
        }
    }

    protected void preCall() {
        ++retry;

        if (!networkStateManager.isConnectedOrConnecting()) {
            Timber.e("no connection");
            mMvpView.onNoConnectionError();
            return;
        }

        if (retry > ApplicationConfiguration.RETRY_COUNT) {
            Timber.e("Reached max retries: %d", retry);
            mMvpView.onRetryError();
            return;
        }

        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        subscription = call();
    }

    protected void handleApiError(Throwable e) {
        checkViewAttached();

        Timber.e(e, "There was an api error");
        ApiError exception = ApiError.internalApiError();

        if (e instanceof HttpException) {
            HttpException ex = (HttpException) e;
            int code = ex.code();

            if (ex.response() != null) {
                exception = ErrorUtils.parseError(ex.response(), retrofit);
            }

            exception.setStatusCode(code);

            switch (exception.getStatusCode()) {
                case UNAUTHORIZED:
                    authorizeAndRetry();
                    break;
                case FORBIDDEN:
                    handleForbiddenError(exception);
                    break;
                default:
                    getMvpView().onErrorResponse(exception.getUserMessage());
            }
        } else {
            getMvpView().onErrorResponse(exception.getUserMessage());
        }
    }

    protected void handleForbiddenError(ApiError exception) {
        //nop
    }

    protected void authorizeAndRetry() {
        dataManager.getNewSession(new OnAuthCallback() {

            @Override
            public void onSuccess(Auth response) {
                Timber.e("authorizeAndRetry.onSuccess");
                resetRetry();
                preCall();
            }

            @Override
            public void onError() {
                Timber.e("authorizeAndRetry.onError");
                mMvpView.onNoConnectionError();
            }
        });
    }
}

