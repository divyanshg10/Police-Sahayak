package com.saibaba.sihpoliceapp.ui.change_status;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class change_status_ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public change_status_ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is tools fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}