package io.vehiclehistory.safebus.data.api.auth;

import io.vehiclehistory.safebus.data.model.Auth;

public interface OnAuthCallback {
    void onSuccess(Auth response);
    void onError();
}
