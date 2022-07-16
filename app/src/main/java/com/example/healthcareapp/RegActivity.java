package com.example.healthcareapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.healthcareapp.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase db;
    FirebaseStorage store;
    EditText regUser, regEmail, regPass, regCpass;
    AppCompatButton button;
    String imguri;
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        store = FirebaseStorage.getInstance();
        regUser = findViewById(R.id.username);
        regEmail = findViewById(R.id.email);
        regPass = findViewById(R.id.pass);
        regCpass = findViewById(R.id.cpass);
        button = findViewById(R.id.button);
        button.setBackgroundResource(R.drawable.button);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String name = regUser.getEditableText().toString();
                String mail = regEmail.getEditableText().toString();
                String password = regPass.getEditableText().toString();
                String cpassword = regCpass.getEditableText().toString();
                if (TextUtils.isEmpty(name) ||TextUtils.isEmpty(mail) ||TextUtils.isEmpty(password) ||TextUtils.isEmpty(cpassword)){
                    progressDialog.dismiss();
                    Toast.makeText(RegActivity.this, "Enter valid details", Toast.LENGTH_SHORT).show();
                }else if(!mail.matches(emailpattern)){
                    progressDialog.dismiss();
                    regEmail.setError("Invalid Email");
                    Toast.makeText(RegActivity.this,"Invalid email", Toast.LENGTH_SHORT).show();
                }else if(password.length()<=6){
                    progressDialog.dismiss();
                    regPass.setError("Invalid password");
                    Toast.makeText(RegActivity.this,"Password must hae 6 characters", Toast.LENGTH_SHORT).show();
                }else if(!password.equals(cpassword)){
                    progressDialog.dismiss();
                    regCpass.setError("Password doesn't match");
                    Toast.makeText(RegActivity.this,"Password does not match", Toast.LENGTH_SHORT).show();
                }else{
                    auth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                DatabaseReference reference = db.getReference().child("users").child(auth.getCurrentUser().getUid());
                                StorageReference storageReference = store.getReference().child("upload").child(auth.getCurrentUser().getUid());
                                imguri = "https://firebasestorage.googleapis.com/v0/b/healthcareapp-48922.appspot.com/o/ss.png?alt=media&token=550c2955-6c5e-4b25-82c5-2b1060eaf670";
                                Users users = new Users(auth.getUid(),name,mail, imguri);
                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            progressDialog.dismiss();
                                            startActivity(new Intent(RegActivity.this, HomeActivity.class));
                                        }else{
                                            progressDialog.dismiss();
                                            Toast.makeText(RegActivity.this, "An error occured!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(RegActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}