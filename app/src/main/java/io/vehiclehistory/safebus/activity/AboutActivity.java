package io.vehiclehistory.safebus.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.vehiclehistory.safebus.BuildConfig;
import io.vehiclehistory.safebus.R;

public class AboutActivity extends BaseActivity {

    public static final String MAILTO_URI_PART = "mailto";

    @BindString(R.string.app_name)
    protected String appName;

    @BindString(R.string.contact_email)
    protected String contactEmail;

    @BindString(R.string.about_contact)
    protected String aboutContact;

    @BindString(R.string.about_application_version)
    protected String aboutApplicationVersion;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.application_version_text_view)
    protected TextView applicationVersionTextView;

    @BindView(R.id.contact_developer_button)
    protected Button contactButton;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        unbinder = ButterKnife.bind(this);
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
        String version = TextUtils.join(" ", new String[]{aboutApplicationVersion, BuildConfig.VERSION_NAME});
        applicationVersionTextView.setText(version);
    }

    private void setupContactButton() {
        contactButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(
                        Intent.ACTION_SENDTO,
                        Uri.fromParts(MAILTO_URI_PART, contactEmail, null)
                );
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, appName);
                startActivity(Intent.createChooser(emailIntent, aboutContact));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
