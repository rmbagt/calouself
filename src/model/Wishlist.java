package model;

import java.sql.Timestamp;

public class Wishlist {
    private String wishlist_id;
    private String user_id;
    private String item_id;
    private Timestamp created_at;

    public Wishlist(String wishlist_id, String user_id, String item_id, Timestamp created_at) {
        this.wishlist_id = wishlist_id;
        this.user_id = user_id;
        this.item_id = item_id;
        this.created_at = created_at;
    }

    // Getters and Setters
    public String getWishlist_id() { return wishlist_id; }
    public void setWishlist_id(String wishlist_id) { this.wishlist_id = wishlist_id; }
    public String getUser_id() { return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }
    public String getItem_id() { return item_id; }
    public void setItem_id(String item_id) { this.item_id = item_id; }
    public Timestamp getCreated_at() { return created_at; }
    public void setCreated_at(Timestamp created_at) { this.created_at = created_at; }
}

