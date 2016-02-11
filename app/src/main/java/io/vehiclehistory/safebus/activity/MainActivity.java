package io.vehiclehistory.safebus.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

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

    @Bind(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;

    @Bind(R.id.nav_view)
    protected NavigationView navigationView;

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

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
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

    public void onNavigationDrawerItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_search_bus: {
                getVehicleHistoryPresenter.getVehicleHistory("SBE12345");
                break;
            }

            case R.id.nav_rate:
                showMarketAppIn();
                break;

            default:

        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        disableNavigationViewScrollbars(navigationView);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        onNavigationDrawerItemSelected(menuItem);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
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
    public void onVehicleFinished(VehicleResponse vehicleResponse) {

    }

    @Override
    public void onErrorResponse(String message) {

    }

    @Override
    public void onNoConnectionError() {

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
