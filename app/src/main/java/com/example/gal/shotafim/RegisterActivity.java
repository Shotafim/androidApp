package com.example.gal.shotafim;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    //Button
    private Button signUpBtn;
    //EditText
    private EditText nameTxt;
    private EditText passTxt;
    private EditText emailTxt;
    private EditText aptTxt;
    private EditText aptIDTxt;
    private EditText cityTxt;
    private EditText streetTxt;
    private EditText countryTxt;

    //RadioButtons
    private RadioButton hasAptRadio;
    private RadioButton createAptRadio;
    private RadioGroup radioGroup;
    
    private Group regUserGroup;
    private Address address;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        nameTxt = findViewById(R.id.nameTxt);
        passTxt = findViewById(R.id.passTxt);
        emailTxt = findViewById(R.id.emailTxt);
        aptTxt = findViewById(R.id.aptTxt);
        aptIDTxt = findViewById(R.id.aptIDTxt);
        cityTxt = findViewById(R.id.cityTxt);
        streetTxt = findViewById(R.id.streetTxt);
        countryTxt = findViewById(R.id.countryTxt);
        hasAptRadio =findViewById(R.id.radioButtonHas);
        createAptRadio = findViewById(R.id.radioButtonCreate);
        signUpBtn = findViewById(R.id.signUpBtn);

        radioGroup = findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if( radioGroup.getCheckedRadioButtonId()==hasAptRadio.getId() ){
                    createAptInvisiable();
                }
                else{
                    int _id = new Random().nextInt(9000) + 1000;
                    aptIDTxt.setText(""+_id);
                    createAptVisiable();
                }
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickSignUp();
            }
        });
    }
    private void createAptVisiable(){
        aptTxt.setVisibility(View.VISIBLE);
        cityTxt.setVisibility(View.VISIBLE);
        streetTxt.setVisibility(View.VISIBLE);
        countryTxt.setVisibility(View.VISIBLE);


    }
    private void createAptInvisiable(){
        aptTxt.setVisibility(View.INVISIBLE);
        cityTxt.setVisibility(View.INVISIBLE);
        streetTxt.setVisibility(View.INVISIBLE);
        countryTxt.setVisibility(View.INVISIBLE);
    }
    public void OnClickSignUp(){
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

       // Sets the user data by the clicked RadioBtn
        final User user = new User(
                Generator.nextSessionId(),
                nameTxt.getText().toString(),
                emailTxt.getText().toString(),
                passTxt.getText().toString(),
                aptIDTxt.getText().toString());

        Log.v("USER DATA : ###" , user.toString());


        address = new Address(countryTxt.getText().toString(),
                cityTxt.getText().toString(),
                aptTxt.getText().toString(),
                streetTxt.getText().toString());

        if(createAptRadio.isChecked()){
            regUserGroup = new Group(Generator.nextSessionId(),
                    aptIDTxt.getText().toString(),
                    user.getName(),
                    4,
                    address);
        }




        if(HasEmptyFields() || (radioGroup.getCheckedRadioButtonId() == -1)) {
            Log.v("REGISTER:::", "RADIO ID IN IF: " + radioGroup.getCheckedRadioButtonId());
            Log.v("REGISTER:::", "RADIO hasAPT: " + hasAptRadio.getId());
            Log.v("REGISTER:::", "RADIO createAPT: " + createAptRadio.getId());
            Toast.makeText(RegisterActivity.this,"There is empty field,try again!",Toast.LENGTH_LONG).show();
        }
        else{
            db.child("Users").child(user.getEmail().replace(".","|").toLowerCase()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Toast.makeText(RegisterActivity.this, "Email already registered ", Toast.LENGTH_LONG).show();
                    }
                    else{ // User dont exist in the system, adding new user to db.
                        if(createAptRadio.isChecked()){
                            //looking if the group is already exist.
                            db.child("Group").child(regUserGroup.getGroupName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        Toast.makeText(RegisterActivity.this, "Group already exists", Toast.LENGTH_LONG).show();
                                    }else { // their is no such group , we can add the user and the group to the database.
                                        db.child("Users").child(user.getEmail().replace(",","|")).setValue(user);
                                        db.child("Group").child(user.getmGroupName()).setValue(regUserGroup);
                                        Toast.makeText(RegisterActivity.this, "User Registration & Group Complete", Toast.LENGTH_LONG).show();
                                        Intent s = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(s);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        else { // hasAptBtn checked
                            db.child("Group").child(user.getmGroupName()).addListenerForSingleValueEvent(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                         if(dataSnapshot.exists()){
                                             db.child("Users").child(user.getEmail().replace(",","|")).setValue(user);
                                             Toast.makeText(RegisterActivity.this, "You added to group: "+ user.getmGroupName(), Toast.LENGTH_LONG).show();
                                         }else{
                                             Toast.makeText(RegisterActivity.this, "Their is no such group: "+ user.getmGroupName(), Toast.LENGTH_LONG).show();
                                         }
                                     }

                                     @Override
                                     public void onCancelled(@NonNull DatabaseError databaseError) {

                                     }
                                 }
                            );
                        }

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }

    /**
     *  Check if all field is filled.
     * @return True / False , True if there is empty field ,False otherwise.
     */
    private boolean HasEmptyFields(){
         if(radioGroup.getCheckedRadioButtonId() == hasAptRadio.getId()){
             return (aptIDTxt.getText().toString().isEmpty() ||
                     nameTxt.getText().toString().isEmpty() ||
                     emailTxt.getText().toString().isEmpty() ||
                     passTxt.getText().toString().isEmpty());
         }
         else{
             return (
                     aptTxt.getText().toString().isEmpty() ||
                     streetTxt.getText().toString().isEmpty() ||
                     countryTxt.getText().toString().isEmpty() ||
                     cityTxt.getText().toString().isEmpty() ||
                     nameTxt.getText().toString().isEmpty() ||
                     emailTxt.getText().toString().isEmpty() ||
                     passTxt.getText().toString().isEmpty());
         }
    }

}
