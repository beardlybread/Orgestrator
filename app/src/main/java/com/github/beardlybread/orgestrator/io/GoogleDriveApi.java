/*
This file is a modification of Google's quick startup code available at:
  https://developers.google.com/drive/v3/web/quickstart/android#step_5_setup_the_sample
which was released under the Apache License, 2.0.

Copyright 2016 Google, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.github.beardlybread.orgestrator.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.ByteArrayContent;
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
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class GoogleDriveApi extends Fragment
        implements  EasyPermissions.PermissionCallbacks {

    ////////////////////////////////////////////////////////////////////////////////
    // Static fields
    ////////////////////////////////////////////////////////////////////////////////

    public static final String tag = GoogleDriveApi.class.getSimpleName();

    private static final int REQUEST_ACCOUNT_PICKER = 1000;
    private static final int REQUEST_AUTHORIZATION = 1001;
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String[] SCOPE = {DriveScopes.DRIVE};

    private static final String ERROR_OFFLINE =
            "No network connection was found. Please confirm your device is connected.";
    private static final String ERROR_REQUEST_GOOGLE_PLAY = "" +
            "Connecting to Google Drive requires Google Play Services. Please install Google " +
            "Play Services on your device and relaunch this app.";
    private static final String REQUEST_RATIONALE =
            "This app needs to access your Google account to talk to Google Drive.";

    private static final int HISTORY_SIZE = 1000;

    ////////////////////////////////////////////////////////////////////////////////
    // Fields
    ////////////////////////////////////////////////////////////////////////////////

    private boolean initialized = false;
    private GoogleAccountCredential credential = null;

    private ArrayDeque<MakeRequest> requestHistory = null;

    ////////////////////////////////////////////////////////////////////////////////
    // Initialization
    ////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate (Bundle b) {
        super.onCreate(b);
        this.requestHistory = new ArrayDeque<>();
        this.initialize();
    }

    /** Set up credential and Google Play Services, and choose Google account.
     */
    public void initialize () {
        if (!this.initialized) {
            if (this.credential == null) {
                this.credential = GoogleAccountCredential.usingOAuth2(
                        getContext().getApplicationContext(),
                        Arrays.asList(GoogleDriveApi.SCOPE))
                        .setBackOff(new ExponentialBackOff());
            }
            if (!this.isGooglePlayServicesAvailable()) {
                this.acquireGooglePlayServices();
            } else if (this.credential.getSelectedAccountName() == null) {
                this.chooseAccount();
            } else if (this.deviceIsOffline()) {
                this.showErrorDialog(new Exception(GoogleDriveApi.ERROR_OFFLINE));
            } else {
                this.initialized = true;
            }
        }
    }

    /** Hook into the Intents sent to initialize Google Drive.
     *
     * @param requestCode indicates the data source.
     * @param resultCode indicates whether the action was successful.
     * @param data holds necessary data returned from the action.
     */
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == GoogleDriveApi.REQUEST_GOOGLE_PLAY_SERVICES) {
            if (resultCode != Activity.RESULT_OK) {
                this.showErrorDialog(new Exception(GoogleDriveApi.ERROR_REQUEST_GOOGLE_PLAY));
            } else {
                this.initialize();
            }
        } else if (requestCode == GoogleDriveApi.REQUEST_ACCOUNT_PICKER) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                if (accountName != null) {
                    (getActivity().getPreferences(Context.MODE_PRIVATE))
                            .edit().putString("accountName", accountName).apply();
                    this.credential.setSelectedAccountName(accountName);
                    this.initialize();
                }
            }
        } else if (requestCode == REQUEST_AUTHORIZATION) {
            if (resultCode == Activity.RESULT_OK) {
                if (this.getLastRequest().getRequest().isIncomplete())
                    new MakeRequest(this.getLastRequest().getRequest()).execute();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // History
    ////////////////////////////////////////////////////////////////////////////////

    /** Add a request to the history of calls.
     * @param request is a MakeRequest to log into the history.
     */
    private synchronized void addToHistory (MakeRequest request) {
        this.requestHistory.addLast(request);
        if (this.requestHistory.size() > GoogleDriveApi.HISTORY_SIZE)
            this.requestHistory.removeFirst();
    }

    public synchronized MakeRequest getLastRequest () {
        return this.requestHistory.peekLast();
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Request generators
    ////////////////////////////////////////////////////////////////////////////////

    /** Create a generic Google Drive query request without extra callbacks.
     *
     * @param query is a Drive...List.setQ valid string defining the query.
     *              docs: https://developers.google.com/drive/v3/web/search-parameters
     * @param then is run after the request if it succeeds.
     * @param otherwise is run after the request if it fails.
     * @return a Request object to feed to a MakeRequest task.
     */
    public Request listQuery (final String query, Afterwards then, Afterwards otherwise) {
        return new Request(then, otherwise) {
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
        };
    }

    /** Create a generic Google Drive download request without extra callbacks.
     *
     * @param id uniquely identifies the resource to download.
     * @param then is run after the request if it succeeds.
     * @param otherwise is run after the request if it fails.
     * @return a Request object to feed to a MakeRequest task.
     */
    public Request downloadRequest (final String id, Afterwards then, Afterwards otherwise) {
        return new Request(then, otherwise) {
            @Override
            public byte[] call(MakeRequest makeRequest) throws IOException {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                makeRequest.getService().files().get(id)
                        .executeMediaAndDownloadTo(out);
                return out.toByteArray();
            }
        };
    }

    /** Create a generic Google Drive upload request without extra callbacks.
     *
     * @param name will be assigned to the resource on Google Drive.
     * @param id uniquely identifies the resource to be replaced.
     * @param parents is a normally one element list containing the parent id.
     * @param data holds the raw data to push to Google Drive.
     * @param then will get called if the request succeeds.
     * @param otherwise will get called if the request fails.
     * @return a Request object to feed to a MakeRequest task.
     */
    public Request uploadRequest (final String name, final String id,
                                  final List<String> parents, final byte[] data,
                                  final Afterwards then, final Afterwards otherwise) {
        return new Request(then, otherwise) {
            @Override
            public byte[] call(MakeRequest makeRequest) throws IOException {
                makeRequest.getService().files().delete(id).execute();
                File meta = new File();
                meta.setName(name);
                meta.setParents(parents);
                makeRequest.getService().files()
                        .create(meta, new ByteArrayContent("text/plain; charset=utf-8", data))
                        .execute();
                return null;
            }
        };
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Request logic
    ////////////////////////////////////////////////////////////////////////////////

    /** The Request class plugs into a MakeRequest to define its behavior.
     *
     * If a Request successfully passes to onPostExecute in a MakeRequest, it will be marked as
     * completed. In a RequestQueue, this means that it will not be executed again. Also, if it
     * was passed a "then" on instantiation, it will be invoked on completion before any further
     * Requests in a RequestQueue are made. An "otherwise" will be called in onCancelled in the
     * finally clause of the try block wrapping everything else. Both "then" and "otherwise" are
     * implementations of the Afterwards interface.
     */
    public abstract class Request {
        private boolean incomplete = true;
        public final Afterwards then;
        public final Afterwards otherwise;

        public Request () {
            this(null, null);
        }

        public Request (Afterwards then) {
            this(then, null);
        }

        public Request (Afterwards then, Afterwards otherwise) {
            this.then = then;
            this.otherwise = otherwise;
        }

        public boolean isIncomplete () { return this.incomplete; }
        public void setCompleted () { this.incomplete = false; }

        /** A MakeRequest task invokes this method in the background.
         *
         * @param makeRequest is a back reference to the calling MakeRequest object.
         * @return a byte array (or null) containing the request response.
         * @throws IOException if something goes wrong accessing Google Drive.
         */
        public abstract byte[] call (MakeRequest makeRequest) throws IOException;

        /** This method is called before the task begins.
         *
         * @param makeRequest is a back reference to the calling MakeRequest object.
         */
        public void before (MakeRequest makeRequest) {}

        /** This method is called after the task completes successfully.
         *
         * @param makeRequest is a back reference to the calling MakeRequest object.
         * @param output holds the request response in a byte array.
         */
        public void after (MakeRequest makeRequest, byte[] output) {}

        /** This method is called if a task fails or is cancelled.
         *
         * @param makeRequest is a back reference to the calling MakeRequest object.
         */
        public void cancelled (MakeRequest makeRequest) {}
    }

    /** Afterwards is a parameterized Runnable-like interface consumed by Request objects.
     */
    public interface Afterwards {
        void run (MakeRequest makeRequest, byte[] output);
    }

    /** MakeRequest is an AsyncTask designed to talk to Google Drive's API.
     *
     * The GoogleDriveApi.Request passed to the constructor describes the actions that the
     * instantiated task will take at each stage of its lifecycle.
     *
     * Every time a MakeRequest is .execute()d, it will be accessible in the GoogleDriveApi
     * object with GoogleDriveApi.getLastRequest(). This is true regardless of whether it completes
     * successfully or not.
     */
    public class MakeRequest extends AsyncTask<RequestQueue, Void, byte[]> {

        private Drive service = null;
        private Request request = null;
        private Exception lastError = null;
        private RequestQueue remaining = null;

        public MakeRequest (Request request) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            this.service = new Drive.Builder(transport, jsonFactory, credential)
                    .setApplicationName(getString(R.string.app_name))
                    .build();
            this.request = request;
        }

        public Request getRequest () { return this.request; }
        public Drive getService () { return this.service; }

        @Override
        protected void onPreExecute () {
            this.request.before(this);
            addToHistory(this);
        }

        @Override
        protected byte[] doInBackground (RequestQueue... nexts) {
            try {
                if (nexts.length > 0) {
                    this.remaining = nexts[0];
                }
                return this.request.call(this);
            } catch (Exception e) {
                this.lastError = e;
                this.cancel(true);
                return null;
            }
        }

        @Override
        protected void onPostExecute (byte[] output) {
            if (this.request.isIncomplete()) {
                this.request.after(this, output);
                this.request.setCompleted();
                if (this.request.then != null) {
                    this.request.then.run(this, output);
                }
            }
            if (this.remaining != null)
                this.remaining.next();
        }

        @Override
        protected void onCancelled () {
            try {
                this.request.cancelled(this);
                if (this.lastError != null) {
                    if (this.lastError instanceof UserRecoverableAuthIOException) {
                        startActivityForResult(
                                ((UserRecoverableAuthIOException) this.lastError).getIntent(),
                                GoogleDriveApi.REQUEST_AUTHORIZATION);
                    } else {
                        showErrorDialog(this.lastError);
                    }
                } else {
                    makeToast("Google Drive action cancelled.");
                }
            } finally {
                if (this.request.otherwise != null)
                    this.request.otherwise.run(this, null);
            }
        }
    }

    private final Request EMPTY_REQUEST = new Request() {
        @Override
        public byte[] call(MakeRequest makeRequest) throws IOException {
            return null;
        }
    };

    public class RequestQueue extends ConcurrentLinkedDeque<Request> {

        private boolean executing = false;
        private Request afterwards = null;

        public RequestQueue enqueue(Request request) {
            this.add(request);
            return this;
        }

        public RequestQueue whenFinished(Afterwards then) {
            this.afterwards = new Request(then) {
                @Override
                public byte[] call(MakeRequest makeRequest) throws IOException {
                    return null;
                }
            };
            return this;
        }

        public void reset () {
            this.remove(this.afterwards);
            if (this.afterwards != null)
                this.add(this.afterwards);
            this.remove(EMPTY_REQUEST);
            this.add(EMPTY_REQUEST);
            this.executing = false;
        }

        public void execute () {
            if (!this.executing) {
                this.reset();
                this.executing = true;
                this.next();
            } else {
                throw new RuntimeException("RequestQueue running or not reset.");
            }
        }

        private void next () {
            if (this.executing) {
                Request first = this.poll();
                if (first != null) {
                    this.add(first);
                    if (first != EMPTY_REQUEST) {
                        new MakeRequest(first).execute(this);
                    } else {
                        this.executing = false;
                    }
                } else {
                    // EMPTY_REQUEST should always be the "end" of the queue.
                    throw new RuntimeException("Found null at end of queue.");
                }
            } else {
                throw new RuntimeException("Cannot call next() before execute().");
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Google credential and account initialization
    ////////////////////////////////////////////////////////////////////////////////

    private void acquireGooglePlayServices () {
        GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
        final int status = gaa.isGooglePlayServicesAvailable(getContext());
        if (gaa.isUserResolvableError(status)) {
            this.showErrorDialog(status);
        }
    }

    @AfterPermissionGranted(GoogleDriveApi.REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount () {
        if (EasyPermissions.hasPermissions(
                getContext(), Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getActivity().getPreferences(Context.MODE_PRIVATE)
                    .getString("accountName", null);
            if (accountName != null) {
                this.credential.setSelectedAccountName(accountName);
                this.initialize();
            } else {
                startActivityForResult(
                        this.credential.newChooseAccountIntent(),
                        GoogleDriveApi.REQUEST_ACCOUNT_PICKER);
            }
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    GoogleDriveApi.REQUEST_RATIONALE,
                    GoogleDriveApi.REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    private boolean deviceIsOffline () {
        ConnectivityManager cm = (ConnectivityManager)
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni == null || !ni.isConnected());
    }

    private boolean isGooglePlayServicesAvailable () {
        GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
        final int status = gaa.isGooglePlayServicesAvailable(getContext());
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

    ////////////////////////////////////////////////////////////////////////////////
    // Helpers
    ////////////////////////////////////////////////////////////////////////////////

    public void showErrorDialog (final int connectionStatusCode) {
        GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
        gaa.getErrorDialog(
                getActivity(),
                connectionStatusCode,
                GoogleDriveApi.REQUEST_GOOGLE_PLAY_SERVICES)
                .show();
    }

    public void showErrorDialog (Exception e) {
        Log.e(tag, "--- BEGIN: " + e.getMessage());
        e.printStackTrace();
        Log.e(tag, "--- END: " + e.getMessage());
    }

    /** Create and show a toast with short duration.
     *
     * @param text is the message to put in the toast.
     */
    public void makeToast (CharSequence text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
