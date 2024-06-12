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

public class Register extends AppCompatActivity {

    EditText Name,Email,Password;
    FirebaseAuth mAuth;
    Button Reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Name = findViewById(R.id.name);
        Email = findViewById(R.id.Semail);
        Password = findViewById( R.id.Spass);
        Reg = findViewById(R.id.reg);


        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String  Demail,Dpassword,Dname;
                Dname = String.valueOf(Name.getText());
                Dpassword = String.valueOf(Password.getText());
                Demail = String.valueOf(Email.getText());
                mAuth = FirebaseAuth.getInstance();

                if(TextUtils.isEmpty(Dname))
                {

                    Toast.makeText(Register.this,"Please enter Name",Toast.LENGTH_SHORT).show();
                }


                if(TextUtils.isEmpty(Demail))
                {

                    Toast.makeText(Register.this,"Please enter Email",Toast.LENGTH_SHORT).show();

                }

                if(TextUtils.isEmpty(Dpassword))
                {

                    Toast.makeText(Register.this,"Please enter password",Toast.LENGTH_SHORT).show();

                }



                mAuth.createUserWithEmailAndPassword(Demail, Dpassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Authentication pass.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent s2 = new Intent(Register.this, MainActivity.class);
                                    startActivity(s2);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });





            }
        });


    }
}