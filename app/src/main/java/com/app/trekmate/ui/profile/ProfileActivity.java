package com.app.trekmate.ui.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import com.app.trekmate.ui.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

import com.app.trekmate.R;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private EditText emailEt, nameEt, phoneEt;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        uid = auth.getCurrentUser().getUid();

        // Bind views
        emailEt = findViewById(R.id.profileEmail);
        nameEt = findViewById(R.id.profileName);
        phoneEt = findViewById(R.id.profilePhone);

        findViewById(R.id.updateBtn).setOnClickListener(v -> updateProfile());
        findViewById(R.id.logoutBtn).setOnClickListener(v -> logoutUser());


        loadUserProfile();
    }

    // 🔹 Fetch user data
    private void loadUserProfile() {
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        emailEt.setText(doc.getString("email"));
                        nameEt.setText(doc.getString("name"));
                        phoneEt.setText(doc.getString("phone"));
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    // 🔹 Update name & phone only
    private void updateProfile() {

        String name = nameEt.getText().toString().trim();
        String phone = phoneEt.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("phone", phone);

        db.collection("users")
                .document(uid)
                .update(updates)
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void logoutUser() {

        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);

        // 🔥 Clear back stack
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }

}
