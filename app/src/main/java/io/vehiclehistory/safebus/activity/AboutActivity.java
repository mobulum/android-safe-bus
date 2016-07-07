package io.vehiclehistory.safebus.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.vehiclehistory.safebus.BuildConfig;
import io.vehiclehistory.safebus.R;

public class AboutActivity extends BaseActivity {

    public static final String MAILTO_URI_PART = "mailto";

    private Toolbar toolbar;
    private TextView applicationVersionTextView;
    private Button contactButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        bindViews();
        setToolbar();
        setupContactButton();
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

    private void bindViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        applicationVersionTextView = (TextView) findViewById(R.id.application_version_text_view);
        applicationVersionTextView.setText(getResources().getString(R.string.about_application_version) + " " + BuildConfig.VERSION_NAME);
        contactButton = (Button) findViewById(R.id.contact_developer_button);
    }

    private void setupContactButton() {
        contactButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        MAILTO_URI_PART, getString(R.string.contact_email), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.about_contact)));
            }
        });
    }
}
