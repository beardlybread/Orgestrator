package com.github.beardlybread.orgestrator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.beardlybread.orgestrator.org.OrgFile;
import com.github.beardlybread.orgestrator.org.Orgestrator;

import java.io.InputStream;

// TODO Add Orgestrator file saving to one of the onAction callbacks.

public class Start extends AppCompatActivity {

    private Orgestrator org = null;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadTestFile();
        setContentView(R.layout.activity_start);
    }

    public void startRawFile (View v) {
        startActivity(new Intent(this, RawFile.class));
    }

    public void startFindToDo (View v) {
        startActivity(new Intent(this, FindToDo.class));
    }

    public void startCheckedList (View v) {
        startActivity(new Intent(this, CheckedList.class));
    }

    public void startTextList (View v) {
        startActivity(new Intent(this, TextList.class));
    }

    public void startTestGoogleDrive (View v) {
        startActivity(new Intent(this, TestGoogleDrive.class));
    }

    private void loadTestFile () {
        Orgestrator org = Orgestrator.getInstance();
        if (org.isEmpty()) {
            InputStream inStream = getResources().openRawResource(R.raw.org_test_file);
            if (!org.add(inStream, "org_test_file", OrgFile.RAW_RESOURCE)) {
                Log.e("Orgestrator.add()", org.getError().getMessage());
                org.clearError();
            }
            inStream = getResources().openRawResource(R.raw.other_org_test_file);
            if (!org.add(inStream, "other_org_test_file", OrgFile.RAW_RESOURCE)) {
                Log.e("Orgestrator.add()", org.getError().getMessage());
                org.clearError();
            }
        }
    }
}
