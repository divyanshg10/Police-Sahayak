package com.saibaba.sihpoliceapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;

public class Constants {

    private static final String TAG = "Constants";

    //Image detection credentials
    public static final String imageDetectionURI="criminalfacerecognition.cognitiveservices.azure.com/face/v1.0/detect";

    //police profile
    public static final String USER_DATA_SHARED_PREFERENCE="userDataSharedPreference";
    public static final String USER_DATA_MAP="userDataMap";
    public static final String USER_UID="uid";
    public static final String USER_EMAIL="email";
    public static final String USER_LEVEL="level";
    public static final String USER_NAME="name";
    public static final String USER_PHONE="phone";
    public static final String USER_RANK="rank";
    public static final String USER_STATION_ID="station-id";
    public static final String USER_DISTRICT="District";
    public static final String USER_STATE="State";

    public static HashMap<String,String> getDataFromSharedPreferences(Context context){
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(USER_DATA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
            String dataJson = sharedPreferences.getString(USER_DATA_MAP, "");
            Gson gson=new Gson();
            HashMap<String,String> hashMap=gson.fromJson(dataJson,new HashMap<String,String>().getClass());
            return hashMap;
        }catch (Exception e){
            Log.d(TAG, "getDataFromSharedPreferences: "+e.getMessage());
            return null;
        }
    }
}
