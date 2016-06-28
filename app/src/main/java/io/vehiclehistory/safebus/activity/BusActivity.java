package io.vehiclehistory.safebus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
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

    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Bind(R.id.bus_make)
    protected TextView busMake;

    @Bind(R.id.bus_model)
    protected TextView busModel;

    @Bind(R.id.bus_production)
    protected TextView busProduction;

    @Bind(R.id.bus_registration_number)
    protected TextView busRegistrationNumber;

    @Bind(R.id.bus_vin_number)
    protected TextView busVinNumber;

    @Bind(R.id.bus_inspection)
    protected TextView busInspection;

    @Bind(R.id.bus_inspection_expire)
    protected TextView busInspectionExpire;

    @Bind(R.id.bus_mileage)
    protected TextView busMileage;

    @Bind(R.id.bus_inspection_icon_positive)
    protected ImageView busInspectionIconPositive;

    @Bind(R.id.bus_inspection_icon_negative)
    protected ImageView busInspectionIconNegative;

    @Bind(R.id.bus_registration_icon_positive)
    protected ImageView busRegistrationIconPositive;

    @Bind(R.id.bus_registration_icon_negative)
    protected ImageView busRegistrationIconNegative;

    @Bind(R.id.bus_policy_icon_positive)
    protected ImageView busPolicyIconPositive;

    @Bind(R.id.bus_policy_icon_negative)
    protected ImageView busPolicyIconNegative;

    private VehicleResponse vehicleResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().inject(this);
        setContentView(R.layout.activity_bus);
        ButterKnife.bind(this);
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
            busMake.setText(vehicleResponse.getVehicle().getName().getMake().toString());
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
                busInspectionIconPositive.setVisibility(View.GONE);
                busInspectionIconNegative.setVisibility(View.GONE);
            }
        }

        if (vehicleResponse.getVehicle().getRegistration() != null) {
            if (vehicleResponse.getVehicle().getRegistration().getStatus() == RegistrationStatus.REGISTERED) {
                busRegistrationIconPositive.setVisibility(View.VISIBLE);
                busRegistrationIconNegative.setVisibility(View.GONE);
            } else if (vehicleResponse.getVehicle().getRegistration().getStatus() == RegistrationStatus.UNREGISTERED) {
                busRegistrationIconPositive.setVisibility(View.GONE);
                busRegistrationIconNegative.setVisibility(View.VISIBLE);
            } else {
                busRegistrationIconPositive.setVisibility(View.GONE);
                busRegistrationIconNegative.setVisibility(View.GONE);
            }
        }

        if (vehicleResponse.getVehicle().getPolicy() != null) {
            if (vehicleResponse.getVehicle().getPolicy().getStatus() == PolicyStatus.UPTODATE) {
                busPolicyIconPositive.setVisibility(View.VISIBLE);
                busPolicyIconNegative.setVisibility(View.GONE);
            } else if (vehicleResponse.getVehicle().getPolicy().getStatus() == PolicyStatus.OUTDATED) {
                busPolicyIconPositive.setVisibility(View.GONE);
                busPolicyIconNegative.setVisibility(View.VISIBLE);
            } else {
                busPolicyIconPositive.setVisibility(View.GONE);
                busPolicyIconNegative.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
}
