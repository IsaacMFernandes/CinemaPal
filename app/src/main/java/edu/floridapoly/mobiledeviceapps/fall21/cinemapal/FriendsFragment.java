package edu.floridapoly.mobiledeviceapps.fall21.cinemapal;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText searchField;

    private UserDocumentAdapter adapter;

    public FriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        FriendsFragment self = this;

        searchField = view.findViewById(R.id.usernameSearchText);
        Button searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("FRDS", "Search for friends");
                self.clickedSearch(view);
            }
        });

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String userId = pref.getString("userId", "");

        adapter = new UserDocumentAdapter();
        adapter.setButtonText("ADD");
        adapter.submitList(new ArrayList());

        adapter.setOnClickListener(new UserDocumentAdapter.OnClickListener() {
            @Override
            public void onClick(UserDocumentAdapter.UserDocument friend, UserDocumentAdapter.UserDocumentHolder holder) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(self.getActivity());
                String userId = pref.getString("userId", "");

                HashMap<String, Object> friendship = new HashMap();
                friendship.put("userIds", Arrays.asList(userId, friend.id));
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("friendships")
                        .add(friendship)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                adapter.notifyDataSetChanged();
                            }
                        });
            }
        });
        // Disable to button if they're already a friend.
        adapter.setOnBind(new UserDocumentAdapter.OnBindView() {
            @Override
            public void onBind(UserDocumentAdapter.UserDocument friend, UserDocumentAdapter.UserDocumentHolder viewHolder) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("friendships")
                        .whereArrayContains("userIds", userId)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot query) {
                                for(QueryDocumentSnapshot doc : query) {
                                    List<String> ids = (List<String>)doc.get("userIds");
                                    if(ids.contains(friend.id))
                                        viewHolder.select.setEnabled(false);
                                }
                            }
                        });
            }
        });

        RecyclerView recycler = view.findViewById(R.id.recycler_view);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);

        return view;
    }

    public void clickedSearch(View view) {
        String searchTerm = searchField.getText().toString();

        Activity activity = getActivity();
        UserDocumentAdapter adapter = this.adapter;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String userId = pref.getString("userId", "");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("username", searchTerm)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<UserDocumentAdapter.UserDocument> users = new ArrayList();
                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if(doc.getId().equals(userId))
                                continue;
                            users.add(new UserDocumentAdapter.UserDocument(doc.getId(), doc.getString("username"), doc.getString("bio")));
                        }
                        adapter.submitList(users);
                        adapter.notifyDataSetChanged();

                        // Hide keyboard.
                        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                });
    }
}