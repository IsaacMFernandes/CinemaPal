package edu.floridapoly.mobiledeviceapps.fall21.cinemapal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import edu.floridapoly.mobiledeviceapps.fall21.cinemapal.data.database.entities.Film;

/*

The Main Activity for CinemaPal
This activity holds three fragments connected by a bottom navigation view

There is also a method provided that defines what happens when the back button is pressed
    - If not on home screen -> go to home screen
    - If on home screen -> exit app

 */

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    StringBuffer response;
    String responseText,name,address,date;
    // This variable will be used to call methods relating to the bottom navigation view
    private BottomNavigationView bottomNav;
    private int count = 0;
    // View Model
    //private CinemaPalViewModel viewModel;

    // A method to define what happens when the main activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Call the super method
        super.onCreate(savedInstanceState);

        // Setting the main activity xml file
        setContentView(R.layout.activity_main);

        // Set the initial fragment/screen as the home screen
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, HomeFragment.newInstance()).commit();

        // Initializing the bottom navigation view object
        bottomNav = findViewById(R.id.bottom_nav_view);

        // Setting a listener for when a bottom item is selected
        bottomNav.setOnItemSelectedListener(navListener);
        //viewModel = new ViewModelProvider(this).get(CinemaPalViewModel.class);

        /*
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT)
                    Toast.makeText(MainActivity.this, "Swiped Left", Toast.LENGTH_SHORT).show();
                else if (direction == ItemTouchHelper.RIGHT)
                    Toast.makeText(MainActivity.this, "Swiped Right", Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    @Override
    protected void onResume() {
        super.onResume();

        String userId = UserUtil.getId(this);
        if(userId.isEmpty()) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
        }

    }

    public void clickedGotoLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    ArrayList<String> arrayListCollection = new ArrayList<>();
    ArrayAdapter<String> adapter;
    EditText txt; // user input bar
    String getInput="";



    android.app.AlertDialog locationDialog(String message) {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText editTextName1 = new EditText(MainActivity.this);
        builder.setMessage(message);
        builder.setTitle("Enter Location: ");
// titles can be used regardless of a custom layout or not
        builder.setView(editTextName1);
        LinearLayout layoutName = new LinearLayout(this);
        layoutName.setOrientation(LinearLayout.VERTICAL);
        layoutName.addView(editTextName1); // displays the user input bar
        builder.setView(layoutName);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                getInput = editTextName1.getText().toString();


                dialogInterface.dismiss();
                //LocationDialog(name + " " + address + " " + date);
            }
        });
        return builder.create();
    }



    public void clickedW2W(View view){

        //locationDialog("City");

        TextView title = (TextView) findViewById(R.id.title_text_view);
        String title1 = title.getText().toString();
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, title1 + " showtime"); // query contains search string
        startActivity(intent);
        finish();
        title1 = "";

    }//clickedW2W

    /*/api
    public void GetDataFRomWebService(View view) {
        TextView title;
        title = findViewById(R.id.title_text_view);
        String titleString = title.getText().toString();
        String urlStr = "https://serpapi.com/search.json?q=";
        urlStr = urlStr + titleString;
        urlStr = urlStr + "&" + "location=" + getInput;
        String url = urlStr + "&hl=en&gl=us&api_key=45b0b3f988ac3a81d24803a182a1e57c667e86527a585920e9cb9b0c0b8c2c96";
        Log.i("URL:", url);
        new BackgroundWebAccess().execute(url);
    }

    class BackgroundWebAccess extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            return AccessInternet(objects[0].toString());
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

        }

        protected Void AccessInternet(String urlStr) {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    // Reading response from input Stream
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream()));
                    String output;
                    response = new StringBuffer();
                    while ((output = in.readLine()) != null) {
                        response.append(output);
                    }
                    in.close();
                }

                responseText = response.toString();
                Log.i("WebService", responseText);

                JSONObject jsonResponse = new JSONObject(responseText);




                JSONArray showtimes = jsonResponse.getJSONArray("showtimes");
                // implement for loop for getting showtimes list data
                for (int i = 0; i < showtimes.length(); i++) {

                    JSONObject showtime = showtimes.getJSONObject(i);
                    date = showtime.getString("date");
                    JSONArray theater = jsonResponse.getJSONArray("theaters");
                    for (int j = 0; j < showtimes.length(); j++) {

                        JSONObject theaterS = theater.getJSONObject(i);
                        name =theaterS.getString("name");
                        address = theaterS.getString("address");
                    }
                }



                // ADD the code to get the latitude and longitude information
                // ADD the code to get the temp information
                // ADD the code to get the weather description information

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }
    */

    // Implementation of the bottom navigation view
    private NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener()
            {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item)
                {
                    // Using a switch statement to know which fragment to switch to
                    switch(item.getItemId())
                    {
                        // The home button was pressed
                        case R.id.nav_home:
                            // Call the openFragment method that we created, and create a home fragment instance
                            openFragment(HomeFragment.newInstance());
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
            openFragment(HomeFragment.newInstance());

        // Making sure the home item in the navigation view is highlighted
        bottomNav.setSelectedItemId(bottomNav.getMenu().getItem(0).getItemId());
    }

    // Method to open a given fragment
    private void openFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .addToBackStack(null)
        .commit();
    }
}