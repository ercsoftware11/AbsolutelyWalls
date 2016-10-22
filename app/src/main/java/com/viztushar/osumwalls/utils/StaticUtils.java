package com.viztushar.osumwalls.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 10/22/16.
 */

public class StaticUtils {

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) drawable = new ColorDrawable(Color.TRANSPARENT);

        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) return bitmapDrawable.getBitmap();
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0)
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        else
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static boolean isColorDark(int color) {
        return (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255 < 0.5;
    }

    public static int muteColor(int color, int variant) {
        int mutedColor = Color.argb(255, (int) (127.5 + Color.red(color)) / 2, (int) (127.5 + Color.green(color)) / 2, (int) (127.5 + Color.blue(color)) / 2);
        switch (variant % 3) {
            case 1:
                return Color.argb(255, Color.red(mutedColor) + 10, Color.green(mutedColor) + 10, Color.blue(mutedColor) + 10);
            case 2:
                return Color.argb(255, Color.red(mutedColor) - 10, Color.green(mutedColor) - 10, Color.blue(mutedColor) - 10);
            default:
                return mutedColor;
        }
    }

    @ColorInt
    public static int getAverageColor(@NonNull Bitmap bitmap) {
        int red = 0, green = 0, blue = 0;
        int width = bitmap.getWidth(), height = bitmap.getHeight(), size = width * height;

        int[] pixels = new int[size];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = pixels[x + y * width];
                red += (color >> 16) & 0xFF;
                green += (color >> 8) & 0xFF;
                blue += (color & 0xFF);
            }
        }

        return Color.argb(255, red / size, green / size, blue / size);
    }

    @Nullable
    @TargetApi(17)
    public static void blurBitmap(final Context context, final Bitmap bitmap, final AsyncListener<Bitmap> listener) {
        if (context == null || bitmap == null || listener == null) return;

        new Thread() {
            @Override
            public void run() {
                final Bitmap blurredBitmap;
                try {
                    blurredBitmap = Bitmap.createBitmap(bitmap);
                } catch (OutOfMemoryError e) {
                    return;
                }

                RenderScript renderScript = RenderScript.create(context);
                Allocation input = Allocation.createFromBitmap(renderScript, bitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SCRIPT);
                Allocation output = Allocation.createTyped(renderScript, input.getType());
                ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

                script.setInput(input);
                script.setRadius(20);

                script.forEach(output);
                output.copyTo(blurredBitmap);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFinish(blurredBitmap);
                    }
                });
            }
        }.start();
    }

    public static float getPixelsFromDp(Context context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static boolean arePermissionsGranted(Context context) {
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        if (info.requestedPermissions != null) {
            for (String permission : info.requestedPermissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.wtf("Permission", permission);
                    return false;
                }
            }
        }

        return true;
    }

    public static void requestPermissions(Activity activity) {
        PackageInfo info;
        try {
            info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return;
        }

        if (info.requestedPermissions != null) {
            List<String> unrequestedPermissions = new ArrayList<>();
            for (String permission : info.requestedPermissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.wtf("Permission", permission);
                    if (permission.length() > 0)
                        unrequestedPermissions.add(permission);
                }
            }

            if (unrequestedPermissions.size() > 0)
                ActivityCompat.requestPermissions(activity, unrequestedPermissions.toArray(new String[unrequestedPermissions.size()]), 0);
        }
    }

    public interface AsyncListener<T> {
        void onFinish(T result);
    }

}
