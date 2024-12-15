package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import connect.Connect;
import model.User;
import model.Offer;
import model.Item;
import view.MakeOfferView;
import view.OfferedItemsView;

public class OfferController {
    private Connect connect;
    private TransactionController transactionController;

    public OfferController() {
        this.connect = Connect.getInstance();
        this.transactionController = new TransactionController();
    }

    public double getHighestOffer(String itemId) {
        try {
            String query = "SELECT MAX(offer_price) as highest_offer FROM offers WHERE item_id = ? AND status = 'pending'";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, itemId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next() && rs.getObject("highest_offer") != null) {
                return rs.getDouble("highest_offer");
            }
            return 0.0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public void makeOffer(String itemId, User buyer, String offerPrice, MakeOfferView makeOfferView) {
        if (!Offer.validateOfferPrice(offerPrice)) {
            makeOfferView.showMessage("Please enter a valid price", true);
            return;
        }

        double offerPriceValue = Double.parseDouble(offerPrice);
        double highestOffer = getHighestOffer(itemId);

        if (offerPriceValue <= highestOffer) {
            makeOfferView.showMessage("Offer must be higher than the current highest offer", true);
            return;
        }

        try {
            String offerId = UUID.randomUUID().toString();
            String query = "INSERT INTO offers (offer_id, item_id, buyer_id, offer_price, status) VALUES (?, ?, ?, ?, 'pending')";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, offerId);
            ps.setString(2, itemId);
            ps.setString(3, buyer.getUser_id());
            ps.setDouble(4, offerPriceValue);

            int result = ps.executeUpdate();

            if (result > 0) {
                makeOfferView.showMessage("Offer submitted successfully!", false);
                makeOfferView.clearFields();
            } else {
                makeOfferView.showMessage("Failed to submit offer", true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            makeOfferView.showMessage("Error submitting offer", true);
        }
    }

    public void loadOfferedItems(OfferedItemsView offeredItemsView, User seller) {
        offeredItemsView.getOfferedItemsView().getItems().clear();
        try {
            String query = "SELECT o.offer_id, o.offer_price, i.item_name, i.category, i.size, i.price, u.Username " +
                          "FROM offers o " +
                          "JOIN items i ON o.item_id = i.item_id " +
                          "JOIN users u ON o.buyer_id = u.User_id " +
                          "WHERE i.seller_id = ? AND o.status = 'pending' " +
                          "ORDER BY o.offer_price DESC";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, seller.getUser_id());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String offerInfo = String.format("%s - %s - %s - %s - Original: Rp %.2f - Offer: Rp %.2f - by %s",
                    rs.getString("offer_id"),
                    rs.getString("item_name"),
                    rs.getString("category"),
                    rs.getString("size"),
                    rs.getDouble("price"),
                    rs.getDouble("offer_price"),
                    rs.getString("Username")
                );
                offeredItemsView.getOfferedItemsView().getItems().add(offerInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            offeredItemsView.showMessage("Error loading offered items", true);
        }
    }

    public void acceptOffer(String offerId, OfferedItemsView offeredItemsView) {
        try {
            connect.setAutoCommit(false);

            // Get offer details
            String getOfferQuery = "SELECT o.*, i.seller_id FROM offers o JOIN items i ON o.item_id = i.item_id WHERE o.offer_id = ?";
            PreparedStatement getOfferPs = connect.prepareStatement(getOfferQuery);
            getOfferPs.setString(1, offerId);
            ResultSet offerRs = getOfferPs.executeQuery();

            if (offerRs.next()) {
                String itemId = offerRs.getString("item_id");
                String buyerId = offerRs.getString("buyer_id");
                String sellerId = offerRs.getString("seller_id");
                double price = offerRs.getDouble("offer_price");

                // Create transaction
                String transactionId = UUID.randomUUID().toString();
                String createTransactionQuery = "INSERT INTO transactions (transaction_id, buyer_id, seller_id, item_id, price) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement createTransactionPs = connect.prepareStatement(createTransactionQuery);
                createTransactionPs.setString(1, transactionId);
                createTransactionPs.setString(2, buyerId);
                createTransactionPs.setString(3, sellerId);
                createTransactionPs.setString(4, itemId);
                createTransactionPs.setDouble(5, price);
                createTransactionPs.executeUpdate();

                // Update item status
                String updateItemQuery = "UPDATE items SET status = 'sold' WHERE item_id = ?";
                PreparedStatement updateItemPs = connect.prepareStatement(updateItemQuery);
                updateItemPs.setString(1, itemId);
                updateItemPs.executeUpdate();

                // Update offer status
                String updateOfferQuery = "UPDATE offers SET status = 'accepted' WHERE offer_id = ?";
                PreparedStatement updateOfferPs = connect.prepareStatement(updateOfferQuery);
                updateOfferPs.setString(1, offerId);
                updateOfferPs.executeUpdate();

                // Delete other offers for this item
                String deleteOtherOffersQuery = "DELETE FROM offers WHERE item_id = ? AND offer_id != ?";
                PreparedStatement deleteOtherOffersPs = connect.prepareStatement(deleteOtherOffersQuery);
                deleteOtherOffersPs.setString(1, itemId);
                deleteOtherOffersPs.setString(2, offerId);
                deleteOtherOffersPs.executeUpdate();

                connect.commit();
                offeredItemsView.showMessage("Offer accepted successfully!", false);
                loadOfferedItems(offeredItemsView, new User(sellerId, "", "", "", "", "Seller"));
            }
        } catch (SQLException e) {
            try {
                connect.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            offeredItemsView.showMessage("Error accepting offer", true);
        } finally {
            try {
                connect.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void declineOffer(String offerId, String reason, OfferedItemsView offeredItemsView, User seller) {
        if (!Offer.validateDeclineReason(reason)) {
            offeredItemsView.showMessage("Please provide a reason for declining", true);
            return;
        }

        try {
            String query = "UPDATE offers SET status = 'declined', decline_reason = ? WHERE offer_id = ?";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, reason);
            ps.setString(2, offerId);

            int result = ps.executeUpdate();

            if (result > 0) {
                offeredItemsView.showMessage("Offer declined successfully", false);
                loadOfferedItems(offeredItemsView, seller);
            } else {
                offeredItemsView.showMessage("Failed to decline offer", true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            offeredItemsView.showMessage("Error declining offer", true);
        }
    }
}

