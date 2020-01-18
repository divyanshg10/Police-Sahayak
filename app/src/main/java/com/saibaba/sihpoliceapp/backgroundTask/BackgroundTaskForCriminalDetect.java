package com.saibaba.sihpoliceapp.backgroundTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


import com.saibaba.sihpoliceapp.Modal.Face;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;


public class BackgroundTaskForCriminalDetect extends AsyncTask<Object,Void,String> {

    public interface OnBackgroundTaskForCriminalDetectCompleteListener{
        void indentifyCriminal(HashMap<String, Face> hashMap);
    }

    private static final String TAG = "backgroundTaskForCrimin";
    private OnBackgroundTaskForCriminalDetectCompleteListener onBackgroundTaskForCriminalDetectCompleteListener;

    public void setOnBackgroudTaskForCriminalDetectCompleteListener(OnBackgroundTaskForCriminalDetectCompleteListener onBackgroudTaskForCriminalDetectCompleteListener){
        this.onBackgroundTaskForCriminalDetectCompleteListener=onBackgroudTaskForCriminalDetectCompleteListener;
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
            String stringURL = "https://criminalfacerecognition.cognitiveservices.azure.com/face/v1.0/detect?detectionModel=detection_02&recognitionModel=recognition_02";
            URL url=new URL(stringURL);
            HttpsURLConnection httpURLConnection=(HttpsURLConnection)url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/octet-stream");
            httpURLConnection.setRequestProperty("Ocp-Apim-Subscription-Key", "915013e79cb744c597d79f5ad25eaa60");
            httpURLConnection.setRequestMethod("POST");
            Log.d(TAG, "doInBackground: url is "+stringURL );
            OutputStream outputStream=httpURLConnection.getOutputStream();
            outputStream.write(image);
            outputStream.flush();
            Log.d(TAG, "doInBackground: response code is "+httpURLConnection.getResponseCode());
            BufferedReader reader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while(null!=(line=reader.readLine())){
                data.append(line).append("\n");
            }
            Log.d(TAG, "doInBackground: response is "+data.toString());
        }catch (Exception e){
            Log.d(TAG, "doInBackground exception is: "+e.getMessage());
        }
        return data.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        JsonFaceIdParser jsonFaceIdParser=new JsonFaceIdParser(s);
        onBackgroundTaskForCriminalDetectCompleteListener.indentifyCriminal(jsonFaceIdParser.getFaceHashMap());
    }
}
