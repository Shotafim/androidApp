package com.example.gal.shotafim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MenuActivity extends AppCompatActivity {
    private ImageButton personalAreaBtn;
    private ImageButton groupHistoryBtn;
    private ImageButton transactionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        personalAreaBtn = findViewById(R.id.personal_area_imgBtn);
        groupHistoryBtn = findViewById(R.id.group_history_imgBtn);
        transactionBtn = findViewById(R.id.new_transaction_imgBtn);

        personalAreaBtn.setImageResource(SettingLib.person_icn_btn);
        groupHistoryBtn.setImageResource(SettingLib.group_icn_btn);
        transactionBtn.setImageResource(SettingLib.transaction_icn_btn);

        personalAreaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

            }
        });



    }
}
