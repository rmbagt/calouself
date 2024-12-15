package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.User;
import main.Main;

public class DashboardView {
    private Scene scene;
    private VBox layout;
    private NavigationBar navigationBar;
    private Label welcomeLabel;
    private ListView<String> itemListView;
    private Button refreshButton;
    private Button addToWishlistButton;
    private Button purchaseButton;
    private Label messageLabel;
    private Button editItemButton;
    private Button deleteItemButton;
    private Button makeOfferButton;

    public DashboardView(User user) {
        createDashboardScene(user);
    }

    private void createDashboardScene(User user) {
        layout = new VBox();
        layout.setSpacing(10);

        navigationBar = new NavigationBar(user);

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
        
        welcomeLabel = new Label("Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
        welcomeLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        itemListView = new ListView<>();
        itemListView.setPrefHeight(400);
        itemListView.setStyle("-fx-border-color: #00bfff; -fx-border-width: 1px;");

        refreshButton = new Button("Refresh Items");
        refreshButton.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5;");

        addToWishlistButton = null;
        purchaseButton = null;
        if (user.getRole().equals("Buyer")) {
            addToWishlistButton = new Button("Add to Wishlist");
            addToWishlistButton.setDisable(true);
            addToWishlistButton.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5;");
            
            purchaseButton = new Button("Purchase");
            purchaseButton.setDisable(true);
            purchaseButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5;");
            
            makeOfferButton = new Button("Make Offer");
            makeOfferButton.setDisable(true);
            makeOfferButton.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-background-radius: 5;");
        } else if (user.getRole().equals("Seller")) {
            editItemButton = new Button("Edit Item");
            editItemButton.setDisable(true);
            editItemButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5;");
    
            deleteItemButton = new Button("Delete Item");
            deleteItemButton.setDisable(true);
            deleteItemButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 5;");
    
            HBox buttonBox = new HBox(10);
            buttonBox.getChildren().addAll(editItemButton, deleteItemButton);
            contentBox.getChildren().add(buttonBox);

            itemListView.setCellFactory(lv -> new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        if (item.contains("Status: pending")) {
                            setStyle("-fx-text-fill: orange;");
                        } else if (item.contains("Status: approved")) {
                            setStyle("-fx-text-fill: green;");
                        } else if (item.contains("Status: sold")) {
                            setStyle("-fx-text-fill: blue;");
                        } else if (item.contains("Status: declined")) {
                            setStyle("-fx-text-fill: red;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            });

            itemListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                boolean isApproved = newSelection != null && newSelection.contains("Status: approved");
                editItemButton.setDisable(!isApproved);
                deleteItemButton.setDisable(!isApproved);
            });
        }

        messageLabel = new Label();
        messageLabel.setWrapText(true);

        contentBox.getChildren().addAll(welcomeLabel, itemListView, refreshButton);
        if (user.getRole().equals("Buyer")) {
            HBox buttonBox = new HBox(10);
            buttonBox.getChildren().addAll(addToWishlistButton, purchaseButton, makeOfferButton);
            contentBox.getChildren().add(buttonBox);
        }
        contentBox.getChildren().add(messageLabel);

        layout.getChildren().add(navigationBar);
        layout.getChildren().add(contentBox);

        scene = new Scene(layout, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);

        if (user.getRole().equals("Buyer")) {
            itemListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                boolean hasSelection = newSelection != null;
                addToWishlistButton.setDisable(!hasSelection);
                purchaseButton.setDisable(!hasSelection);
                makeOfferButton.setDisable(!hasSelection);
            });
        }
    }

    public Scene getScene() {
        return scene;
    }

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }

    public ListView<String> getItemListView() {
        return itemListView;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public Button getAddToWishlistButton() {
        return addToWishlistButton != null ? addToWishlistButton : new Button("Add to Wishlist");
    }

    public Button getPurchaseButton() {
        return purchaseButton != null ? purchaseButton : new Button("Purchase");
    }

    public void setWelcomeMessage(String message) {
        welcomeLabel.setText(message);
    }

    public void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    public void clearMessage() {
        messageLabel.setText("");
    }

    public Button getEditItemButton() {
        return editItemButton;
    }

    public Button getDeleteItemButton() {
        return deleteItemButton;
    }

    public Button getMakeOfferButton() {
        return makeOfferButton;
    }
}

