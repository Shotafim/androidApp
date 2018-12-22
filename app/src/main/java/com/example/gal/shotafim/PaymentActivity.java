package com.example.gal.shotafim;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gal.shotafim.WorkingClasses.Generator;
import com.example.gal.shotafim.WorkingClasses.Payment;
import com.example.gal.shotafim.WorkingClasses.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    //EditTxt
    private EditText sendToTxt;
    private EditText amountTxt;
    private EditText noteTxt;
    //Button
    private Button paymentBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        amountTxt = findViewById(R.id.amountTxt);
        noteTxt = findViewById(R.id.noteTxt);

        paymentBtn = findViewById(R.id.paymentBtn);
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HasEmptyField()) {
                    Toast.makeText(PaymentActivity.this,"There is empty field,try again!",Toast.LENGTH_LONG).show();
                }
                else{
                    User user = AuthenticatedUserHolder.instance.getAppUser();

                    final Payment p = new Payment(amountTxt.getText().toString(),noteTxt.getText().toString());
                    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    final DatabaseReference TransRef = database.child("Transcation").child(user.getmGroupName());


                    final DatabaseReference usersRef = database.child("Users").child(user.getEmail().toLowerCase());
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
                                newCredit.put("credit",Double.parseDouble(oldAmount)+Double.parseDouble(amountTxt.getText().toString()));
                                //Enter To DB
                                usersRef.updateChildren(newCredit);
                                //Enter Transfer to DB
                                TransRef.child("Payments").child(Generator.nextSessionId()).setValue(p);
                                //Make Toast
                                Toast.makeText(PaymentActivity.this,"Transfer Success!",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(PaymentActivity.this,"Something went wrong,try again!",Toast.LENGTH_LONG).show();
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
        return (amountTxt.getText().toString().isEmpty() ||
                noteTxt.getText().toString().isEmpty());
    }
}
