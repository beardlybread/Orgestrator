package com.github.beardlybread.orgestrator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void startRawFileLines (View v) {
        startActivity(new Intent(this, RawFileLinesActivity.class));
    }

    public void startFindToDos (View v) {
        startActivity(new Intent(this, FindToDoActivity.class));
    }
}
