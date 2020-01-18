package com.saibaba.sihpoliceapp.backgroundTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class BackgroundTaskForVehicle extends AsyncTask<Object,Void,String>  {

    private static final String TAG = "BackgroundTaskForVehicl";
    private dataProcessed callback;

    public interface dataProcessed{
        void onDataProcessed(String string);
    }

    public BackgroundTaskForVehicle(dataProcessed callback) {
        this.callback=callback;
    }

    @Override
    protected String doInBackground(Object... objects) {
        StringBuilder data=new StringBuilder();
        try {
            Bundle bundle=(Bundle)objects[0];
            byte[] image=bundle.getByteArray("image");
//            String stringURL=new Uri.Builder()
//                    .scheme("https")
//                    .authority(Constants.imageDetectionURI)
////                    .appendQueryParameter("returnFaceId", "true")
////                    .appendQueryParameter("returnFaceLandmarks", "true")
////                    .appendQueryParameter("recognitionModel", "recognition_02")
////                    .appendQueryParameter("returnRecognitionModel", "false")
//                    .appendQueryParameter("detectionModel", "detection_02").build().toString();
            String stringURL = "https://prescriptionreader.cognitiveservices.azure.com/vision/v2.1/read/core/asyncBatchAnalyze";
            URL url=new URL(stringURL);
            HttpsURLConnection httpURLConnection=(HttpsURLConnection)url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/octet-stream");
            httpURLConnection.setRequestProperty("Ocp-Apim-Subscription-Key", "305d918662434c049f1f5eda1a361bb0");
            httpURLConnection.setRequestMethod("POST");
            Log.d(TAG, "doInBackground: url is "+stringURL );
            OutputStream outputStream=httpURLConnection.getOutputStream();
            outputStream.write(image);
            outputStream.flush();
            Log.d(TAG, "doInBackground: response code is "+httpURLConnection.getResponseCode());
            int response_code=httpURLConnection.getResponseCode();
            if(response_code==202) {
                String header=httpURLConnection.getHeaderField("Operation-Location");
                Log.d(TAG, "doInBackground: header is  "+header);
                Thread.sleep(5000);
                URL url2 = new URL(header);
                HttpURLConnection httpURLConnection1=(HttpURLConnection) url2.openConnection();
                httpURLConnection1.setRequestMethod("GET");
                httpURLConnection1.setRequestProperty("Ocp-Apim-Subscription-Key","305d918662434c049f1f5eda1a361bb0");
                BufferedReader reader=new BufferedReader(new InputStreamReader(httpURLConnection1.getInputStream()));
                String line;
                while(null!=(line=reader.readLine())){
                    data.append(line).append("\n");
                }
            }

            Log.d(TAG, "doInBackground: response is "+data.toString());
            return data.toString();
        }catch (Exception e){
            Log.d(TAG, "doInBackground exception is: "+e.getMessage());
        }
        return data.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        callback.onDataProcessed(s);
    }
}
