package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import connect.Connect;
import model.Item;
import model.User;
import view.UploadItemView;
import view.DashboardView;

public class ItemController {
    private Connect connect;

    public ItemController() {
        this.connect = Connect.getInstance();
    }

    public void handleItemUpload(UploadItemView uploadItemView, User currentUser) {
        String itemName = uploadItemView.getItemNameField().getText();
        String itemCategory = uploadItemView.getItemCategoryField().getText();
        String itemSize = uploadItemView.getItemSizeField().getText();
        String itemPrice = uploadItemView.getItemPriceField().getText();
        
        if (!validateItemInput(itemName, itemCategory, itemSize, itemPrice, uploadItemView)) {
            return;
        }
        
        try {
            String itemId = UUID.randomUUID().toString();
            String query = "INSERT INTO items (item_id, seller_id, item_name, category, size, price, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, itemId);
            ps.setString(2, currentUser.getUser_id());
            ps.setString(3, itemName);
            ps.setString(4, itemCategory);
            ps.setString(5, itemSize);
            ps.setDouble(6, Double.parseDouble(itemPrice));
            ps.setString(7, "pending");
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                uploadItemView.showMessage("Item uploaded successfully! Waiting for admin approval.", false);
                uploadItemView.clearFields();
            } else {
                uploadItemView.showMessage("Failed to upload item. Please try again.", true);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            uploadItemView.showMessage("Database error occurred", true);
        }
    }

    public void loadItems(DashboardView dashboardView, User currentUser) {
        dashboardView.getItemListView().getItems().clear();
        try {
            String query;
            if (currentUser.getRole().equals("Buyer")) {
                query = "SELECT * FROM items WHERE status = 'approved'";
            } else {
                query = "SELECT * FROM items WHERE seller_id = ?";
            }
            PreparedStatement ps = connect.prepareStatement(query);
            if (currentUser.getRole().equals("Seller")) {
                ps.setString(1, currentUser.getUser_id());
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String itemInfo = rs.getString("item_name") + " - $" + rs.getDouble("price");
                dashboardView.getItemListView().getItems().add(itemInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            dashboardView.showMessage("Error loading items", true);
        }
    }

    public void handleAddToWishlist(DashboardView dashboardView, User currentUser) {
        String selectedItem = dashboardView.getItemListView().getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            dashboardView.showMessage("Please select an item to add to wishlist", true);
            return;
        }

        String itemName = selectedItem.split(" - ")[0];
        try {
            // First, get the item_id
            String getItemIdQuery = "SELECT item_id FROM items WHERE item_name = ?";
            PreparedStatement getItemIdPs = connect.prepareStatement(getItemIdQuery);
            getItemIdPs.setString(1, itemName);
            ResultSet rs = getItemIdPs.executeQuery();
            
            if (rs.next()) {
                String itemId = rs.getString("item_id");
                
                // Now, add to wishlist
                String addToWishlistQuery = "INSERT INTO wishlist (user_id, item_id) VALUES (?, ?)";
                PreparedStatement addToWishlistPs = connect.prepareStatement(addToWishlistQuery);
                addToWishlistPs.setString(1, currentUser.getUser_id());
                addToWishlistPs.setString(2, itemId);
                
                int result = addToWishlistPs.executeUpdate();
                
                if (result > 0) {
                    dashboardView.showMessage("Item added to wishlist", false);
                } else {
                    dashboardView.showMessage("Failed to add item to wishlist", true);
                }
            } else {
                dashboardView.showMessage("Item not found", true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            dashboardView.showMessage("Error adding item to wishlist", true);
        }
    }

    private boolean validateItemInput(String name, String category, String size, String price, UploadItemView uploadItemView) {
        if (!Item.validateItemName(name)) {
            uploadItemView.showMessage("Item name must be at least 3 characters long", true);
            return false;
        }
        
        if (!Item.validateCategory(category)) {
            uploadItemView.showMessage("Item category must be at least 3 characters long", true);
            return false;
        }
        
        if (!Item.validateSize(size)) {
            uploadItemView.showMessage("Item size cannot be empty", true);
            return false;
        }
        
        try {
            double priceValue = Double.parseDouble(price);
            if (!Item.validatePrice(priceValue)) {
                uploadItemView.showMessage("Item price must be greater than 0", true);
                return false;
            }
        } catch (NumberFormatException e) {
            uploadItemView.showMessage("Invalid price format. Please enter a valid number", true);
            return false;
        }
        
        return true;
    }
}

