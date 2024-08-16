package com.feiruirobots.jabil.p1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

    private long backPressedTime = 0;
    private static final int BACK_PRESSED_INTERVAL = 2000; // 2 seconds
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String LAST_LOGIN_TIME_KEY = "lastLoginTime";
    private static final long SIX_HOURS_IN_MILLIS = 6 * 60 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(isUserLoggedIn()){
//            startLoginActivity();
//        }else{
            setContentView(R.layout.activity_login);
            Button buttonLogin = (Button)findViewById(R.id.buttonLogin);
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLoginClick(view);
                }
            });
//        }

    }

    @Override
    public void onBackPressed() {
        // Check if it's within the interval to exit the app
        if (System.currentTimeMillis() - backPressedTime < BACK_PRESSED_INTERVAL) {
            super.onBackPressed(); // This will finish the activity and exit the app
        } else {
            // If not within the interval, show a toast message
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            // Record the time of the last back press
            backPressedTime = System.currentTimeMillis();
        }
    }

    public void onLoginClick(View view) {
        // Handle login button click here
        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextPassword = findViewById(R.id.editTextPassword);

        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        // Perform authentication logic (e.g., check username and password)
        // For simplicity, this example just shows a toast message
        if (username.equals("123") && password.equals("123")) {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            //finish();
            saveLastLoginTime();
            startLoginActivity();
        } else {
            Toast.makeText(this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void saveLastLoginTime() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(LAST_LOGIN_TIME_KEY, System.currentTimeMillis());
        editor.apply();
    }

    private boolean isUserLoggedIn() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long lastLoginTime = preferences.getLong(LAST_LOGIN_TIME_KEY, 0);
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastLoginTime) < SIX_HOURS_IN_MILLIS;
    }
}
