package com.viztushar.osumwalls.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.viztushar.osumwalls.MainActivity;
import com.viztushar.osumwalls.R;
import com.viztushar.osumwalls.others.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by architjn on 15/07/15.
 */
public class GetWallpapers extends AsyncTask<Void, Void, Void> {

    private String url, jsonResult;
    FileOutputStream outputStream;
    Context contexxt;
    boolean newWalls = false;
    private Callbacks callbacks;
    public GetWallpapers(Context context, Callbacks callbacks) {
        this.callbacks = callbacks;
        contexxt = context;
        url = context.getResources().getString(R.string.wall_url);
    }

    @Override
    protected Void doInBackground(Void... z) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost(url);
        try {
            httppost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = httpclient.execute(httppost);
            jsonResult = inputStreamToString(response.getEntity().getContent())
                    .toString();
            Log.i("response", "doInBackground: "+ jsonResult);

        } catch (ClientProtocolException e) {
            Log.e("e", "error1");
            Utils.noConnection = true;
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("e", "error2");
            e.printStackTrace();
            Utils.noConnection = true;
        }
        try {
            if (fileExistance("walls")) {
                InputStream is = contexxt.openFileInput("walls");
                String lastWallsFile;
                lastWallsFile = inputStreamToString(is).toString();
                Log.e("LastWallsFile:", lastWallsFile);
                newWalls = !lastWallsFile.equals(jsonResult);
                writeWallFile();
            } else {
                try {
                    writeWallFile();
                } catch (Exception e) {
                    //Do nothing because something is wrong! Oh well this feature just wont work on whatever device.
                }
            }
        }
        catch (Exception ex)
        {
            //Do nothing because something is wrong! Oh well this feature just wont work on whatever device.
        }

        return null;
    }
    private boolean fileExistance(String fname){
        File file = contexxt.getFileStreamPath(fname);
        return file.exists();
    }
    private void writeWallFile()
    {
        try
        {
            outputStream = contexxt.openFileOutput("walls", Context.MODE_PRIVATE);
            outputStream.write(jsonResult.getBytes());
            outputStream.close();
        }
        catch (Exception ex)
        {
            //Do nothing because something is wrong! Oh well this feature just wont work on whatever device.
        }
    }

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (callbacks != null)
            callbacks.onListLoaded(jsonResult, newWalls);
        super.onPostExecute(aVoid);
    }

    public interface Callbacks {
        void onListLoaded(String jsonResult, boolean jsonSwitch);
    }

}
