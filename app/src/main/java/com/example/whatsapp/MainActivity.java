package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {


    EditText usernameEditText;
    EditText passwordEditText;
    TextView loginTextView;
    Button signupButton;
    boolean signupModeActive = true;

    public void redirectUser(){
        if(ParseUser.getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
            signUp(view);
        }
        return false;
    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.loginTextView){
            if(signupModeActive){
                signupModeActive = false;
                signupButton.setText("LOGIN");
                loginTextView.setText("New user? Create an account ");
            }
            else{
                signupModeActive = true;
                signupButton.setText("Create an account");
                loginTextView.setText("Already have an account? Login");
            }
        }
        else if(view.getId() == R.id.logoImageView || view.getId() == R.id.whatsappTextView){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    public void signUp(View view) {
        if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")) {
            Toast.makeText(MainActivity.this, "A username and a password is required", Toast.LENGTH_SHORT).show();
        }
        else {
            if (signupModeActive) {
                ParseUser user = new ParseUser();  // Signing up new user
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), "You are successsfully signed up", Toast.LENGTH_SHORT).show();
                            redirectUser();
                        }
                        else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else{
                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {   // logging in existing user
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(e == null && user!=null){
                            Toast.makeText(MainActivity.this,"Logged in successfully", Toast.LENGTH_SHORT).show();
                            redirectUser();
                        }
                        else{
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        signupButton = (Button)findViewById(R.id.signupButton);
        loginTextView = (TextView)findViewById(R.id.loginTextView);
        TextView whatsappTextView = (TextView)findViewById(R.id.whatsappTextView);
        loginTextView.setOnClickListener(this);
        passwordEditText.setOnKeyListener(this);
        ImageView logoImageView  = (ImageView)findViewById(R.id.logoImageView);
        logoImageView.setOnClickListener(this);
        whatsappTextView.setOnClickListener(this);
        redirectUser();
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}