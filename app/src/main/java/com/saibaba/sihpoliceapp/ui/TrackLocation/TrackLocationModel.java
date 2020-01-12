package com.saibaba.sihpoliceapp.ui.TrackLocation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class TrackLocationModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TrackLocationModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}