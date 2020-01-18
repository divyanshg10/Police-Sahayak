package com.saibaba.sihpoliceapp.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.saibaba.sihpoliceapp.Constants;
import com.saibaba.sihpoliceapp.MainActivity;
import com.saibaba.sihpoliceapp.R;

import java.util.HashMap;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Login";
    private EditText emailEditText,passEditText;
    private Button loginButton;
    private String email,password;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String uid;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userExists();
        init();
        progressDialog.setMessage("checking user");
        progressDialog.show();
        if(userExists()){
            progressDialog.dismiss();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else{
            progressDialog.dismiss();
        }
    }

    private void init(){
        emailEditText = findViewById(R.id.enter_emailLogin);
        passEditText = findViewById(R.id.enter_passwordLogin);
        loginButton = findViewById(R.id.login_getin);
        email="";
        password="";
        uid="";
        loginButton.setOnClickListener(this);
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        email = emailEditText.getText().toString();
        password=passEditText.getText().toString();
        if(!email.equals("")&&!password.equals("")){
            login();
        }
    }

    private void login(){
        progressDialog.setMessage("logging you in");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            firebaseUser=firebaseAuth.getCurrentUser();
                            try{
                                uid=firebaseUser.getUid();
                            }catch (Exception e){
                                Log.d(TAG, "onComplete: "+e.getMessage());
                                uid="";
                            }
                            getDetails();
                        }else{
                            progressDialog.dismiss();
                            emailEditText.setText("");
                            passEditText.setText("");
                            emailEditText.setError("invalid email or password");
                            emailEditText.requestFocus();
                        }
                    }
                });
    }

    private void getDetails(){
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("police").child(uid);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: "+dataSnapshot);
                    if(dataSnapshot.exists()){
                        createSharedPreference(dataSnapshot);
                    }else{
                        progressDialog.dismiss();
                        showToast("Some error occurred");
                        firebaseAuth.signOut();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Log.d(TAG, "onCancelled: "+databaseError.getMessage());
                    showToast("Some error occurred");
                }
            });
        }catch (Exception e){
            progressDialog.dismiss();
            Log.e(TAG, "createSharedPreference: "+ e.getMessage() );
        }
    }

    private void createSharedPreference(DataSnapshot dataSnapshot){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.USER_DATA_SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(Constants.USER_UID,uid);
        hashMap.put(Constants.USER_EMAIL,dataSnapshot.child(Constants.USER_EMAIL).getValue());
        hashMap.put(Constants.USER_LEVEL,dataSnapshot.child(Constants.USER_LEVEL).getValue());
        hashMap.put(Constants.USER_NAME,dataSnapshot.child(Constants.USER_NAME).getValue());
        hashMap.put(Constants.USER_PHONE,dataSnapshot.child(Constants.USER_PHONE).getValue());
        hashMap.put(Constants.USER_RANK,dataSnapshot.child(Constants.USER_RANK).getValue());
        hashMap.put(Constants.USER_STATION_ID,dataSnapshot.child(Constants.USER_STATION_ID).getValue());
        hashMap.put(Constants.USER_STATE,dataSnapshot.child(Constants.USER_STATE).getValue());
        hashMap.put(Constants.USER_DISTRICT,dataSnapshot.child(Constants.USER_DISTRICT).getValue());

        Gson gson=new Gson();
        String stringMap=gson.toJson(hashMap);
        Log.d(TAG, "createSharedPreference: data is "+stringMap);
        editor.putString(Constants.USER_DATA_MAP,stringMap).apply();
        progressDialog.dismiss();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private boolean userExists(){
        SharedPreferences sharedPreferences=getSharedPreferences(Constants.USER_DATA_SHARED_PREFERENCE,MODE_PRIVATE);
        String stringHashMap=sharedPreferences.getString(Constants.USER_DATA_MAP,"");
        Log.d(TAG, "userExists: data is "+stringHashMap);
        if(stringHashMap.equals("")){
            return false;
        }
        if(firebaseUser==null){
            return false;
        }
        return true;
    }

}
