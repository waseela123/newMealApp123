package com.example.myapplication.Activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.myapplication.DataBase.FirebaseServices;
import com.example.myapplication.Fragments.AddMealFragment;
import com.example.myapplication.Fragments.FavoriteFragment;
import com.example.myapplication.Fragments.LoginFragment;
import com.example.myapplication.Fragments.MealListFragment;
import com.example.myapplication.Fragments.ListFragmentType;
import com.example.myapplication.R;
import com.example.myapplication.DataBase.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private FirebaseServices fbs;
    private BottomNavigationView bottomNavigationView;
    private User userData;
    private Stack<Fragment> fragmentStack = new Stack<>();
    private FrameLayout fragmentContainer;
    private ListFragmentType listType;
    private ProgressDialog progressDialog;

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        fbs = FirebaseServices.getInstance();
        //fbs.getAuth().signOut();
        listType = ListFragmentType.Regular;
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);

        if (fbs.getAuth().getCurrentUser() != null) gotoMealListFragment();
        else gotoLoginFragment();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.action_home) {

                    selectedFragment = new MealListFragment();

                } else if (item.getItemId() == R.id.action_fav) {

                    selectedFragment = new FavoriteFragment();

                } else if (item.getItemId() == R.id.action_add) {

                    selectedFragment = new AddMealFragment();

                } else if (item.getItemId() == R.id.action_search) { // Add search bar

                    selectedFragment = new MealListFragment();

                } else if (item.getItemId() == R.id.action_signout) {
                    signout();
                    bottomNavigationView.setVisibility(View.GONE);

                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, selectedFragment)
                            .commit();
                }
                return true;
            }
        });
        fragmentContainer = findViewById(R.id.frameLayout);
        userData = getUserData();

        if (fbs.getAuth().getCurrentUser() == null) {
            bottomNavigationView.setVisibility(View.GONE);
            gotoLoginFragment();
            pushFragment(new LoginFragment());
        } else {
            bottomNavigationView.setVisibility(View.VISIBLE);
            //fbs.getCurrentObjectUser();
            gotoMealListFragment();
            pushFragment(new MealListFragment());
        }

    }

    private void signout() {
        fbs.getAuth().signOut();
        bottomNavigationView.setVisibility(View.INVISIBLE);
        gotoLoginFragment();
    }

    private void gotoLoginFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, new LoginFragment());
        ft.commit();
    }

    private void gotoMealListFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        bottomNavigationView.setVisibility(View.VISIBLE);
        ft.replace(R.id.frameLayout, new MealListFragment());
        ft.commit();
    }

    public void onBackPressed() {
        if (fragmentStack.size() > 1) {
            fragmentStack.pop(); // Remove the current fragment from the stack
            Fragment previousFragment = fragmentStack.peek(); // Get the previous fragment

            // Replace the current fragment with the previous one
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, previousFragment)
                    .commit();
        } else {
            super.onBackPressed(); // If there's only one fragment left, exit the app
        }
    }

    public User getUserDataObject() {
        return this.userData;
    }

    public void pushFragment(Fragment fragment) {
        fragmentStack.push(fragment);
    }

    public User getUserData() {
        final User[] currentUser = {null};
        try {
            progressDialog.show();
            fbs.getFire().collection("users")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    User user = document.toObject(User.class);
                                    if (fbs.getAuth().getCurrentUser() != null && (fbs.getAuth().getCurrentUser().getEmail().equals(user.getUserName()))) {
                                        currentUser[0] = document.toObject(User.class);
                                        fbs.setCurrentUser(currentUser[0]);
                                    }
                                }
                            } else {
                                Log.e("AllRestActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "error reading!" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return currentUser[0];
    }
}
