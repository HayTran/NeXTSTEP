package com.a20170208.tranvanhay.appat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.a20170208.tranvanhay.appat.UtilitiesClass.DialogExecution;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    DatabaseReference mData;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    EditText editTextUsername, editTextPassword;
    TextView textViewForgotPassword;
    Button btnLogIn, btnSignUp;
    CheckBox checkBoxRememberAccount;
    boolean existingUser = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkStepSignInActivity();
        setContentView(R.layout.activity_signin);
        init();
        mapping();
        addControl();
    }

    private void init() {
            // Hide Action Bar
        getSupportActionBar().hide();
            // Define instance for firebase connection
        mData = FirebaseDatabase.getInstance().getReference();
            // Initial mAuth for Authenication methods
        mAuth = FirebaseAuth.getInstance();
        configureFirebaseMessaging();
    }

    private void mapping(){
        editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        btnLogIn = (Button)findViewById(R.id.btnLogIn);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        textViewForgotPassword = (TextView)findViewById(R.id.textViewForgotPassword);
        checkBoxRememberAccount  = (CheckBox)findViewById(R.id.checkBoxRemember);
    }
    private void configureFirebaseMessaging() {
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        Log.d(TAG, "Subscribed topic news");
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Token Instane ID is: "+ token);
    }
    private void addControl() {
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextUsername.getText().toString().equals("") && !editTextPassword.getText().toString().equals("")){
                    logIn();
                }
                else {
                    Toast.makeText(SignInActivity.this, "Bạn phải điền đầy đủ các mục", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextUsername.getText().toString().equals("") && !editTextPassword.getText().toString().equals("")){
                    signUp();
                }
                else {
                    Toast.makeText(SignInActivity.this, "Bạn phải điền đầy đủ các mục", Toast.LENGTH_SHORT).show();
                }
            }
        });
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignInActivity.this, "Chức năng này chưa được cài đặt", Toast.LENGTH_SHORT).show();
            }
        });
        checkBoxRememberAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignInActivity.this, "Chức năng này chưa được cài đặt", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signUp(){
        String email = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        final DialogExecution dialogExecution = new DialogExecution(this,"Đăng đăng ký tài khoản, vui lòng chờ");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignInActivity.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                            dialogExecution.dismissProgressDialog();
                        }else{
                            Toast.makeText(SignInActivity.this,"Đăng ký thất bại",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void logIn(){
        String email = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        final DialogExecution dialogExecution = new DialogExecution(this,"Đăng đăng nhập tài khoản, vui lòng chờ");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignInActivity.this,DashboardActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(SignInActivity.this,"Đăng nhập thất bại",Toast.LENGTH_SHORT).show();
                        }
                        dialogExecution.dismissProgressDialog();
                    }
                });
    }
    private void checkAccount(){
        // Listen user
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    existingUser = true;
                    Log.d(TAG,"User = " + user.getUid());
                } else {
                    existingUser = false;
                    Log.d(TAG,"Not user");
                }
                Log.d(TAG,"existingUser:" + existingUser);
            }
        };
    }
    private void checkStepSignInActivity(){
        checkAccount();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(TAG,"CheckStep: User = " + user.getUid());
            Log.d(TAG,"Đã check account");
            Intent intent = new Intent(SignInActivity.this,DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.d(TAG,"CheckStep: Not user");
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
