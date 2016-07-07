package io.vehiclehistory.safebus.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.vehiclehistory.safebus.BuildConfig;
import io.vehiclehistory.safebus.R;
import io.vehiclehistory.safebus.data.api.DateFormatter;
import io.vehiclehistory.safebus.data.model.vehicle.Event;
import io.vehiclehistory.safebus.data.model.vehicle.EventType;
import io.vehiclehistory.safebus.data.model.vehicle.InspectionStatus;
import io.vehiclehistory.safebus.data.model.vehicle.PolicyStatus;
import io.vehiclehistory.safebus.data.model.vehicle.RegistrationStatus;
import io.vehiclehistory.safebus.data.model.vehicle.VehicleResponse;

public class BusActivity extends BaseActivity {
    public static final String BUS_RESPONSE_KEY = "bus_response";

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.bus_make)
    protected TextView busMake;

    @BindView(R.id.bus_model)
    protected TextView busModel;

    @BindView(R.id.bus_production)
    protected TextView busProduction;

    @BindView(R.id.bus_registration_number)
    protected TextView busRegistrationNumber;

    @BindView(R.id.bus_vin_number)
    protected TextView busVinNumber;

    @BindView(R.id.bus_inspection)
    protected TextView busInspection;

    @BindView(R.id.bus_inspection_expire)
    protected TextView busInspectionExpire;

    @BindView(R.id.bus_mileage)
    protected TextView busMileage;

    @BindView(R.id.bus_inspection_icon_positive)
    protected ImageView busInspectionIconPositive;

    @BindView(R.id.bus_inspection_icon_negative)
    protected ImageView busInspectionIconNegative;

    @BindView(R.id.bus_registration_icon_positive)
    protected ImageView busRegistrationIconPositive;

    @BindView(R.id.bus_registration_icon_negative)
    protected ImageView busRegistrationIconNegative;

    @BindView(R.id.bus_policy_icon_positive)
    protected ImageView busPolicyIconPositive;

    @BindView(R.id.bus_policy_icon_negative)
    protected ImageView busPolicyIconNegative;

    @BindView(R.id.bus_inspection_row)
    protected TableRow busInspectionRow;

    @BindView(R.id.bus_registration_row)
    protected TableRow busRegistrationRow;

    @BindView(R.id.bus_policy_row)
    protected TableRow busPolicyRow;

    private VehicleResponse vehicleResponse;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().inject(this);
        setContentView(R.layout.activity_bus);
        unbinder = ButterKnife.bind(this);
        setToolbar();

        Intent i = getIntent();
        Bundle args = i.getExtras();
        vehicleResponse = (VehicleResponse) args.getSerializable(
                BusActivity.BUS_RESPONSE_KEY);
        bindViewResult();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void bindViewResult() {
        if (vehicleResponse.getVehicle().getName() != null && vehicleResponse.getVehicle().getName().getMake() != null) {
            busMake.setText(vehicleResponse.getVehicle().getName().getMake());
        }
        if (vehicleResponse.getVehicle().getName() != null && vehicleResponse.getVehicle().getName().getModel() != null) {
            busModel.setText(vehicleResponse.getVehicle().getName().getModel());
        }
        if (vehicleResponse.getVehicle().getProduction() != null && vehicleResponse.getVehicle().getProduction().getYear() != null) {
            busProduction.setText(vehicleResponse.getVehicle().getProduction().getYear());
        }
        if (vehicleResponse.getVehicle().getPlate() != null && vehicleResponse.getVehicle().getPlate().getValue() != null) {
            busRegistrationNumber.setText(vehicleResponse.getVehicle().getPlate().getValue());
        }
        if (vehicleResponse.getVehicle().getVin() != null) {
            busVinNumber.setText(vehicleResponse.getVehicle().getVin());
        }

        for (Event event : vehicleResponse.getEvents()) {
            if (event.getType() == EventType.INSPECTION) {
                busInspection.setText(new DateFormatter().formatDateFromApi(event.getCreatedAt()));
                busInspectionExpire.setText(new DateFormatter().formatDateFromApi(event.getExpireAt()));
            }
        }

        if (vehicleResponse.getVehicle().getMileage() != null
                && vehicleResponse.getVehicle().getMileage().getValue() != null
                && vehicleResponse.getVehicle().getMileage().getType() != null) {
            busMileage.setText(vehicleResponse.getVehicle().getMileage().getValue() + " " + vehicleResponse.getVehicle().getMileage().getType());
        }

        if (vehicleResponse.getVehicle().getInspection() != null) {
            if (vehicleResponse.getVehicle().getInspection().getStatus() == InspectionStatus.UPTODATE) {
                busInspectionIconPositive.setVisibility(View.VISIBLE);
                busInspectionIconNegative.setVisibility(View.GONE);
            } else if (vehicleResponse.getVehicle().getInspection().getStatus() == InspectionStatus.OUTDATED) {
                busInspectionIconPositive.setVisibility(View.GONE);
                busInspectionIconNegative.setVisibility(View.VISIBLE);
            } else {
                busInspectionRow.setVisibility(View.GONE);
            }
        } else {
            busInspectionRow.setVisibility(View.GONE);
        }

        if (vehicleResponse.getVehicle().getRegistration() != null) {
            if (vehicleResponse.getVehicle().getRegistration().getStatus() == RegistrationStatus.REGISTERED) {
                busRegistrationIconPositive.setVisibility(View.VISIBLE);
                busRegistrationIconNegative.setVisibility(View.GONE);
            } else if (vehicleResponse.getVehicle().getRegistration().getStatus() == RegistrationStatus.UNREGISTERED) {
                busRegistrationIconPositive.setVisibility(View.GONE);
                busRegistrationIconNegative.setVisibility(View.VISIBLE);
            } else {
                busRegistrationRow.setVisibility(View.GONE);
            }
        } else {
            busRegistrationRow.setVisibility(View.GONE);
        }

        if (vehicleResponse.getVehicle().getPolicy() != null) {
            if (vehicleResponse.getVehicle().getPolicy().getStatus() == PolicyStatus.UPTODATE) {
                busPolicyIconPositive.setVisibility(View.VISIBLE);
                busPolicyIconNegative.setVisibility(View.GONE);
            } else if (vehicleResponse.getVehicle().getPolicy().getStatus() == PolicyStatus.OUTDATED) {
                busPolicyIconPositive.setVisibility(View.GONE);
                busPolicyIconNegative.setVisibility(View.VISIBLE);
            } else {
                busPolicyRow.setVisibility(View.GONE);
            }
        } else {
            busPolicyRow.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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
}
