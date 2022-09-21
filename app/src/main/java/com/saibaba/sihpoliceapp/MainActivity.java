package com.saibaba.sihpoliceapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.saibaba.sihpoliceapp.login.Login;
import com.saibaba.sihpoliceapp.map.MapsActivity;
import com.saibaba.sihpoliceapp.services.locationService;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
Button capturecri;
    FloatingActionButton fab;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();

        drawer.openDrawer(Gravity.LEFT);

        HashMap<String,String> dataHashMap=Constants.getDataFromSharedPreferences(this);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        goForPermissionCheck();
        setFabButton();
        try {
            View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
            TextView textView = headerView.findViewById(R.id.nav_head_name);
            textView.setText(dataHashMap.get(Constants.USER_NAME));

            textView = null;
            textView = headerView.findViewById(R.id.nav_head_post);
            textView.setText(dataHashMap.get(Constants.USER_RANK));

            textView = null;
            textView = headerView.findViewById(R.id.nav_head_scode);
            if (dataHashMap.get(Constants.USER_LEVEL).equals("1")) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setText("," + dataHashMap.get(Constants.USER_STATION_ID));
            }
        }catch (Exception e){
            Log.e(TAG, "onCreate: "+e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void goForPermissionCheck(){
        String permissions[]=new String[2];
        int permissionCount=0;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissions[permissionCount]=Manifest.permission.ACCESS_FINE_LOCATION;
            permissionCount++;
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            permissions[permissionCount]=Manifest.permission.CAMERA;
            permissionCount++;
        }
        if(permissionCount>0){
            askForPermission(permissions,1);
        }
    }


    private void askForPermission(String permission[],int request_code){
        ActivityCompat.requestPermissions(this,permission,request_code);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_logout){
            stopService(new Intent(this, locationService.class));
            SharedPreferences.Editor editor=getSharedPreferences(Constants.USER_DATA_SHARED_PREFERENCE,MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, Login.class));
            finish();
        }
        return true;
    }

    private void setFabButton(){
        HashMap<String,String> hashMap=Constants.getDataFromSharedPreferences(this);
        if(hashMap.get(Constants.USER_LEVEL).equals("3")){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v,"You are not allowed",Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }


}
