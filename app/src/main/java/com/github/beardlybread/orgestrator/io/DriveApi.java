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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.github.beardlybread.orgestrator.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class DriveApi extends Fragment
        implements  EasyPermissions.PermissionCallbacks {

    public static final String tag = "orgestrator.io.DriveApi";

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
     * @param requestCode indicates the data source.
     * @param resultCode indicates whether the action was successful.
     * @param data holds necessary data returned from the action.
     */
    @Override
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

    private synchronized void addToHistory (MakeRequest r) {
        this.requestHistory.addLast(r);
        if (this.requestHistory.size() > DriveApi.HISTORY_SIZE)
            this.requestHistory.removeFirst();
    }

    public synchronized MakeRequest getLastRequest () {
        return this.requestHistory.peekLast();
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Request logic
    ////////////////////////////////////////////////////////////////////////////////

    public Request emptyRequest (Runnable then) {
        return new Request(then) {
            @Override
            public byte[] call(MakeRequest makeRequest) throws IOException {
                return null;
            }
        };
    }

    public final Request EMPTY_REQUEST = emptyRequest(null);

    /** Create a generic Google Drive query request without extra callbacks.
     *
     * @param query is a Drive...List.setQ valid string defining the query.
     *              docs: https://developers.google.com/drive/v3/web/search-parameters
     * @return a Request object to feed to a MakeRequest task.
     */
    public Request queryRequest (final String query, Runnable then, Runnable otherwise) {
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
     * @return a Request object to feed to a MakeRequest task.
     */
    public Request downloadRequest (final String id, Runnable then, Runnable otherwise) {
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
     * @param data is a byte array containing the data to upload.
     * @param id uniquely identifies the resource to upload.
     * @return a Request object to feed to a MakeRequest task.
     */
    public Request uploadRequest (byte[] data, final String id) {
        final BufferedInputStream content =
                new BufferedInputStream(new ByteArrayInputStream(data));
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
        };
    }

    /** The Request class adds functionality to the request interface.
     *
     * If a Request successfully passes to onPostExecute in a MakeRequest, it will be marked as
     * completed. In a RequestQueue, this means that it will not be executed again. Also, if it
     * was passed a "then" on instantiation, it will be invoked on completion before any further
     * Requests in a RequestQueue are made. An "otherwise" will be called in onCancelled in the
     * finally clause of the try block wrapping everything else.
     */
    public abstract class Request implements RequestInterface {
        private boolean incomplete = true;
        public final Runnable then;
        public final Runnable otherwise;

        public Request () {
            this.then = null;
            this.otherwise = null;
        }

        public Request (Runnable then) {
            this.then = then;
            this.otherwise = null;
        }

        public Request (Runnable then, Runnable otherwise) {
            this.then = then;
            this.otherwise = otherwise;
        }

        public boolean isIncomplete () { return this.incomplete; }
        public void setCompleted () { this.incomplete = false; }

        public void before (MakeRequest makeRequest) {}
        public void after (MakeRequest makeRequest, byte[] output) {}
        public void cancelled (MakeRequest makeRequest) {}
    }

    /** This interface plugs into a MakeRequest task to define its behavior.
     */
    public interface RequestInterface {

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
    public class MakeRequest extends AsyncTask<RequestQueue, Void, byte[]> {

        private Drive service = null;
        private Request request = null;
        private Exception lastError = null;
        private Request next = null;
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
                    this.next = this.remaining.poll();
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
                    this.request.then.run();
                }
            }
            if (this.next != null)
                new MakeRequest(this.next).execute(this.remaining);
        }

        @Override
        protected void onCancelled () {
            try {
                this.request.cancelled(this);
                if (this.lastError != null) {
                    if (this.lastError instanceof UserRecoverableAuthIOException) {
                        startActivityForResult(
                                ((UserRecoverableAuthIOException) this.lastError).getIntent(),
                                DriveApi.REQUEST_AUTHORIZATION);
                    } else {
                        showErrorDialog(this.lastError);
                    }
                } else {
                    makeToast("Google Drive action cancelled.");
                }
            } finally {
                if (this.request.otherwise != null)
                    this.request.otherwise.run();
            }
        }
    }


    public class RequestQueue extends ConcurrentLinkedDeque<Request> {

        public RequestQueue request(Request request) {
            this.add(request);
            return this;
        }

        public void execute () {
            Request first = this.poll();
            if (first != null)
                new MakeRequest(first).execute(this);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // Helpers
    ////////////////////////////////////////////////////////////////////////////////

    private void acquireGooglePlayServices () {
        GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
        final int status = gaa.isGooglePlayServicesAvailable(getContext());
        if (gaa.isUserResolvableError(status)) {
            this.showErrorDialog(status);
        }
    }

    @AfterPermissionGranted(DriveApi.REQUEST_PERMISSION_GET_ACCOUNTS)
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

    public void showErrorDialog (final int connectionStatusCode) {
        GoogleApiAvailability gaa = GoogleApiAvailability.getInstance();
        gaa.getErrorDialog(
                getActivity(),
                connectionStatusCode,
                DriveApi.REQUEST_GOOGLE_PLAY_SERVICES)
                .show();
    }

    public void showErrorDialog (Exception e) {
        Log.e(tag, "--- BEGIN: " + e.getMessage());
        e.printStackTrace();
        Log.e(tag, "--- END: " + e.getMessage());
    }

    public void makeToast (CharSequence text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
