package edu.floridapoly.mobiledeviceapps.fall21.cinemapal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account);

        SelectAccountActivity activity = this;

        UserDocumentAdapter adapter = new UserDocumentAdapter();
        adapter.setButtonText("THIS ME");

        Intent intent = getIntent();
        //noinspection unchecked
        ArrayList<UserDocumentAdapter.UserDocument> users = (ArrayList<UserDocumentAdapter.UserDocument>)intent.getSerializableExtra("USERS");
        adapter.submitList(users);
        adapter.setOnClickListener(new UserDocumentAdapter.OnClickListener() {
            @Override
            public void onClick(UserDocumentAdapter.UserDocument user, UserDocumentAdapter.UserDocumentHolder holder) {

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("userId", user.id);
                editor.commit();

                activity.setResult(RESULT_OK);
                activity.finish();
            }
        });

        RecyclerView recycler = findViewById(R.id.recycler_view);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);
    }

}