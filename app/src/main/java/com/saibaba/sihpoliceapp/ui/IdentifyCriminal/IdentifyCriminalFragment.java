package com.saibaba.sihpoliceapp.ui.IdentifyCriminal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.saibaba.sihpoliceapp.Modal.Face;
import com.saibaba.sihpoliceapp.R;
import com.saibaba.sihpoliceapp.backgroundTask.BackgroundTaskForCriminalDetect;
import com.saibaba.sihpoliceapp.backgroundTask.BackgroundTaskForCriminalIdentification;
import com.saibaba.sihpoliceapp.ui.CriminalList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class IdentifyCriminalFragment extends Fragment implements BackgroundTaskForCriminalDetect.OnBackgroundTaskForCriminalDetectCompleteListener {

    private static final String TAG = "IdentifyCriminalFragmen";

    private IdentifyCriminalModel mIdentifyCriminalModel;
    private int REQUEST_IMAGE_CAPTURE=1;
    private ProgressDialog progressDialog;
    private Bitmap imageBitmap;
    private AlertDialog alertDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mIdentifyCriminalModel =
                ViewModelProviders.of(this).get(IdentifyCriminalModel.class);
        View root = inflater.inflate(R.layout.fragment_identifycriminal, container, false);
        root.findViewById(R.id.capture).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        goForPhoto();
    //        PercentageMatch gf=new PercentageMatch();
    //        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction()
    //                .replace(R.id.nav_host_fragment,gf);
    //        fragmentTransaction.commit();
    }
});
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog=new ProgressDialog(getContext());
        alertDialog=new androidx.appcompat.app.AlertDialog.Builder(getActivity()).setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismissAlert();
                    }
                }).create();
    }

    private void goForPhoto(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_IMAGE_CAPTURE&&resultCode== Activity.RESULT_OK){
            Bundle Extras=data.getExtras();
            imageBitmap=(Bitmap) Extras.get("data");
            BackgroundTaskForCriminalDetect backgroundTaskForCriminalDetect =new BackgroundTaskForCriminalDetect();
            backgroundTaskForCriminalDetect.setOnBackgroudTaskForCriminalDetectCompleteListener(this);
            byte[] byteArray = bitmapToBytesArray(imageBitmap);
            Bundle bundle=new Bundle();
            bundle.putByteArray("image",byteArray);
            backgroundTaskForCriminalDetect.execute(bundle);
            progressDialog.setMessage("detecting faces");
            progressDialog.show();
        }
    }

    private static byte[] bitmapToBytesArray(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void indentifyCriminal(HashMap<String, Face> hashMap) {
        Log.d(TAG, "indentifyCriminal: data came back ");
        if(hashMap.size()==0){
            showAlert("No face detected in image");
            return;
        }
        try {
            progressDialog.setMessage("Matching face(s)");
            BackgroundTaskForCriminalIdentification backgroundTaskForCriminalIdentification = new BackgroundTaskForCriminalIdentification();
            String response=(String) backgroundTaskForCriminalIdentification.execute(hashMap.keySet().toArray(new String[0])).get();
            Log.d(TAG, "indentifyCriminal: "+response);
            parseJson(response,hashMap);
        }catch (Exception e){
            Log.d(TAG, "indentifyCriminal: exception is "+e.getMessage());
        }
    }

    private void showAlert(String message){
        progressDialog.dismiss();
        alertDialog.setMessage(message);
        alertDialog.show();;
    }

    private void dismissAlert() {
        if(alertDialog!=null){
            alertDialog.dismiss();
        }
    }

    private void parseJson(String response,HashMap<String,Face> hashMap){
        try{
            JSONArray jsonArray=new JSONArray(response);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                JSONArray jsonArray1=jsonObject.getJSONArray("candidates");
                if(jsonArray1.length()==0){
                    hashMap.remove(jsonObject.getString("faceId"));
                }else{
                    JSONObject jsonObject1=jsonArray1.getJSONObject(0);
                    Face face=hashMap.get(jsonObject.getString("faceId"));
                    face.setPersonID(jsonObject1.getString("personId"));
                    face.setProbability(jsonObject1.getDouble("confidence"));
                    hashMap.put(face.getFaceID(),face);
                }
            }
            Log.d(TAG, "parseJson: final data size is "+hashMap.size());
            if(hashMap.size()==0){
                showAlert("No criminal detected in captured photo");
            }else{
                startActivityForCriminalProfile(hashMap);
            }
        }catch (Exception e){
            showAlert("Some error occurred while communicating with server");
            Log.e(TAG, "parseJson: exception is "+e.getMessage());
        }
    }

    private void startActivityForCriminalProfile(HashMap<String,Face> hashMap){
        for(Map.Entry entry : hashMap.entrySet()){
            String key=(String) entry.getKey();
            Face face=(Face)entry.getValue();
            face.setImage(bitmapToBytesArray(Bitmap.createBitmap(imageBitmap,face.getLeft(),face.getTop(),face.getWidth(),face.getHeight())));
            entry.setValue(face);
            Log.d(TAG, "startActivityForCriminalProfile: face details are "+entry.getValue());
//            Intent intent=new Intent(getActivity(), CriminalDetails.class);
//            intent.putExtra("hashmap",hashMap);
            progressDialog.dismiss();
//            startActivity(intent);
            Intent intent=new Intent();
            intent.putExtra("hashmap",hashMap);
            CriminalList criminalList=new CriminalList(intent);
            FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment,criminalList);
            fragmentTransaction.commit();
        }
    }

}