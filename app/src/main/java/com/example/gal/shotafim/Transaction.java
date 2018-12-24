package com.example.gal.shotafim;

import java.util.Date;

public class Transaction {




    private String mSender;
    private String mReceiver;

    private double mAmount;

    private String mType;



    public Transaction(){
    }

    // type - {Transfer, Payment}
    public Transaction(String type, String sender, String receiver, double amount){
        mSender = sender;
        mReceiver = receiver;
        mAmount = amount;
        mType = type;
    }

    public String getmSender() {
        return mSender;
    }

    public void setmSender(String mSender) {
        this.mSender = mSender;
    }

    public String getmReceiver() {
        return mReceiver;
    }

    public void setmReceiver(String mReceiver) {
        this.mReceiver = mReceiver;
    }

    public double getmAmount() {
        return mAmount;
    }

    public void setmAmount(double mAmount) {
        this.mAmount = mAmount;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }
}
