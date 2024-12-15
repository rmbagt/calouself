package controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import connect.Connect;
import model.Item;
import view.AdminView;
import javafx.stage.Stage;
import view.LoginView;

public class AdminController {
    private Connect connect;
    private Stage stage;

    public AdminController(Stage stage) {
        this.connect = Connect.getInstance();
        this.stage = stage;
    }

    public void loadPendingItems(AdminView adminView) {
        adminView.getPendingItemsView().getItems().clear();
        try {
            String query = "SELECT * FROM items WHERE status = 'pending'";
            PreparedStatement ps = connect.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String itemInfo = rs.getString("item_name") + " - Rp " + rs.getDouble("price");
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
            String query = "UPDATE items SET status = 'approved' WHERE item_name = ? AND status = 'pending'";
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
            String query = "UPDATE items SET status = 'declined' WHERE item_name = ? AND status = 'pending'";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, itemName);
            int result = ps.executeUpdate();
            
            if (result > 0) {
                adminView.showMessage("Item declined successfully", false);
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

    public void handleLogout() {
        LoginView loginView = new LoginView();
        stage.setScene(loginView.getScene());
        stage.setTitle("CaLouselF - Login");
    }
}

