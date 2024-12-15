package model;

import java.sql.Timestamp;

public class Offer {
    private String offer_id;
    private String item_id;
    private String buyer_id;
    private double offer_price;
    private String status;
    private String decline_reason;
    private Timestamp created_at;

    public Offer(String offer_id, String item_id, String buyer_id, double offer_price, 
                String status, String decline_reason, Timestamp created_at) {
        this.offer_id = offer_id;
        this.item_id = item_id;
        this.buyer_id = buyer_id;
        this.offer_price = offer_price;
        this.status = status;
        this.decline_reason = decline_reason;
        this.created_at = created_at;
    }

    // Getters and Setters
    public String getOffer_id() { return offer_id; }
    public void setOffer_id(String offer_id) { this.offer_id = offer_id; }
    public String getItem_id() { return item_id; }
    public void setItem_id(String item_id) { this.item_id = item_id; }
    public String getBuyer_id() { return buyer_id; }
    public void setBuyer_id(String buyer_id) { this.buyer_id = buyer_id; }
    public double getOffer_price() { return offer_price; }
    public void setOffer_price(double offer_price) { this.offer_price = offer_price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDecline_reason() { return decline_reason; }
    public void setDecline_reason(String decline_reason) { this.decline_reason = decline_reason; }
    public Timestamp getCreated_at() { return created_at; }
    public void setCreated_at(Timestamp created_at) { this.created_at = created_at; }

    // Validation methods
    public static boolean validateOfferPrice(String price) {
        if (price == null || price.trim().isEmpty()) {
            return false;
        }
        try {
            double priceValue = Double.parseDouble(price);
            return priceValue > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean validateDeclineReason(String reason) {
        return reason != null && !reason.trim().isEmpty();
    }
}

