package com.arca.equipfix.gambachanneltv.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import com.arca.equipfix.gambachanneltv.R;
import com.arca.equipfix.gambachanneltv.data.network.model.SessionInformation;

public class SettingsActivity extends BaseActivity {

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void onRegistration() {

    }

    @Override
    public void onSessionStarted(SessionInformation sessionInformation) {

    }

    @Override
    public void onSessionError() {

    }
}
