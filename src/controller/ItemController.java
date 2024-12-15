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
import view.EditItemView;

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
        if (currentUser == null) {
            dashboardView.showMessage("Error: User not logged in", true);
            return;
        }
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
                String itemInfo = rs.getString("item_id") + " - " + rs.getString("item_name") + " - Rp " + rs.getDouble("price");
                if (currentUser.getRole().equals("Seller")) {
                    itemInfo += " - Status: " + rs.getString("status");
                }
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

        String[] itemParts = selectedItem.split(" - ");
        String itemId = itemParts[0]; // The item ID is now the first part

        try {
            // Check if the item is already in the wishlist
            String checkWishlistQuery = "SELECT * FROM wishlist WHERE user_id = ? AND item_id = ?";
            PreparedStatement checkWishlistPs = connect.prepareStatement(checkWishlistQuery);
            checkWishlistPs.setString(1, currentUser.getUser_id());
            checkWishlistPs.setString(2, itemId);
            ResultSet wishlistRs = checkWishlistPs.executeQuery();
        
            if (wishlistRs.next()) {
                dashboardView.showMessage("Item is already in your wishlist", true);
                return;
            }
        
            // Now, add to wishlist
            String addToWishlistQuery = "INSERT INTO wishlist (wishlist_id, user_id, item_id) VALUES (?, ?, ?)";
            PreparedStatement addToWishlistPs = connect.prepareStatement(addToWishlistQuery);
            addToWishlistPs.setString(1, UUID.randomUUID().toString());
            addToWishlistPs.setString(2, currentUser.getUser_id());
            addToWishlistPs.setString(3, itemId);
        
            int result = addToWishlistPs.executeUpdate();
        
            if (result > 0) {
                dashboardView.showMessage("Item added to wishlist", false);
            } else {
                dashboardView.showMessage("Failed to add item to wishlist", true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            dashboardView.showMessage("Error adding item to wishlist", true);
        }
    }

    private boolean validateItemInput(String name, String category, String size, String price, Object view) {
        boolean isValid = true;
        String errorMessage = "";

        if (!Item.validateItemName(name)) {
            errorMessage += "Item name must be at least 3 characters long\n";
            isValid = false;
        }
        
        if (!Item.validateCategory(category)) {
            errorMessage += "Item category must be at least 3 characters long\n";
            isValid = false;
        }
        
        if (!Item.validateSize(size)) {
            errorMessage += "Item size cannot be empty\n";
            isValid = false;
        }
        
        if (!Item.validatePrice(price)) {
            errorMessage += "Item price must be greater than 0 and a valid number\n";
            isValid = false;
        }

        if (!isValid) {
            if (view instanceof UploadItemView) {
                ((UploadItemView) view).showMessage(errorMessage, true);
            } else if (view instanceof EditItemView) {
                ((EditItemView) view).showMessage(errorMessage, true);
            }
        }
        
        return isValid;
    }

    public void editItem(String itemId, String newName, String newCategory, String newSize, String newPrice, EditItemView editItemView) {
        if (!validateItemInput(newName, newCategory, newSize, newPrice, editItemView)) {
            return;
        }
    
        try {
            String query = "UPDATE items SET item_name = ?, category = ?, size = ?, price = ? WHERE item_id = ? AND status = 'approved'";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, newName);
            ps.setString(2, newCategory);
            ps.setString(3, newSize);
            ps.setDouble(4, Double.parseDouble(newPrice));
            ps.setString(5, itemId);
        
            int result = ps.executeUpdate();
        
            if (result > 0) {
                editItemView.showMessage("Item updated successfully!", false);
            } else {
                editItemView.showMessage("Failed to update item. Item may not be approved or doesn't exist.", true);
            }
        
        } catch (SQLException e) {
            e.printStackTrace();
            editItemView.showMessage("Database error occurred", true);
        }
    }

    public void deleteItem(String itemId, DashboardView dashboardView, User currentUser) {
        try {
            String query = "DELETE FROM items WHERE item_id = ? AND status = 'approved' AND seller_id = ?";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, itemId);
            ps.setString(2, currentUser.getUser_id());
        
            int result = ps.executeUpdate();
        
            if (result > 0) {
                dashboardView.showMessage("Item deleted successfully!", false);
                loadItems(dashboardView, currentUser); // Refresh the item list
            } else {
                dashboardView.showMessage("Failed to delete item. Item may not be approved or doesn't exist.", true);
            }
        
        } catch (SQLException e) {
            e.printStackTrace();
            dashboardView.showMessage("Database error occurred", true);
        }
    }

    public Item getItemById(String itemId, User currentUser) {
        try {
            String query = "SELECT * FROM items WHERE item_id = ? AND seller_id = ?";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, itemId);
            ps.setString(2, currentUser.getUser_id());
            ResultSet rs = ps.executeQuery();
        
            if (rs.next()) {
                return new Item(
                    rs.getString("item_id"),
                    rs.getString("seller_id"),
                    rs.getString("item_name"),
                    rs.getString("category"),
                    rs.getString("size"),
                    rs.getDouble("price"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

