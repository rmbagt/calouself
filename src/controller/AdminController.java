package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import connect.Connect;
import model.Item;
import view.AdminView;

public class AdminController {
    private Connect connect;

    public AdminController() {
        this.connect = Connect.getInstance();
    }

    public void loadPendingItems(AdminView adminView) {
        adminView.getPendingItemsView().getItems().clear();
        try {
            String query = "SELECT * FROM items WHERE status = 'pending'";
            PreparedStatement ps = connect.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String itemInfo = rs.getString("item_name") + " - $" + rs.getDouble("price");
                adminView.getPendingItemsView().getItems().add(itemInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            adminView.showMessage("Error loading pending items", true);
        }
    }

    public void handleApproveItem(AdminView adminView) {
        String selectedItem = adminView.getPendingItemsView().getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            adminView.showMessage("Please select an item to approve", true);
            return;
        }
        
        String itemName = selectedItem.split(" - ")[0];
        try {
            String query = "UPDATE items SET status = 'approved' WHERE item_name = ?";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, itemName);
            int result = ps.executeUpdate();
            
            if (result > 0) {
                adminView.showMessage("Item approved successfully", false);
                loadPendingItems(adminView);
            } else {
                adminView.showMessage("Failed to approve item", true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            adminView.showMessage("Error approving item", true);
        }
    }

    public void handleDeclineItem(AdminView adminView) {
        String selectedItem = adminView.getPendingItemsView().getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            adminView.showMessage("Please select an item to decline", true);
            return;
        }
        
        String itemName = selectedItem.split(" - ")[0];
        String reason = adminView.getDeclineReasonField().getText();
        if (reason.isEmpty()) {
            adminView.showMessage("Please provide a reason for declining", true);
            return;
        }
        
        try {
            String query = "DELETE FROM items WHERE item_name = ?";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, itemName);
            int result = ps.executeUpdate();
            
            if (result > 0) {
                adminView.showMessage("Item declined and removed successfully", false);
                loadPendingItems(adminView);
                // TODO: Notify the seller about the declined item and reason
            } else {
                adminView.showMessage("Failed to decline item", true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            adminView.showMessage("Error declining item", true);
        }
    }
}

