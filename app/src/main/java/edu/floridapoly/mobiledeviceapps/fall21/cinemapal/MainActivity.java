package edu.floridapoly.mobiledeviceapps.fall21.cinemapal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

/*

The Main Activity for CinemaPal
This activity holds three fragments connected by a bottom navigation view

There is also a method provided that defines what happens when the back button is pressed
    - If not on home screen -> go to home screen
    - If on home screen -> exit app

 */

public class MainActivity extends AppCompatActivity {

    // This variable will be used to call methods relating to the bottom navigation view
    private BottomNavigationView bottomNav;


    // A method to define what happens when the main activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Call the super method
        super.onCreate(savedInstanceState);

        // Setting the main activity xml file
        setContentView(R.layout.activity_main);

        // Set the initial fragment/screen as the home screen
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, HomeFragment.newInstance("","")).commit();

        // Initializing the bottom navigation view object
        bottomNav = findViewById(R.id.bottom_nav_view);

        // Setting a listener for when a bottom item is selected
        bottomNav.setOnItemSelectedListener(navListener);

    }

    // Implementation of the bottom navigation view
    private NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item)
                {
                    // Using a switch statement to know which fragment to switch to
                    switch(item.getItemId())
                    {
                        // The home button was pressed
                        case R.id.nav_home:
                            // Call the openFragment method that we created, and create a home fragment instance
                            openFragment(HomeFragment.newInstance("",""));
                            return true;

                        // The explore button was pressed
                        case R.id.nav_explore:
                            openFragment(ExploreFragment.newInstance("",""));
                            return true;

                        // The friends button was pressed
                        case R.id.nav_friends:
                            openFragment(FriendsFragment.newInstance("",""));
                            return true;
                    }

                    return false;
                }
            };

    // Method to define what happens when the back button is pressed
    @Override
    public void onBackPressed()
    {
        // Initializing a Fragment object to represent the current fragment selected
        Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        // Comparing the current fragment to the home fragment
        if (currentFragment instanceof HomeFragment)
            // The user is on the home screen, exit the app
            finish();
        else
            // The user is not on the home screen, go to the home screen
            openFragment(HomeFragment.newInstance("",""));

        // Making sure the home item in the navigation view is highlighted
        bottomNav.setSelectedItemId(bottomNav.getMenu().getItem(0).getItemId());
    }

    // Method to open a given fragment
    private void openFragment(Fragment fragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}