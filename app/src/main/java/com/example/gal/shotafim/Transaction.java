package com.example.gal.shotafim;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Transaction {




    private String mSender;
    private String mReceiver;

    private double mAmount;

    private String mType;

    private String mDate;




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

    private String createDate() {
        java.util.Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E dd.mm.yyy HH:mm:ss");
        ft.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        return ft.format(d);
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}
