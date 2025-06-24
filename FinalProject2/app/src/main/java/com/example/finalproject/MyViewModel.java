package com.example.finalproject;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MyViewModel extends AndroidViewModel {


    public MyViewModel(@NonNull Application application) {
        super(application);
    }
    MutableLiveData<Integer> itemSelected;



    public MutableLiveData<Integer> GetCurRow() {

        if (itemSelected == null) {

            itemSelected = new MutableLiveData<Integer>();


        }

        return itemSelected;

    }

    public void setItemSelected(int position) {
        itemSelected.setValue(position);
    }





}
