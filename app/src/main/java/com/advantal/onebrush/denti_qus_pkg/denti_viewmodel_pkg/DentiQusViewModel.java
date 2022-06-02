package com.advantal.onebrush.denti_qus_pkg.denti_viewmodel_pkg;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.advantal.onebrush.denti_qus_pkg.DentiQusModel;

/**
 * Created by Surbhi joshi on 01-03-2022 11:52.
 */
public class DentiQusViewModel extends ViewModel {
    private final Context context;
    private final DentiQusModel dentiQusModel;
    public MutableLiveData<String> qus = new MutableLiveData<>();
    public MutableLiveData<String> btn = new MutableLiveData<>();


    public DentiQusViewModel(Context context, DentiQusModel dentiQusModel) {
        this.context = context;
        this.dentiQusModel = dentiQusModel;
    }
}
