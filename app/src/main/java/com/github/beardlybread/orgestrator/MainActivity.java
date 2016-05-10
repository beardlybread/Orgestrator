package com.github.beardlybread.orgestrator;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends Activity implements EasyPermissions.PermissionCallbacks {

    GoogleAccountCredential credential;
    private TextView outputText;
    private Button callApiButton;
    ProgressDialog progress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String BUTTON_TEXT = "Call Drive API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {
            DriveScopes.DRIVE_METADATA_READONLY
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout ll = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(lp);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(16, 16, 16, 16);

        ViewGroup.LayoutParams vglp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        callApiButton = new Button(this);
        callApiButton.setText(BUTTON_TEXT);
        callApiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                callApiButton.setEnabled(false);
                outputText.setText("");
                getResultsFromApi();
                callApiButton.setEnabled(true);
            }
        });
        ll.addView(callApiButton);

        outputText = new TextView(this);
        outputText.setLayoutParams(vglp);
        outputText.setPadding(16, 16, 16, 16);
        outputText.setVerticalScrollBarEnabled(true);
        outputText.setMovementMethod(new ScrollingMovementMethod());
        outputText.setText("Click the \"" + BUTTON_TEXT + "\" button to test the API.");
        ll.addView(outputText);

        progress = new ProgressDialog(this);
        progress.setMessage("Calling Drive API...");

        setContentView(ll);

        credential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    private void getResultsFromApi () {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (credential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            outputText.setText("No network connection available.");
        } else {
            new MakeRequestTask(credential).execute();
        }
    }

    @AfterPermissionGranted(MainActivity.REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount () {
        if (EasyPermissions.hasPermissions(this, android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                credential.setSelectedAccountName(accountName);
            } else {
                startActivityForResult(
                        credential.newChooseAccountIntent(),
                        MainActivity.REQUEST_ACCOUNT_PICKER);
            }
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    MainActivity.REQUEST_PERMISSION_GET_ACCOUNTS,
                    android.Manifest.permission.GET_ACCOUNTS);
        }
    }

    @Override
    protected void onActivityResult (int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        switch (req) {
            case MainActivity.REQUEST_GOOGLE_PLAY_SERVICES:
                if (res != RESULT_OK) {
                    outputText.setText(
                            "This app requires Google Play Services. Please install Google Play " +
                            "Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case MainActivity.REQUEST_ACCOUNT_PICKER:
                if (res == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        credential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case MainActivity.REQUEST_AUTHORIZATION:
                if (res == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult (int req,
                                            @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(req, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(req, permissions, grantResults, this);
    }

    private boolean isDeviceOnline () {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null && ni.isConnected());
    }

    private boolean isGooglePlayServicesAvailable () {
        GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
        return gaa.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices () {
        GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
        final int conStatus = gaa.isGooglePlayServicesAvailable(this);
        if (gaa.isUserResolvableError(conStatus)) {
            showGooglePlayServicesAvailabilityErrorDialog(conStatus);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog (final int conStatus) {
        GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
        Dialog d = gaa.getErrorDialog(
                MainActivity.this,
                conStatus,
                MainActivity.REQUEST_GOOGLE_PLAY_SERVICES);
        d.show();
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {

        private com.google.api.services.drive.Drive service = null;
        private Exception lastErr = null;

        public MakeRequestTask (GoogleAccountCredential cred) {
            HttpTransport ht = AndroidHttp.newCompatibleTransport();
            JsonFactory jf = JacksonFactory.getDefaultInstance();
            service = new com.google.api.services.drive.Drive.Builder(ht, jf, cred)
                    .setApplicationName("Orgestrator")
                    .build();
        }

        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                lastErr = e;
                cancel(true);
                return null;
            }
        }

        private List<String> getDataFromApi () throws IOException {
            List<String> fileInfo = new ArrayList<String>();
            FileList res = service.files().list()
                    .setPageSize(10)
                    .setFields("nextPageToken, items(id, name)")
                    .execute();
            List<File> files = res.getFiles();
            if (files != null) {
                for (File f: files) {
                    fileInfo.add(String.format("%s (%s)\n", f.getName(), f.getId()));
                }
            }
            return fileInfo;
        }

        @Override
        protected void onPreExecute () {
            outputText.setText("");
            progress.show();
        }

        @Override
        protected void onCancelled () {
            progress.hide();
            if (lastErr != null) {
                if (lastErr instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) lastErr)
                            .getConnectionStatusCode());
                } else if (lastErr instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) lastErr).getIntent(),
                            MainActivity.REQUEST_AUTHORIZATION);
                } else {
                    outputText.setText("The following error occurred:\n" + lastErr.getMessage());
                }
            } else {
                outputText.setText("Request cancelled.");
            }
        }
    }

    @Override
    public void onPermissionsGranted (int req, List<String> list) {
        // nothing
    }

    @Override
    public void onPermissionsDenied (int req, List<String> list) {
        // nothing
    }
}
