package com.app.trekmate.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.trekmate.R;
import com.app.trekmate.ui.home.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.emailInput);
        password = findViewById(R.id.passwordInput);

        findViewById(R.id.loginBtn).setOnClickListener(v -> loginUser());

        findViewById(R.id.goToRegister).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        findViewById(R.id.forgotPassword).setOnClickListener(v ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }

    private void loginUser() {
        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (TextUtils.isEmpty(mail)) {
            email.setError("Email required");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            email.setError("Invalid email");
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            password.setError("Password required");
            return;
        }

        auth.signInWithEmailAndPassword(mail, pass)
                .addOnSuccessListener(result -> {
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
