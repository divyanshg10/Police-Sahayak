package com.saibaba.sihpoliceapp.ui.IdentifyVehicle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IdentifyVehicleViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public IdentifyVehicleViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is share fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}