package com.github.beardlybread.orgestrator.util;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.github.beardlybread.orgestrator.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class DriveApi extends Fragment {

    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String[] SCOPE = {DriveScopes.DRIVE};
    private static final String PREF_ACCOUNT_NAME = "accountName";

    private GoogleAccountCredential credential = null;

    @Override
    public void onCreate (Bundle b) {
        super.onCreate(b);
        this.credential = GoogleAccountCredential.usingOAuth2(
                getActivity().getApplicationContext(),
                Arrays.asList(DriveApi.SCOPE))
                .setBackOff(new ExponentialBackOff());
    }

    public String getOrgTestFolder () {
        if (!this.isGooglePlayServicesAvailable()) {
            this.acquireGooglePlayServices();
        } else if (this.credential.getSelectedAccountName() == null) {
            this.chooseAccount();
        } else if (!this.isDeviceOnline()) {
            Log.e("DriveApi", "no network connection");
        } else {
            DoRequest<String> request =
                new DoRequest<>(this.credential, DriveApi.orgTestFolder);
            request.execute();
            try {
                return request.get(10, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                Log.e("DriveApi", "request for Org folder timed out");
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean isGooglePlayServicesAvailable () {
        GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
        final int status = gaa.isGooglePlayServicesAvailable(getActivity());
        return status == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices () {
        GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
        final int status = gaa.isGooglePlayServicesAvailable(getActivity());
        if (gaa.isUserResolvableError(status)) {
            gaa.getErrorDialog(
                    getActivity(),
                    status,
                    DriveApi.REQUEST_GOOGLE_PLAY_SERVICES)
                    .show();
        }
    }

    @AfterPermissionGranted(DriveApi.REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount () {
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getActivity().getPreferences(Context.MODE_PRIVATE)
                    .getString(DriveApi.PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                this.credential.setSelectedAccountName(accountName);
            } else {
                startActivityForResult(
                        this.credential.newChooseAccountIntent(),
                        DriveApi.REQUEST_ACCOUNT_PICKER);
            }

        } else {
            EasyPermissions.requestPermissions(
                    getActivity(),
                    getString(R.string.drive_api_request_permissions),
                    DriveApi.REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    private boolean isDeviceOnline () {
        NetworkInfo ni =
                ((ConnectivityManager) getActivity()
                        .getSystemService(Context.CONNECTIVITY_SERVICE))
                        .getActiveNetworkInfo();
        return (ni != null && ni.isConnected());
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Request Logic
    ////////////////////////////////////////////////////////////////////////////////

    interface Request<T> {
        T call (DoRequest doRequest) throws IOException;
    }

    public static final DriveApi.Request<String> orgTestFolder = new Request<String>() {
        @Override
        public String call (DoRequest doRequest) throws IOException {
            FileList result = doRequest.service.files().list()
                    .setSpaces("drive")
                    .setQ("mimeType='application/vnd.google-apps.folder" +
                            " and trashed=false and name='org-test'")
                    .setFields("nextPageToken, files(id)")
                    .execute();
            if (result.getFiles().size() > 0)
                return result.getFiles().get(0).getId();
            return null;
        }
    };

    public class DoRequest<T> extends AsyncTask<Void, Void, T> {

        private Drive service = null;
        private DriveApi.Request<T> request = null;
        private Exception lastError = null;
        private ProgressDialog progress = null;

        public DoRequest(GoogleAccountCredential credential, DriveApi.Request<T> request) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            this.service = new com.google.api.services.drive.Drive.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName(getString(R.string.app_name))
                    .build();
            this.request = request;
        }

        @Override
        protected T doInBackground (Void... params) {
            try {
                return this.request.call(this);
            } catch (Exception e) {
                this.lastError = e;
                this.cancel(true);
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            this.progress = new ProgressDialog(getActivity());
            this.progress.setMessage("Talking to Google Drive...");
            this.progress.show();
        }

        @Override
        protected void onPostExecute(T output) {
            this.progress.dismiss();
        }

        @Override
        protected void onCancelled() {
            this.progress.dismiss();
            if (this.lastError != null) {
                Log.e("DoRequest", this.lastError.getMessage());
            }
        }
    }
}
