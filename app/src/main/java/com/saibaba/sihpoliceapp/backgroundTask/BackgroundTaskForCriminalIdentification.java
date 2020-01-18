package com.saibaba.sihpoliceapp.backgroundTask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class BackgroundTaskForCriminalIdentification extends AsyncTask<String[],Void,Object> {
    private static final String TAG = "BackgroundTaskForCrimin";

    @Override
    protected Object doInBackground(String[]... objects) {
        StringBuilder data = new StringBuilder();
        try {
            String[] faceIds = (String[]) objects[0];
            String stringURL = "https://criminalfacerecognition.cognitiveservices.azure.com/face/v1.0/identify";
            URL url = new URL(stringURL);
            HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Ocp-Apim-Subscription-Key", "915013e79cb744c597d79f5ad25eaa60");
            httpURLConnection.setRequestMethod("POST");
            Log.d(TAG, "doInBackground: url is " + stringURL);
            OutputStream outputStream = httpURLConnection.getOutputStream();

            String jsonObject = "{\"personGroupId\":\"ncrbcriminal\",\"faceIds\":[";
            if (faceIds.length <= 1) {
                jsonObject += "\"" + faceIds[0] + "\"]}";
            } else {
                jsonObject += "\"" + faceIds[0] + "\"";
                for (int i = 1; i < faceIds.length; i++) {
                    jsonObject += ",\"" + faceIds[i] + "\"";
                }
                jsonObject += "]}";
            }
            Log.d(TAG, "doInBackground: json is " + jsonObject);
            outputStream.write(jsonObject.getBytes());

            outputStream.flush();
            Log.d(TAG, "doInBackground: response code is " + httpURLConnection.getResponseCode());
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while (null != (line = reader.readLine())) {
                data.append(line).append("\n");
            }
            Log.d(TAG, "doInBackground: response is " + data.toString());
        } catch (Exception e) {
            Log.d(TAG, "doInBackground exception is: " + e.getMessage());
        }
        return data.toString();
    }

}
