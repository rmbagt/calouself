package model;

import java.sql.Timestamp;

public class Transaction {
    private String transaction_id;
    private String buyer_id;
    private String item_id;
    private Timestamp transaction_date;

    public Transaction(String transaction_id, String buyer_id, String item_id, Timestamp transaction_date) {
        this.transaction_id = transaction_id;
        this.buyer_id = buyer_id;
        this.item_id = item_id;
        this.transaction_date = transaction_date;
    }

    // Getters and Setters
    public String getTransaction_id() { return transaction_id; }
    public void setTransaction_id(String transaction_id) { this.transaction_id = transaction_id; }
    public String getBuyer_id() { return buyer_id; }
    public void setBuyer_id(String buyer_id) { this.buyer_id = buyer_id; }
    public String getItem_id() { return item_id; }
    public void setItem_id(String item_id) { this.item_id = item_id; }
    public Timestamp getTransaction_date() { return transaction_date; }
    public void setTransaction_date(Timestamp transaction_date) { this.transaction_date = transaction_date; }
}

