package com.example.planner;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceGroup;

import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Login extends AppCompatActivity {
    // Getting data from activity_login.xml part 1
    EditText username, password;
    Button login;
    TextView register;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.not_registered);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // switch to the register view
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        //Login to app
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Getting user input
                String user = username.getText().toString().trim();
                String pword = password.getText().toString().trim();


                // Checking if username is empty
                if (user.isEmpty()) {
                    username.setError("Field cannot be empty");
                    //Will move on to next error message
                    username.requestFocus();
                }

                // Checking if password is empty
                else if (pword.isEmpty()) {
                    password.setError("Field cannot be empty");
                    password.requestFocus();
                }

                // Checking is username and password are not empty
                else {
                    // Signing user in
                    auth.signInWithEmailAndPassword(user, pword).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // Checks if user successfully logged in
                            if (!task.isSuccessful()) {
//                                login.setError("User not recognised");
                                // Prints message
                                Toast.makeText(Login.this, "Login error", Toast.LENGTH_SHORT).show();
                            } else {
                                // Switching to home page
                                startActivity(new Intent(Login.this, MainActivity.class));
                                Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
                }

            }
        });

    }

    // Will keep the user logged in to app until they sign out
    @Override
    public void onStart() {
        super.onStart();

        // Checking if user is logged in upon launch
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, MainActivity.class));
        }
    }

}
