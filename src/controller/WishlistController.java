package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import connect.Connect;
import model.User;
import view.WishlistView;

public class WishlistController {
    private Connect connect;

    public WishlistController() {
        this.connect = Connect.getInstance();
    }

    public void loadWishlistItems(WishlistView wishlistView, User currentUser) {
        wishlistView.getWishlistItemsView().getItems().clear();
        try {
            String query = "SELECT i.item_name, i.price FROM wishlist w JOIN items i ON w.item_id = i.item_id WHERE w.user_id = ?";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, currentUser.getUser_id());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String itemInfo = rs.getString("item_name") + " - $" + rs.getDouble("price");
                wishlistView.getWishlistItemsView().getItems().add(itemInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            wishlistView.showMessage("Error loading wishlist items", true);
        }
    }

    public void handleRemoveFromWishlist(WishlistView wishlistView, User currentUser) {
        String selectedItem = wishlistView.getWishlistItemsView().getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            wishlistView.showMessage("Please select an item to remove", true);
            return;
        }
        
        String itemName = selectedItem.split(" - ")[0];
        try {
            String query = "DELETE w FROM wishlist w JOIN items i ON w.item_id = i.item_id WHERE w.user_id = ? AND i.item_name = ?";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, currentUser.getUser_id());
            ps.setString(2, itemName);
            int result = ps.executeUpdate();
            
            if (result > 0) {
                wishlistView.showMessage("Item removed from wishlist", false);
                loadWishlistItems(wishlistView, currentUser);
            } else {
                wishlistView.showMessage("Failed to remove item from wishlist", true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            wishlistView.showMessage("Error removing item from wishlist", true);
        }
    }
}

