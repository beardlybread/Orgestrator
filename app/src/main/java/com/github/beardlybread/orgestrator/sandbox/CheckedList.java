package com.github.beardlybread.orgestrator.sandbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.beardlybread.orgestrator.R;
import com.github.beardlybread.orgestrator.org.Orgestrator;

public class CheckedList extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked_list);
    }

    @Override
    protected void onStart () {
        super.onStart();
    }

    @Override
    protected void onPause () {
        Orgestrator.getInstance().saveFilesToGoogleDrive();
        super.onPause();
    }
}
