package com.github.beardlybread.orgestrator.sandbox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.beardlybread.orgestrator.R;
import com.github.beardlybread.orgestrator.io.DriveApi;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class DriveApiClassActivity extends AppCompatActivity {

    private ProgressDialog progressMessage = null;
    private String folderId = null;

    private static final String FOLDER_ID_QUERY =
            "trashed=false and 'root' in parents and name='orgestrator-test'";
    private static final String FOLDER_CONTENTS_QUERY_FORMAT =
            "trashed=false and '%s' in parents";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.progressMessage = new ProgressDialog(this);
        this.progressMessage.setMessage("Talking to Google Drive");
        setContentView(R.layout.activity_drive_api_class);
    }

    @Override
    protected void onDestroy () {
        this.progressMessage.dismiss();
        super.onDestroy();
    }

    @Override
    protected void onStart () {
        super.onStart();
        DriveApi.initialize(this);
    }

    @Override
    protected void onActivityResult (int req, int res, Intent data) {
        DriveApi.getInstance().onActivityResult(req, res, data);
    }

    public void getTestFolderContents (View v) {
        if (this.folderId == null) {
            DriveApi.getInstance()
                    .new MakeRequest(this.folderIdRequest(true))
                    .execute();
        } else {
            DriveApi.getInstance()
                    .new MakeRequest(this.testFolderRequest(this.folderId))
                    .execute();
        }
    }

    private DriveApi.Request folderIdRequest () {
        return this.folderIdRequest(false);
    }

    private DriveApi.Request folderIdRequest (final boolean andFolderContents) {
        return new DriveApi.Request() {
            @Override
            public byte[] call(DriveApi.MakeRequest makeRequest) throws IOException {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                FileList result = makeRequest.getService().files().list()
                        .setQ(FOLDER_ID_QUERY)
                        .execute();
                List<File> files = result.getFiles();
                if (files != null) {
                    return files.get(0).getId().getBytes();
                }
                return null;
            }

            @Override
            public void before(DriveApi.MakeRequest makeRequest) {

            }

            @Override
            public void after(DriveApi.MakeRequest makeRequest, byte[] output) {
                folderId = new String(output);
                if (folderId.length() > 0 && andFolderContents) {
                    DriveApi.MakeRequest r = DriveApi.getInstance()
                            .new MakeRequest(testFolderRequest(folderId));
                    r.execute();
                }
            }

            @Override
            public void cancelled(DriveApi.MakeRequest makeRequest) {

            }
        };
    }

    private DriveApi.Request testFolderRequest (final String id) {
        return new DriveApi.Request() {
            @Override
            public byte[] call(DriveApi.MakeRequest makeRequest) throws IOException {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                FileList result = makeRequest.getService().files().list()
                        .setQ(String.format(FOLDER_CONTENTS_QUERY_FORMAT, id))
                        .execute();
                List<File> files = result.getFiles();
                if (files != null) {
                    for (File file: files) {
                        out.write(file.getId().getBytes());
                        out.write('\n');
                    }
                }
                return out.toByteArray();
            }

            @Override
            public void before(DriveApi.MakeRequest makeRequest) {
                progressMessage.show();
            }

            @Override
            public void after(DriveApi.MakeRequest makeRequest, byte[] output) {
                progressMessage.hide();
                TextView tv = (TextView) findViewById(R.id.drive_api_class_text);
                tv.setText(new String(output));
            }

            @Override
            public void cancelled(DriveApi.MakeRequest makeRequest) {
                progressMessage.hide();
            }
        };
    }
}
