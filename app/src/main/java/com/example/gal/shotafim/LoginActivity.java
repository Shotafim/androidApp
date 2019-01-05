package com.example.gal.shotafim;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity  {
    //Buttons
    private Button signInBtn;
    private Button signUpBtn;
    //TextField
    private EditText usrNameTxt;
    private EditText passTxt;
    //Strings
    private String usrNameTxtString;
    private String passTxtString;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();


        signInBtn=findViewById(R.id.signInBtn);
        signUpBtn=findViewById(R.id.signUpLoginBtn);

        usrNameTxt = findViewById(R.id.usrNameTxt);
        passTxt = (EditText)findViewById(R.id.passTxt);

        usrNameTxtString = usrNameTxt.getText().toString();
        passTxtString = passTxt.getText().toString();


        //On Click sign in Button
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usrNameTxtString = usrNameTxt.getText().toString();
                passTxtString = passTxt.getText().toString();
                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference();
                ValidateEmail();
                myRef.child("Users").child(usrNameTxtString.replace(".","|").toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean loginIsOk = false;
                        User user=null;
                        if(dataSnapshot.exists()){
                            user = (User)dataSnapshot.getValue(User.class);
                            if(passTxtString.toString().equals(user.getPassword())){
                                loginIsOk = true;
                            }
                            else {
                                passTxt.setError("Password wrong,try again");
                            }
                        }
                        if(loginIsOk){
                            AuthenticatedUserHolder.instance.setAppUser(user);
                            setGroupHolderCard(user);

                            ShowAuthenticating();//Pop Up
                            final Timer t = new Timer();
                            t.schedule(new TimerTask() {
                                public void run() {
                                     // when the task active then close the dialog
                                    startActivity(new Intent("com.example.gal.shotafim.MenuActivity"));
                                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                                }
                            }, 3500);
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"Email or password is incorrect,try again!",Toast.LENGTH_LONG).show();
                            passTxt.setError("Wrong password ,try again");
                            usrNameTxt.setError("Wrong email ,try again");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        //On click sign up button
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToRegister();
            }
        });
    }

    public void switchToRegister(){
        Intent i = new Intent("com.example.gal.shotafim.RegisterActivity");
        startActivity(i);
    }

    /**
     * Check if all field is filled.
     * @return True / False , True if there is empty field ,False otherwise.
     */
    private boolean HasEmptyField(){
        return (passTxtString.isEmpty() || usrNameTxtString.isEmpty());
    }
    private void ValidateEmail(){
        if(!usrNameTxtString.contains("@") || usrNameTxt.length()<=5){
            usrNameTxt.setError("Invalid email!");
        }
    }
    /*
    This method is for create popup windows after press login.
     */
    private void ShowAuthenticating(){
        ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setTitle("Login");
        dialog.setMessage("Authenticating . . .");
        dialog.setCancelable(false);
        dialog.show();

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                dialog.dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 3000); // after 5 second (or 5000 milliseconds), the task will be active.
    }

    private void setGroupHolderCard(User user){
        DatabaseReference fb = FirebaseDatabase.getInstance().getReference();
        fb.child("Group").child(user.getmGroupName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Group group = dataSnapshot.getValue(Group.class);
                GroupHolder.instance.setAppGroup(group);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
