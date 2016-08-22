package com.viztushar.osumwalls.dialogs;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.concurrent.Callable;

/**
 * This Class was created by Patrick Jung
 * on 07.01.16. For more Details and Licensing
 * have a look at the README.md
 */

public final class ISDialogs {

    public static void showApplyWallpaperDialog(Context context, MaterialDialog.SingleButtonCallback onPositive, MaterialDialog.SingleButtonCallback onNeutral) {
        new MaterialDialog.Builder(context)
                .title("apply")
                .content("confirm_apply")
                .positiveText("apply")
                .neutralText("crop")
                .negativeText(android.R.string.cancel)
                .onPositive(onPositive)
                .onNeutral(onNeutral)
                .show();
    }

    public static MaterialDialog showDownloadDialog(Context context) {
        return new MaterialDialog.Builder(context)
                .content("Downloading Wallpaper")
                .progress(true, 0)
                .cancelable(false)
                .build();
    }






}