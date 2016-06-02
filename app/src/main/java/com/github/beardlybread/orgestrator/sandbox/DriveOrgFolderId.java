package com.github.beardlybread.orgestrator.sandbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.github.beardlybread.orgestrator.R;

public class DriveOrgFolderId extends AppCompatActivity {

    private static final int GET_ORGESTRATOR_FOLDER = 1;
    private static final int GET_ORG_FILES = 2;

    private String folderId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_drive_org_folder_id);
    }

    @Override
    protected void onActivityResult (int req, int res, Intent data) {
        if (res == Activity.RESULT_OK) {
            byte[] response;
            if (req == GET_ORGESTRATOR_FOLDER) {
                this.getOrgFiles(data.getByteArrayExtra(DriveApiActivity.OP_RESULT));
            } else if (req == GET_ORG_FILES) {
                response = data.getByteArrayExtra(DriveApiActivity.OP_RESULT);
                TextView tv = (TextView) findViewById(R.id.drive_org_folder_id_text);
                tv.setText(new String(response));
            }
        }
    }

    public void getFolderId (View v) {
        if (this.folderId == null) {
            Intent getOrgFolder = new Intent(this, DriveApiActivity.class);
            getOrgFolder.putExtra(DriveApiActivity.OP_TYPE, DriveApiActivity.QUERY);
            getOrgFolder.putExtra(
                    DriveApiActivity.OP_BODY, ("trashed=false and name='orgestrator-test'").getBytes());
            startActivityForResult(getOrgFolder, GET_ORGESTRATOR_FOLDER);
        } else {
            this.getOrgFiles();
        }
    }

    public void getOrgFiles () {
        StringBuilder query = new StringBuilder("trashed=false and ");
        query.append("'").append(this.folderId).append("'").append(" in parents");
        Intent getOrgFolder = new Intent(this, DriveApiActivity.class);
        getOrgFolder.putExtra(DriveApiActivity.OP_TYPE, DriveApiActivity.QUERY);
        getOrgFolder.putExtra(
                DriveApiActivity.OP_BODY, (query.toString()).getBytes());
        startActivityForResult(getOrgFolder, GET_ORG_FILES);
    }

    public void getOrgFiles (byte[] folderInfo) {
        String[] nameAndId = (new String(folderInfo)).trim().split("\t");
        if (nameAndId.length == 2) {
            this.folderId = nameAndId[1];
            this.getOrgFiles();
        } else {
            TextView tv = (TextView) findViewById(R.id.drive_org_folder_id_text);
            tv.setText("Could not find orgestrator-test.");
        }
    }
}
