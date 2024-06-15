package com.example.meetingsceduling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    Button man,emp;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.primaryColor));
        man = findViewById(R.id.btnmanager);

        emp = findViewById(R.id.btnemployee);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            db.collection("users").document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            role = task.getResult().getString("role");
                            if ("manager".equals(role)) {
                                startActivity(new Intent(MainActivity.this, DashBoard.class));
                            } else if ("employee".equals(role)) {
                                startActivity(new Intent(MainActivity.this, DashBoardE.class));
                            }

                        }
                    });
        }

        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent s1 = new Intent(MainActivity.this, login.class);
                startActivity(s1);
            }
        });

        emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent s1 = new Intent(MainActivity.this, loginE.class);
                startActivity(s1);
            }
        });


    }
}