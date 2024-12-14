package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.User;
import main.Main;

public class WishlistView {
    private Scene scene;
    private VBox layout;
    private NavigationBar navigationBar;
    private ListView<String> wishlistItemsView;
    private Button removeFromWishlistButton;
    private Label messageLabel;

    public WishlistView(User user) {
        createWishlistScene(user);
    }

    private void createWishlistScene(User user) {
        layout = new VBox();

        navigationBar = new NavigationBar(user);

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10));

        Label titleLabel = new Label("Your Wishlist");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        wishlistItemsView = new ListView<>();
        wishlistItemsView.setPrefHeight(400);
        wishlistItemsView.setStyle("-fx-border-color: #00bfff; -fx-border-width: 1px;");

        removeFromWishlistButton = new Button("Remove from Wishlist");
        removeFromWishlistButton.setDisable(true);
        removeFromWishlistButton.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5;");

        messageLabel = new Label();
        messageLabel.setWrapText(true);

        contentBox.getChildren().addAll(
            titleLabel,
            wishlistItemsView,
            removeFromWishlistButton,
            messageLabel
        );

        layout.getChildren().addAll(navigationBar, contentBox);

        scene = new Scene(layout, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);

        wishlistItemsView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            removeFromWishlistButton.setDisable(newSelection == null);
        });
    }

    public Scene getScene() {
        return scene;
    }

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    public ListView<String> getWishlistItemsView() {
        return wishlistItemsView;
    }

    public Button getRemoveFromWishlistButton() {
        return removeFromWishlistButton;
    }

    public void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    public void clearMessage() {
        messageLabel.setText("");
    }
}

