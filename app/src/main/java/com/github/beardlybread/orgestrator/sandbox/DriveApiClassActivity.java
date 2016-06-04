package com.github.beardlybread.orgestrator.sandbox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.beardlybread.orgestrator.R;
import com.github.beardlybread.orgestrator.io.DriveApi;
import com.github.beardlybread.orgestrator.org.Orgestrator;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DriveApiClassActivity extends AppCompatActivity {

    private static final String FOLDER_ID_QUERY =
            "trashed=false and 'root' in parents and name='orgestrator-test'";
    private static final String FOLDER_CONTENTS_QUERY_FORMAT =
            "trashed=false and '%s' in parents";

    private ProgressDialog progressMessage = null;
    private String folderId = null;
    private String[] filePaths = null;

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

    public String getFolderId () { return this.folderId; }

    public String[] getFilePaths () { return this.filePaths; }

    public void getResourceIds (View v) {
        ConcurrentLinkedDeque<DriveApi.Request> requests = new ConcurrentLinkedDeque<>();
        if (this.folderId == null)
            requests.add(this.folderIdRequest());
        requests.add(this.folderContentRequest());
        DriveApi.Request first = requests.poll();
        DriveApi.getInstance().new MakeRequest(first).execute(requests);
    }

    public void downloadFiles (View v) {
        ConcurrentLinkedDeque<DriveApi.Request> requests = new ConcurrentLinkedDeque<>();
        if (this.folderId == null || this.filePaths == null) {
            requests.add(this.folderIdRequest());
            requests.add(this.folderContentRequest(new Runnable() {
                @Override
                public void run() {
                    downloadFiles(null);
                }
            });
        } else {
            for (String filePath: this.getFilePaths()) {

            }
        }
    }

    private DriveApi.Request folderIdRequest () {
        return new DriveApi.Request() {
            @Override
            public byte[] call(DriveApi.MakeRequest makeRequest) throws IOException {
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
            public void after (DriveApi.MakeRequest makeRequest, byte[] output) {
                folderId = new String(output);
            }

            @Override
            public void before (DriveApi.MakeRequest makeRequest) {}
            @Override
            public void cancelled (DriveApi.MakeRequest makeRequest) {}
        };
    }

    private DriveApi.Request folderContentRequest () {
        return this.folderContentRequest(null);
    }

    private DriveApi.Request folderContentRequest (final Runnable then) {
        return new DriveApi.Request() {
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
            public void before(DriveApi.MakeRequest makeRequest) {
                progressMessage.show();
            }

            @Override
            public void after(DriveApi.MakeRequest makeRequest, byte[] output) {
                progressMessage.hide();
                String response = new String(output);
                filePaths = response.trim().split("\n");
                Toast t = new Toast(getApplicationContext());
                t.setText("Content located on Google Drive.");
                t.show();
                if (then != null) {
                    then.run();
                }
            }

            @Override
            public void cancelled(DriveApi.MakeRequest makeRequest) {
                progressMessage.hide();
            }
        };
    }

    private List<DriveApi.Request> downloadContentRequests () {
        ArrayList<DriveApi.Request> out = new ArrayList<>();
        return new DriveApi.Request() {
            @Override
            public byte[] call(DriveApi.MakeRequest makeRequest) throws IOException {
                return new byte[0];
            }

            @Override
            public void before(DriveApi.MakeRequest makeRequest) {

            }

            @Override
            public void after(DriveApi.MakeRequest makeRequest, byte[] output) {

            }

            @Override
            public void cancelled(DriveApi.MakeRequest makeRequest) {

            }
        }
    }
}
