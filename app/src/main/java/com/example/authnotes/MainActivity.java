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

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView register;
    private EditText editEmailAddress,editPassword;
    private Button signIn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView)findViewById(R.id.register);
        register.setOnClickListener(this);

        editEmailAddress = (EditText)findViewById(R.id.editEmailAddress);

        editPassword = (EditText)findViewById(R.id.editPassword);

        signIn = (Button) findViewById(R.id.signIn);
        signIn.setOnClickListener(this);

        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this,RegisterUser.class));
                break;

            case R.id.signIn:
                userLogin();
                break;
        }
    }
    private void userLogin() {
        String email = editEmailAddress.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if(email.isEmpty()){
            editEmailAddress.setError("empty");
            editEmailAddress.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmailAddress.setError("no valid email");
            editEmailAddress.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editPassword.setError("empty");
            editPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editPassword.setError("min password is 6 char");
            editPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);


        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this,ProfileActivity.class));

            }else {
                    Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_LONG).show();
                }
        }
        });
}
}