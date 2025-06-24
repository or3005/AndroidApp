package com.example.finalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditUser extends AppCompatActivity {
    private static final String TAG = "EditUser";
    private DatabaseReference database;
    private TextView banner;
    private EditText editTextFullName, editTextAge, editTextEmail, editTextPassword;
    private Button editFullName,editAge,editEmail,editPassword;
    private User logInUser;
    private String userID;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private DatabaseReference reference;
    private DatabaseReference dbreference;
    private String oldmail;
    private String oldFullName;
    private BroadcastReceiver broadcastReceiver;
    private BroadcastReceiver broadcastReceiverService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users");
        dbreference = FirebaseDatabase.getInstance().getReference("Reservations");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                logInUser = snapshot.getValue(User.class);
                editTextEmail.setText(logInUser.email);
                oldmail = logInUser.email;
                editTextAge.setText(logInUser.age);
                editTextFullName.setText(logInUser.fullName);
                oldFullName = logInUser.fullName;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        editTextFullName = findViewById(R.id.fullName);
        editTextAge = findViewById(R.id.age);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        editFullName = findViewById(R.id.editfullname);
        editFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateFullName();
            }
        });

        editAge = findViewById(R.id.editage);
        editAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAge();
            }
        });

        editEmail = findViewById(R.id.editemail);
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateEmail();
            }
        });

        editPassword = findViewById(R.id.editpassword);
        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatePassword();
            }
        });

        progressBar = findViewById(R.id.progressBar);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("dbUpdated");
        broadcastReceiverService = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int foregroundServiceDb = intent.getIntExtra("foregroundServiceUpdate", 0);
                if(foregroundServiceDb == 1){
                    popDialogWithExplanation("Update","There is update in show");
                }
            }
        };
        registerReceiver(broadcastReceiverService, intentFilter);
    }

    void UpdateEmail()
    {
        String email = editTextEmail.getText().toString().trim();
        if(email.isEmpty()) {
            editTextEmail.setError("Field is Empty, enter new desired email!");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email");
            editTextEmail.requestFocus();
            return;
        }
        DialogInterface.OnClickListener dialogClickListenerExit = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        logInUser.setEmail(email);
                        user.updateEmail(email);
                        UpdateSettings();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builderExit = new AlertDialog.Builder(EditUser.this);
        builderExit.setTitle("Change Email");
        builderExit.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListenerExit)
                .setNegativeButton("No", dialogClickListenerExit).show();


    }

    void UpdatePassword()
    {
        String password = editTextPassword.getText().toString().trim();

        if(password.isEmpty()) {
            editTextPassword.setError("Field is Empty, enter new desired password!");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            editTextPassword.setError("Min password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        DialogInterface.OnClickListener dialogClickListenerExit = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        user.updatePassword(password);
                        Toast.makeText(EditUser.this, "Password updated successfully", Toast.LENGTH_LONG).show();

                        //popDialogWithExplanation("Password","Password updated successfully");
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builderExit = new AlertDialog.Builder(EditUser.this);
        builderExit.setTitle("Change Password");
        builderExit.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListenerExit)
                .setNegativeButton("No", dialogClickListenerExit).show();


    }

    void UpdateAge()
    {
        String age = editTextAge.getText().toString().trim();
        if(age.isEmpty()) {
            editTextAge.setError("Age is required");
            editTextAge.requestFocus();
            return;
        }
        logInUser.age = age;

        DialogInterface.OnClickListener dialogClickListenerExit = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        UpdateSettings();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builderExit = new AlertDialog.Builder(EditUser.this);
        builderExit.setTitle("Change Age");
        builderExit.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListenerExit)
                .setNegativeButton("No", dialogClickListenerExit).show();

    }

    void UpdateFullName()
    {
        String fullName = editTextFullName.getText().toString().trim();
        if(fullName.isEmpty()) {
            editTextFullName.setError("Full name is required");
            editTextFullName.requestFocus();
            return;
        }
        logInUser.fullName = fullName;

        DialogInterface.OnClickListener dialogClickListenerExit = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        UpdateSettings();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builderExit = new AlertDialog.Builder(EditUser.this);
        builderExit.setTitle("Change Full Name");
        builderExit.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListenerExit)
                .setNegativeButton("No", dialogClickListenerExit).show();
    }

    void UpdateSettings()
    {
        reference
                .child(userID)
                .setValue(logInUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(EditUser.this, "Updated Successfully", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(EditUser.this, "Fail to update! Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dbreference.orderByChild("email").equalTo(oldmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for(DataSnapshot ds:snapshot.getChildren()) {
                        String tempEmail = logInUser.getEmail();
                        String tempFullName = logInUser.getFullName();
                        ds.getRef().child("email").setValue(tempEmail);
                        ds.getRef().child("fullName").setValue(tempFullName);
                    }
                }
                oldmail = logInUser.email;
                oldFullName = logInUser.fullName;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        startActivity(new Intent(EditUser.this,ProfileActivity.class));
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
        startActivity(new Intent(EditUser.this, MainActivity.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(broadcastReceiverService);
    }

    private void popDialogWithExplanation(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditUser.this);
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