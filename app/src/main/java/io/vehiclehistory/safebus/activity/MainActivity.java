package io.vehiclehistory.safebus.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewAnimator;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.vehiclehistory.safebus.BuildConfig;
import io.vehiclehistory.safebus.R;
import io.vehiclehistory.safebus.data.api.caller.GetVehicleHistoryCaller;
import io.vehiclehistory.safebus.data.model.vehicle.VehicleResponse;
import io.vehiclehistory.safebus.ui.view.VehicleMvpView;


public class MainActivity extends BaseActivity implements VehicleMvpView {

    private static final int ANIMATOR_BUTTON = 0;
    private static final int ANIMATOR_PROGRESS = 1;
    private static final String EXAMPLE_PLATE = "SBE12345";

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.findBus)
    protected Button findBusButton;

    @Bind(R.id.busPlate)
    protected EditText busPlate;

    @Bind(R.id.find_bus_animator)
    protected ViewAnimator findBusAnimator;

    @Bind(R.id.input_layout)
    protected CardView inputLayout;

    @Bind(R.id.show_example)
    protected Button exampleButton;

    @Inject
    protected GetVehicleHistoryCaller getVehicleHistoryPresenter;

    private final Handler handler = new Handler();

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

        exampleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                busPlate.setText(EXAMPLE_PLATE);
                findBusButton.callOnClick();
            }
        });
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

    private String getValidatedInput() throws IllegalArgumentException {
        String plate = busPlate.getText().toString();
        if (plate.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return plate;
    }

    private void validateAndPerformSearch() {
        try {
            getVehicleHistoryPresenter.getVehicleHistory(getValidatedInput());
            setUiLocked(true);
            setButtonAnimator(ANIMATOR_PROGRESS);
        } catch (IllegalArgumentException e) {
            handleValidationIssues();
        }
    }

    private void handleValidationIssues() {
        Toast.makeText(getApplicationContext(), R.string.invalid_input, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_rate:
                showMarketAppIn();
                return true;
            case R.id.menu_about:
                showAboutActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showMarketAppIn() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="
                    + BuildConfig.APPLICATION_ID)));
        }
    }

    private void showAboutActivity() {
        startActivity(new Intent(this, AboutActivity.class));
    }

    @Override
    public void onVehicleFinished(VehicleResponse response) {
        Intent i = new Intent(this, BusActivity.class);
        i.putExtra(BusActivity.BUS_RESPONSE_KEY, response);
        startActivity(i);
    }

    @Override
    public void onErrorResponse(String message) {
        finishedLoadingData();
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoConnectionError() {
        finishedLoadingData();
        Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRetryError() {

    }

    @Override
    public void finishedLoadingData() {
        setButtonAnimator(ANIMATOR_BUTTON);
        setUiLocked(false);
    }

    private void setUiLocked(final boolean locked) {
        this.handler.post(new Runnable() {

            @Override
            public void run() {
                inputLayout.setEnabled(!locked);
            }
        });
    }

    private void setButtonAnimator(final int childPosition) {
        this.handler.post(new Runnable() {

            @Override
            public void run() {
                findBusAnimator.setDisplayedChild(childPosition);
            }
        });
    }
}
