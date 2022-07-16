package com.example.healthcareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth;
    EditText logEmail, logPassword;
    AppCompatButton logIn;
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        logEmail = findViewById(R.id.email2);
        logPassword = findViewById(R.id.pass2);
        logIn = findViewById(R.id.loginButton);
        logIn.setBackgroundResource(R.drawable.button);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String email1 = logEmail.getEditableText().toString();
                String pass1 = logPassword.getEditableText().toString();
                if (TextUtils.isEmpty(email1) || TextUtils.isEmpty(pass1)){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Enter valid details", Toast.LENGTH_SHORT).show();
                }else if(!email1.matches(emailpattern)){
                    progressDialog.dismiss();
                    logEmail.setError("Invalid email");
                    Toast.makeText(LoginActivity.this,"Invalid email", Toast.LENGTH_SHORT).show();
                }else if(pass1.length()<=6){
                    progressDialog.dismiss();
                    logPassword.setError("Invalid password");
                    Toast.makeText(LoginActivity.this,"Password must have 6 characters", Toast.LENGTH_SHORT).show();
                }else{
                    auth.signInWithEmailAndPassword(email1, pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "WRONG PASSWORD! \nTry again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}