package com.example.gal.shotafim;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GroupHistoryActivity extends AppCompatActivity {

    private FirebaseDatabase fb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_deal);

        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr2", "usr2", 33));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr2", "usr1", 3332));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr2", "usr1", 3332));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr2", "usr1", 3332));
        transactions.add(new Transaction(SettingLib.PAYMENT, "usr1", "TV", 33));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr2", "usr1", 3332));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr2", "usr1", 3332));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr2", "usr1", 3332));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr2", "usr1", 3332));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr2", "usr1", 3332));
        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));

        TransactionAdapter adapter = new TransactionAdapter(this, transactions);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);











    }
}
