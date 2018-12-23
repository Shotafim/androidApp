package com.example.gal.shotafim;

public class Transfer extends Transaction {

    private String SenderID;
    private String ReceiverID; // can be also: private String ReceiverID;
    private String transferID;

    public Transfer(String amount, String note, String senderID, String receiverID) {
        super(amount, note);
        SenderID = senderID;
        ReceiverID = receiverID;
        transferID = Generator.nextSessionId();
    }

    public Transfer() {
    }

    public String getSenderID() {
        return SenderID;
    }

    public void setSenderID(String senderID) {
        SenderID = senderID;
    }

    public String getReceiverID() {
        return ReceiverID;
    }

    public void setReceiverID(String receiverID) {
        ReceiverID = receiverID;
    }
}
