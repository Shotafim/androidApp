package com.example.gal.shotafim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class TransactionAdapter extends ArrayAdapter<Transaction> {


    public TransactionAdapter(Context context, ArrayList<Transaction> transactions){
        super(context, 0, transactions);
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_item, parent, false);
        }

        Transaction currentTransaction = getItem(position);

        TextView userInitiatorTxt = (TextView)listItemView.findViewById(R.id.usr_initiator);
        TextView receiverTxt = listItemView.findViewById(R.id.receiver);
        TextView dealTypeTxt = listItemView.findViewById(R.id.deal_type);
        TextView amountTxt = listItemView.findViewById(R.id.deal_amount);
        TextView dealDateTxt = listItemView.findViewById(R.id.deal_date);

        userInitiatorTxt.setText(currentTransaction.getmSender());
        receiverTxt.setText(currentTransaction.getmReceiver());
        dealTypeTxt.setText(currentTransaction.getmType());
        amountTxt.setText(Double.toString(currentTransaction.getmAmount()));
        dealDateTxt.setText(currentTransaction.getmDate());


        return listItemView;
    }
}
