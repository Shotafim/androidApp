package com.example.gal.shotafim;

import java.util.Date;

public class Transaction {


    private final int TRANSFER = 0;
    private final int PAYMENT = 1;

    private String mSender;
    private String mReceiver;

    private double mAmount;

    private String mType;

    private Date mDate;


    public Transaction(int Type, String sender, String receiver, double amount){
        if(Type == TRANSFER){
            mSender = sender;
            mReceiver = receiver;
            mAmount = amount;
            mType = SettingLib.TRANSFER_STR;
        }
        else if(Type == PAYMENT){
            mSender = sender;
            mReceiver = receiver;
            mType = SettingLib.PAYMENT_STR;
            mAmount = amount;
        }

        mDate = new Date();
    }

    public String getmSender() {
        return mSender;
    }

    public String getmReceiver() {
        return mReceiver;
    }

    public double getmAmount() {
        return mAmount;
    }

    public String getmType() {
        return mType;
    }

    public Date getmDate(){ return mDate;}
}
