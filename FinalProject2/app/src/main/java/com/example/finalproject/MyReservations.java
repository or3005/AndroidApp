package com.example.finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MyReservations extends AppCompatActivity implements MyReservationAdapter.OnShowsListenerReservations {

    RecyclerView recyclerView;
    DatabaseReference database;
    MyReservationAdapter adapter;
    ArrayList<Reservations> list;
    HashMap<Reservations,String> idReservation;
    BroadcastReceiver broadcastReceiver;
    BroadcastReceiver broadcastReceiverService;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);
        idReservation = new HashMap<>();
        recyclerView = findViewById(R.id.recycle_view_reservations);
        database = FirebaseDatabase.getInstance().getReference("Reservations");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list  = new ArrayList<>();
        adapter = new MyReservationAdapter(this,list);
        adapter.setInterface(this);
        recyclerView.setAdapter(adapter);
        Intent i = getIntent();
        userEmail = i.getExtras().getString("email");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                idReservation.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Reservations reservation = dataSnapshot.getValue(Reservations.class);
                    if(reservation.getEmail().equals(userEmail)) {
                        idReservation.put(reservation, dataSnapshot.getKey());
                        list.add(reservation);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
    public void cancelOrder(int position) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Reservations reservation = list.get(position);
                        DatabaseReference reservationReference = FirebaseDatabase.getInstance().getReference("Reservations").child(idReservation.get(reservation));
                        reservationReference.removeValue();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(MyReservations.this);
        builder.setTitle("Cancel Order");
        builder.setMessage("Are you sure you want to cancel the reservation?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        startActivity(new Intent(MyReservations.this,ProfileActivity.class));
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

                    }else{

                    }
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void logOutFromTheSystem() {
        FirebaseAuth.getInstance().signOut();
        this.finish();
        startActivity(new Intent(MyReservations.this, MainActivity.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(broadcastReceiverService);
    }

    private void popDialogWithExplanation(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyReservations.this);
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

