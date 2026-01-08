package com.app.trekmate.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.trekmate.R;
import com.app.trekmate.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    // UI
    private EditText email, password;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind views
        email = findViewById(R.id.regEmail);
        password = findViewById(R.id.regPassword);

        // Button click
        findViewById(R.id.registerBtn).setOnClickListener(v -> registerUser());
    }

    private void registerUser() {

        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pass.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase Auth registration
        auth.createUserWithEmailAndPassword(mail, pass)
                .addOnSuccessListener(authResult -> {

                    // 🔑 Get auto-generated UID
                    String uid = auth.getCurrentUser().getUid();

                    // Create user profile object
                    User user = new User(
                            uid,
                            mail,
                            "New User",
                            ""
                    );

                    // Save user to Firestore using UID as document ID
                    db.collection("users")
                            .document(uid)
                            .set(user)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                finish(); // back to Login
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()
                            );

                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
