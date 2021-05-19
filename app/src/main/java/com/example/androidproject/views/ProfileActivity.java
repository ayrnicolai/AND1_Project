package com.example.androidproject.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.androidproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    CircleImageView circleImageView;
    TextInputEditText displayNameEdit;
    Button updateProfileButton;
    ProgressBar progressBar;

    String DISPLAY_NAME = null;
    String PROFILE_IMAGE_URL = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        circleImageView = findViewById(R.id.circleImageView);
        displayNameEdit = findViewById(R.id.displayNameEdit);
        updateProfileButton = findViewById(R.id.updateProfileButton);
        progressBar = findViewById(R.id.progressBar);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(TAG, "onCreate: " + user.getDisplayName());
            if (user.getDisplayName() != null) {
                displayNameEdit.setText(user.getDisplayName());
                displayNameEdit.setSelection(user.getDisplayName().length());
            }
        }
        progressBar.setVisibility(View.GONE);


    }

    public void updateProfile(View view) {

        view.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        DISPLAY_NAME = displayNameEdit.getText().toString();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(DISPLAY_NAME)
                .build();

        firebaseUser.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        view.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ProfileActivity.this, "Succesfully Updated Profile", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        view.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "onFailure: ", e.getCause());

                    }
                });
    }
}