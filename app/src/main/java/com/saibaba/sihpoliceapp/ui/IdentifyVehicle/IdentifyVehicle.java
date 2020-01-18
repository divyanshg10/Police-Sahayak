package com.saibaba.sihpoliceapp.ui.IdentifyVehicle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.saibaba.sihpoliceapp.R;
import com.saibaba.sihpoliceapp.backgroundTask.BackgroundTaskForCriminalDetect;
import com.saibaba.sihpoliceapp.backgroundTask.BackgroundTaskForVehicle;
import com.saibaba.sihpoliceapp.ui.IdentifyCriminal.IdentifyCriminalFragment;

import org.json.JSONArray;
import org.json.JSONObject;

public class IdentifyVehicle extends Fragment {

    private static final String TAG = "IdentifyVehicle";
    private IdentifyVehicleViewModel mIdentifyVehicleViewModel;
    private int REQUEST_IMAGE_CAPTURE_FOR_VEHICLE=2;
    private Bitmap imageBitmap;
    ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mIdentifyVehicleViewModel =
                ViewModelProviders.of(this).get(IdentifyVehicleViewModel.class);
        View root = inflater.inflate(R.layout.fragment_identify_vehicle, container, false);
        Button button=root.findViewById(R.id.capture);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goForPhoto();
            }
        });
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return root;
    }
    private void goForPhoto(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE_FOR_VEHICLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_IMAGE_CAPTURE_FOR_VEHICLE&&resultCode== Activity.RESULT_OK){
            Bundle Extras=data.getExtras();
            imageBitmap=(Bitmap) Extras.get("data");
            BackgroundTaskForVehicle backgroundTaskForVehicle =new BackgroundTaskForVehicle();
//            backgroundTaskForCriminalDetect.setOnBackgroudTaskForCriminalDetectCompleteListener(g);
            byte[] byteArray = IdentifyCriminalFragment.bitmapToBytesArray(imageBitmap);
            Bundle bundle=new Bundle();
            bundle.putByteArray("image",byteArray);
            progressDialog.setMessage("detecting faces");
            progressDialog.show();
            String response="";
            try {
                response = backgroundTaskForVehicle.execute(bundle).get();
                Log.d(TAG, "onActivityResult: response "+response);
            }catch (Exception e){
                Log.e(TAG, "onActivityResult: exception thrown with "+e.getMessage() );
            }
            if(response.equals(response.equals(""))){
                progressDialog.dismiss();
                showToast("Some Error Occurred");
            }else{
                parseJSON(response);
            }
        }
    }
    private void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    private void parseJSON(String response){
        try{
        JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("recognitionResults");
            JSONObject jsonObject1=jsonArray.getJSONObject(0);
            JSONArray jsonArray1=jsonObject1.getJSONArray("lines");
            JSONObject jsonObject2=jsonArray1.getJSONObject(jsonArray1.length()-1);
            String regNumber=(String)jsonObject2.get("text");
            progressDialog.dismiss();
            showToast(regNumber);
        }catch (Exception e){
            progressDialog.dismiss();
            showToast("Some Error Occurred");
            Log.e(TAG, "parseJSON: exception thrown "+e.getMessage() );;
        }
    }

}