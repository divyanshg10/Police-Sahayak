package com.saibaba.sihpoliceapp.ui.beatsallocation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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