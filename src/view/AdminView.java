package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.Main;
import model.User;

public class AdminView {
    private Scene scene;
    private VBox layout;
    private NavigationBar navigationBar;
    private ListView<String> pendingItemsView;
    private Button approveButton;
    private Button declineButton;
    private TextField declineReasonField;
    private Button logoutButton;
    private Label messageLabel;

    public AdminView() {
        createAdminScene();
    }

    private void createAdminScene() {
        layout = new VBox();

        // Create a dummy admin user for the navigation bar
        User adminUser = new User("admin", "admin", "admin", "", "", "Admin");
        navigationBar = new NavigationBar(adminUser);

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10));

        Label titleLabel = new Label("Admin Dashboard");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        pendingItemsView = new ListView<>();
        pendingItemsView.setPrefHeight(400);
        pendingItemsView.setStyle("-fx-border-color: #00bfff; -fx-border-width: 1px;");

        HBox buttonBox = new HBox(10);
        approveButton = new Button("Approve");
        declineButton = new Button("Decline");
        approveButton.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5;");
        declineButton.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5;");
        buttonBox.getChildren().addAll(approveButton, declineButton);

        declineReasonField = new TextField();
        declineReasonField.setPromptText("Reason for declining");

        logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5;");

        messageLabel = new Label();
        messageLabel.setWrapText(true);

        contentBox.getChildren().addAll(
            titleLabel,
            new Label("Pending Items:"),
            pendingItemsView,
            buttonBox,
            new Label("Decline Reason:"),
            declineReasonField,
            logoutButton,
            messageLabel
        );

        layout.getChildren().addAll(navigationBar, contentBox);

        scene = new Scene(layout, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
    }

    public Scene getScene() {
        return scene;
    }

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    public ListView<String> getPendingItemsView() {
        return pendingItemsView;
    }

    public Button getApproveButton() {
        return approveButton;
    }

    public Button getDeclineButton() {
        return declineButton;
    }

    public TextField getDeclineReasonField() {
        return declineReasonField;
    }

    public Button getLogoutButton() {
        return logoutButton;
    }

    public void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    public void clearMessage() {
        messageLabel.setText("");
    }
}

