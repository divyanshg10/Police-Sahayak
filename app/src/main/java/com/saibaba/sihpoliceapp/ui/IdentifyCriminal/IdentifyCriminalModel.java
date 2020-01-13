package com.saibaba.sihpoliceapp.ui.IdentifyCriminal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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