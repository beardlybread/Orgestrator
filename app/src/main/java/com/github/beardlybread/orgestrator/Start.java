package com.github.beardlybread.orgestrator;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.beardlybread.orgestrator.io.DriveApi;
import com.github.beardlybread.orgestrator.org.Orgestrator;
import com.github.beardlybread.orgestrator.sandbox.Sandbox;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Start extends AppCompatActivity {

    private static final String FOLDER_ID_QUERY =
            "trashed=false and 'root' in parents and name='orgestrator-test'";
    private static final String FOLDER_CONTENTS_QUERY_FORMAT =
            "trashed=false and '%s' in parents";

    private DriveApi driveApi = null;
    private String folderId = null;
    private String[] filePaths = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.driveApi = new DriveApi();
        Orgestrator.getInstance().setDriveApi(this.driveApi);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(this.driveApi, DriveApi.tag);
        ft.commit();
        setContentView(R.layout.activity_start);
    }

    public void startSandbox (View v) {
        startActivity(new Intent(this, Sandbox.class));
    }

    public void downloadFromDrive (View v) {
        if (this.folderId == null || this.filePaths == null) {
            this.driveApi.new RequestQueue()
                    .request(this.folderIdRequest())
                    .request(this.folderContentRequest(new Runnable() {
                        @Override
                        public void run() {
                            downloadFromDrive(null);
                        }
                    })).execute();
        } else {
            Orgestrator.getInstance().loadFilesFromGoogleDrive(this.getFilePaths());
        }
    }

    public void uploadToDrive (View v) {
        Orgestrator.getInstance().saveFilesToGoogleDrive();
    }

    private DriveApi.Request folderIdRequest () {
        return this.driveApi.queryRequest(
                FOLDER_ID_QUERY,
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String raw = new String(
                                    driveApi.getLastRequest().get(5, TimeUnit.SECONDS));
                            folderId = raw.trim().split("\t")[1];
                            Orgestrator.getInstance().setDriveApiFolderId(folderId);
                        } catch (Exception e) {
                            driveApi.getLastRequest().cancel(true);
                        }
                    }
                }, null);
    }

    private DriveApi.Request folderContentRequest (final Runnable then) {
        return this.driveApi.new Request(then) {
            @Override
            public byte[] call(DriveApi.MakeRequest makeRequest) throws IOException {
                if (getFolderId() == null)
                    return null;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                FileList result = makeRequest.getService().files().list()
                        .setQ(String.format(FOLDER_CONTENTS_QUERY_FORMAT, getFolderId()))
                        .execute();
                List<File> files = result.getFiles();
                if (files != null) {
                    for (File file: files) {
                        out.write(file.getName().getBytes());
                        out.write('\t');
                        out.write(file.getId().getBytes());
                        out.write('\n');
                    }
                }
                return out.toByteArray();
            }

            @Override
            public void after(DriveApi.MakeRequest makeRequest, byte[] output) {
                String response = new String(output);
                filePaths = response.trim().split("\n");
                driveApi.makeToast("Content located on Google Drive.");
            }
        };
    }

    public String getFolderId () { return this.folderId; }
    public String[] getFilePaths () { return this.filePaths; }
}

