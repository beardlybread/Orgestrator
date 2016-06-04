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
import java.util.concurrent.ConcurrentLinkedDeque;

public class DriveApiClassActivity extends AppCompatActivity {

    private static final String FOLDER_ID_QUERY =
            "trashed=false and 'root' in parents and name='orgestrator-test'";
    private static final String FOLDER_CONTENTS_QUERY_FORMAT =
            "trashed=false and '%s' in parents";

    private DriveApi api = null;
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
        this.api = new DriveApi();
        ft.add(api, DriveApi.tag);
        ft.commit();
    }

    public String getFolderId () { return this.folderId; }

    public String[] getFilePaths () { return this.filePaths; }

    public void getResourceIds (View v) {
        ConcurrentLinkedDeque<DriveApi.Request> requests = new ConcurrentLinkedDeque<>();
        if (this.folderId == null)
            requests.add(this.folderIdRequest());
        requests.add(this.folderContentRequest());
        DriveApi.Request first = requests.poll();
        this.api.new MakeRequest(first).execute(requests);
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
            }));
            DriveApi.Request first = requests.poll();
            this.api.new MakeRequest(first).execute(requests);
        } else {
            for (String filePath: this.getFilePaths())
                requests.add(this.downloadContentRequests(filePath));
            DriveApi.Request first = requests.poll();
            this.api.new MakeRequest(first).execute(requests);
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
            public void after(DriveApi.MakeRequest makeRequest, byte[] output) {
                String response = new String(output);
                filePaths = response.trim().split("\n");
                Toast.makeText(getApplicationContext(),
                        "Content located on Google Drive.", Toast.LENGTH_SHORT)
                        .show();
                if (then != null) {
                    then.run();
                }
            }

            @Override
            public void before(DriveApi.MakeRequest makeRequest) {}
            @Override
            public void cancelled(DriveApi.MakeRequest makeRequest) {}
        };
    }

    private DriveApi.Request downloadContentRequests (final String filePath) {
        String[] nameAndId = filePath.split("\t");
        final String name = nameAndId[0], id = nameAndId[1];
        return new DriveApi.Request() {
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
                Orgestrator.getInstance().add(input, filePath, OrgFile.DRIVE_RESOURCE);
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
