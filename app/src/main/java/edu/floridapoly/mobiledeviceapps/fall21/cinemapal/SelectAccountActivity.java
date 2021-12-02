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

        Intent intent = getIntent();
        ArrayList<String> ids = intent.getStringArrayListExtra("IDS");
        ArrayList<String> bios = intent.getStringArrayListExtra("BIOS");
        ArrayList<UserDocument> users = new ArrayList();
        for(int i = 0; i < ids.size(); i++) {
            users.add(new UserDocument(ids.get(i), bios.get(i)));
        }

        UserDocumentAdapter adapter = new UserDocumentAdapter();
        adapter.submitList(users);
        RecyclerView recycler = findViewById(R.id.recycler_view);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);
    }

    private class UserDocument {
        String id;
        String bio;

        public UserDocument(String id, String bio) {
            this.id = id;
            this.bio = bio;
        }
    }

    private class UserDocumentHolder extends RecyclerView.ViewHolder {
        TextView bio;
        Button select;

        public UserDocumentHolder(@NonNull View itemView) {
            super(itemView);
            bio = itemView.findViewById(R.id.userBioView);
            select = itemView.findViewById(R.id.thisMeButton);
        }
    }

    private class UserDocumentAdapter extends ListAdapter<UserDocument, UserDocumentHolder> {

        protected UserDocumentAdapter() {
            super(new DiffUtil.ItemCallback<UserDocument>() {
                @Override
                public boolean areItemsTheSame(@NonNull UserDocument oldItem, @NonNull UserDocument newItem) {
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull UserDocument oldItem, @NonNull UserDocument newItem) {
                    return oldItem.bio.equals(newItem.bio);
                }
            });
        }

        @NonNull
        @Override
        public UserDocumentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_login_item, parent, false);
            return new UserDocumentHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull UserDocumentHolder holder, int position) {
            UserDocument user = getItem(position);
            holder.bio.setText(user.bio);
            holder.select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("userId", user.id);
                    editor.commit();

                    // Close activity.
                    Context context = view.getContext();
                    while(context instanceof ContextWrapper) {
                        if(context instanceof Activity) {
                            Activity a = (Activity)context;
                            a.setResult(Activity.RESULT_OK);
                            a.finish();
                            break;
                        }
                        context = ((ContextWrapper)context).getBaseContext();
                    }
                }
            });
        }
    }
}