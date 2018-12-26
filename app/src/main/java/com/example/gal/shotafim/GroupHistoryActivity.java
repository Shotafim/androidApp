package com.example.gal.shotafim;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * childeventlistener
 *
 * */
public class GroupHistoryActivity extends AppCompatActivity {
    private DatabaseReference initGroup;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mGroupPayments;
    private DatabaseReference mGroupTransfers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_deal);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mGroupPayments = mDatabaseRef.child("Transaction").child("group113"); // TODO: Set the reference to users-Group

        final ArrayList<Transaction> transactions = new ArrayList<>();

        final TransactionAdapter adapter = new TransactionAdapter(this, transactions);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);


        mGroupPayments.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    Transaction transaction = userSnapshot.getValue(Transaction.class);
                    transactions.add(transaction);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







    }


    /**
     *  DEBUG
     * */
    private ArrayList<Transaction> DEBUG_LISTVIEW_ADAPTER(ArrayList<Transaction> transactions){
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr2", "usr2", 33));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr2", "usr1", 3332));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr2", "usr1", 3332));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr2", "usr1", 3332));
//        transactions.add(new Transaction(SettingLib.PAYMENT, "usr1", "TV", 33));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr2", "usr1", 3332));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr2", "usr1", 3332));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr2", "usr1", 3332));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr2", "usr1", 3332));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr2", "usr1", 3332));
//        transactions.add(new Transaction(SettingLib.TRANSFER, "usr1", "usr2", 33));
        return transactions;
    }

    private void DEBUG_SET_DATA_TO_FIREBASE(){
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mGroupPayments = mDatabaseRef.child("Transaction").child("group113");

        final Transaction t1 = new Transaction(SettingLib.TRANSFER_STR, "tommy", "harry", 220);
        final Transaction t2 = new Transaction(SettingLib.TRANSFER_STR, "garry", "harry", 20);
        final Transaction t3 = new Transaction(SettingLib.TRANSFER_STR, "harry", "tommy", 10);
        final Transaction t4 = new Transaction(SettingLib.TRANSFER_STR, "snow", "timka", 19);

        mGroupPayments.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mGroupPayments.child("Payments").child(Generator.nextSessionId()).setValue(t1);
                mGroupPayments.child("Payments").child(Generator.nextSessionId()).setValue(t2);
                mGroupPayments.child("Payments").child(Generator.nextSessionId()).setValue(t3);
                mGroupPayments.child("Payments").child(Generator.nextSessionId()).setValue(t4);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



//                        Log.v("Transaction## :", userSnapshot.toString() + "\n");
//                    Transaction transaction = new Transaction(SettingLib.PAYMENT,
//                            userSnapshot.getKey("senderID"),
//                            userSnapshot.getValue("receiverID"),
//                            Double.parseDouble(userSnapshot.getValue("amount")));

}
