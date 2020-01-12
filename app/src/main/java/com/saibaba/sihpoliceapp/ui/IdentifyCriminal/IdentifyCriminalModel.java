package com.saibaba.sihpoliceapp.ui.IdentifyCriminal;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class IdentifyCriminalModel extends ViewModel {

    private MutableLiveData<String> mText;

    public IdentifyCriminalModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}