package com.example.gal.shotafim;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.lang.Thread.sleep;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailTxt;
    private EditText passTxt;

    private Button signInBtn;
    private Button signUpBtn;

    private User user=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        emailTxt = findViewById(R.id.emailTxt_Login);
        passTxt = findViewById(R.id.passTxt_Login);
        signInBtn = findViewById(R.id.signInBtn_Login);
        signUpBtn = findViewById(R.id.signUpLoginBtn_Login);


        mAuth = FirebaseAuth.getInstance();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm()){
                    signIn(emailTxt.getText().toString(),passTxt.getText().toString());
                }
                else{
                    Toast.makeText(LoginActivity.this,"Email or password incorrect, try again!",Toast.LENGTH_LONG).show();
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToRegister();
            }
        });

    }

    private void switchToRegister() {
        startActivity(new Intent("com.example.gal.shotafim.RegisterActivity"));
    }
    private void switchToMenu(){
        startActivity(new Intent("com.example.gal.shotafim.MenuActivity"));
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }
    private void signIn(String email,String password){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithEmail:success");
                                AuthenticatedUserHolder.instance.setAppUser(getUserInfo());
                                GroupHolder.initGroupMembers();
//                                FirebaseUser user = mAuth.getCurrentUser();
                                try {
                                    sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                GroupHolder.getGroupMembers().forEach(x-> Toast.makeText(LoginActivity.this,""+x.getEmail()+"\n",Toast.LENGTH_LONG).show());
                                try {
                                    sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                switchToMenu();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                            }
                        }
                    });
    }

    private boolean validateForm() {
        boolean flag = true;
        if(emailTxt.getText().toString().isEmpty() || emailTxt.getText().toString().length() < 5){
            emailTxt.setError("Email invalid,try again!");
            flag = false;
        }
        if(passTxt.getText().toString().isEmpty() || passTxt.getText().toString().length() < 6){
            passTxt.setError("Password invalid,try again!");
            flag = false;
        }
        return flag;
    }
    private User getUserInfo(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        myRef.child("Users").child(emailTxt.getText().toString().replace(".","|").toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean loginIsOk = false;
                if(dataSnapshot.exists()){
                    user = (User)dataSnapshot.getValue(User.class);
                    AuthenticatedUserHolder.instance.setAppUser(user);
                    GroupHolder.initGroupMembers();
                }
                else {
                    Toast.makeText(LoginActivity.this,"Invalid input,try again!",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return user;
    }
    private void UpdateUI(){

    }
}
