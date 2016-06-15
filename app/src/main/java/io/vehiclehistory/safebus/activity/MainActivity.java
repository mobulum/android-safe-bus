package io.vehiclehistory.safebus.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.vehiclehistory.safebus.BuildConfig;
import io.vehiclehistory.safebus.R;
import io.vehiclehistory.safebus.data.api.caller.GetVehicleHistoryCaller;
import io.vehiclehistory.safebus.data.model.vehicle.VehicleResponse;
import io.vehiclehistory.safebus.ui.view.VehicleMvpView;
import timber.log.Timber;


public class MainActivity extends BaseActivity implements VehicleMvpView {

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.findBus)
    protected Button findBusButton;

    @Inject
    protected GetVehicleHistoryCaller getVehicleHistoryPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        bindViews();
        resetValues();
        setupSearchButton();
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        getVehicleHistoryPresenter.detachView();
    }

    private void bindViews() {
        getVehicleHistoryPresenter.attachView(this);
    }

    private void resetValues() {
        //TODO
    }

    private void setupSearchButton() {
        findBusButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //clearErrors();
                validateAndPerformSearch();
            }

        });
    }

    private void validateAndPerformSearch() {
        getVehicleHistoryPresenter.getVehicleHistory("SBE12345");
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMarketAppIn() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="
                    + BuildConfig.APPLICATION_ID)));
        }
    }

    @Override
    public void onVehicleFinished(VehicleResponse response) {
        Intent i = new Intent(this, BusActivity.class);
        i.putExtra(BusActivity.BUS_RESPONSE_KEY, response);
        startActivity(i);
    }

    @Override
    public void onErrorResponse(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoConnectionError() {
        Toast.makeText(getApplicationContext(), "No conneciton", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRetryError() {

    }

    @Override
    public void unableToGetTokenError() {

    }

    @Override
    public void startedLoadingData() {

    }

    @Override
    public void finishedLoadingData() {

    }
}
