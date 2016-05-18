package com.github.beardlybread.orgestrator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.beardlybread.orgestrator.org.Orgestrator;

import java.io.InputStream;

// TODO Add Orgestrator file saving to one of the onAction callbacks.

public class StartActivity extends AppCompatActivity {

    private Orgestrator org = null;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadTestFile();
        setContentView(R.layout.activity_start);
    }

    public void startRawFileLines (View v) {
        startActivity(new Intent(this, RawFileLinesActivity.class));
    }

    public void startFindToDos (View v) {
        startActivity(new Intent(this, FindToDoActivity.class));
    }


    public void startToDoListActivity (View v) {
        startActivity(new Intent(this, ToDoTextListActivity.class));
    }

    private void loadTestFile () {
        Orgestrator org = Orgestrator.getInstance();
        if (org.isEmpty()) {
            InputStream inStream = getResources().openRawResource(R.raw.org_test_file);
            if (!org.add(inStream, "org_test_file", Orgestrator.RAW_RESOURCE)) {
                Log.e("Orgestrator.add()", org.getError().getMessage());
                org.clearError();
            }
            inStream = getResources().openRawResource(R.raw.other_org_test_file);
            if (!org.add(inStream, "other_org_test_file", Orgestrator.RAW_RESOURCE)) {
                Log.e("Orgestrator.add()", org.getError().getMessage());
                org.clearError();
            }
        }
    }
}
