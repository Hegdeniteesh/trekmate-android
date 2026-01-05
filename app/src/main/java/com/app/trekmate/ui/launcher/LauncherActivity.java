package com.app.trekmate.ui.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.app.trekmate.R;
import com.app.trekmate.ui.auth.LoginActivity;
import com.app.trekmate.ui.home.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        new Handler().postDelayed(() -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                // User NOT logged in → Login screen
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                // User logged in → Home
                startActivity(new Intent(this, HomeActivity.class));
            }
            finish();
        }, 1500); // 1.5 sec splash
    }
}
