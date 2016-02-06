package io.vehiclehistory.safebus.ui.view;

import io.vehiclehistory.safebus.data.api.auth.OnAuthCallback;
import io.vehiclehistory.safebus.data.model.Auth;

public interface AuthMvpView {

    void onAuth(Auth auth);

    void onRefreshFailed(OnAuthCallback onFinishedListener);

    void onPasswordFailed();

    void onNoConnectionError();
}
