package com.github.beardlybread.orgestrator.sandbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.github.beardlybread.orgestrator.R;
import com.github.beardlybread.orgestrator.org.OrgFile;
import com.github.beardlybread.orgestrator.org.Orgestrator;

import java.io.InputStream;

// TODO Add Orgestrator file saving to one of the onAction callbacks.

public class Sandbox extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadTestFile();
        Toolbar tb = (Toolbar) findViewById(R.id.start_toolbar);
        setSupportActionBar(tb);
        setContentView(R.layout.activity_sandbox);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.start_menu_settings:
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
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

    public void startDriveAPIQuickstart (View v) {
        startActivity(new Intent(this, DriveAPIQuickstart.class));
    }

    public void startDriveOrgFolderId (View v) {
        startActivity(new Intent(this, DriveOrgFolderId.class));
    }

    public void startDriveApiClassActivity (View v) {
        startActivity(new Intent(this, DriveApiClassActivity.class));
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
