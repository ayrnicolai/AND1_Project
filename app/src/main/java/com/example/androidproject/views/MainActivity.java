package com.example.androidproject.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.androidproject.R;
import com.example.androidproject.models.CartItem;
import com.example.androidproject.viewmodels.ShopViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    NavController navController;
    ShopViewModel shopViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);
        shopViewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        shopViewModel.getCart().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {

            }
        });

        //If their is no user logged it will show the login register activity
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginRegisterActivity.class);
            startActivity(intent);
            finish();

        }


   //     MainActivityViewModel viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

    }
    //app navigates back to Shop
    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        return super.onSupportNavigateUp();
    }
    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginRegisterActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                AuthUI.getInstance().signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    startLoginActivity();

                                } else {
                                    Log.e(TAG, "onComplete", task.getException());
                                }

                            }
                        });

                return true;
            case R.id.action_profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;

            case R.id.action_Booking:
                Toast.makeText(this, "Booking System", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, BookingActivity.class));
                return true;

        }
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);



    }
}