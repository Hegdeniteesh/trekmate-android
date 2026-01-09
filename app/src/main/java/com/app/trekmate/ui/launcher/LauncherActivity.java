package com.app.trekmate.ui.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.app.trekmate.ui.auth.LoginActivity;
import com.app.trekmate.ui.home.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LauncherActivity extends AppCompatActivity {

    private boolean navigated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (navigated) return;
        navigated = true;

        //  Delay avoids Android background launch restriction + ANR
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            FirebaseAuth auth = FirebaseAuth.getInstance();

            Intent intent;
            if (auth.getCurrentUser() == null) {
                intent = new Intent(LauncherActivity.this, LoginActivity.class);
            } else {
                intent = new Intent(LauncherActivity.this, HomeActivity.class);
            }

            // Clear launcher from back stack
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            finish();

        }, 300);
    }
}
