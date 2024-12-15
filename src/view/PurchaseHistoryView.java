package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.User;
import main.Main;

public class PurchaseHistoryView {
    private Scene scene;
    private VBox layout;
    private NavigationBar navigationBar;
    private ListView<String> purchaseHistoryView;
    private Label messageLabel;

    public PurchaseHistoryView(User user) {
        createPurchaseHistoryScene(user);
    }

    private void createPurchaseHistoryScene(User user) {
        layout = new VBox();

        navigationBar = new NavigationBar(user);

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10));

        Label titleLabel = new Label("Purchase History");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        purchaseHistoryView = new ListView<>();
        purchaseHistoryView.setPrefHeight(400);
        purchaseHistoryView.setStyle("-fx-border-color: #00bfff; -fx-border-width: 1px;");

        messageLabel = new Label();
        messageLabel.setWrapText(true);

        contentBox.getChildren().addAll(
            titleLabel,
            purchaseHistoryView,
            messageLabel
        );

        layout.getChildren().add(navigationBar);
        layout.getChildren().add(contentBox);

        scene = new Scene(layout, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
    }

    public Scene getScene() {
        return scene;
    }

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    public ListView<String> getPurchaseHistoryView() {
        return purchaseHistoryView;
    }

    public void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    public void clearMessage() {
        messageLabel.setText("");
    }
}

