package edu.floridapoly.mobiledeviceapps.fall21.cinemapal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");

        TextView usernameView = findViewById(R.id.usernameView);
        usernameView.setText(username);
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

    public void clickedRegister(View view) {
        EditText bioField = findViewById(R.id.bioTextField);
        String bio = bioField.getText().toString();

        if(bio.length() < 20) {
            okDialog("Bio must be at least 20 characters").show();
            return;
        }

        RegisterActivity activity = this;

        Map<String, Object> document = new HashMap<String, Object>();
        document.put("username", username);
        document.put("bio", bio);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .add(document)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        activity.okDialog("Registered as `" + username + "`");
                        activity.setResult(RESULT_OK);
                        activity.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        activity.okDialog("failed adding to database").show();
                    }
                });
    }

    public void clickedCancel(View view) {
        finish();
    }
}