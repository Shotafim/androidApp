package com.example.gal.shotafim;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private FirebaseAuth mAuth;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        nameTxt = findViewById(R.id.nameTxt_Reg);
        passTxt = findViewById(R.id.passTxt_Reg);
        emailTxt = findViewById(R.id.emailTxt_Reg);
        aptTxt = findViewById(R.id.aptTxt_Reg);
        aptIDTxt = findViewById(R.id.aptIDTxt_Reg);
        cityTxt = findViewById(R.id.cityTxt_Reg);
        streetTxt = findViewById(R.id.streetTxt_Reg);
        countryTxt = findViewById(R.id.countryTxt_Reg);
        hasAptRadio = findViewById(R.id.radioButtonHas);
        createAptRadio = findViewById(R.id.radioButtonCreate);
        signUpBtn = findViewById(R.id.signUpBtn_Reg);

        radioGroup = findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (radioGroup.getCheckedRadioButtonId() == hasAptRadio.getId()) {
                    createAptInvisiable();
                } else {
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

    private void createAptVisiable() {
        aptTxt.setVisibility(View.VISIBLE);
        cityTxt.setVisibility(View.VISIBLE);
        streetTxt.setVisibility(View.VISIBLE);
        countryTxt.setVisibility(View.VISIBLE);


    }

    private void createAptInvisiable() {
        aptTxt.setVisibility(View.INVISIBLE);
        cityTxt.setVisibility(View.INVISIBLE);
        streetTxt.setVisibility(View.INVISIBLE);
        countryTxt.setVisibility(View.INVISIBLE);
    }

    public void OnClickSignUp() {
        if(!validateForm()){
            Toast.makeText(RegisterActivity.this,"Make sure you enter correct inputs.",Toast.LENGTH_LONG).show();
        }else{
            // Sets the user data by the clicked RadioBtn
            //-------Create User Object-------
            user = new User(
                    Generator.nextSessionId(),
                    nameTxt.getText().toString(),
                    emailTxt.getText().toString(),
                    passTxt.getText().toString(),
                    aptIDTxt.getText().toString());

            Log.v("USER DATA : ###", user.toString());
            //-------Create Address Object-------
            address = new Address(countryTxt.getText().toString(),
                    cityTxt.getText().toString(),
                    aptTxt.getText().toString(),
                    streetTxt.getText().toString());
            //-------Check if user didnt fill all fields-------
            if (HasEmptyFields() || (radioGroup.getCheckedRadioButtonId() == -1)) {
                Toast.makeText(RegisterActivity.this, "There is empty field,try again!", Toast.LENGTH_LONG).show();
            }
            else{
                //Don't have apt,create new one:
                if (createAptRadio.isChecked()) {
                    // Create new group from given details
                    regUserGroup = new Group(Generator.nextSessionId(),
                            aptIDTxt.getText().toString(),
                            user.getName(),
                            4,
                            address);
                    user.setmGroupName(aptIDTxt.getText().toString());
                    //-------Enter To DB-------
                    enterUserToDB();
                    enterGroupToDB();
                    enterEmailPasswordToDB();
                }
                else{//Have apt already ,just join to group:
                    //-------Enter To DB-------
                    enterUserToDB();
                    enterEmailPasswordToDB();
                    Toast.makeText(RegisterActivity.this, "You added to group: "+ user.getmGroupName(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Check if all field is filled.
     *
     * @return True / False , True if there is empty field ,False otherwise.
     */
    private boolean HasEmptyFields() {
        if (radioGroup.getCheckedRadioButtonId() == hasAptRadio.getId()) {
            return (aptIDTxt.getText().toString().isEmpty() ||
                    nameTxt.getText().toString().isEmpty() ||
                    emailTxt.getText().toString().isEmpty() ||
                    passTxt.getText().toString().isEmpty());
        } else {
            return (aptTxt.getText().toString().isEmpty() ||
                    streetTxt.getText().toString().isEmpty() ||
                    countryTxt.getText().toString().isEmpty() ||
                    cityTxt.getText().toString().isEmpty() ||
                    nameTxt.getText().toString().isEmpty() ||
                    emailTxt.getText().toString().isEmpty() ||
                    passTxt.getText().toString().isEmpty());
        }
    }

    private boolean validateForm(){
        boolean flag=true;
        if(passTxt.getText().toString().length()<5){
            passTxt.setError("Password length need to be at least 6");
            flag = false;
        }
        if(!emailTxt.getText().toString().contains("@") || emailTxt.getText().toString().length()<=5){
            emailTxt.setError("Invalid email!");
            flag = false;
        }
        if(nameTxt.getText().toString().length()<4){
            nameTxt.setError("User name length need to be at least 4");
            flag = false;
        }
        if(aptIDTxt.getText().toString().length() < 4){
            aptIDTxt.setError("Must enter ID!");
            flag = false;
        }
        if(radioGroup.getCheckedRadioButtonId() == -1){
            createAptRadio.setError("");
            hasAptRadio.setError("");
        }
        flag = !HasEmptyFields();
        return flag;
    }
    private void enterUserToDB() {
        db.child("Users").child(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(RegisterActivity.this, "Email already registered ", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    db.child("Users").child(user.getEmail()).setValue(user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void enterEmailPasswordToDB(){
        mAuth.createUserWithEmailAndPassword(emailTxt.getText().toString(), passTxt.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "createUserWithEmail:success");
                        FirebaseUser user1 = mAuth.getCurrentUser();
//                                updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
//                                updateUI(null);
                    }

                });
    }
    private void enterGroupToDB(){
        db.child("Group").child(regUserGroup.getGroupName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(RegisterActivity.this, "Group already exists", Toast.LENGTH_LONG).show();
                } else { // their is no such group , we can add the user and the group to the database.
                    db.child("Group").child(user.getmGroupName()).setValue(regUserGroup);
                    Toast.makeText(RegisterActivity.this, "User Registration & Group Complete", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
