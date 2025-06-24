package com.example.newshow;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button insertshow;
    private EditText bandname, date, hour, min, city, area,  ticket_price ;
    private ProgressBar progressBar;
    private DatabaseReference MyDatabaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        insertshow = (Button) findViewById(R.id.insertshow);
        insertshow.setOnClickListener(this);

        bandname = findViewById(R.id.bandname);

        date = findViewById(R.id.date);
        hour = findViewById(R.id.hour);
        min = findViewById(R.id.min);

        city = findViewById(R.id.stadium_city);
        area = findViewById(R.id.stadium_area);

        ticket_price= findViewById(R.id.ticket_price);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.insertshow:
                MyDatabaseReference = FirebaseDatabase.getInstance().getReference().child("show");
                insertShow();
                break;
        }

    }

    private void insertShow() {

        String s_bandname = bandname.getText().toString().trim();

        String s_date = date.getText().toString().trim();
        String s_hour = hour.getText().toString().trim();
        String s_min = min.getText().toString().trim();

        String s_stadium_city = city.getText().toString().trim();
        String s_stadium_area = area.getText().toString().trim();

        String s_ticket_price = ticket_price.getText().toString().trim();




        if(s_bandname.isEmpty()) {
            bandname.setError("Band name is required!");
            bandname.requestFocus();
            return;
        }

        if(s_date.isEmpty()) {
            date.setError("Show date is required!");
            date.requestFocus();
            return;
        }


        if(s_hour.isEmpty()) {
            hour.setError("Time of Show is required!");
            hour.requestFocus();
            return;
        }

        if(s_min.isEmpty()) {
            min.setError("The area of the Show is required!");
            min.requestFocus();
            return;
        }

        if(s_stadium_city.isEmpty()) {
            city.setError("The Type of the Show is required!");
            city.requestFocus();
            return;
        }
        if(s_stadium_area.isEmpty()) {
            area.setError("The Price of the Show is required!");
            area.requestFocus();
            return;
        }

        if(s_ticket_price.isEmpty()) {
            ticket_price.setError("The Price of the Show is required!");
            ticket_price.requestFocus();
            return;
        }

        Show show = new Show(s_bandname ,s_date,s_hour+':'+s_min,s_stadium_city,s_stadium_area,s_ticket_price);
        // progressBar.setVisibility(View.VISIBLE);
        MyDatabaseReference.push().setValue(show);
        Context context = getApplicationContext();
        CharSequence text = "A new show has been added to the DB and is available for purchase";
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();



    }
}