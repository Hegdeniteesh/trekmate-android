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
        // NO XML, NO VIEW BINDING, NOTHING
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (navigated) return;
        navigated = true;

        // Give Android time to finish startup work
        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            FirebaseAuth auth = FirebaseAuth.getInstance();

            Intent intent = (auth.getCurrentUser() == null)
                    ? new Intent(this, LoginActivity.class)
                    : new Intent(this, HomeActivity.class);

            startActivity(intent);
            finish();

        }, 300); // <-- THIS DELAY IS CRITICAL
    }
}
