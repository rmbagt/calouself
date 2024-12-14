package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.User;
import main.Main;

public class UploadItemView {
    private Scene scene;
    private VBox layout;
    private NavigationBar navigationBar;
    private TextField itemNameField;
    private TextField itemCategoryField;
    private TextField itemSizeField;
    private TextField itemPriceField;
    private Button uploadButton;
    private Label messageLabel;

    public UploadItemView(User user) {
        createUploadItemScene(user);
    }

    private void createUploadItemScene(User user) {
        layout = new VBox();
        
        navigationBar = new NavigationBar(user);

        VBox contentBox = new VBox(10);
        contentBox.setPadding(new Insets(10));

        Label titleLabel = new Label("Upload New Item");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        itemNameField = new TextField();
        itemNameField.setPromptText("Item Name");

        itemCategoryField = new TextField();
        itemCategoryField.setPromptText("Item Category");

        itemSizeField = new TextField();
        itemSizeField.setPromptText("Item Size");

        itemPriceField = new TextField();
        itemPriceField.setPromptText("Item Price");

        uploadButton = new Button("Upload Item");
        uploadButton.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5;");

        messageLabel = new Label();
        messageLabel.setWrapText(true);

        contentBox.getChildren().addAll(
            titleLabel,
            new Label("Item Name:"),
            itemNameField,
            new Label("Item Category:"),
            itemCategoryField,
            new Label("Item Size:"),
            itemSizeField,
            new Label("Item Price:"),
            itemPriceField,
            uploadButton,
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

    public TextField getItemNameField() {
        return itemNameField;
    }

    public TextField getItemCategoryField() {
        return itemCategoryField;
    }

    public TextField getItemSizeField() {
        return itemSizeField;
    }

    public TextField getItemPriceField() {
        return itemPriceField;
    }

    public Button getUploadButton() {
        return uploadButton;
    }

    public void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    public void clearFields() {
        itemNameField.clear();
        itemCategoryField.clear();
        itemSizeField.clear();
        itemPriceField.clear();
        messageLabel.setText("");
    }
}

