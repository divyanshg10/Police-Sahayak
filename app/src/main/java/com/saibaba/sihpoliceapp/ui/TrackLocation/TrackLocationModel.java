package com.saibaba.sihpoliceapp.ui.TrackLocation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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