package com.example.meetingsceduling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {

    Button Login;
    FirebaseAuth myAuth;
    FirebaseFirestore db;
    EditText email, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.Lemail);
        pass = findViewById(R.id.Lpassword);
        Login = findViewById(R.id.login12);

        myAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Demail = email.getText().toString();
                String Dpassword = pass.getText().toString();

                if (TextUtils.isEmpty(Demail) || TextUtils.isEmpty(Dpassword)) {
                    Toast.makeText(login.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                myAuth.signInWithEmailAndPassword(Demail, Dpassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = myAuth.getCurrentUser();
                                    if (user != null) {
                                        db.collection("users").document(user.getUid())
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document != null && document.exists()) {
                                                                String role = document.getString("role");
                                                                if ("manager".equals(role)) {
                                                                    startActivity(new Intent(login.this, DashBoard.class));
                                                                } else if ("employee".equals(role)) {
                                                                    startActivity(new Intent(login.this, DashBoardE.class));
                                                                } else {
                                                                    Toast.makeText(login.this, "Unknown role.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            } else {
                                                                Toast.makeText(login.this, "Failed to fetch user role.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(login.this, "Failed to fetch user role.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    Toast.makeText(login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
