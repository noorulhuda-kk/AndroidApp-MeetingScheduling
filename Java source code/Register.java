package com.example.meetingsceduling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText Name, Email, Password;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Button Reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setStatusBarColor(ContextCompat.getColor(Register.this, R.color.primaryColor));

        Name = findViewById(R.id.name);
        Email = findViewById(R.id.Semail);
        Password = findViewById(R.id.Spass);

        Reg = findViewById(R.id.reg);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Dname = Name.getText().toString();
                String Demail = Email.getText().toString();
                String Dpassword = Password.getText().toString();
                String role = getSelectedRole();

                if (TextUtils.isEmpty(Dname) || TextUtils.isEmpty(Demail) || TextUtils.isEmpty(Dpassword) || role == null) {
                    Toast.makeText(Register.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(Demail, Dpassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String userId = mAuth.getCurrentUser().getUid();
                                    setUserRole(userId, role, Dname);
                                } else {
                                    Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private String getSelectedRole() {
        return "manager";
    }

    private void setUserRole(String userId, String role, String Dname) {
        Map<String, Object> user = new HashMap<>();
        user.put("role", role);
        user.put("name", Dname);

        db.collection("users").document(userId)
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register.this, MainActivity.class));
                        } else {
                            Toast.makeText(Register.this, "Failed to set user role.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
