package com.app.trekmate.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.trekmate.R;
import com.app.trekmate.ui.auth.LoginActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    // UI
    private EditText emailEt, nameEt, phoneEt;
    private ImageView profileImage;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private StorageReference storageRef;

    private String uid;
    private Uri imageUri;

    private static final int PICK_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Firebase init
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        // 🔐 Safety check (prevents ANR / crash)
        if (auth.getCurrentUser() == null) {
            redirectToLogin();
            return;
        }

        uid = auth.getCurrentUser().getUid();

        // Bind views
        emailEt = findViewById(R.id.profileEmail);
        nameEt = findViewById(R.id.profileName);
        phoneEt = findViewById(R.id.profilePhone);
        profileImage = findViewById(R.id.profileImage);

        findViewById(R.id.updateBtn).setOnClickListener(v -> updateProfile());
        findViewById(R.id.logoutBtn).setOnClickListener(v -> logoutUser());
        findViewById(R.id.changePhotoBtn).setOnClickListener(v -> openGallery());

        loadUserProfile();
    }

    // 🔹 Load user data
    private void loadUserProfile() {
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        emailEt.setText(doc.getString("email"));
                        nameEt.setText(doc.getString("name"));
                        phoneEt.setText(doc.getString("phone"));

                        String photoUrl = doc.getString("photoUrl");
                        if (!TextUtils.isEmpty(photoUrl)) {
                            Glide.with(this)
                                    .load(photoUrl)
                                    .placeholder(R.drawable.ic_profile)
                                    .into(profileImage);
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    // 🔹 Update name + phone
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

    // 📸 Open gallery
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    // 📸 Receive image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
            uploadProfilePhoto();
        }
    }

    // ☁ Upload to Firebase Storage
    private void uploadProfilePhoto() {
        if (imageUri == null) return;

        StorageReference photoRef =
                storageRef.child("profile_photos/" + uid + ".jpg");

        photoRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot ->
                        photoRef.getDownloadUrl().addOnSuccessListener(uri ->
                                savePhotoUrl(uri.toString())
                        )
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    // 🔗 Save URL in Firestore
    private void savePhotoUrl(String url) {
        db.collection("users")
                .document(uid)
                .update("photoUrl", url)
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, "Profile photo updated", Toast.LENGTH_SHORT).show()
                );
    }

    // 🚪 Logout
    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        redirectToLogin();
    }

    // 🔁 Redirect safely
    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
