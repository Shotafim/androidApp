package com.example.gal.shotafim;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {
    private ImageButton personalAreaBtn;
    private ImageButton groupHistoryBtn;
    private ImageButton transactionBtn;

    //temp flag // TODO: Make the initGroupMembers each x time - to check if someone left the group or joined.
    private boolean checkOnlyOnce = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        personalAreaBtn = findViewById(R.id.personal_area_imgBtn);
        groupHistoryBtn = findViewById(R.id.group_history_imgBtn);
        transactionBtn = findViewById(R.id.new_transaction_imgBtn);


        //init Group members
        if(checkOnlyOnce){
            GroupHolder.initGroupMembers();
            checkOnlyOnce = false;
        }

        personalAreaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent personal_area_activity = new Intent(MenuActivity.this, PersonalAreaActivity.class);
              startActivity(personal_area_activity);
            }
        });

        groupHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent group_historyActivity = new Intent(MenuActivity.this, GroupHistoryActivity.class);
                startActivity(group_historyActivity);
            }
        });

        transactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent payment_activity = new Intent(MenuActivity.this, PaymentActivity.class);
                startActivity(payment_activity);
            }
        });
    }

    /**
     * Pop an alert when user push back button.
     */
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        AuthenticatedUserHolder.instance.setAppUser(null);
                        GroupHolder.getGroupMembers().clear();
                        MenuActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}
