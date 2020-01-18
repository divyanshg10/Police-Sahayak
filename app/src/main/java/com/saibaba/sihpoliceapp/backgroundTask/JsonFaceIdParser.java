package com.saibaba.sihpoliceapp.backgroundTask;

import android.util.Log;

import com.saibaba.sihpoliceapp.Modal.Face;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class JsonFaceIdParser {
    private static final String TAG = "JsonFaceIdParser";
    private HashMap<String, Face> faceHashMap;
    private String data;

    public JsonFaceIdParser(String data) {
        this.faceHashMap=new HashMap<>();
        this.data=data;
        parseJSON();
    }

    private void parseJSON(){
        try {
            JSONArray jsonArray = new JSONArray(data);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                JSONObject childJsonObject=jsonObject.getJSONObject("faceRectangle");
                faceHashMap.put(jsonObject.getString("faceId"),new Face(jsonObject.getString("faceId"),
                        childJsonObject.getInt("top"),
                        childJsonObject.getInt("left"),
                        childJsonObject.getInt("width"),
                        childJsonObject.getInt("height")));
            }
        }catch (Exception e){
            Log.e(TAG, "parseJSON: "+e.getMessage() );
        }
    }

    public HashMap<String, Face> getFaceHashMap() {
        return faceHashMap;
    }
}
