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

public class login extends AppCompatActivity {

    Button Login;
    FirebaseAuth myAuth;
    EditText email, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.Lemail);
        pass = findViewById(R.id.Lpassword);
        Login = findViewById(R.id.login12);


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Demail, Dpassword;
                Dpassword = String.valueOf(pass.getText());
                Demail = String.valueOf(email.getText());
                myAuth = FirebaseAuth.getInstance();


                if (TextUtils.isEmpty(Demail)) {

                    Toast.makeText(login.this, "Please enter Email", Toast.LENGTH_SHORT).show();

                }

                if (TextUtils.isEmpty(Dpassword)) {

                    Toast.makeText(login.this, "Please enter password", Toast.LENGTH_SHORT).show();

                }


                myAuth.signInWithEmailAndPassword(Demail, Dpassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Intent s2 = new Intent(login.this, DashBoard.class);
                                    startActivity(s2);

                                    Toast.makeText(login.this, "Sucess.",
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(login.this, "failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });


    }
}