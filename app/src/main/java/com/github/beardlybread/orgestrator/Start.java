/*
The MIT License (MIT)

Copyright (c) 2016 Bradley Steinbacher

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.github.beardlybread.orgestrator;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.beardlybread.orgestrator.io.GoogleDriveApi;
import com.github.beardlybread.orgestrator.org.OrgFile;
import com.github.beardlybread.orgestrator.org.Orgestrator;
import com.github.beardlybread.orgestrator.ui.ToDoList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Start extends AppCompatActivity {

    ////////////////////////////////////////////////////////////////////////////////
    // Static fields
    ////////////////////////////////////////////////////////////////////////////////

    private static final String FOLDER_ID_QUERY =
            "trashed=false and 'root' in parents and name='%s'";
    private static final String FOLDER_CONTENTS_QUERY =
            "trashed=false and '%s' in parents";

    ////////////////////////////////////////////////////////////////////////////////
    // Fields
    ////////////////////////////////////////////////////////////////////////////////

    private GoogleDriveApi driveApi = null;
    private String folderName = "orgestrator";
    private String folderId = null;
    private String[] filePaths = null;

    ////////////////////////////////////////////////////////////////////////////////
    // Lifecycle callbacks
    ////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.driveApi = new GoogleDriveApi();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(this.driveApi, GoogleDriveApi.tag);
        ft.commit();
        setContentView(R.layout.activity_start);
        Toolbar tb = (Toolbar) findViewById(R.id.activity_start_toolbar);
        setSupportActionBar(tb);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.start_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_start_menu_upload:
                this.uploadToDrive();
                return true;
            case R.id.action_start_menu_download:
                this.downloadFromDrive();
                return true;
            case R.id.action_start_menu_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Actions
    ////////////////////////////////////////////////////////////////////////////////

    /** Download files in the base folder.
     *
     * If the base folder id is unknown, Google Drive is queried for it. After files are found,
     * another call to downloadFromDrive will not download them again.
     */
    public void downloadFromDrive () {
        if (this.folderId == null) {
            this.driveApi.new RequestQueue()
                    .enqueue(this.folderIdRequest())
                    .whenFinished(this.restartDownload)
                    .execute();
        } else if (this.filePaths == null) {
            this.driveApi.new RequestQueue()
                    .enqueue(this.folderContentRequest())
                    .whenFinished(this.restartDownload)
                    .execute();
        } else {
            Orgestrator org = Orgestrator.getInstance();
            if (this.filePaths.length > 0) {
                GoogleDriveApi.RequestQueue downloads = this.driveApi.new RequestQueue();
                for (String filePath: this.filePaths) {
                    if (org.find(filePath, OrgFile.GOOGLE_DRIVE_RESOURCE) == null)
                        downloads.enqueue(this.downloadRequest(filePath));
                }
                downloads.whenFinished(this.refreshList).execute();
            }
        }
    }

    /** Upload all files to the base folder.
     *
     * The upload process deletes and replaces the existing file.
     */
    public void uploadToDrive () {
        List<OrgFile> files = Orgestrator.getInstance().getFiles();
        if (this.driveApi != null && files.size() > 0) {
            GoogleDriveApi.RequestQueue uploads = this.driveApi.new RequestQueue();
            for (OrgFile file : files) {
                if (file.getResourceType() == OrgFile.GOOGLE_DRIVE_RESOURCE)
                    try {
                        uploads.enqueue(this.uploadRequest(file));
                    } catch (IOException e) {
                        driveApi.makeToast(
                                "File upload failed:" + file.getRawPath().split("\t")[0]);
                    }
            }
            uploads.whenFinished(this.refreshList).execute();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Action helpers
    ////////////////////////////////////////////////////////////////////////////////

    /** The restartDownload field is a callback to restart the download method if it fails.
     */
    private final GoogleDriveApi.Afterwards restartDownload =
            new GoogleDriveApi.Afterwards() {
                @Override
                public void run(GoogleDriveApi.MakeRequest makeRequest, byte[] output) {
                    downloadFromDrive();
                }
            };

    /** The refreshList field is a callback to refresh the list view.
     */
    private final GoogleDriveApi.Afterwards refreshList =
            new GoogleDriveApi.Afterwards() {
                @Override
                public void run(GoogleDriveApi.MakeRequest makeRequest, byte[] output) {
                    ((ToDoList) getSupportFragmentManager()
                            .findFragmentById(R.id.start_to_do_list))
                            .refresh();
                }
            };

    ////////////////////////////////////////////////////////////////////////////////
    // Google Drive Requests
    ////////////////////////////////////////////////////////////////////////////////

    /** Return a query for the id of Orgestrator's folder name.
     *
     * @return a Request object defining a query for thd id of Orgestrator's folder name.
     */
    private GoogleDriveApi.Request folderIdRequest () {
        return this.driveApi.listQuery(
                String.format(FOLDER_ID_QUERY, getFolderName()),
                new GoogleDriveApi.Afterwards() {
                    @Override
                    public void run(GoogleDriveApi.MakeRequest makeRequest, byte[] output) {
                        String raw = new String(output);
                        folderId = raw.trim().split("\t")[1];
                    }
                }, null);
    }

    /** Return a query for the name and id of all files in the base folder.
     *
     * @return a Request object defining a query for all files in the base folder.
     */
    private GoogleDriveApi.Request folderContentRequest () {
        return this.driveApi.listQuery(
                String.format(FOLDER_CONTENTS_QUERY, getFolderId()),
                new GoogleDriveApi.Afterwards() {
                    @Override
                    public void run(GoogleDriveApi.MakeRequest makeRequest, byte[] output) {
                        String response = new String(output);
                        filePaths = response.trim().split("\n");
                        driveApi.makeToast(filePaths.length + " files located on Google Drive.");
                    }
                }, null);
    }

    /** Return a download request.
     *
     * @param filePath is a tab delimited name/id pair identifying the download target.
     * @return a Request object defining a download for the given file path.
     */
    public GoogleDriveApi.Request downloadRequest (final String filePath) {
        String[] nameAndId = filePath.split("\t");
        final String name = nameAndId[0], id = nameAndId[1];
        return this.driveApi.downloadRequest(id,
                new GoogleDriveApi.Afterwards() {
                    @Override
                    public void run(GoogleDriveApi.MakeRequest makeRequest, byte[] output) {
                        ByteArrayInputStream input = new ByteArrayInputStream(output);
                        Orgestrator.getInstance()
                                .add(input, filePath, OrgFile.GOOGLE_DRIVE_RESOURCE);
                        driveApi.makeToast("Loaded: " + name);
                    }
                }, null);
    }

    /** Return an upload request.
     *
     * @param file is the OrgFile object to upload to Google Drive.
     * @return a Request object defining an upload for the given file.
     * @throws IOException if reconstructing the target file fails.
     */
    public GoogleDriveApi.Request uploadRequest (final OrgFile file) throws IOException {
        String[] nameAndId = file.getRawPath().split("\t");
        final String name = nameAndId[0], id = nameAndId[1];
        final ArrayList<String> parents = new ArrayList<>();
        parents.add(folderId);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        file.write(out);
        final byte[] data = out.toByteArray();
        return this.driveApi.uploadRequest(name, id, parents, data,
                new GoogleDriveApi.Afterwards() {
                    @Override
                    public void run(GoogleDriveApi.MakeRequest makeRequest, byte[] output) {
                        driveApi.makeToast("Saved: " + name);
                    }
                }, null);
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////////

    public String getFolderName () { return this.folderName; }
    public String getFolderId () { return this.folderId; }

}

