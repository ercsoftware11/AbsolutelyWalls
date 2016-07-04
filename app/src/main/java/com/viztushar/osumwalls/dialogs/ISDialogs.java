package com.viztushar.osumwalls.dialogs;

import android.content.Context;
import android.content.res.Resources;

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
                .content("Downloading...")
                .progress(true, 0)
                .cancelable(false)
                .build();
    }

    public static MaterialDialog showChangelogDialog(Context context) {
        return new MaterialDialog.Builder(context)
                .title("Changelog")
                .content("Absolutely Wallpapers 2.0 " +
                        "\n- Small UI Changes (added a spinner when loading walls from server)\n- New changelog section (as you can see)\n- Fixed a crash issue" +
                        " on early Android version (4.x)\n- All round awesome improvements ;-)")
                .show();
    }




}