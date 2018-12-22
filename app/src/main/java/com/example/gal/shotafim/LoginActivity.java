package com.example.gal.shotafim;

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
//
//    ValueEventListener db_event_listener = new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//        }
//    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


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
                        }
                        if(loginIsOk){
                            AuthenticatedUserHolder.instance.setAppUser(user);
                            Toast.makeText(LoginActivity.this
                                    ,"LOGIN GOOD GOOD",(Toast.LENGTH_LONG)).show();
                            startActivity(new Intent("com.example.gal.shotafim.PaymentActivity"));
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"Email or password is incorrect,try again!",Toast.LENGTH_LONG).show();
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
}
