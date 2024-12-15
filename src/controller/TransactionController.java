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
                String transactionInfo = String.format("%s - %s - Rp %.2f - %s",
                    rs.getString("transaction_id"),
                    rs.getString("item_name"),
                    rs.getDouble("price"),
                    rs.getTimestamp("transaction_date").toString()
                );
                purchaseHistoryView.getPurchaseHistoryView().getItems().add(transactionInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            purchaseHistoryView.showMessage("Error loading purchase history", true);
        }
    }

    public void handlePurchaseItem(String itemId, User buyer) {
        try {
            connect.setAutoCommit(false); // Start transaction

            try {
                // 1. Create transaction record
                String transactionId = UUID.randomUUID().toString();
                String insertQuery = "INSERT INTO transactions (transaction_id, buyer_id, item_id, transaction_date) VALUES (?, ?, ?, NOW())";
                PreparedStatement insertPs = connect.prepareStatement(insertQuery);
                insertPs.setString(1, transactionId);
                insertPs.setString(2, buyer.getUser_id());
                insertPs.setString(3, itemId);
                insertPs.executeUpdate();

                // 2. Update item status to 'sold'
                String updateItemQuery = "UPDATE items SET status = 'sold' WHERE item_id = ? AND status = 'approved'";
                PreparedStatement updatePs = connect.prepareStatement(updateItemQuery);
                updatePs.setString(1, itemId);
                updatePs.executeUpdate();

                // 3. Remove item from all wishlists
                String removeFromWishlistQuery = "DELETE FROM wishlist WHERE item_id = ?";
                PreparedStatement removePs = connect.prepareStatement(removeFromWishlistQuery);
                removePs.setString(1, itemId);
                removePs.executeUpdate();

                connect.commit(); // Commit transaction
                System.out.println("Purchase successful");
            } catch (SQLException e) {
                connect.rollback(); // Rollback on error
                throw e;
            } finally {
                connect.setAutoCommit(true); // Reset auto-commit
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error processing purchase");
        }
    }
}

