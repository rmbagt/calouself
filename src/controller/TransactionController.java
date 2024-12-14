package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import connect.Connect;
import model.User;
import view.PurchaseHistoryView;

public class TransactionController {
    private Connect connect;

    public TransactionController() {
        this.connect = Connect.getInstance();
    }

    public void loadPurchaseHistory(PurchaseHistoryView purchaseHistoryView, User currentUser) {
        purchaseHistoryView.getPurchaseHistoryView().getItems().clear();
        try {
            String query = "SELECT t.transaction_id, i.item_name, i.price, t.transaction_date " +
                           "FROM transactions t " +
                           "JOIN items i ON t.item_id = i.item_id " +
                           "WHERE t.buyer_id = ? " +
                           "ORDER BY t.transaction_date DESC";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, currentUser.getUser_id());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String transactionInfo = rs.getString("transaction_id") + " - " +
                                         rs.getString("item_name") + " - $" +
                                         rs.getDouble("price") + " - " +
                                         rs.getTimestamp("transaction_date");
                purchaseHistoryView.getPurchaseHistoryView().getItems().add(transactionInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            purchaseHistoryView.showMessage("Error loading purchase history", true);
        }
    }

    public void handlePurchaseItem(String itemId, User buyer) {
        try {
            String transactionId = UUID.randomUUID().toString();
            String query = "INSERT INTO transactions (transaction_id, item_id, buyer_id, transaction_date) VALUES (?, ?, ?, NOW())";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, transactionId);
            ps.setString(2, itemId);
            ps.setString(3, buyer.getUser_id());
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                // Update item status to 'sold'
                String updateItemQuery = "UPDATE items SET status = 'sold' WHERE item_id = ?";
                PreparedStatement updatePs = connect.prepareStatement(updateItemQuery);
                updatePs.setString(1, itemId);
                updatePs.executeUpdate();
                
                // Remove item from all wishlists
                String removeFromWishlistQuery = "DELETE FROM wishlist WHERE item_id = ?";
                PreparedStatement removePs = connect.prepareStatement(removeFromWishlistQuery);
                removePs.setString(1, itemId);
                removePs.executeUpdate();
                
                System.out.println("Purchase successful");
            } else {
                System.out.println("Purchase failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error processing purchase");
        }
    }
}

