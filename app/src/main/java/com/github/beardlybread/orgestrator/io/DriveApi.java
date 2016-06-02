package com.github.beardlybread.orgestrator.io;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.beardlybread.orgestrator.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class DriveApi extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks {

    public static final int INVALID = -1;
    public static final int QUERY = 0;
    public static final int DOWNLOAD = 1;
    public static final int UPLOAD = 2;

    public static final String OP_TYPE =
            "com.github.beardlybread.orgestrator.io.DriveApi.OP_TYPE";

    public static final String OP_BODY =
            "com.github.beardlybread.orgestrator.io.DriveApi.OP_BODY";

    public static final String OP_RESULT =
            "com.github.beardlybread.orgestrator.io.DriveApi.OP_RESULT";

    private static final String[] SCOPE = {DriveScopes.DRIVE};
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final int REQUEST_ACCOUNT_PICKER = 1000;
    private static final int REQUEST_AUTHORIZATION = 1001;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private GoogleAccountCredential credential;
    private int opType = DriveApi.INVALID;
    private byte[] opBody = null;
    private Request request = null;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_api);
        this.credential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(DriveApi.SCOPE))
                .setBackOff(new ExponentialBackOff());
    }

    @Override
    protected void onStart () {
        super.onStart();
        Intent intent = getIntent();
        this.opType = intent.getIntExtra(DriveApi.OP_TYPE, DriveApi.INVALID);
        this.opBody = intent.getByteArrayExtra(DriveApi.OP_BODY);
        this.makeRequest();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_PERMISSION_GET_ACCOUNTS:
                Log.v("DriveApi", "get accounts appears in onActivityResult");
                break;
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    this.showErrorDialog(new Exception(
                            getString(R.string.drive_api_request_google_play_services_error)));
                } else {
                    this.makeRequest();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        this.credential.setSelectedAccountName(accountName);
                        this.makeRequest();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    this.makeRequest();
                }
                break;
        }
    }

    public void makeRequest () {
        if (this.request == null) {
            switch (this.opType) {
                case DriveApi.QUERY:
                    this.request = this.queryRequest(new String(this.opBody));
                    break;
                case DriveApi.DOWNLOAD:
                    this.request = this.downloadRequest(new String(this.opBody));
                    break;
                case DriveApi.UPLOAD:
                    this.request = this.uploadRequest(new String(this.opBody));
                    break;
                case DriveApi.INVALID:
                default:
                    break;
            }
        }
        try {
            if (this.request == null)
                throw new DriveApi.InvalidOpBody("An invalid Drive operation was sent.");
            if (!this.isGooglePlayServicesAvailable()) {
                this.acquireGooglePlayServices();
            } else if (this.credential.getSelectedAccountName() == null) {
                this.chooseAccount();
            } else if (this.deviceOffline()) {
                throw new Exception("No network connection available");
            } else {
                (new MakeRequest(this.request)).execute();
            }
        } catch (Exception e) {
            this.showErrorDialog(e);
            this.sendErrorIntent();
        }
    }

    private boolean isGooglePlayServicesAvailable () {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private boolean deviceOffline () {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo == null || !networkInfo.isConnected());
    }

    private void acquireGooglePlayServices () {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            this.showErrorDialog(connectionStatusCode);
        }
    }

    @AfterPermissionGranted(DriveApi.REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount () {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                this.credential.setSelectedAccountName(accountName);
                this.makeRequest();
            } else {
                startActivityForResult(
                        this.credential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            Log.v("DriveApi", "in requestPermissions");
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    DriveApi.REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    public void sendErrorIntent () {
        Intent badness = new Intent();
        setResult(Activity.RESULT_CANCELED, badness);
        finish();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    public void showErrorDialog (Exception e) {
        e.printStackTrace();
        Log.e("DriveApi", e.getMessage());
        if (e instanceof GooglePlayServicesAvailabilityIOException) {
            GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
            gaa.getErrorDialog(
                    DriveApi.this,
                    ((GooglePlayServicesAvailabilityIOException) e).getConnectionStatusCode(),
                    DriveApi.REQUEST_GOOGLE_PLAY_SERVICES)
                    .show();
        } else {
            (new android.app.AlertDialog.Builder(getApplicationContext()))
                    .setTitle(getString(R.string.drive_api_error_title))
                    .setMessage(e.getMessage())
                    .create().show();
        }
    }

    public void showErrorDialog (final int connectionStatusCode) {
        GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
        gaa.getErrorDialog(
                DriveApi.this,
                connectionStatusCode,
                DriveApi.REQUEST_GOOGLE_PLAY_SERVICES)
                .show();
    }

    ////////////////////////////////////////////////////////////////////////////////
    // request logic
    ////////////////////////////////////////////////////////////////////////////////

    public Request queryRequest (final String query) {
        return new Request() {
            @Override
            public byte[] call (MakeRequest makeRequest)
                    throws IOException {
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
        };
    }

    public Request downloadRequest (final String id) {
        return null;
    }

    public Request uploadRequest (final String id) {
        return null;
    }

    interface Request {
        byte[] call (MakeRequest makeRequest) throws IOException;
    }

    private class MakeRequest extends AsyncTask<Void, Void, byte[]> {

        private Drive service = null;
        private Request request = null;
        private Exception lastError = null;

        public MakeRequest (Request request) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            this.service = new Drive.Builder(transport, jsonFactory, credential)
                    .setApplicationName(getString(R.string.app_name))
                    .build();
            this.request = request;
        }

        public Drive getService () { return this.service; }

        @Override
        protected void onPreExecute () {
            // nothin'
        }

        @Override
        protected byte[] doInBackground (Void... params) {
            try {
                return this.request.call(this);
            } catch (Exception e) {
                this.lastError = e;
                this.cancel(true);
                return null;
            }
        }

        @Override
        protected void onPostExecute (byte[] output) {
            Log.d("DriveApi", "in onPostExecute");
            Intent result = new Intent();
            result.putExtra(DriveApi.OP_RESULT, output);
            setResult(Activity.RESULT_OK, result);
            finish();
        }

        @Override
        protected void onCancelled () {
            if (this.lastError != null) {
                if (this.lastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) this.lastError).getIntent(),
                            DriveApi.REQUEST_AUTHORIZATION);
                } else {
                    showErrorDialog(this.lastError);
                }
            } else {
                Toast cancelled = new Toast(getApplicationContext());
                cancelled.setText("Google Drive action cancelled.");
                cancelled.show();
            }
            sendErrorIntent();
        }
    }

    public class InvalidOpBody extends Exception {
        public InvalidOpBody (String msg) {
            super(msg);
        }
    }

}
