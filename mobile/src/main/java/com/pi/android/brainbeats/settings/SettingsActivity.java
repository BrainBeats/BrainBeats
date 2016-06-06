package com.pi.android.brainbeats.settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;

import com.pi.android.brainbeats.R;
import com.pi.android.brainbeats.ui.BaseActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        initializeToolbar();
        Intent intent = new Intent(this, PreferenceWithHeaders.class);
        startActivity(intent);
    }
}