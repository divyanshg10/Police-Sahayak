package com.saibaba.sihpoliceapp.ui.beatsallocation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class beatsallocationviewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public beatsallocationviewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}