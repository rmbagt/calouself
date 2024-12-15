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
    private Label messageLabel;

    public AdminView() {
        createAdminScene();
    }

    private void createAdminScene() {
        layout = new VBox(10);
        // Remove this line
        //layout.setPadding(new Insets(20));
        layout.setSpacing(10);

        // Create a dummy admin user for the navigation bar
        User adminUser = new User("admin", "admin", "admin", "", "", "Admin");
        navigationBar = new NavigationBar(adminUser);

        Label titleLabel = new Label("Admin Dashboard");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        pendingItemsView = new ListView<>();
        pendingItemsView.setPrefHeight(300);

        HBox buttonBox = new HBox(10);
        approveButton = new Button("Approve");
        declineButton = new Button("Decline");
        buttonBox.getChildren().addAll(approveButton, declineButton);

        declineReasonField = new TextField();
        declineReasonField.setPromptText("Reason for declining");


        messageLabel = new Label();
        messageLabel.setWrapText(true);

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
        contentBox.getChildren().addAll(
            titleLabel,
            new Label("Pending Items:"),
            pendingItemsView,
            buttonBox,
            new Label("Decline Reason:"),
            declineReasonField,
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

    public void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    public void clearMessage() {
        messageLabel.setText("");
    }
}

