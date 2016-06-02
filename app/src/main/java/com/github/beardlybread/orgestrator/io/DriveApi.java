package com.github.beardlybread.orgestrator.io;

import android.app.IntentService;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by bradley on 6/2/16.
 */
public class DriveApi implements EasyPermissions.PermissionCallbacks {

    public static final String tag = "io.DriveApi";

    ////////////////////////////////////////////////////////////////////////////////
    // Initialization
    ////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////
    // Request logic
    ////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////
    // Helpers
    ////////////////////////////////////////////////////////////////////////////////

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

}
