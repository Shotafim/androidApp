package com.example.gal.shotafim;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public abstract class Transaction {

    private String Date;
    private double Amount;
    private String Note;

    public Transaction(String amount, String note) {
        Date = createDate();
        Amount = Double.parseDouble(amount);
        Note = note;
    }
    public Transaction(){}

    private String createDate() {
        java.util.Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E dd.mm.yyy HH:mm:ss");
        ft.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        return ft.format(d);
    }
    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = Double.parseDouble(amount);
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "Date='" + Date + '\'' +
                ", Amount=" + Amount +
                ", Note='" + Note + '\'' +
                '}';
    }
}
