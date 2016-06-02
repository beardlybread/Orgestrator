package com.github.beardlybread.orgestrator.sandbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.github.beardlybread.orgestrator.R;
import com.github.beardlybread.orgestrator.io.DriveApi;

public class DriveOrgFolderId extends AppCompatActivity {

    private String folderId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_drive_org_folder_id);
    }

    @Override
    protected void onActivityResult (int req, int res, Intent data) {
        if (req == DriveApi.QUERY && res == Activity.RESULT_OK) {
            byte[] response = data.getByteArrayExtra(DriveApi.OP_RESULT);
            TextView tv = (TextView) findViewById(R.id.drive_org_folder_id_text);
            tv.setText(new String(response));
        }
    }

    public void getFolderId (View v) {
        Intent getOrgFolder = new Intent(this, DriveApi.class);
        getOrgFolder.putExtra(DriveApi.OP_TYPE, DriveApi.QUERY);
        getOrgFolder.putExtra(
                DriveApi.OP_BODY, ("trashed=false and name='orgestrator-test'").getBytes());
        startActivityForResult(getOrgFolder, DriveApi.QUERY);
    }
}
