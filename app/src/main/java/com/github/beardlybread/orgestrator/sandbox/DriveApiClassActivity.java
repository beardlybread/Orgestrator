package com.github.beardlybread.orgestrator.sandbox;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.github.beardlybread.orgestrator.R;
import com.github.beardlybread.orgestrator.io.DriveApi;
import com.github.beardlybread.orgestrator.org.OrgFile;
import com.github.beardlybread.orgestrator.org.Orgestrator;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DriveApiClassActivity extends AppCompatActivity {

    private static final String FOLDER_ID_QUERY =
            "trashed=false and 'root' in parents and name='orgestrator-test'";
    private static final String FOLDER_CONTENTS_QUERY_FORMAT =
            "trashed=false and '%s' in parents";

    private DriveApi driveApi = null;
    private String folderId = null;
    private String[] filePaths = null;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_api_class);

    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
    }

    @Override
    protected void onStart () {
        super.onStart();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        this.driveApi = new DriveApi();
        ft.add(driveApi, DriveApi.tag);
        ft.commit();
    }

    public String getFolderId () { return this.folderId; }

    public String[] getFilePaths () { return this.filePaths; }

    public void getResourceIds (View v) {
        this.driveApi.new RequestQueue()
                .request(this.folderIdRequest())
                .request(this.folderContentRequest())
                .execute();
    }

    public void downloadFiles (View v) {
        if (this.folderId == null || this.filePaths == null) {
            this.driveApi.new RequestQueue()
                    .request(this.folderIdRequest())
                    .request(this.folderContentRequest(new Runnable() {
                        @Override
                        public void run() {
                            downloadFiles(null);
                        }
                    })).execute();
        } else {
            DriveApi.RequestQueue reqs = this.driveApi.new RequestQueue();
            for (String filePath: this.getFilePaths())
                reqs.request(this.downloadContentRequests(filePath));
            reqs.execute();
        }
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
                        } catch (Exception e) {
                            driveApi.getLastRequest().cancel(true);
                        }
                    }
                }, null);
    }

    private DriveApi.Request folderContentRequest () {
        return this.folderContentRequest(null);
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
                Toast.makeText(getApplicationContext(),
                        "Content located on Google Drive.", Toast.LENGTH_SHORT)
                        .show();
            }
        };
    }

    private DriveApi.Request downloadContentRequests (final String filePath) {
        String[] nameAndId = filePath.split("\t");
        final String name = nameAndId[0], id = nameAndId[1];
        return this.driveApi.new Request() {
            @Override
            public byte[] call(DriveApi.MakeRequest makeRequest) throws IOException {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                makeRequest.getService().files().get(id)
                        .executeMediaAndDownloadTo(out);
                return out.toByteArray();
            }

            @Override
            public void after(DriveApi.MakeRequest makeRequest, byte[] output) {
                ByteArrayInputStream input = new ByteArrayInputStream(output);
                Orgestrator.getInstance().add(input, filePath, OrgFile.GOOGLE_DRIVE_RESOURCE);
                Toast.makeText(getApplicationContext(),
                        "file added: " + name, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void before(DriveApi.MakeRequest makeRequest) {}
            @Override
            public void cancelled(DriveApi.MakeRequest makeRequest) {}
        };
    }
}
