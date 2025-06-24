package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements ProfileAdapter.OnShowListener {
    private Button logout;
    RecyclerView recyclerView;
    DatabaseReference database;
    DatabaseReference reservationsDb;
    ProfileAdapter adapter;
    ArrayList<Show> list;
    ArrayList<Reservations> reservationList;
    User logInUser;
    FirebaseUser user;
    DatabaseReference reference;
    String userID;
    BroadcastReceiver broadcastReceiver;
    BroadcastReceiver broadcastReceiverService;

    boolean north,south,center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        recyclerView = findViewById(R.id.recycle_view_games);
        database = FirebaseDatabase.getInstance().getReference("show");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list  = new ArrayList<Show>();
        adapter = new ProfileAdapter(this,list);
        adapter.setInterfae(this);
        recyclerView.setAdapter(adapter);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        north = sharedPref.getBoolean("north",true);
        south = sharedPref.getBoolean("south",true);
        center = sharedPref.getBoolean("center",true);


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Show show = dataSnapshot.getValue(Show.class);
                    show.setIdInDb(dataSnapshot.getKey());
                    if((show.getArea().equals("North") || show.getArea().equals("north"))&& north) {
                        list.add(show);
                    }
                    if((show.getArea().equals("South") || show.getArea().equals("south")) && south) {
                        list.add(show);
                    }
                    if((show.getArea().equals("Center") || show.getArea().equals("center")) && center) {
                        list.add(show);
                    }

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reservationList = new ArrayList<Reservations>();
        reservationsDb = FirebaseDatabase.getInstance().getReference("Reservations");
        reservationsDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reservationList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Reservations reservation = dataSnapshot.getValue(Reservations.class);
                    reservationList.add(reservation);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                logInUser = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        getMenuInflater().inflate(R.menu.filter_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                FirebaseAuth.getInstance().signOut();
                                closeProfile();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Log out");
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
            case R.id.myProfile:
                this.finish();
                startActivity(new Intent(ProfileActivity.this,EditUser.class));
                return true;
            case R.id.stopmusic:
                adapter.mediaPlayer.pause();
                return true;
            case R.id.exit:
                DialogInterface.OnClickListener dialogClickListenerExit = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                FirebaseAuth.getInstance().signOut();
                                System.exit(0);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builderExit = new AlertDialog.Builder(ProfileActivity.this);
                builderExit.setTitle("Exit APP");
                builderExit.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListenerExit)
                        .setNegativeButton("No", dialogClickListenerExit).show();
                return true;
            case R.id.filter:
                this.finish();
                startActivity(new Intent(ProfileActivity.this,FilterClass.class));
                return true;
            case R.id.myReservations:
                this.finish();
                Intent i =new Intent(ProfileActivity.this,MyReservations.class);
                i.putExtra("email",logInUser.getEmail());
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void closeProfile() {
        this.finish();
        startActivity(new Intent(ProfileActivity.this,MainActivity.class));
    }

    @Override
    public void doOrder(int position,int progress) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Show show = list.get(position);
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Reservations");
                        Reservations reservation = new Reservations(logInUser.email, logInUser.fullName, show.getBandname(),show.getDate(), show.getTime(), String.valueOf(progress), show.getPrice(), show.getIdInDb(), show.getCity());
                        if(checkIfReservationAllreadyInDb(logInUser, reservation)) {
                            databaseReference.push().setValue(reservation);
                            popDialogWithExplanation("Information","Your reservation has been saved in the system");
                        }
                        else{
                            popDialogWithExplanation("Information","This reservation already exist.\nIf you want to change the number of people please cancel the reservation and order again");
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Information");
        builder.setMessage("Are you sure you want to make the reservation for " + progress + " people?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void popDialogWithExplanation(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private boolean checkIfReservationAllreadyInDb(User logInUser, Reservations reservationCheck) {
        for(Reservations reservation : reservationList){
            if(reservationCheck.getShowId().equals(reservation.getShowId()) && reservationCheck.getDate().equals(reservation.getDate()) && reservationCheck.getTime().equals(reservation.getTime()) && reservationCheck.getFullName().equals(reservation.getFullName())){
                return false;
            }
        }
        return true;
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

        IntentFilter intentFilterService = new IntentFilter();
        intentFilterService.addAction("dbUpdated");

        broadcastReceiverService = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int foregroundServiceDb = intent.getIntExtra("foregroundServiceUpdate", 0);
                if(foregroundServiceDb == 1){
                    popDialogWithExplanation("Update","There is update in game");
                }
            }
        };
        registerReceiver(broadcastReceiverService, intentFilterService);


    }

    private void logOutFromTheSystem() {
        FirebaseAuth.getInstance().signOut();
        closeProfile();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(broadcastReceiverService);
    }

}