package com.example.gal.shotafim;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gal.shotafim.WorkingClasses.Generator;
import com.example.gal.shotafim.WorkingClasses.Transfer;
import com.example.gal.shotafim.WorkingClasses.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TransferActivity extends AppCompatActivity {

    //EditTxt
    private EditText sendToTxt;
    private EditText amountTxt;
    private EditText noteTxt;
    //Button
    private Button transferBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);


        sendToTxt = findViewById(R.id.sendToTxt);
        amountTxt = findViewById(R.id.amountTxt);
        noteTxt = findViewById(R.id.noteTxt);

        transferBtn = findViewById(R.id.transferBtn);
        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HasEmptyField()) {
                    Toast.makeText(TransferActivity.this,"There is empty field,try again!",Toast.LENGTH_LONG).show();
                }
                else{
                    User user = AuthenticatedUserHolder.instance.getAppUser();
                    /*
                    Thought that it will be more nice to make a method(inside "User" class) that handle the transfer, it is possible .
                    We need to choose what will be better.

                      user.Transfer(sendToTxt.getText().toString(),amountTxt.getText().toString(),noteTxt.getText().toString());
                    */

                    final Transfer t = new Transfer(amountTxt.getText().toString(),noteTxt.getText().toString(),user.getEmail(),sendToTxt.getText().toString());
                    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    final DatabaseReference TransRef = database.child("Transcation").child(user.getmGroupName());


                    final DatabaseReference usersRef = database.child("Users").child(sendToTxt.getText().toString().replace(".","|").toLowerCase());
                    //Update credit in receiver
                    usersRef.child("credit").addListenerForSingleValueEvent(new ValueEventListener() {
                        public String oldAmount;
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                //Get oldAmount
                                oldAmount = dataSnapshot.getValue().toString();
                                Map<String,Object> newCredit = new HashMap<>();
                                //Add the old amount and the credit that sent to this user
                                newCredit.put("credit",Double.parseDouble(oldAmount)-Double.parseDouble(amountTxt.getText().toString()));
                                //Enter To DB
                                usersRef.updateChildren(newCredit);
                                //Enter Transfer to DB
                                TransRef.child("Transfers").child(Generator.nextSessionId()).setValue(t);
                                //Make Toast
                                Toast.makeText(TransferActivity.this,"Transfer Success!",Toast.LENGTH_LONG).show();
                            }
                            else{
                            Toast.makeText(TransferActivity.this,"Something went wrong,try again!",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    /**
     * Check if all field is filled.
     * @return True / False , True if there is empty field ,False otherwise.
     */
    private boolean HasEmptyField(){
        return (sendToTxt.getText().toString().isEmpty() ||
                amountTxt.getText().toString().isEmpty() ||
                noteTxt.getText().toString().isEmpty());
    }
}
