package com.github.beardlybread.orgestrator.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.github.beardlybread.orgestrator.R;

public class StartSettings extends PreferenceFragment {

    @Override
    public void onCreate (Bundle b) {
        super.onCreate(b);
        addPreferencesFromResource(R.xml.preferences);
    }

}
