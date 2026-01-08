package com.app.trekmate.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.trekmate.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.resetEmail);

        findViewById(R.id.resetBtn).setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String mail = email.getText().toString().trim();

        if (TextUtils.isEmpty(mail)) {
            email.setError("Email required");
            return;
        }

        auth.sendPasswordResetEmail(mail)
                .addOnSuccessListener(v ->
                        Toast.makeText(this, "Reset email sent", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
