package com.example.finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class FilterClass extends AppCompatActivity {
    CheckBox north, south, center;
    BroadcastReceiver broadcastReceiver;
    BroadcastReceiver broadcastReceiverService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_layout);
        north = findViewById(R.id.north_checkbox);
        south = findViewById(R.id.south_checkBox);
        center = findViewById(R.id.center_checkBox);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean north_bool = sharedPref.getBoolean("north",true);
        boolean south_bool = sharedPref.getBoolean("south",true);
        boolean center_bool = sharedPref.getBoolean("center",true);
        north.setChecked(north_bool);
        south.setChecked(south_bool);
        center.setChecked(center_bool);
        SharedPreferences.Editor editor = sharedPref.edit();

        north.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("north", isChecked);
                editor.apply();
            }
        });

        south.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("south", isChecked);
                editor.apply();
            }
        });

        center.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("center", isChecked);
                editor.apply();
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("dbUpdated");
        broadcastReceiverService = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int foregroundServiceDb = intent.getIntExtra("foregroundServiceUpdate", 0);
                if(foregroundServiceDb == 1){
                    popDialogWithExplanation("Update","There is update in Show");
                }
            }
        };
        registerReceiver(broadcastReceiverService, intentFilter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        startActivity(new Intent(FilterClass.this,ProfileActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction("Counter");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
                    boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false);
                    if(noConnectivity){
                        logOutFromTheSystem();

                    }else{}
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void logOutFromTheSystem() {
        FirebaseAuth.getInstance().signOut();
        this.finish();
        startActivity(new Intent(FilterClass.this, MainActivity.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(broadcastReceiverService);
    }

    private void popDialogWithExplanation(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FilterClass.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
