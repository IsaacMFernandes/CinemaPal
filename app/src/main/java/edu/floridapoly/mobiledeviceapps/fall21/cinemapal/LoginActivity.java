package edu.floridapoly.mobiledeviceapps.fall21.cinemapal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LGN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    boolean validateUsername(String username) {
        boolean isValid = username.matches("^[a-zA-Z0-9_\\-.]+$");
        if(!isValid) {
            okDialog("Username must contain only alphanumeric characters.").show();
        }
        return isValid;
    }

    AlertDialog okDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        return builder.create();
    }

    void setControlsEnabled(boolean enabled) {
        View username = findViewById(R.id.usernameField);
        View login = findViewById(R.id.loginButton);
        View register = findViewById(R.id.registerButton);
        username.setEnabled(enabled);
        login.setEnabled(enabled);
        register.setEnabled(enabled);
    }

    public void clickedLogin(View view) {
        EditText usernameField = findViewById(R.id.usernameField);
        String username = usernameField.getText().toString();
        if(!validateUsername(username)) {
            return;
        }

        setControlsEnabled(false);

        Activity activity = this;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db
                .collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        try {
                            if(!task.isSuccessful()) {
                                Log.e(TAG, "Error accessing database");
                                okDialog("Error accessing database").show();
                                return;
                            }

                            QuerySnapshot query = task.getResult();
                            if(query.isEmpty()) {
                                okDialog("Found no usernames matching `" + username + "`. Consider creating an account?").show();
                                return;
                            }

                            if(query.size() > 1) {
                                ArrayList<String> ids = new ArrayList();
                                ArrayList<String> bios = new ArrayList();
                                for(QueryDocumentSnapshot doc : query) {
                                    ids.add(doc.getId());
                                    bios.add(doc.getString("bio"));
                                }
                                Intent intent = new Intent(activity, SelectAccountActivity.class);
                                intent.putStringArrayListExtra("IDS", ids);
                                intent.putStringArrayListExtra("BIOS", bios);
                                activity.startActivityForResult(intent, RESULT_CANCELED);
                                return;
                            }

                            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("userId", query.getDocuments().get(0).getId());
                            editor.commit();

                            activity.finish();
                        }
                        finally {
                            setControlsEnabled(true);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            finish();
        }
    }
}