package com.example.gal.shotafim;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.LongFunction;

import static java.lang.Thread.sleep;

public class PaymentActivity extends AppCompatActivity {

    //EditTxt
    private EditText sendToTxt;
    private EditText amountTxt;
    private EditText noteTxt;
    //Button
    private Button paymentBtn;

    //list
    ArrayList<SearchBarItem> searchItem_list;
    ArrayList<User> usersFBGroup;

    //AutoCompleteView
    private AppCompatAutoCompleteTextView autoComplete_View;
    private AutocompleteItemAdapter autocomplete_adapter;

    //searchbar item
    private SearchBarItem search_item;

    //user that will get transfer
    private User sendUser = null;
    //
    final private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    //Pop up
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        autoComplete_View = findViewById(R.id.autoComplete_field);
        searchItem_list = new ArrayList<>();
        searchItem_list.add(new SearchBarItem("TV",SettingLib.icon_list_tv ,false));
        searchItem_list.add(new SearchBarItem("Internet",SettingLib.icon_list_internet ,false));
        searchItem_list.add(new SearchBarItem("Electricity",SettingLib.icon_list_electricity ,false));
        searchItem_list.add(new SearchBarItem("Water",SettingLib.icon_list_water ,false));
        searchItem_list.add(new SearchBarItem("Arnona",SettingLib.icon_list_arnona ,false));
        usersFBGroup = GroupHolder.getGroupMembers();
        Log.v("Log###1", "users: "+ usersFBGroup.toString());

        for(User user : usersFBGroup){
            searchItem_list.add(new SearchBarItem(user.getEmail(), SettingLib.person_icn_btn, true));
        }

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
                    SettingLib.HideKeyboard(PaymentActivity.this);
                    User user = AuthenticatedUserHolder.instance.getAppUser();
                    String mtype = getTypeFromSearchItem(search_item);
                    double mamount = Double.parseDouble(amountTxt.getText().toString());

                    //Create transaction object with the sender details.
                    final Transaction p = new Transaction(mtype, user.getEmail(),search_item.getmName(),mamount);
                    final DatabaseReference TransRef = database.child(SettingLib.FB_TRANSACTION).child(user.getmGroupName());
                    final DatabaseReference usersRef = database.child("Users").child(user.getEmail().toLowerCase());


                    //Update credit in receiver
                    usersRef.child("credit").addListenerForSingleValueEvent(new ValueEventListener() {
                        public String oldAmount;
                        public String oldAmount_sendUser;
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                //Get oldAmount
                                oldAmount = dataSnapshot.getValue().toString();
                                Map<String,Object> newCredit = new HashMap<>();
                                //Add the old amount and the credit that sent to this user
                                if(search_item.ismIsUser()){// if it user select - then its Transfer
                                    newCredit.put("credit",Double.parseDouble(oldAmount)+Double.parseDouble(amountTxt.getText().toString()));
                                    usersRef.updateChildren(newCredit);
                                    TransRef.child("Transfers").child(Generator.nextSessionId()).setValue(p);//Enter Transfer to DB
                                    /**
                                     *  Change receiver user credit
                                     *  First iteration: A.credit = 60 , b.credit = 0
                                     *  Second iteration: A.credit = 40 , b.credit = 20;
                                     */
                                    changeReceiverCredit();
                                }
                                else{ // its Payment
                                    newCredit.put("credit",Double.parseDouble(oldAmount)+Double.parseDouble(amountTxt.getText().toString()));
                                    usersRef.updateChildren(newCredit);
                                    //Enter Transfer to DB
                                    TransRef.child("Payments").child(Generator.nextSessionId()).setValue(p);
                                }
                                //Make Toast
                                showLoadingPopUp();
//                                Toast.makeText(PaymentActivity.this,"Payment Success!",Toast.LENGTH_LONG).show();

//                                switchToMenu();
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

        autocomplete_adapter = new AutocompleteItemAdapter(this,R.layout.list_item_autocomplete, searchItem_list);
        autoComplete_View.setThreshold(1);
        autoComplete_View.setOnItemClickListener(onItemClickListener);
        autoComplete_View.setAdapter(autocomplete_adapter);
    }
    /**
     * Check if all field is filled.
     * @return True / False , True if there is empty field ,False otherwise.
     */
    private boolean HasEmptyField(){
        return (amountTxt.getText().toString().isEmpty() ||
                noteTxt.getText().toString().isEmpty());
    }

    private String getTypeFromSearchItem(SearchBarItem item){
        if(item.ismIsUser()){
            return SettingLib.TRANSFER_STR;
        }
        return SettingLib.PAYMENT_STR;
    }

    /**
     * Switch to menu after X seconds.
     * By default it's 1000 milliseconds(1 seconds).
     */
    private void switchToMenu(){
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                Intent MenuActivity = new Intent(PaymentActivity.this, MenuActivity.class);
                startActivity(MenuActivity);
                t.cancel();
            }
        }, 5000);
    }

    /**
     * show loading pop up
     * @return
     */
    private void showLoadingPopUp(){

        dialog=new ProgressDialog(PaymentActivity.this);
        dialog.setTitle("Transaction");
        dialog.setMessage("Processing . . .");
        dialog.setCancelable(false);
        dialog.show();

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
//                dialog.dismiss(); // when the task active then close the dialog
                runOnUiThread(changeMessage);
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 3000); // after 5 second (or 5000 milliseconds), the task will be active.

    }
    private Runnable changeMessage = new Runnable() {
        @Override
        public void run() {
            //Log.v(TAG, strCharacters);
            dialog.setMessage("Success ! ! !");
            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                public void run() {
                    dialog.dismiss(); // when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }, 1000);

        }
    };
    private void changeReceiverCredit(){
        final DatabaseReference usersRef = database.child("Users").child(search_item.getmName());
        usersRef.child("credit").addListenerForSingleValueEvent(new ValueEventListener() {
            public double oldAmount_recieverUser;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    oldAmount_recieverUser = dataSnapshot.getValue(Double.class);
                    Map<String,Object> newCredit = new HashMap<>();
                    newCredit.put("credit",oldAmount_recieverUser-Double.parseDouble(amountTxt.getText().toString()));
                    usersRef.updateChildren(newCredit);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    search_item = (SearchBarItem)adapterView.getItemAtPosition(i);
                    Toast.makeText(PaymentActivity.this,
                            "Clicked item from auto completion list "
                                    +( (SearchBarItem)adapterView.getItemAtPosition(i)).getmName()
                            , Toast.LENGTH_SHORT).show();
                    autoComplete_View.setText(( (SearchBarItem)adapterView.getItemAtPosition(i)).getmName());
                }
            };
}
