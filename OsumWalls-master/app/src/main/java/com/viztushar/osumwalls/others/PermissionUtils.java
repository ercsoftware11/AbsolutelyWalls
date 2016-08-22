package com.viztushar.osumwalls.others;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * This Class was created by Patrick J
 * on 07.01.16. For more Details and Licensing
 * have a look at the README.md
 */

public final class PermissionUtils {

    public static final int PERMISSION_REQUEST_CODE = 42;
    private static String VIEWER_ACTIVITY_ACTION;
    private static OnPermissionResultListener onPermissionResultListener;

    public interface OnPermissionResultListener {
        void onStoragePermissionGranted();
    }

    public static boolean canAccessStorage(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int res = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return res == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void requestStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    public static void requestStoragePermission(Activity activity, OnPermissionResultListener permissionResultListener) {
        onPermissionResultListener = permissionResultListener;
        requestStoragePermission(activity);
    }

    public static OnPermissionResultListener permissionReceived() {
        return onPermissionResultListener;
    }

    public static void setViewerActivityAction(String viewerActivityAction) {
        VIEWER_ACTIVITY_ACTION = viewerActivityAction;
    }

    public static String getViewerActivityAction() {
        return VIEWER_ACTIVITY_ACTION;
    }

}