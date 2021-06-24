package com.example.planner;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;


public class Register extends AppCompatActivity {

    EditText email, password, confirm_password;
    Button register;
    TextView login;
    FirebaseAuth firebase;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebase = FirebaseAuth.getInstance();

        // Getting data from activity_register.xml
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        register = findViewById(R.id.register);
        login = findViewById(R.id.already_registered);


        // Switching back to the login page
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        // Registering user
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }



    // Method for putting a new user into the database
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
        writeDefaultSettings();
    }

    // Will create a new user inside the database
    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(Objects.requireNonNull(user.getEmail()));

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        // Go to MainActivity
        startActivity(new Intent(Register.this, MainActivity.class));
        finish();
    }

    // Will create a username from the email entered
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    public void register(){
        // Get user input
        String userEmail = email.getText().toString().trim();
        String pword = password.getText().toString().trim();
        String confirm_pword = confirm_password.getText().toString().trim();


        // Checks if email is empty
        if(userEmail.isEmpty()){
            // Prints message underneath email field
            email.setError("Field cannot be empty");
            // Will move on to next error message
            email.requestFocus();
        }

        // Checks if password is empty
        else if(pword.isEmpty()){
            // Prints message underneath password field
            password.setError("Field cannot be empty");
            //Will move on to next error message
            password.requestFocus();
        }

        // Checks if confirm_password is empty
        else if(confirm_pword.isEmpty()){
            // Prints message underneath confirm_password field
            confirm_password.setError("Field cannot be empty");
            //Will move on to next error message
            password.requestFocus();
        }

        // Check if all fields are not empty
        else {
            // Checking if password's match
            if(pword.equals(confirm_pword)){
                // Adds user to the database
                firebase.createUserWithEmailAndPassword(userEmail, pword).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Checks if user was successfully added to the database
                        if(task.isSuccessful()){
                            onAuthSuccess(Objects.requireNonNull(task.getResult().getUser()));
                        }
                        else {
                            // Prints message
                            Toast.makeText(Register.this,"Registration not successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else {
                // Prints message underneath confirm_password field
                confirm_password.setError("Password is not matching");
            }
        }
    }


    // Writing default settings for when user initially created
    public void writeDefaultSettings(){
        String label = "Greater than or equal to 7(default)";
        String value = ">=7";
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String user_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child("users").child(user_ID).child("Settings").child("Bookable").setValue("0");
        databaseReference.child("users").child(user_ID).child("Settings").child("Eat and Drink Score").child("label").setValue(label);
        databaseReference.child("users").child(user_ID).child("Settings").child("Eat and Drink Score").child("value").setValue(value);
        databaseReference.child("users").child(user_ID).child("Settings").child("Hotel Score").child("label").setValue(label);
        databaseReference.child("users").child(user_ID).child("Settings").child("Hotel Score").child("value").setValue(value);
        databaseReference.child("users").child(user_ID).child("Settings").child("Nightlife Score").child("label").setValue(label);
        databaseReference.child("users").child(user_ID).child("Settings").child("Nightlife Score").child("value").setValue(value);
        databaseReference.child("users").child(user_ID).child("Settings").child("Sightseeing Score").child("label").setValue(label);
        databaseReference.child("users").child(user_ID).child("Settings").child("Sightseeing Score").child("value").setValue(value);
    }

}
