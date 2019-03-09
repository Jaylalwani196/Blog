package com.example.blog.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private Button loginButton;
    private Button createActButton;
    private EditText emailField , passwordField;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button)findViewById(R.id.loginButtonEt);
        createActButton = (Button)findViewById(R.id.createAccountEt);
        emailField = (EditText)findViewById(R.id.loginEmailEt);
        passwordField = (EditText)findViewById(R.id.loginPasswordEt);

        createActButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , CreateAccountActivity.class));
                finish();
            }
        });


        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if(mUser!=null){
                    Toast.makeText(MainActivity.this , "Signed in" , Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this , PostListActivity.class));
                    finish();

                }else{
                    Toast.makeText(MainActivity.this , "Not Signed in" , Toast.LENGTH_LONG).show();
                }

            }
        };


        //LOGIN BUTTON WILL BE IN 'ONCREATE'

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                String pwd = passwordField.getText().toString();

                login(email,pwd);
            }
        });



    }

    public void login(String email , String pwd){
        mAuth.signInWithEmailAndPassword(email,pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //Yay , We're in
                           Toast.makeText(MainActivity.this, "Signed in" , Toast.LENGTH_LONG).show();
                           startActivity(new Intent(MainActivity.this , PostListActivity.class));
                            finish(); //VERY IMPORTANT

                        } else{
                            Toast.makeText(MainActivity.this, "Not Signed in" , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_signout){
            mAuth.signOut();
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener  !=null ){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }




}
