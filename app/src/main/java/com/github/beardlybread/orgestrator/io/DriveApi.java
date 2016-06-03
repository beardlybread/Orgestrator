/*
 * Borrowed very heavily from:
 *  https://developers.google.com/drive/v3/web/quickstart/android#step_5_setup_the_sample
 */

package com.github.beardlybread.orgestrator.io;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.github.beardlybread.orgestrator.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class DriveApi implements EasyPermissions.PermissionCallbacks {

    public static final String tag = "io.DriveApi";

    private static final int REQUEST_ACCOUNT_PICKER = 1000;
    private static final int REQUEST_AUTHORIZATION = 1001;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String[] SCOPE = {DriveScopes.DRIVE};

    private static final String ERROR_TITLE = "Google Drive Error";
    private static final String ERROR_OFFLINE =
            "No network connection was found. Please confirm your device is connected.";
    private static final String ERROR_REQUEST_GOOGLE_PLAY = "" +
            "Connecting to Google Drive requires Google Play Services. Please install Google " +
            "Play Services on your device and relaunch this app.";
    private static final String REQUEST_RATIONALE =
            "This app needs to access your Google account to talk to Google Drive.";

    ////////////////////////////////////////////////////////////////////////////////
    // Fields
    ////////////////////////////////////////////////////////////////////////////////

    private static DriveApi instance = null;
    private boolean initialized = false;

    private Activity activity = null;
    private GoogleAccountCredential credential = null;

    private MakeRequest lastRequest = null;

    ////////////////////////////////////////////////////////////////////////////////
    // Initialization
    ////////////////////////////////////////////////////////////////////////////////

    private DriveApi () {}

    /** DriveApi.initialize(Activity) must be called explicitly first.
     *
     * @return the DriveApi singleton
     */
    public static DriveApi getInstance () {
        return DriveApi.instance;
    }

    /** Set up all necessary credentials and permissions.
     *
     * @param activity is the Activity to which the DriveApi singleton is bound
     */
    public static void initialize (Activity activity) {
        if (DriveApi.instance == null) {
            DriveApi.instance = new DriveApi();
        }
        DriveApi.instance.activity = activity;
        DriveApi.instance.initialized = false;
        DriveApi.instance.initialize();
    }

    /** Set up credential and Google Play Services, and choose Google account.
     */
    private void initialize () {
        if (!this.initialized) {
            if (this.credential == null) {
                this.credential = GoogleAccountCredential.usingOAuth2(
                        this.activity.getApplicationContext(),
                        Arrays.asList(DriveApi.SCOPE))
                        .setBackOff(new ExponentialBackOff());
            }
            if (!this.isGooglePlayServicesAvailable()) {
                this.acquireGooglePlayServices();
            } else if (this.credential.getSelectedAccountName() == null) {
                this.chooseAccount();
            } else if (this.deviceIsOffline()) {
                this.showErrorDialog(new Exception(DriveApi.ERROR_OFFLINE));
            } else {
                this.initialized = true;
            }
        }
    }

    /** Hook into the Intents sent to initialize Google Drive.
     *
     * For DriveApi to work correctly, the containing activity that uses DriveApi must call
     * this method in its onActivityResult implementation.
     *
     * @param requestCode indicates the data source.
     * @param resultCode indicates whether the action was successful.
     * @param data holds necessary data returned from the action.
     */
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == DriveApi.REQUEST_GOOGLE_PLAY_SERVICES) {
            if (resultCode != Activity.RESULT_OK) {
                this.showErrorDialog(new Exception(DriveApi.ERROR_REQUEST_GOOGLE_PLAY));
            } else {
                this.initialize();
            }
        } else if (requestCode == DriveApi.REQUEST_ACCOUNT_PICKER) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                if (accountName != null) {
                    (this.activity.getPreferences(Context.MODE_PRIVATE))
                            .edit().putString("accountName", accountName).apply();
                    this.credential.setSelectedAccountName(accountName);
                    this.initialize();
                }
            }
        } else if (requestCode == REQUEST_AUTHORIZATION) {
            if (resultCode == Activity.RESULT_OK) {
                // Try to make the request again.
                if (this.lastRequest != null) {
                    Request r = this.lastRequest.getRequest();
                    this.lastRequest = new MakeRequest(r);
                    this.lastRequest.execute();
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Request logic
    ////////////////////////////////////////////////////////////////////////////////

    public MakeRequest getLastRequest () { return this.lastRequest; }

    /** Create a generic Google Drive query request without extra callbacks.
     *
     * @param query is a Drive...List.setQ valid string defining the query.
     *              docs: https://developers.google.com/drive/v3/web/search-parameters
     * @return a Request object to feed to a MakeRequest task.
     */
    public static Request queryRequest (final String query) {
        return new Request() {
            @Override
            public byte[] call(MakeRequest makeRequest) throws IOException {
                ByteArrayOutputStream fileInfo = new ByteArrayOutputStream();
                FileList result = makeRequest.getService().files().list()
                        .setQ(query)
                        .execute();
                List<File> files = result.getFiles();
                if (files != null) {
                    for (File file : files) {
                        fileInfo.write(file.getName().getBytes());
                        fileInfo.write('\t');
                        fileInfo.write(file.getId().getBytes());
                        fileInfo.write('\n');
                    }
                }
                return fileInfo.toByteArray();
            }

            @Override
            public void before(MakeRequest makeRequest) {}
            @Override
            public void after(MakeRequest makeRequest, byte[] output) {}
            @Override
            public void cancelled(MakeRequest makeRequest) {}
        };
    }

    /** Create a generic Google Drive download request without extra callbacks.
     *
     * @param id uniquely identifies the resource to download.
     * @return a Request object to feed to a MakeRequest task.
     */
    public static Request downloadRequest (final String id) {
        return new Request() {
            @Override
            public byte[] call(MakeRequest makeRequest) throws IOException {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                makeRequest.getService().files().get(id)
                        .executeMediaAndDownloadTo(out);
                return out.toByteArray();
            }

            @Override
            public void before(MakeRequest makeRequest) {}
            @Override
            public void after(MakeRequest makeRequest, byte[] output) {}
            @Override
            public void cancelled(MakeRequest makeRequest) {}
        };
    }

    /** Create a generic Google Drive upload request without extra callbacks.
     *
     * @param data is a byte array containing the data to upload.
     * @param id uniquely identifies the resource to upload.
     * @return a Request object to feed to a MakeRequest task.
     */
    public Request uploadRequest (byte[] data, final String id) {
        final ByteArrayInputStream content = new ByteArrayInputStream(data);
        final long dataLength = data.length;
        return new Request() {
            @Override
            public byte[] call(MakeRequest makeRequest) throws IOException {
                makeRequest.getService().files().update(id, null,
                        new AbstractInputStreamContent(null) {
                    @Override
                    public InputStream getInputStream () throws IOException {
                        return content;
                    }

                    @Override
                    public long getLength () throws IOException {
                        return dataLength;
                    }

                    @Override
                    public boolean retrySupported () {
                        return false;
                    }
                }).execute();
                return null;
            }

            @Override
            public void before (MakeRequest makeRequest) {}
            @Override
            public void after (MakeRequest makeRequest, byte[] output) {}
            @Override
            public void cancelled (MakeRequest makeRequest) {}
        };
    }

    /** This interface plugs into a MakeRequest task to define its behavior.
     */
    public interface Request {
        /** A MakeRequest task invokes this method in the background.
         *
         * @param makeRequest is a back reference to the calling MakeRequest object.
         * @return a byte array (or null) containing the request response.
         * @throws IOException if something goes wrong accessing Google Drive.
         */
        byte[] call (MakeRequest makeRequest) throws IOException;

        /** This method is called before the task begins.
         *
         * @param makeRequest is a back reference to the calling MakeRequest object.
         */
        void before (MakeRequest makeRequest);

        /** This method is called after the task completes successfully.
         *
         * @param makeRequest is a back reference to the calling MakeRequest object.
         * @param output holds the request response in a byte array.
         */
        void after (MakeRequest makeRequest, byte[] output);

        /** This method is called if a task fails or is cancelled.
         *
         * @param makeRequest is a back reference to the calling MakeRequest object.
         */
        void cancelled (MakeRequest makeRequest);
    }

    /** MakeRequest is an AsyncTask designed to talk to Google Drive's API.
     *
     * The DriveApi.Request passed to the constructor describes the actions that the instantiated
     * task will take at each stage of its lifecycle.
     *
     * Every time a MakeRequest is .execute()d, it will be accessible in the DriveApi
     * object with DriveApi.getLastRequest(). This is true regardless of whether it completes
     * successfully or not.
     */
    public class MakeRequest extends AsyncTask<Void, Void, byte[]> {

        private Drive service = null;
        private Request request = null;
        private Exception lastError = null;

        public MakeRequest (Request request) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            this.service = new Drive.Builder(transport, jsonFactory, credential)
                    .setApplicationName(activity.getString(R.string.app_name))
                    .build();
            this.request = request;
        }

        public Request getRequest () { return this.request; }
        public Drive getService () { return this.service; }

        @Override
        protected void onPreExecute () {
            this.request.before(this);
            lastRequest = this;
        }

        @Override
        protected byte[] doInBackground (Void... nothing) {
            try {
                return this.request.call(this);
            } catch (Exception e) {
                this.lastError = e;
                this.cancel(true);
                return null;
            }
        }

        @Override
        protected void onPostExecute (byte[] output) { this.request.after(this, output); }

        @Override
        protected void onCancelled () {
            this.request.cancelled(this);
            if (this.lastError != null) {
                if (this.lastError instanceof UserRecoverableAuthIOException) {
                    activity.startActivityForResult(
                            ((UserRecoverableAuthIOException) this.lastError).getIntent(),
                            DriveApi.REQUEST_AUTHORIZATION);
                } else {
                    showErrorDialog(this.lastError);
                }
            } else {
                Toast cancelled = new Toast(activity.getApplicationContext());
                cancelled.setText("Google Drive action cancelled.");
                cancelled.show();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Helpers
    ////////////////////////////////////////////////////////////////////////////////

    private void acquireGooglePlayServices () {
        GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
        final int status = gaa.isGooglePlayServicesAvailable(this.activity);
        if (gaa.isUserResolvableError(status)) {
            this.showErrorDialog(status);
        }
    }

    @AfterPermissionGranted(DriveApi.REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount () {
        if (EasyPermissions.hasPermissions(
                this.activity, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = this.activity.getPreferences(Context.MODE_PRIVATE)
                    .getString("accountName", null);
            if (accountName != null) {
                this.credential.setSelectedAccountName(accountName);
                this.initialize();
            } else {
                this.activity.startActivityForResult(
                        this.credential.newChooseAccountIntent(),
                        DriveApi.REQUEST_ACCOUNT_PICKER);
            }
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    DriveApi.REQUEST_RATIONALE,
                    DriveApi.REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    private boolean deviceIsOffline () {
        ConnectivityManager cm = (ConnectivityManager)
                this.activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni == null || !ni.isConnected());
    }

    private boolean isGooglePlayServicesAvailable () {
        GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
        final int status = gaa.isGooglePlayServicesAvailable(this.activity);
        return status == ConnectionResult.SUCCESS;
    }

    @Override
    public void onPermissionsGranted (int requestCode, List<String> perms) {
        Log.v(tag, "permission granted: " + requestCode);
    }

    @Override
    public void onPermissionsDenied (int requestCode, List<String> perms) {
        Log.v(tag, "permission denied: " + requestCode);
    }

    @Override
    public void onRequestPermissionsResult (int requestCode,
                                            @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    public void showErrorDialog (final int connectionStatusCode) {
        GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
        gaa.getErrorDialog(
                this.activity,
                connectionStatusCode,
                DriveApi.REQUEST_GOOGLE_PLAY_SERVICES)
                .show();
    }

    public void showErrorDialog (Exception e) {
        Log.e(tag, "--- BEGIN: " + e.getMessage());
        e.printStackTrace();
        Log.e(tag, "--- END: " + e.getMessage());
        (new AlertDialog.Builder(this.activity.getApplicationContext()))
                .setTitle(DriveApi.ERROR_TITLE)
                .setMessage(e.getMessage())
                .create().show();
    }
}
