package com.example.authnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText full_name, age_reg, email_reg, password_reg;
    private TextView banner, register_user;
    private ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        register_user = (Button) findViewById(R.id.register_user);
        register_user.setOnClickListener(this);

        full_name = (EditText) findViewById(R.id.full_name);
        age_reg = (EditText) findViewById(R.id.age_reg);
        email_reg = (EditText) findViewById(R.id.email_reg);
        password_reg = (EditText) findViewById(R.id.password_reg);

        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.register_user:
                register_user();
                break; }
    }

    private void register_user() {
        String email = email_reg.getText().toString().trim();
        String age = age_reg.getText().toString().trim();
        String name = full_name.getText().toString().trim();
        String password = password_reg.getText().toString().trim();

        if (name.isEmpty()) {
            full_name.setError("Full name is required");
            full_name.requestFocus();
            return;
        }
        if (age.isEmpty()) {
            age_reg.setError("Age is required");
            age_reg.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            email_reg.setError("Email is required");
            email_reg.requestFocus();
            return;
        }
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_reg.setError("Please provide valid email");
            email_reg.requestFocus();
        }
        if (password.isEmpty()) {
            password_reg.setError("Password id required");
            password_reg.requestFocus();
            return;
        }
        if(password.length() < 6){
            password_reg.setError("Should be 6 characters");
            password_reg.requestFocus();
            return;
        }
        progress_bar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User (name,age,email);

                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull  Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this,"User has been registred",Toast.LENGTH_LONG).show();
                                        progress_bar.setVisibility(View.VISIBLE);
                                    }else{
                                        Toast.makeText(RegisterUser.this,"Failed to register",Toast.LENGTH_LONG).show();
                                        progress_bar.setVisibility(View.GONE);
                                    }
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(RegisterUser.this,MainActivity.class));
                                    }
                                }
                            });


                        }

                    }
                });
    }
}
